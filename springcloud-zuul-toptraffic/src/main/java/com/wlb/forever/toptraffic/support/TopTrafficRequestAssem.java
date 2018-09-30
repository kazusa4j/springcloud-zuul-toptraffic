package com.wlb.forever.toptraffic.support;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: william
 * @Date: 18/9/30 11:12
 * @Description:
 */
public class TopTrafficRequestAssem {

    /**
     * 初始化访问统计KEY
     * @param request
     * @return
     */
    public static String initStatisticsKey(HttpServletRequest request) {
        String ip=getIpAddr(request);
        String basePath=getBasePath(request);
        StringBuffer sb=new StringBuffer();
        sb.append(ip);
        sb.append(":");
        sb.append(basePath);
        return sb.toString();
    }

    /**
     * 获取URL项目路径
     * @param request
     * @return
     */
    public static String getBasePath(HttpServletRequest request) {
        StringBuffer sb=new StringBuffer();
        String uri =request.getRequestURI();
        String[] uriArr=uri.split("/");
        sb.append(request.getScheme());
        sb.append("://");
        sb.append(request.getServerName());
        sb.append(":");
        sb.append(request.getServerPort());
        if(uriArr.length>0){
            sb.append("/");
            sb.append(uriArr[1]);
        }
        return sb.toString();
    }

    /**
     *
     * @param time
     * @param cell
     * @return
     */
    public static Long getTimeRound(Long time,int cell){
        return time/cell;
    }

    /**
     * 获取访问请求ip
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
