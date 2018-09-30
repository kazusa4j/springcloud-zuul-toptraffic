package com.wlb.forever.toptraffic.service.impl;

import com.wlb.forever.toptraffic.domain.VisitMonitor;
import com.wlb.forever.toptraffic.service.TopTrafficService;

/**
 * @Auther: william
 * @Date: 18/9/28 16:31
 * @Description:
 */
public abstract class AbstractTopTrafficServiceImpl implements TopTrafficService {
    @Override
    public void setVisitMonitor(VisitMonitor visitMonitor) {
        insertVisitMonitor(visitMonitor);
    }

    @Override
    public void setStatistics(String key ,String value) {
        insertStatistics(key,value);
    }

    @Override
    public long getStatisticsMumber(String key) {
        return selectStatisticsNumber(key);
    }

    protected abstract void insertVisitMonitor(VisitMonitor visitMonitor);

    protected abstract void insertStatistics(String key ,String value);

    protected abstract long selectStatisticsNumber(String key);


}
