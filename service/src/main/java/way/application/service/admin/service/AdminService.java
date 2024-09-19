package way.application.service.admin.service;

import static way.application.service.admin.dto.request.AdminRequestDto.*;
import static way.application.service.admin.dto.response.AdminResponseDto.*;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.admin.entity.AdminImageEntity;
import way.application.infrastructure.jpa.admin.repository.AdminImageRepository;
import way.application.service.admin.mapper.AdminImageMapper;
import way.application.utils.s3.S3Utils;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final AdminImageRepository adminImageRepository;

	private final S3Utils s3Utils;
	private final AdminImageMapper adminMapper;

	@Transactional
	public AddHomeImageResponseDto addHomeImage(AddHomeImageRequestDto addHomeImageRequestDto) throws IOException {
		String homeImageUrl = s3Utils.uploadMultipartFile(addHomeImageRequestDto.homeImage());

		AdminImageEntity adminEntity = adminImageRepository.saveAdminEntity(adminMapper.toAdminEntity(homeImageUrl));

		return new AddHomeImageResponseDto(adminEntity.getAdminImageSeq());
	}

	@Transactional(readOnly = true)
	public GetHomeImageResponseDto getHomeImage(GetHomeImageRequestDto getHomeImageRequestDto) {
		/*
		 1. Admin Image 유효성 검사
		*/
		AdminImageEntity adminImageEntity
			= adminImageRepository.findByAdminImageSeq(getHomeImageRequestDto.adminImageSeq());

		return new GetHomeImageResponseDto(adminImageEntity.imageURL);
	}

	@Transactional
	public void deleteHomeImage(DeleteHomeImageRequestDto deleteHomeImageRequestDto) {
		/*
		 1. Admin Image 유효성 검사
		*/
		AdminImageEntity adminImageEntity
			= adminImageRepository.findByAdminImageSeq(deleteHomeImageRequestDto.adminImageSeq());

		adminImageRepository.deleteAdminImageEntity(adminImageEntity);
	}
}
