/**********************************************************************
Copyright (c) 2009 Asfun Net.

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
package net.asfun.jangod.util;

import java.util.HashMap;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class Variable {

    private String name;

    public Variable(String variable) {
	name = variable;
    }

    public String getName() {
	return name;
    }

    public Object resolve(Object value) {
	ExpressionParser parser = new SpelExpressionParser();
	try {
	    String rootName = name;
	    if (name.contains(".")) rootName = rootName.substring(0, name.indexOf("."));
	    HashMap<String, Object> hashMap = new HashMap<String, Object>();
	    hashMap.put(rootName, value);
	    StandardEvaluationContext context = new StandardEvaluationContext(
		    hashMap);
	    context.addPropertyAccessor(new ReflectivePropertyAccessor());
	    context.addPropertyAccessor(new MapAccessor());
	    return parser.parseExpression(name).getValue(context);
	} catch (Exception e) {
	    return value;
	}
    }

    @Override
    public String toString() {
	return "<Variable: " + name + ">";
    }
}
