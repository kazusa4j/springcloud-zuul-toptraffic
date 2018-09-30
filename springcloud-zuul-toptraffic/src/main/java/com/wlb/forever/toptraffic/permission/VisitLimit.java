package com.wlb.forever.toptraffic.permission;

/**
 * @Auther: william
 * @Date: 18/9/30 13:30
 * @Description:
 */
public interface VisitLimit {
    boolean visitTimeValid(long num);
}
