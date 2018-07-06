package com.unicom.utils;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

import com.google.gson.Gson;

import javassist.runtime.Desc;

public class HttpServletRequestReplacedFilter implements Filter {
	private static Logger logger = Logger.getLogger(HttpServletRequestReplacedFilter.class);

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		response.setCharacterEncoding("UTF-8");
		HttpServletRequest requestWrapper = null;
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;

			if ("POST".equals(httpServletRequest.getMethod().toUpperCase())
					&& httpServletRequest.getContentType().equalsIgnoreCase("application/json")) {
				requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);
				// String url = requestWrapper.getRequestURL().toString();
				String reqStr = CommonMethod.ReadInputStreamFromClientRequest(requestWrapper);
				logger.info(reqStr);
				chain.doFilter(requestWrapper, response);

			}
		}

		if (requestWrapper == null) {
			chain.doFilter(request, response);
		}
		// else {
		// chain.doFilter(requestWrapper, response);
		// }
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
