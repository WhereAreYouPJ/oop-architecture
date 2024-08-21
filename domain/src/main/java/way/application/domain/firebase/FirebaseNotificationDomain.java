package way.application.domain.firebase;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import way.application.domain.firebase.vo.FcmMessageVo;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.utils.exception.ErrorResult;
import way.application.utils.exception.ServerException;

@Component
@RequiredArgsConstructor
public class FirebaseNotificationDomain {

	private final String API_URL = "https://fcm.googleapis.com/v1/projects/whereareyou-34289/messages:send";
	private final ObjectMapper objectMapper;

	/**
	 * @param targetMemberEntity
	 * @param sendMemberEntity
	 *
	 * sendMemberEntity -> targetMemberEntity 에게 푸시 알림 보내기
	 * 추후 친구 추가와 관련된 로직의 경우 하드 코딩되어 있는 푸시 알림 메세지 Constant 변수로 수정하여 인자로 입력
	 */
	public void sendNotification(MemberEntity targetMemberEntity, MemberEntity sendMemberEntity) {
		try {
			String body = sendMemberEntity.getUserName() + "이(가) 일정 초대를 했습니다.";
			sendMessageTo(targetMemberEntity.getFireBaseTargetToken(), "일정 요청이 들어왔습니다.", body);
		} catch (IOException e) {
			throw new ServerException(ErrorResult.FIREBASE_CLOUD_MESSAGING_EXCEPTION);
		}
	}

	public void sendFriendRequestNotification(MemberEntity targetMemberEntity, MemberEntity sendMemberEntity) {
		try {
			String body = sendMemberEntity.getUserName() + "이(가) 친구 요청을 보냈습니다.";
			sendMessageTo(targetMemberEntity.getFireBaseTargetToken(), "친구 요청이 들어왔습니다.", body);
		} catch (IOException e) {
			throw new ServerException(ErrorResult.FIREBASE_CLOUD_MESSAGING_EXCEPTION);
		}
	}

	public void acceptNotification(MemberEntity targetMemberEntity, MemberEntity sendMemberEntity) {
		try {
			String body = sendMemberEntity.getUserName() + "이(가) 친구 요청을 수락했습니다..";
			sendMessageTo(targetMemberEntity.getFireBaseTargetToken(), "친구 요청을 수락했습니다.", body);
		} catch (IOException e) {
			throw new ServerException(ErrorResult.FIREBASE_CLOUD_MESSAGING_EXCEPTION);
		}
	}

	private void sendMessageTo(String targetToken, String title, String body) throws IOException {
		String message = makeMessage(targetToken, title, body);

		OkHttpClient client = new OkHttpClient();
		RequestBody requestBody = RequestBody.create(message,
			MediaType.get("application/json; charset=utf-8"));
		Request request = new Request.Builder()
			.url(API_URL)
			.post(requestBody)
			.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
			.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
			.build();

		Response response = client.newCall(request).execute();

		System.out.println(response.body().string());
	}

	private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
		FcmMessageVo fcmMessage = FcmMessageVo.builder()
			.message(FcmMessageVo.Message.builder()
				.token(targetToken)
				.notification(FcmMessageVo.Notification.builder()
					.title(title)
					.body(body)
					.image(null)
					.build()
				).build()).validateOnly(false).build();

		return objectMapper.writeValueAsString(fcmMessage);
	}

	private String getAccessToken() throws IOException {
		String firebaseConfigPath = "firebase/firebase_service_key.json";

		GoogleCredentials googleCredentials = GoogleCredentials
			.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
			.createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

		googleCredentials.refreshIfExpired();
		return googleCredentials.getAccessToken().getTokenValue();
	}
}
