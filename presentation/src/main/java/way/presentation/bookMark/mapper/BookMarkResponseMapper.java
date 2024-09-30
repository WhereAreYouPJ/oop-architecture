package way.presentation.bookMark.mapper;

import static way.application.service.bookMark.dto.response.BookMarkResponseDto.*;
import static way.application.service.feed.dto.response.FeedResponseDto.*;
import static way.presentation.bookMark.vo.response.BookMarkResponseVo.*;
import static way.presentation.feed.vo.response.FeedResponseVo.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import way.application.service.bookMark.dto.response.BookMarkResponseDto;
import way.presentation.bookMark.vo.response.BookMarkResponseVo;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMarkResponseMapper {
	AddBookMarkResponse toAddBookMarkResponse(AddBookMarkResponseDto responseDto);
}
