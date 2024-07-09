package way.application.utils.s3;

import static java.util.UUID.*;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class S3Utils {
	private AmazonS3 s3client;
	private String bucketName;

	public S3Utils(@Value("${cloud.aws.s3.bucket}") String bucketName, AmazonS3 s3client) {
		this.bucketName = bucketName;
		this.s3client = s3client;
	}

	public String uploadMultipartFile(MultipartFile multipartFile) throws IOException {
		String fileName = randomUUID().toString() + ".jpg";
		InputStream inputStream = multipartFile.getInputStream();

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(multipartFile.getSize());
		metadata.setContentType(multipartFile.getContentType());

		uploadFile(fileName, inputStream, metadata);
		return getFileUrl(fileName);
	}

	private void uploadFile(String fileName, InputStream inputStream, ObjectMetadata metadata) {
		s3client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));
	}

	private String getFileUrl(String fileName) {
		return s3client.getUrl(bucketName, fileName).toString();
	}
}
