package org.springframework.web.servlet.view.jangod;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.asfun.jangod.template.TemplateEngine;

import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.InternalResourceView;

public class JangodView extends InternalResourceView {

    private TemplateEngine engine;

    public JangodView() throws Exception {
	engine = new TemplateEngine();
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
	    HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
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
	engine.process(templateFile, model, response.getWriter(), locale);
    }

}
