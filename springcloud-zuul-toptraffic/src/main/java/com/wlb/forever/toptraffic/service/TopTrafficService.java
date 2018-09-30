package com.wlb.forever.toptraffic.service;

import com.wlb.forever.toptraffic.domain.VisitMonitor;

/**
 * @Auther: william
 * @Date: 18/9/28 16:31
 * @Description:
 */
public interface TopTrafficService {
    void setVisitMonitor(VisitMonitor visitMonitor);

    void setStatistics(String key, String value);

    long getStatisticsMumber(String key);
}

