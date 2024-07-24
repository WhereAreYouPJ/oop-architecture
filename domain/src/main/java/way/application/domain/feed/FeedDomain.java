package way.application.domain.feed;

import org.springframework.stereotype.Component;

import way.application.infrastructure.feed.entity.FeedEntity;

@Component
public class FeedDomain {
	// Toggle hide
	public FeedEntity toggleHide(FeedEntity feed) {
		return feed.toBuilder().hide(!feed.getHide()).build();
	}
}
