package sixman.stackoverflow.module.aws.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.entity.MyInfo;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.global.exception.businessexception.s3exception.S3FileNotValidException;
import sixman.stackoverflow.global.exception.businessexception.s3exception.S3PathNotValidException;
import sixman.stackoverflow.module.ModuleServiceTest;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


class S3ServiceTest extends ModuleServiceTest {

    @Autowired PasswordEncoder passwordEncoder;
    @Autowired S3Service s3Service;
    @Autowired AwsCredentialsProvider credentialsProvider;
    @Autowired MemberRepository memberRepository;
    RestTemplate restTemplate = new RestTemplate();

    @TestFactory
    @DisplayName("s3 에 이미지를 업로드 및 수정, 삭제를 할 수 있다.")
    Collection<DynamicTest> uploadImage() {
        //given
        Member member = createAndSaveMember(null);

        return List.of(
                dynamicTest("s3 에 이미지를 업로드하고 presignedurl 을 받는다.", () -> {
                    //given
                    MultipartFile multipartFile =
                            new MockMultipartFile(
                                    "test.png",
                                    "test.png",
                                    "image/png",
                                    "test".getBytes());

                    member.updateImagePath(getImageType(multipartFile));

                    //when
                    String url = s3Service.uploadAndGetUrl(member.getMyInfo().getImage(), multipartFile);

                    //then
                    ResponseEntity<byte[]> response = getResponseEntity(url);
                    assertThat(response.getStatusCodeValue()).isEqualTo(200);
                }),
                dynamicTest("member MyInfo 의 image 로 presignurl 을 받을 수 있다.", () -> {
                    //when
                    String url = s3Service.getPreSignedUrl(member.getMyInfo().getImage());

                    //then
                    assertThat(url).isNotNull();
                }),
                dynamicTest("member MyInfo 의 image 로 image 를 삭제할 수 있다. 해당 경로로 접근하면 NotFound 가 발생한다.", () -> {
                    //when
                    s3Service.deleteImage(member.getMyInfo().getImage());

                    //then
                    String url = s3Service.getPreSignedUrl(member.getMyInfo().getImage());
                    assertThatThrownBy(
                                    () -> getResponseEntity(url))
                            .isInstanceOf(HttpClientErrorException.NotFound.class);
                })
        );

    }

    private ResponseEntity<byte[]> getResponseEntity(String url) throws UnsupportedEncodingException {
        return restTemplate.getForEntity(
                URLDecoder.decode(url, "UTF-8"),
                byte[].class);
    }

    private String getImageType(MultipartFile file){
        return file.getContentType().split("/")[1];
    }

    @Test
    @DisplayName("image 의 PreSignedUrl 을 받을 때 image 경로가 images/ 로 시작하지 않으면 S3PathNotValidException 이 발생한다.")
    void getPreSignedUrlException() {
        //given
        Member member = createAndSaveMember("test.png");

        //when & then
        assertThatThrownBy(
                        () -> s3Service.getPreSignedUrl(member.getMyInfo().getImage()))
                .isInstanceOf(S3PathNotValidException.class)
                .hasMessage("이미지 경로에 오류가 있습니다. 다시 시도해주세요.");
    }

    @Test
    @DisplayName("image 의 PreSignedUrl 을 받을 때 image 경로가 없으면 S3PathNotValidException 이 발생한다.")
    void getPreSignedUrlNullException() {
        //given
        Member member = createAndSaveMember(null);

        //when & then
        assertThatThrownBy(
                        () -> s3Service.getPreSignedUrl(member.getMyInfo().getImage()))
                .isInstanceOf(S3PathNotValidException.class)
                .hasMessage("이미지 경로에 오류가 있습니다. 다시 시도해주세요.");
    }

    @Test
    @DisplayName("이미지를 업로드할 때 파일 확장자가 jpg, jpeg, png 가 아니면 S3FileNotValidException 이 발생한다.")
    void uploadImageException() {
        //given
        Member member = createAndSaveMember("images/test.jpg");

        MultipartFile multipartFile =
                new MockMultipartFile(
                        "test",
                        "test.txt",
                        "text/plain",
                        "test".getBytes()
                );

        //when & then
        assertThatThrownBy(
                        () -> s3Service.uploadAndGetUrl(member.getMyInfo().getImage(), multipartFile))
                .isInstanceOf(S3FileNotValidException.class)
                .hasMessage("유효하지 않은 file 확장자입니다.");
    }

    @Test
    @DisplayName("이미지를 업로드할 때 imagePath 가 null 이면 S3PathNotValidException 이 발생한다.")
    void uploadImagePathNullException() {
        //given
        Member member = createAndSaveMember(null);

        MultipartFile image =
                new MockMultipartFile(
                        "image",
                        "image.png",
                        "image/png",
                        "image".getBytes());

        //when & then
        assertThatThrownBy(
                        () -> s3Service.uploadAndGetUrl(member.getMyInfo().getImage(), image))
                .isInstanceOf(S3PathNotValidException.class)
                .hasMessage("이미지 경로에 오류가 있습니다. 다시 시도해주세요.");
    }

    @Test
    @DisplayName("이미지를 업로드할 때 imagePath 가 images/ 로 시작하지 않으면 S3PathNotValidException 이 발생한다.")
    void uploadImagePathException() {
        //given
        Member member = createAndSaveMember("test.png");

        MultipartFile image =
                new MockMultipartFile(
                        "image",
                        "image.png",
                        "image/png",
                        "image".getBytes());

        //when & then
        assertThatThrownBy(
                        () -> s3Service.uploadAndGetUrl(member.getMyInfo().getImage(), image))
                .isInstanceOf(S3PathNotValidException.class)
                .hasMessage("이미지 경로에 오류가 있습니다. 다시 시도해주세요.");
    }

    @Test
    @DisplayName("이미지를 삭제할 때 imagePath 가 images/ 로 시작하지 않으면 S3PathNotValidException 이 발생한다.")
    void deleteImage() {
        //given
        Member member = createAndSaveMember("test.png");

        //when & then
        assertThatThrownBy(
                        () -> s3Service.deleteImage(member.getMyInfo().getImage()))
                .isInstanceOf(S3PathNotValidException.class)
                .hasMessage("이미지 경로에 오류가 있습니다. 다시 시도해주세요.");
    }

    @Test
    @DisplayName("이미지를 삭제할 때 imagePath 가 null 이면 S3PathNotValidException 이 발생한다.")
    void deleteImagePathNullException() {
        //given
        Member member = createAndSaveMember(null);

        //when & then
        assertThatThrownBy(
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

    private Member createAndSaveMember(String imagePath) {
        Member member = createMember(imagePath);

        memberRepository.save(member);
        return member;
    }
}