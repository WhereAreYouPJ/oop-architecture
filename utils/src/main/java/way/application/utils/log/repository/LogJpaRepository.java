package way.application.utils.log.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import way.application.utils.log.entity.LogEntity;

import java.util.List;


@Repository
public interface LogJpaRepository extends JpaRepository<LogEntity, Long> {

    @Query("SELECT l FROM LogEntity l " +
            "WHERE (:level IS NULL OR l.httpStatus = :level) " +
            "AND (:startDate IS NULL OR l.timestamp >= :startDate) " +
            "AND (:endDate IS NULL OR l.timestamp <= :endDate)" +
            "order by l.timestamp desc ")
    List<LogEntity> searchLogs(
            @Param("level") Integer level,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );
}

