package com.crm.repository;

import com.crm.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    @Query("update Point p set p.totalPoint = p.totalPoint+?1 where p.memberId=?2")
    void addPoint(Long point,Long memberId);
}
