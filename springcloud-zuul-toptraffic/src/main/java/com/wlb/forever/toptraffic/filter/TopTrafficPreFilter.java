package com.wlb.forever.toptraffic.filter;

import com.netflix.zuul.context.RequestContext;
import com.wlb.forever.toptraffic.permission.VisitLimit;
import com.wlb.forever.toptraffic.service.TopTrafficService;
import com.wlb.forever.toptraffic.support.TopTrafficConstants;
import com.wlb.forever.toptraffic.support.TopTrafficRequestAssem;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;

/**
 * @Auther: william
 * @Date: 18/9/28 16:31
 * @Description:
 */
public class TopTrafficPreFilter extends AbstractTopTrafficFilter {
    private final TopTrafficService topTrafficService;

    private final VisitLimit visitLimit;

    public TopTrafficPreFilter(TopTrafficService topTrafficService,VisitLimit visitLimit) {
        this.topTrafficService = topTrafficService;
        this.visitLimit=visitLimit;
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        final HttpServletRequest request = ctx.getRequest();
        String statisticsKey = TopTrafficRequestAssem.initStatisticsKey(request);
        long num=topTrafficService.getStatisticsMumber(statisticsKey);
        if(!visitLimit.visitTimeValid(num)){
            ctx.setSendZuulResponse(false); //不对其进行路由
            ctx.setResponseStatusCode(401);
            ctx.setResponseBody("Exceeding maximum permitted access times !");
            ctx.set("isSuccess", false);
            return null;
        }
        request.setAttribute(TopTrafficConstants.REQUEST_START_TIME, System.currentTimeMillis());
        return null;
    }
}
