package com.zz.map.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {
    //一级域名下，二级域名能读到，同级的读不到
    private final static String COOKIE_DOMAIN= "localhost";
    private final static String COOKIE_NAME="auth";
    //写入cookie
    public static void writeLoginToken(HttpServletResponse response, String token){
        Cookie ck = new Cookie(COOKIE_NAME,token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/"); //代表设置在根目录
        ck.setMaxAge(60*60*24*365); //如果是-1 代表永久,如果不设置这个则cookie不写入硬盘只写入内存
        ck.setHttpOnly(true);//防止脚本访问cookie
        log.info("write cookieNmae:{},cookieValue:{}",ck.getName(),ck.getValue());
        response.addCookie(ck);
    }


    //读取cookie
    //domain和/的关系：
    /** e.g.
     * a.zz.com                 cookie: domain :a.zz.com;path : /
     * b.zz.com                 cookie: domain :b.zz.com;path : /
     * a.zz.com/test/cc         cookie: domain :a.zz.com;path : /test/cc
     * a.zz.com/test/dd         cookie: domain :a.zz.com;path : /test/dd
     * a.zz.com/test            cookie: domain :a.zz.com;path : /test
     *
     * 后三个能共享第一个的cookie
     * 第三个第四个能拿到最后一个的cookie
     */
    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cks = request.getCookies();
        if(cks!=null){
            for(Cookie cookie : cks){
                log.info("read cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
                if(StringUtils.equals(cookie.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    //删除cookie
    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cks = request.getCookies();
        if(cks!=null) {
            for (Cookie cookie : cks) {
                if(StringUtils.equals(cookie.getName(),COOKIE_NAME)){
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    log.info("delete cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
                }
            }
        }
    }
}
