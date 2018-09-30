package com.wlb.forever.toptraffic.service.impl;

import com.wlb.forever.toptraffic.domain.VisitMonitor;
import com.wlb.forever.toptraffic.support.TopTrafficCrudRepository;

/**
 * @Auther: william
 * @Date: 18/9/28 16:31
 * @Description:
 */
public class JpaTopTrafficServiceImpl extends AbstractTopTrafficServiceImpl {
    private final TopTrafficCrudRepository repository;

    public JpaTopTrafficServiceImpl(TopTrafficCrudRepository topTrafficCrudRepository) {
        this.repository = topTrafficCrudRepository;
    }

    @Override
    protected void insertVisitMonitor(VisitMonitor visitMonitor) {
        repository.save(visitMonitor);
    }

    @Override
    protected void insertStatistics(String key, String value) {

    }

    @Override
    protected long selectStatisticsNumber(String key) {
        return 0;
    }
}
