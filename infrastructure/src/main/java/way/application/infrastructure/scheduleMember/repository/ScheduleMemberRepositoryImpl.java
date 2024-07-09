package way.application.infrastructure.scheduleMember.repository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import way.application.infrastructure.scheduleMember.entity.ScheduleMemberEntity;

@Component
@RequiredArgsConstructor
public class ScheduleMemberRepositoryImpl implements ScheduleMemberRepository {
	private final ScheduleMemberJpaRepository scheduleMemberJpaRepository;

	@Override
	public ScheduleMemberEntity saveScheduleMemberEntity(ScheduleMemberEntity scheduleMemberEntity) {
		return scheduleMemberJpaRepository.save(scheduleMemberEntity);
	}
}
