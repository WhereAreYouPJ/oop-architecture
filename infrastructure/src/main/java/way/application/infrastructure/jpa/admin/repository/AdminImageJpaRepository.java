package way.application.infrastructure.jpa.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import way.application.infrastructure.jpa.admin.entity.AdminImageEntity;

@Repository
public interface AdminImageJpaRepository extends JpaRepository<AdminImageEntity, Long> {

}
