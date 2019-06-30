/*
 * Copyright (c) 2018. paascloud.net All Rights Reserved.
 * 项目名称：paascloud快速搭建企业级分布式微服务平台
 * 类名称：AuthHeaderFilter.java
 * 创建人：刘兆明
 * 联系方式：paascloud.net@gmail.com
 * 开源地址: https://github.com/paascloud
 * 博客地址: http://blog.paascloud.net
 * 项目官网: http://paascloud.net
 */

package com.paascloud.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.constants.ZuulConstants;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;

/**
 *
 */


/**
 * 前置 过滤器
 */
@Slf4j
@Component
public class AuthHeaderFilter extends ZuulFilter {

	private static final String BEARER_TOKEN_TYPE = "bearer ";
	private static final String OPTIONS = "OPTIONS";
	private static final String AUTH_PATH = "/auth";
	private static final String LOGOUT_URI = "/oauth/token";
	private static final String ALIPAY_CALL_URI = "/pay/alipayCallback";


	/**
	 * Filter type string.
	 *	过滤器类型选择：
	 *pre 路由前，route 路由中，post 路由后，error 出现错误时
	 * @return the string
	 */
	@Override
	public String filterType() {
		//ZuulConstants;


		return FilterConstants.PRE_TYPE;
	}

	/**
	 * Filter order int.
	 *
	 * @return the int
	 */
	@Override
	public int filterOrder() {

		//return 0;
		return PRE_DECORATION_FILTER_ORDER-1;
	}

	/**
	 * Should filter boolean.
	 *
	 * @return the boolean
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/**
	 * Run object.
	 *
	 * @return the object
	 */
	@Override
	public Object run() {
		log.info("AuthHeaderFilter - 开始鉴权...");
		// 获取当前的请求上下文
		RequestContext requestContext = RequestContext.getCurrentContext();
		//取出当前请求
		HttpServletRequest request = requestContext.getRequest();
		log.info("进入访问过滤器，访问的url:{}，访问的方法：{}",request.getRequestURL(),request.getMethod());
		//从headers中取出key为accessToken值
		String accessToken = request.getHeader("accessToken");//这里我把token写进headers中了
		//这里简单校验下如果headers中没有这个accessToken或者该值为空的情况
		//那么就拦截不入放行，返回401状态码
		if(StringUtils.isEmpty(accessToken)) {
			log.info("当前请求没有accessToken");
			//使用Zuul来过滤这次请求
			requestContext.setSendZuulResponse(false);
			requestContext.setResponseStatusCode(401);
			return null;
		}
		log.info("请求通过过滤器");
		return null;
	}


	private void doSomething(RequestContext requestContext) throws ZuulException {
		HttpServletRequest request = requestContext.getRequest();
		String requestURI = request.getRequestURI();

		/*if (OPTIONS.equalsIgnoreCase(request.getMethod()) || !requestURI.contains(AUTH_PATH) || !requestURI.contains(LOGOUT_URI) || !requestURI.contains(ALIPAY_CALL_URI)) {
			return;
		}
		String authHeader = RequestUtil.getAuthHeader(request);

		if (PublicUtil.isEmpty(authHeader)) {
			throw new ZuulException("刷新页面重试", 403, "check token fail");
		}

		if (authHeader.startsWith(BEARER_TOKEN_TYPE)) {
			requestContext.addZuulRequestHeader(HttpHeaders.AUTHORIZATION, authHeader);

			log.info("authHeader={} ", authHeader);
			// 传递给后续微服务
			requestContext.addZuulRequestHeader(CoreHeaderInterceptor.HEADER_LABEL, authHeader);
		}*/
	}

}
