package way.application.infrastructure.jpa.admin.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.jpa.admin.entity.AdminImageEntity;

@Component
@RequiredArgsConstructor
public class AdminImageRepositoryImpl implements AdminImageRepository {
	private final AdminImageJpaRepository adminJpaRepository;

	@Override
	public AdminImageEntity saveAdminEntity(AdminImageEntity adminEntity) {
		return adminJpaRepository.save(adminEntity);
	}

	@Override
	public AdminImageEntity findByAdminImageSeq(Long adminImageSeq) {
		return adminJpaRepository.findById(adminImageSeq)
			.orElseThrow(() -> new IllegalArgumentException("IMAGE SEQ 오류"));
	}

	@Override
	public List<AdminImageEntity> findAllAdminImageEntity() {
		return adminJpaRepository.findAll();
	}

	@Override
	public void deleteAdminImageEntity(AdminImageEntity adminImageEntity) {
		adminJpaRepository.delete(adminImageEntity);
	}
}
