package com.pl.reegi.common;

import com.alibaba.fastjson.JSON;
import com.pl.reegi.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {
    public final static AntPathMatcher antPathMatcher = new AntPathMatcher();

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request =  (HttpServletRequest) req;
        HttpServletResponse response =  (HttpServletResponse)resp;

        BaseContext.setCurrentId((Long)request.getSession().getAttribute("USER"));

        String URL = request.getRequestURI();

        log.info("拦截到的路径： "+URL);

        //定义不需要处理的路径
        String []urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };
        //匹配路径是否一致
        boolean check = check(urls, URL);

        if(check){
            log.info("路径匹配成功");
            chain.doFilter(request,response);
            return;
        }
        //判断用户是否登录，及判断session里是否存有user
        if(request.getSession().getAttribute("USER")!=null){
            log.info("用户已登录");
            chain.doFilter(request,response);
            return;
        }

        //判断前台用户是否登录
        if(request.getSession().getAttribute("user")!=null){
            log.info("用户已登录");
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("user"));
            chain.doFilter(request,response);
            return;
        }
        //如果没有登录，向客户端写值
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    public boolean check(String []urls,String url){
        for (String s : urls) {
            boolean match = antPathMatcher.match(s, url);
            if (match){
                return true;
            }
        }
        return false;
    }

}
