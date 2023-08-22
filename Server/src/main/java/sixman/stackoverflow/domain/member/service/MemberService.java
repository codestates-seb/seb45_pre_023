package sixman.stackoverflow.domain.member.service;

import org.springframework.beans.factory.annotation.Value;
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
import sixman.stackoverflow.domain.member.service.dto.request.*;
import sixman.stackoverflow.domain.member.service.dto.response.MemberResponse;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.global.exception.businessexception.emailexception.EmailAuthNotAttemptException;
import sixman.stackoverflow.global.exception.businessexception.emailexception.EmailAuthNotCompleteException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.*;
import sixman.stackoverflow.module.aws.service.S3Service;
import sixman.stackoverflow.module.email.service.MailService;
import sixman.stackoverflow.module.redis.service.RedisService;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    private final S3Service s3Service;
    private final MailService mailService;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;
    @Value("${spring.mail.email-complete-expiration-millis}")
    private long emailCompleteExpirationMillis;

    public MemberService(MemberRepository memberRepository,
                         S3Service s3Service,
                         MailService mailService,
                         RedisService redisService,
                         PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.s3Service = s3Service;
        this.mailService = mailService;
        this.redisService = redisService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Long signup(MemberCreateServiceRequest request) {

        checkDuplicateMember(request.getEmail());

        checkEmailAuthComplete(request.getEmail());

        Member member = createMember(request);

        return memberRepository.save(member).getMemberId();
    }

    private Member createMember(MemberCreateServiceRequest request) {
        return Member.createMember(
                request.getEmail(),
                request.getNickname(),
                passwordEncoder.encode(request.getPassword())
        );
    }

    public MemberResponse getMember(Long memberId) {

        Member member = verifiedMember(memberId);

        return getMemberResponseFrom(member);

    }

    private MemberResponse getMemberResponseFrom(Member member) {
        return MemberResponse.of(
                member,
                getPreSignedUrl(member),
                getMemberQuestionPageResponse(member.getMemberId(), 0, 5),
                getMemberAnswerPageResponse(member.getMemberId(), 0, 5),
                getMemberTag(member.getMemberId())
        );
    }

    private String getPreSignedUrl(Member member) {

        if(member.getMyInfo().getImage() == null) return null;

        return s3Service.getPreSignedUrl(member.getMyInfo().getImage());
    }

    public Page<MemberResponse.MemberQuestion> getMemberQuestion(Long memberId, Integer page, Integer size) {

        verifiedMember(memberId);

        return getMemberQuestionPageResponse(memberId, page, size);
    }

    public Page<MemberResponse.MemberAnswer> getMemberAnswer(Long memberId, Integer page, Integer size) {

        verifiedMember(memberId);

        return getMemberAnswerPageResponse(memberId, page, size);
    }


    private Page<MemberResponse.MemberQuestion> getMemberQuestionPageResponse(Long memberId, Integer page, Integer size) {

        Page<MemberQuestionData> memberQuestionData = memberRepository.findQuestionByMemberId(memberId, PageRequest.of(page, size));

        return memberQuestionData.map(MemberResponse.MemberQuestion::of);
    }

    private Page<MemberResponse.MemberAnswer> getMemberAnswerPageResponse(Long memberId, Integer page, Integer size) {

        Page<MemberAnswerData> memberAnswerData = memberRepository.findAnswerByMemberId(memberId, PageRequest.of(page, size));

        return memberAnswerData.map(MemberResponse.MemberAnswer::of);
    }

    @Transactional
    public void updateMember(Long loginMemberId, MemberUpdateServiceRequest request){

        checkAccessAuthority(loginMemberId, request.getUpdateMemberId());

        Member member = verifiedMember(loginMemberId);

        updateMember(member, request);
    }

    private void updateMember(Member member, MemberUpdateServiceRequest request) {

        member.updateNickname(request.getNickname());

        member.updateMyInfo(
                request.getMyIntro(),
                request.getTitle(),
                request.getLocation(),
                request.getAccounts()
        );
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

    private void checkPassword(String password, String savedPassword) {
        if (!passwordEncoder.matches(password, savedPassword)) {
            throw new MemberPasswordException();
        }
    }

    @Transactional
    public void findPassword(MemberFindPasswordServiceRequest request) {

        Member member = verifiedMember(request.getEmail());

        checkEmailAuthComplete(request.getEmail());

        member.enable();

        member.updatePassword(
                passwordEncoder.encode(request.getPassword())
        );
    }

    @Transactional
    public String updateImage(Long loginMemberId, Long updateMemberId, MultipartFile file){

        checkAccessAuthority(loginMemberId, updateMemberId);

        Member member = verifiedMember(updateMemberId);

        member.updateImagePath(getImageType(file));

        return s3Service.uploadAndGetUrl(member.getMyInfo().getImage(), file);
    }

    private String getImageType(MultipartFile file){
        return file.getContentType().split("/")[1];
    }

    @Transactional
    public void deleteImage(Long loginMemberId, Long updateMemberId) {
        checkAccessAuthority(loginMemberId, updateMemberId);

        Member member = verifiedMember(updateMemberId);

        deleteImageFrom(member);
    }

    private void deleteImageFrom(Member member) {
        if(member.getMyInfo().getImage() != null) {
            s3Service.deleteImage(member.getMyInfo().getImage());
            member.getMyInfo().updateImage(null);
        }
    }

    @Transactional
    public void deleteMember(Long memberId, Long deleteMemberId){

        checkAccessAuthority(memberId, deleteMemberId);

        Member member = verifiedMember(deleteMemberId);

        member.disable();
    }

    public void sendFindPasswordCodeToEmail(String toEmail) {

        verifiedMember(toEmail);

        String authCode = mailService.sendAuthEmail(toEmail);

        saveCodeInRedis(toEmail, authCode);
    }

    public void sendSignupCodeToEmail(String toEmail) {

        checkDuplicateMember(toEmail);

        String authCode = mailService.sendAuthEmail(toEmail);

        saveCodeInRedis(toEmail, authCode);
    }

    private void saveCodeInRedis(String toEmail, String authCode) {
        redisService.saveValues(
                AUTH_CODE_PREFIX + toEmail,
                authCode,
                Duration.ofMillis(authCodeExpirationMillis));
    }

    public boolean checkCode(String toEmail, String givenCode) {

        boolean isValid = checkCodeValid(toEmail, givenCode);

        if(isValid) {
            saveTrueInRedis(toEmail);
        }

        return isValid;
    }

    private boolean checkCodeValid(String toEmail, String givenCode){

        String savedCode = redisService.getValues(AUTH_CODE_PREFIX + toEmail);

        if(savedCode == null) throw new EmailAuthNotAttemptException();

        return savedCode.equals(givenCode);
    }

    private void saveTrueInRedis(String toEmail) {
        redisService.saveValues(
                AUTH_CODE_PREFIX + toEmail,
                "true",
                Duration.ofMillis(emailCompleteExpirationMillis));
    }


    private void checkDuplicateMember(String email) {

        Member member = memberRepository.findByEmail(email).orElse(null);

        if (member != null) {

            if(member.isEnabled()) throw new MemberDuplicateException();
            throw new MemberDisabledException();

        }
    }

    private Member verifiedMember(Long memberId){
        Member member = memberRepository.findByMemberIdWithInfo(memberId).orElseThrow(MemberNotFoundException::new);
        if(!member.isEnabled()) throw new MemberDisabledException();
        return member;
    }

    private Member verifiedMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    private List<MemberResponse.MemberTag> getMemberTag(Long memberId) {

        verifiedMember(memberId);

        List<Tag> tags = memberRepository.findTagByMemberId(memberId);

        return MemberResponse.MemberTag.of(tags);
    }

    private void checkAccessAuthority(Long loginMemberId, Long requestMemberId) {
        if(!loginMemberId.equals(requestMemberId)) throw new MemberAccessDeniedException();
    }

    private void checkEmailAuthComplete(String email) {
        if(redisService.getValues(AUTH_CODE_PREFIX + email) == null || !redisService.getValues(AUTH_CODE_PREFIX + email).equals("true")){
            throw new EmailAuthNotCompleteException();
        }

        redisService.deleteValues(AUTH_CODE_PREFIX + email);
    }
}
