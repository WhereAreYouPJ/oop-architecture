package way.application.domain.feed;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import way.application.utils.exception.BadRequestException;
import way.application.utils.exception.ErrorResult;

@Component
@RequiredArgsConstructor
public class FeedDomain {

	public void validateFeedImageSize(List<Integer> feedImageOrders, List<MultipartFile> images) {
		if(feedImageOrders.size() != images.size()) {
			throw new BadRequestException(ErrorResult.DTO_BAD_REQUEST_EXCEPTION);
		}
	}
}
