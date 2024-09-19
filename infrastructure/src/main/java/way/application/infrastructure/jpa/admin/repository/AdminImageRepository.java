package way.application.infrastructure.jpa.admin.repository;

import java.util.List;

import way.application.infrastructure.jpa.admin.entity.AdminImageEntity;

public interface AdminImageRepository {
	AdminImageEntity saveAdminEntity(AdminImageEntity adminEntity);

	AdminImageEntity findByAdminImageSeq(Long adminImageSeq);

	List<AdminImageEntity> findAllAdminImageEntity();

	void deleteAdminImageEntity(AdminImageEntity adminImageEntity);
}
