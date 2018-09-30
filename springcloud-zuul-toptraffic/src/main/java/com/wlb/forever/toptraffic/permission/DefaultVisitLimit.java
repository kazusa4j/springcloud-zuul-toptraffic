package com.wlb.forever.toptraffic.permission;

import org.springframework.beans.factory.annotation.Value;

import static com.wlb.forever.toptraffic.support.TopTrafficConstants.PREFIX;

/**
 * @Auther: william
 * @Date: 18/9/30 13:28
 * @Description:
 */
public class DefaultVisitLimit implements VisitLimit {
    @Value("${" + PREFIX + ".defaultVisitTime}")
    private int defaultVisitTime;

    public DefaultVisitLimit() {

    }

    @Override
    public boolean visitTimeValid(long num) {
        if (num < defaultVisitTime) {
            return true;
        }
        return false;
    }

}
