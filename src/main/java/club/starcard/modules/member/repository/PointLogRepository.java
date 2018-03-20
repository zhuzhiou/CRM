package club.starcard.modules.member.repository;

import club.starcard.modules.member.entity.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wlrllr on 2018/3/20.
 */
@Repository
public interface PointLogRepository  extends JpaRepository<PointLog,Long>,JpaSpecificationExecutor<PointLog> {

    List<PointLog> findByMemberId(Long memberId);
}
