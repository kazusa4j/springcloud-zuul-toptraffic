package com.wlb.forever.toptraffic.support;

import com.wlb.forever.toptraffic.domain.VisitMonitor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: william
 * @Date: 18/9/28 16:31
 * @Description:
 */
public interface TopTrafficCrudRepository extends JpaRepository<VisitMonitor,String> {
}
