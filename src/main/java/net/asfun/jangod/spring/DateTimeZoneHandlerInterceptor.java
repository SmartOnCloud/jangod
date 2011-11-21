package net.asfun.jangod.spring;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTimeZone;
import org.springframework.format.datetime.joda.JodaTimeContext;
import org.springframework.format.datetime.joda.JodaTimeContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

public class DateTimeZoneHandlerInterceptor extends HandlerInterceptorAdapter {
    private CookieGenerator cookieGen = new CookieGenerator();

    private String cookieName = "timeZone";
    private String reqParam = "tz";
    private String defaultTimeZone = "GMT";

    @PostConstruct
    public void init() {
	cookieGen.setCookieName(cookieName);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
	    HttpServletResponse response, Object handler) throws Exception {
	JodaTimeContext context = new JodaTimeContext();
	String timezone = request.getParameter(reqParam);
	if (timezone == null) {
	    Cookie cookie = WebUtils.getCookie(request, cookieName);
	    if (cookie == null)
		timezone = defaultTimeZone;
	    else
		timezone = cookie.getValue();
	}
	System.out.println(timezone);
	cookieGen.addCookie(response, timezone);
	context.setTimeZone(DateTimeZone.forID(timezone));
	JodaTimeContextHolder.setJodaTimeContext(context);
	return true;
    }

    public void postHandle(HttpServletRequest request,
	    HttpServletResponse response, Object handler,
	    ModelAndView modelAndView) throws Exception {
	if (modelAndView != null) {
	    modelAndView.addObject("jodaTimeContext",
		    JodaTimeContextHolder.getJodaTimeContext());

	}
    }

    public void setCookieName(String cookieName) {
	this.cookieName = cookieName;
    }

    public void setReqParam(String reqParam) {
	this.reqParam = reqParam;
    }

    public void setDefaultTimeZone(String defaultTimeZone) {
	this.defaultTimeZone = defaultTimeZone;

    }
}
