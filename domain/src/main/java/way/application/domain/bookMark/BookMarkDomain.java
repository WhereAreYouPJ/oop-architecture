package way.application.domain.bookMark;

import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.bookMark.entity.QBookMarkEntity;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;

@Component
@RequiredArgsConstructor
public class BookMarkDomain {
	private final JPAQueryFactory queryFactory;

	public boolean isFeedBookMarkedByMember(FeedEntity feedEntity, MemberEntity memberEntity) {
		QBookMarkEntity bookMark = QBookMarkEntity.bookMarkEntity;

		Integer count = queryFactory
			.selectOne()
			.from(bookMark)
			.where(
				bookMark.feedEntity.eq(feedEntity)
					.and(bookMark.memberEntity.eq(memberEntity))
			)
			.fetchFirst();

		return count != null;
	}
}
