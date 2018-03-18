package club.starcard.modules.member.repository;

import club.starcard.modules.member.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    @Modifying
    @Transactional
    @Query("update Point p set p.totalPoint = p.totalPoint+?1 where p.memberId=?2")
    void addPoint(Long point,Long memberId);
}
