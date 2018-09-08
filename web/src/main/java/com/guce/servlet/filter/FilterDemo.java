package com.guce.servlet.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FilterDemo implements Filter {

    private Logger logger = LoggerFactory.getLogger(FilterDemo.class);

    private static Set<String> filterURISet ;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        filterURISet = new HashSet<>();
        filterURISet.add("/favicon.ico");
        filterURISet.add("/error");
        filterURISet.add("/");

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String uri = request.getRequestURI();
        if(StringUtils.isBlank(uri) || filterURISet.contains(uri)){
            return;
        }

        logger.warn(" spring boot filter start asyn handler uri:{} ; curr thread:{} ; time: {}",uri,Thread.currentThread().getName(),System.currentTimeMillis());

        filterChain.doFilter(servletRequest,servletResponse);

        logger.warn(" spring boot filter end asyn handler uri:{} ; curr thread:{} ; time: {}",uri,Thread.currentThread().getName(),System.currentTimeMillis());
    }

    @Override
    public void destroy() {

    }
}
