package sixman.stackoverflow.module.aws.service;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sixman.stackoverflow.global.exception.businessexception.s3exception.S3FileNotValidException;
import sixman.stackoverflow.global.exception.businessexception.s3exception.S3PathNotValidException;
import sixman.stackoverflow.global.exception.businessexception.s3exception.S3UploadException;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

@Service
public class S3Service {

    private final String BUCKET_NAME = "sixman-images";
    private final Region REGION = Region.AP_NORTHEAST_2;
    private final S3Client s3Client;
    private final AwsCredentialsProvider credentialsProvider;

    public S3Service(S3Client s3Client, AwsCredentialsProvider credentialsProvider) {
        this.s3Client = s3Client;
        this.credentialsProvider = credentialsProvider;
    }

    public String getPreSignedUrl(String imagePath) {

        checkValidPath(imagePath);

        S3Presigner presigner = getS3Presigner();

        String preSingedUrl = getPreSignedUrl(presigner, imagePath);

        presigner.close();

        return preSingedUrl;
    }

    public String uploadImage(String imagePath, MultipartFile file) {

        checkValid(imagePath, file);

        S3Presigner presigner = getS3Presigner();

        upload(presigner, imagePath, file);

        String preSingedUrl = getPreSignedUrl(presigner, imagePath);

        presigner.close();

        return preSingedUrl;
    }

    public void deleteImage(String imagePath) {

        checkValidPath(imagePath);

        deleteFromS3(imagePath);
    }

    private S3Presigner getS3Presigner() {
        S3Presigner presigner = S3Presigner.builder()
                .region(REGION)
                .credentialsProvider(credentialsProvider)
                .build();
        return presigner;
    }

    private String getPreSignedUrl(S3Presigner presigner, String imagePath) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(imagePath)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url().toString();
    }

    private void upload(S3Presigner presigner, String imagePath, MultipartFile file) {

        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(imagePath)
                    .contentType(file.getContentType())
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

            URL url = presignedRequest.url();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", file.getContentType());
            connection.setRequestMethod("PUT");

            // 파일 내용을 URL 연결을 통해 전송
            try (OutputStream os = connection.getOutputStream()) {
                IOUtils.copy(file.getInputStream(), os);
            }

            // 연결의 응답 코드 확인 (HTTP 200 OK는 성공을 나타냄)
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new S3UploadException();
            }

        } catch (S3Exception | IOException e) {
            throw new S3UploadException();
        }
    }

    private void deleteFromS3(String imagePath) {

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(imagePath)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    private void checkValid(String imagePath, MultipartFile file) {
        checkValidPath(imagePath);
        checkValidFile(file);
    }

    private void checkValidFile(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null || !isImage(contentType)) {
            throw new S3FileNotValidException();
        }
    }

    private boolean isImage(String contentType) {

        return contentType.equals("image/jpg") || contentType.equals("image/jpeg") || contentType.equals("image/png");
    }

    private void checkValidPath(String imagePath) {

        if(imagePath == null || !imagePath.startsWith("images/")) throw new S3PathNotValidException();
    }
}
