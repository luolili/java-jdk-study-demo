##DispacherServlet的 noHandlerFound 方法
```$xslt
protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 打印日志
		if (pageNotFoundLogger.isWarnEnabled()) {
			pageNotFoundLogger.warn("No mapping for " + request.getMethod() + " " + getRequestUri(request));
		}
		//抛异常来解决
		if (this.throwExceptionIfNoHandlerFound) {
			throw new NoHandlerFoundException(request.getMethod(), getRequestUri(request),
					new ServletServerHttpRequest(request).getHeaders());
		}
		//输出到页面 的 response
		else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
```

在进入 handler 的 handleRequest方法的时候，首先判断请求路径是否有：  
ResourceHttpRequestHandler
```$xslt
	// For very general mappings (e.g. "/") we need to check 404 first
		Resource resource = getResource(request);
		if (resource == null) {
			logger.debug("Resource not found");
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

```
https://www.jianshu.com/p/b06584591086  
sendError 方法:tomcat的ResponseFacade
```$xslt
   @Override
    public void sendError(int sc)
        throws IOException {

        if (isCommitted()) {
            throw new IllegalStateException
                (sm.getString("coyoteResponse.sendError.ise"));
        }

        response.setAppCommitted(true);

        response.sendError(sc);

    }
```