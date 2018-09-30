package com.wlb.forever.toptraffic.filter;

import com.netflix.zuul.context.RequestContext;
import com.wlb.forever.toptraffic.service.TopTrafficService;
import com.wlb.forever.toptraffic.domain.VisitMonitor;
import com.wlb.forever.toptraffic.support.TopTrafficConstants;
import com.wlb.forever.toptraffic.support.TopTrafficRequestAssem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import static com.wlb.forever.toptraffic.support.TopTrafficConstants.PREFIX;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;


/**
 * @Auther: william
 * @Date: 18/9/28 16:31
 * @Description:
 */
@Slf4j
public class TopTrafficPostFilter extends AbstractTopTrafficFilter {

    private final TopTrafficService topTrafficService;

    @Value("${" + PREFIX + ".defaultVisitIntervalTime}")
    private int defaultVisitTimeInterval;

    public TopTrafficPostFilter(TopTrafficService topTrafficService) {
        this.topTrafficService = topTrafficService;
    }

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_RESPONSE_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        int requestTraffic = getResponseDataStreamSize();
        String requestIp = TopTrafficRequestAssem.getIpAddr(request);
        String requestUrl = getRequestAddr(request);
        if(getRequestStartTime()==null){
            return null;
        }
        Long timeConsum = System.currentTimeMillis() - getRequestStartTime();
        VisitMonitor visitMonitor = new VisitMonitor(UUID.randomUUID().toString(), requestUrl, requestIp, new Date(), timeConsum, requestTraffic, ctx.getResponseStatusCode(), null);
        topTrafficService.setVisitMonitor(visitMonitor);
        String statisticsKey=TopTrafficRequestAssem.initStatisticsKey(request);
        long totalMinutes = TopTrafficRequestAssem.getTimeRound(System.currentTimeMillis(),defaultVisitTimeInterval*1000);
        topTrafficService.setStatistics(statisticsKey,totalMinutes+"");
        return null;
    }

    /**
     * 获取返回数据流量
     *
     * @return
     */
    private int getResponseDataStreamSize() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int responseDataStreamSize = 0;
        try {
            InputStream is = ctx.getResponseDataStream();
            if (null == is) {
                return responseDataStreamSize;
            }
            baos = cloneInputStream(is);
            responseDataStreamSize = baos.size();
            final InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
            ctx.setResponseDataStream(inputStream);
            baos.close();
            return responseDataStreamSize;
        } catch (Exception e) {
            log.error("获取数据流量大小出现异常!");
            return responseDataStreamSize;
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    log.error("ByteArrayOutputStream关闭失败!");
                    return responseDataStreamSize;
                }
            }
        }
    }

    /**
     * 获取请求开始时间
     *
     * @return
     */
    private Long getRequestStartTime() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        final HttpServletRequest request = ctx.getRequest();
        return (Long) request.getAttribute(TopTrafficConstants.REQUEST_START_TIME);
    }

    /**
     * InputSteam转ByteArrayOutputStream
     *
     * @param input
     * @return
     */
    private ByteArrayOutputStream cloneInputStream(InputStream input) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            log.error("");
            return null;
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                log.error("cloneInputStreamc出现异常!");
                return null;
            }
        }
    }



    /**
     * 获取请求URL
     *
     * @param request
     * @return
     */
    private String getRequestAddr(HttpServletRequest request) {
        String requestPath = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (queryString != null) {
            return requestPath + "?" + request.getQueryString();
        } else {
            return requestPath;
        }
    }

}
