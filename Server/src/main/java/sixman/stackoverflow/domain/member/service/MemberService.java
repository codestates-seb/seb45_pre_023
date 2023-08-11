package sixman.stackoverflow.domain.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.member.repository.dto.MemberAnswerData;
import sixman.stackoverflow.domain.member.repository.dto.MemberQuestionData;
import sixman.stackoverflow.domain.member.service.dto.request.MemberCreateServiceRequest;
import sixman.stackoverflow.domain.member.service.dto.request.MemberDeleteServiceRequest;
import sixman.stackoverflow.domain.member.service.dto.request.MemberPasswordUpdateServiceRequest;
import sixman.stackoverflow.domain.member.service.dto.request.MemberUpdateServiceRequest;
import sixman.stackoverflow.domain.member.service.dto.response.MemberResponse;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.global.exception.businessexception.memberexception.*;
import sixman.stackoverflow.module.aws.s3service.S3Service;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, S3Service s3Service, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.s3Service = s3Service;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Long signup(MemberCreateServiceRequest request) {

        checkDuplicateMember(request.getEmail());

        Member member = createMember(request);

        return memberRepository.save(member).getMemberId();
    }

    public MemberResponse getMember(Long memberId) {

        Member member = verifiedMember(memberId);

        return MemberResponse.of(
                member,
                getPreSignedUrl(member),
                getMemberQuestionPageResponse(memberId, 0, 5),
                getMemberAnswerPageResponse(memberId, 0, 5),
                getMemberTag(memberId)
        );

    }

    public MemberResponse.MemberQuestionPageResponse getMemberQuestion(Long memberId, Integer page, Integer size) {

        verifiedMember(memberId);

        return getMemberQuestionPageResponse(memberId, page, size);
    }

    public MemberResponse.MemberAnswerPageResponse getMemberAnswer(Long memberId, Integer page, Integer size) {

        verifiedMember(memberId);

        return getMemberAnswerPageResponse(memberId, page, size);
    }


    @Transactional
    public void updateMember(Long loginMemberId, MemberUpdateServiceRequest request){

        checkAccessAuthority(loginMemberId, request.getUpdateMemberId());

        Member member = verifiedMember(loginMemberId);

        member.updateMember(request.getNickname(), request.getMyIntro());
    }

    @Transactional
    public void updatePassword(Long memberId, MemberPasswordUpdateServiceRequest request){

        checkAccessAuthority(memberId, request.getUpdateMemberId());

        Member member = verifiedMember(memberId);

        checkPassword(request.getPassword(), member.getPassword());

        member.updatePassword(
                passwordEncoder.encode(request.getNewPassword())
        );
    }

    @Transactional
    public String updateImage(Long loginMemberId, Long updateMemberId, MultipartFile file){

        checkAccessAuthority(loginMemberId, updateMemberId);

        Member member = verifiedMember(updateMemberId);

        if(member.getMyInfo().getImage() == null) {
            String type = file.getContentType().split("/")[1];
            member.getMyInfo().updateImage(String.format("images/%s.%s", member.getEmail(), type));
        }

        return s3Service.uploadImage(member.getMyInfo().getImage(), file);
    }

    @Transactional
    public void deleteMember(Long memberId, MemberDeleteServiceRequest request){

        checkAccessAuthority(memberId, request.getDeleteMemberId());

        Member member = verifiedMember(memberId);

        checkPassword(request.getPassword(), member.getPassword());

        member.disable();
    }

    private void checkPassword(String password, String savedPassword) {
        if (!passwordEncoder.matches(password, savedPassword)) {
            throw new MemberPasswordException();
        }
    }

    private void checkDuplicateMember(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new MemberDuplicateException();
        }
    }

    private Member createMember(MemberCreateServiceRequest request) {
        return Member.createMember(
                request.getEmail(),
                request.getNickname(),
                passwordEncoder.encode(request.getPassword())
        );
    }

    private Member verifiedMember(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        if(!member.isEnabled()) throw new MemberDisabledException();
        return member;
    }

    private MemberResponse.MemberQuestionPageResponse getMemberQuestionPageResponse(Long memberId, Integer page, Integer size) {

        Page<MemberQuestionData> memberQuestionData = memberRepository.findQuestionByMemberId(memberId, PageRequest.of(page, size));

        return MemberResponse.MemberQuestionPageResponse.of(memberQuestionData);
    }

    private MemberResponse.MemberAnswerPageResponse getMemberAnswerPageResponse(Long memberId, Integer page, Integer size) {

        Page<MemberAnswerData> memberAnswerData = memberRepository.findAnswerByMemberId(memberId, PageRequest.of(page, size));

        return MemberResponse.MemberAnswerPageResponse.of(memberAnswerData);
    }

    private List<MemberResponse.MemberTag> getMemberTag(Long memberId) {

        verifiedMember(memberId);

        List<Tag> tags = memberRepository.findTagByMemberId(memberId);

        return MemberResponse.MemberTag.of(tags);
    }

    private void checkAccessAuthority(Long loginMemberId, Long requestMemberId) {
        if(!loginMemberId.equals(requestMemberId)) throw new MemberAccessDeniedException();
    }

    private String getPreSignedUrl(Member member) {

        if(member.getMyInfo().getImage() == null) return null;

        return s3Service.getPreSignedUrl(member.getMyInfo().getImage());
    }
}
