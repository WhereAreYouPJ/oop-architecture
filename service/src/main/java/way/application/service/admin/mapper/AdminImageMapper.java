package way.application.service.admin.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.admin.entity.AdminImageEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminImageMapper {
	@Mapping(target = "adminImageSeq", ignore = true)
	@Mapping(target = "imageURL", source = "imageURL")
	AdminImageEntity toAdminEntity(String imageURL);
}
