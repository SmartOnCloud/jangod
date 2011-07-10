/**********************************************************************
Copyright (c) 2010 Asfun Net.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 **********************************************************************/
package net.asfun.jangod.base;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class Context {

    public static final int SCOPE_GLOBAL = 1;
    public static final int SCOPE_SESSION = 2;

    protected Map<String, Object> sessionBindings;
    protected Application application;
    private ExpressionParser parser = new SpelExpressionParser();
    private StandardEvaluationContext context = new StandardEvaluationContext();

    public Context() {
	this(null);
    }

    public Context(Application application) {
	if (application == null) {
	    application = new Application();
	}
	this.application = application;
	sessionBindings = new HashMap<String, Object>();
	context.addPropertyAccessor(new ReflectivePropertyAccessor());
	context.addPropertyAccessor(new MapAccessor());
    }

    public Application getApplication() {
	return application;
    }

    public Configuration getConfiguration() {
	return application.config;
    }

    public Object getAttribute(String varName, int scope) {
	switch (scope) {
	case SCOPE_GLOBAL:
	    return application.globalBindings.get(varName);
	case SCOPE_SESSION:
	    return sessionBindings.get(varName);
	default:
	    return null;
	}
    }

    public Object getAttribute(String varName) {
	try {
	    context.setRootObject(sessionBindings);
	    return parser.parseExpression(varName).getValue(context);
	} catch (Exception e) {
	    // ignore. try global application global bindings
	}
	try {
	    context.setRootObject(application.globalBindings);
	    return parser.parseExpression(varName).getValue(context);
	} catch (Exception e) {
	    return null;
	}
    }

    public void setAttribute(String varName, Object value, int scope) {
	switch (scope) {
	case SCOPE_GLOBAL:
	    application.globalBindings.put(varName, value);
	    break;
	case SCOPE_SESSION:
	    sessionBindings.put(varName, value);
	    break;
	default:
	    throw new IllegalArgumentException("Illegal scope value.");
	}
    }

    public void initBindings(Map<String, Object> bindings, int scope) {
	switch (scope) {
	case SCOPE_GLOBAL:
	    application.globalBindings = bindings;
	    break;
	case SCOPE_SESSION:
	    sessionBindings = bindings;
	    break;
	default:
	    throw new IllegalArgumentException("Illegal scope value.");
	}
    }

    public void setAttributes(Map<String, Object> bindings, int scope) {
	switch (scope) {
	case SCOPE_GLOBAL:
	    application.globalBindings.putAll(bindings);
	    break;
	case SCOPE_SESSION:
	    sessionBindings.putAll(bindings);
	    break;
	default:
	    throw new IllegalArgumentException("Illegal scope value.");
	}
    }

    public void reset(int scope) {
	switch (scope) {
	case SCOPE_GLOBAL:
	    application.globalBindings.clear();
	    break;
	case SCOPE_SESSION:
	    sessionBindings.clear();
	    break;
	default:
	    throw new IllegalArgumentException("Illegal scope value.");
	}
    }

}
