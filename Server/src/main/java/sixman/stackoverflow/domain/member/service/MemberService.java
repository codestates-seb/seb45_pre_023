package sixman.stackoverflow.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.member.service.dto.request.MemberCreateServiceRequest;
import sixman.stackoverflow.domain.member.service.dto.request.MemberPasswordUpdateServiceRequest;
import sixman.stackoverflow.domain.member.service.dto.request.MemberUpdateServiceRequest;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberDuplicateException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberPasswordException;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Long signup(MemberCreateServiceRequest request) {

        checkDuplicateMember(request.getEmail());

        Member member = createMember(request);

        return memberRepository.save(member).getMemberId();
    }

    @Transactional
    public void updateMember(Long memberId, MemberUpdateServiceRequest request){

        Member member = verifyMember(memberId);

        member.updateMember(request.getNickname(), request.getMyIntro());
    }

    @Transactional
    public void updatePassword(Long memberId, MemberPasswordUpdateServiceRequest request){

        Member member = verifyMember(memberId);

        checkPassword(request.getPassword(), member.getPassword());

        member.updatePassword(
                passwordEncoder.encode(request.getNewPassword())
        );
    }

    private void checkPassword(String password, String newPassword) {
        if (!passwordEncoder.matches(password, newPassword)) {
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

    private Member verifyMember(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    }
}
