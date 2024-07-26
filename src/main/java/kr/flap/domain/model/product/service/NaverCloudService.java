package kr.flap.domain.model.product.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import kr.flap.domain.model.product.dto.ImageUploadResponse;
import kr.flap.domain.model.product.dto.UnsplashDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverCloudService {
  @Value("${UNSPLASH_API_KEY}")
  private String unSplashAccessKey;

  @Value("${UNSPLASH_API_URL}")
  private String apiUrl;

  @Value("${NAVER_CLOUD_ACCESS_KEY}")
  private String objectStorageAccessKey;

  @Value("${NAVER_CLOUD_SECRET_KEY}")
  private String objectStorageSecretKey;

  @Value("${NAVER_CLOUD_ENDPOINT}")
  private String endPoint;

  @Value("${NAVER_CLOUD_REGION}")
  private String regionName;

  private final RestTemplate restTemplate;

  public ImageUploadResponse uploadImage(InputStream inputStream, String filename, long contentLength, String contentType) throws IOException {
    final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(objectStorageAccessKey, objectStorageSecretKey)))
            .build();

    String bucketName = "test-image-upload";
    String objectKey = "uploads/" + UUID.randomUUID().toString() + "_" + filename;

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(contentLength);
    metadata.setContentType(contentType);

    PutObjectResult result = s3.putObject(new PutObjectRequest(bucketName, objectKey, inputStream, metadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));
    String objectUrl = s3.getUrl(bucketName, objectKey).toString();
    String eTag = result.getETag();

    return new ImageUploadResponse(objectUrl, eTag);
  }

  private byte[] downloadImage(String downloadUrl) throws IOException {
    URL url = new URL(downloadUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");

    try (InputStream in = connection.getInputStream()) {
      return in.readAllBytes();
    }
  }
}
