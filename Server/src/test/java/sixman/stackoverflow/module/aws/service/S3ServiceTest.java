package sixman.stackoverflow.module.aws.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.entity.MyInfo;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.global.exception.businessexception.s3exception.S3FileNotValidException;
import sixman.stackoverflow.global.exception.businessexception.s3exception.S3PathNotValidException;

@SpringBootTest
class S3ServiceTest {

    @Autowired PasswordEncoder passwordEncoder;
    @Autowired S3Service s3Service;
    @Autowired MemberRepository memberRepository;
    @Test
    @DisplayName("image 의 PreSignedUrl 을 받을 때 image 경로가 images/ 로 시작하지 않으면 S3PathNotValidException 이 발생한다.")
    void getPreSignedUrlException() {
        //given
        Member member = createMember("test.png");

        memberRepository.save(member);

        //when & then
        Assertions.assertThatThrownBy(
                        () -> s3Service.getPreSignedUrl(member.getMyInfo().getImage()))
                .isInstanceOf(S3PathNotValidException.class)
                .hasMessage("이미지 경로에 오류가 있습니다. 다시 시도해주세요.");
    }

    @Test
    @DisplayName("image 의 PreSignedUrl 을 받을 때 image 경로가 없으면 S3PathNotValidException 이 발생한다.")
    void getPreSignedUrlNullException() {
        //given
        Member member = createMember(null);

        memberRepository.save(member);

        //when & then
        Assertions.assertThatThrownBy(
                        () -> s3Service.getPreSignedUrl(member.getMyInfo().getImage()))
                .isInstanceOf(S3PathNotValidException.class)
                .hasMessage("이미지 경로에 오류가 있습니다. 다시 시도해주세요.");
    }

    @Test
    @DisplayName("이미지를 업로드할 때 파일 확장자가 jpg, jpeg, png 가 아니면 S3FileNotValidException 이 발생한다.")
    void uploadImage() {
        //given
        Member member = createMember("images/test.jpg");

        memberRepository.save(member);

        MultipartFile multipartFile =
                new MockMultipartFile(
                        "test",
                        "test.txt",
                        "text/plain",
                        "test".getBytes()
                );

        //when & then
        Assertions.assertThatThrownBy(
                        () -> s3Service.uploadAndGetUrl(member.getMyInfo().getImage(), multipartFile))
                .isInstanceOf(S3FileNotValidException.class)
                .hasMessage("유효하지 않은 file 확장자입니다.");
    }

    @Test
    @DisplayName("이미지를 업로드할 때 imagePath 가 null 이면 S3PathNotValidException 이 발생한다.")
    void uploadImagePathNullException() {
        //given
        Member member = createMember(null);

        memberRepository.save(member);

        MultipartFile image =
                new MockMultipartFile(
                        "image",
                        "image.png",
                        "image/png",
                        "image".getBytes());

        //when & then
        Assertions.assertThatThrownBy(
                        () -> s3Service.uploadAndGetUrl(member.getMyInfo().getImage(), image))
                .isInstanceOf(S3PathNotValidException.class)
                .hasMessage("이미지 경로에 오류가 있습니다. 다시 시도해주세요.");
    }

    @Test
    @DisplayName("이미지를 업로드할 때 imagePath 가 images/ 로 시작하지 않으면 S3PathNotValidException 이 발생한다.")
    void uploadImagePathException() {
        //given
        Member member = createMember("test.png");

        memberRepository.save(member);

        MultipartFile image =
                new MockMultipartFile(
                        "image",
                        "image.png",
                        "image/png",
                        "image".getBytes());

        //when & then
        Assertions.assertThatThrownBy(
                        () -> s3Service.uploadAndGetUrl(member.getMyInfo().getImage(), image))
                .isInstanceOf(S3PathNotValidException.class)
                .hasMessage("이미지 경로에 오류가 있습니다. 다시 시도해주세요.");
    }

    @Test
    @DisplayName("이미지를 삭제할 때 imagePath 가 images/ 로 시작하지 않으면 S3PathNotValidException 이 발생한다.")
    void deleteImage() {
        //given
        Member member = createMember("test.png");

        memberRepository.save(member);

        //when & then
        Assertions.assertThatThrownBy(
                        () -> s3Service.deleteImage(member.getMyInfo().getImage()))
                .isInstanceOf(S3PathNotValidException.class)
                .hasMessage("이미지 경로에 오류가 있습니다. 다시 시도해주세요.");
    }

    @Test
    @DisplayName("이미지를 삭제할 때 imagePath 가 null 이면 S3PathNotValidException 이 발생한다.")
    void deleteImagePathNullException() {
        //given
        Member member = createMember(null);

        memberRepository.save(member);

        //when & then
        Assertions.assertThatThrownBy(
                        () -> s3Service.deleteImage(member.getMyInfo().getImage()))
                .isInstanceOf(S3PathNotValidException.class)
                .hasMessage("이미지 경로에 오류가 있습니다. 다시 시도해주세요.");
    }

    protected Member createMember(String imagePath) {
        return Member.builder()
                .email("test@test.com")
                .nickname("test")
                .password(passwordEncoder.encode("1234abcd!"))
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().image(imagePath).build())
                .enabled(true)
                .build();
    }
}