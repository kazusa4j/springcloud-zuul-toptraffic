package com.wlb.forever.toptraffic.service.impl;

import com.wlb.forever.toptraffic.domain.VisitMonitor;
import com.wlb.forever.util.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import static com.wlb.forever.toptraffic.support.TopTrafficConstants.PREFIX;

/**
 * @Auther: william
 * @Date: 18/9/28 16:31
 * @Description:
 */
public class RedisTopTrafficServiceImpl extends AbstractTopTrafficServiceImpl {
    private final RedisUtil redisUtil;

    @Value("${" + PREFIX + ".defaultRedisStatisticsTime}")
    private long defaultRedisStatisticsTime;

    public RedisTopTrafficServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisUtil = new RedisUtil(redisTemplate);
    }

    @Override
    public void insertVisitMonitor(VisitMonitor visitMonitor) {
        redisUtil.lSet("visitlog", visitMonitor);
    }

    @Override
    protected void insertStatistics(String key, String value) {
        redisUtil.sSetAndTime(key,defaultRedisStatisticsTime*60,value);
    }

    @Override
    protected long selectStatisticsNumber(String key) {
        return redisUtil.sGetSetSize(key);
    }
}
