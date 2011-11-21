package net.asfun.jangod.spring;

import static org.springframework.format.datetime.joda.JodaTimeContextHolder.getJodaTimeContext;

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.asfun.jangod.template.TemplateEngine;

import org.joda.time.DateTimeZone;
import org.springframework.format.datetime.joda.JodaTimeContext;
import org.springframework.format.datetime.joda.JodaTimeContextHolder;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.InternalResourceView;

/**
 * JangodView
 * 
 * @author igor.mihalik
 */
public class JangodView extends InternalResourceView {

    private static final String PARAM_TIMEZONE = "timezone";
    private TemplateEngine engine;

    public JangodView() throws Exception {
	engine = new TemplateEngine();
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
	    HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	setTimeZoneForDateTimeFilter();

	String templateFile = getServletContext().getRealPath(getUrl());
	{
	    // ThemeResolver themeResolver = RequestContextUtils
	    // .getThemeResolver(request);
	    // String theme = themeResolver.resolveThemeName(request);
	    // if (logger.isDebugEnabled()) {
	    // logger.debug("Current theme is " + theme);
	    // }
	    // templateFile = theme + File.separator + getUrl();
	    // templateFile = getServletContext().getRealPath(templateFile);
	}
	Locale locale = RequestContextUtils.getLocale(request);
	response.setContentType(getContentType());
	engine.process(templateFile, model, response.getWriter(), locale);
    }

    private void setTimeZoneForDateTimeFilter() {
	JodaTimeContext jodaTimeContext = getJodaTimeContext();
	// check JodaTimeContext first and use it if set
	if (jodaTimeContext != null)
	    engine.getConfiguration().setTimezone(
		    jodaTimeContext.getTimeZone().toTimeZone());
	else { // try attrib param next
	    String timeZone = (String) getAttributesMap().get(PARAM_TIMEZONE);
	    if (timeZone != null) {
		engine.getConfiguration().setTimezone(
			TimeZone.getTimeZone(timeZone));
		JodaTimeContext context = JodaTimeContextHolder
			.getJodaTimeContext();
		if (context == null)
		    context = new JodaTimeContext();
		context.setTimeZone(DateTimeZone.forID(timeZone));
		JodaTimeContextHolder.setJodaTimeContext(context);
	    }
	}
    }
}
