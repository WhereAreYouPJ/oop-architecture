package way.application.infrastructure.jpa.admin.repository;

import way.application.infrastructure.jpa.admin.entity.AdminImageEntity;

public interface AdminImageRepository {
	AdminImageEntity saveAdminEntity(AdminImageEntity adminEntity);

	AdminImageEntity findByAdminImageSeq(Long adminImageSeq);

	void deleteAdminImageEntity(AdminImageEntity adminImageEntity);
}
