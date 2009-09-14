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
package net.asfun.template.tag;

import java.util.List;

import net.asfun.template.compile.CompilerException;
import net.asfun.template.compile.JangodCompiler;
import net.asfun.template.compile.Node;
import net.asfun.template.compile.Tag;
import net.asfun.template.util.HelperStringTokenizer;

/**
 * {% cycle a,b,c %}   
 * {% cycle a,'b',c as d %} 
 * {% cycle d %}
 * @author anysome
 *
 */
public class CycleTag implements Tag{
	

	@Override
	public String compile(List<Node> carries, String helpers, JangodCompiler compiler)
			throws CompilerException {
		String[] values;
		String var = null;
		HelperStringTokenizer tk = new HelperStringTokenizer(helpers);
		//TODO tokenize in one time
		String[] helper = tk.allTokens();
		if (helper.length == 1) {
			HelperStringTokenizer items = new HelperStringTokenizer(helper[0]);
			items.splitComma(true);
			values = items.allTokens();
			Integer forindex = (Integer) compiler.retraceVariable("loop.index");
			if (forindex == null) {
				forindex = 0;
			}
			if (values.length == 1) {
				var = values[0];
				values = (String[]) compiler.retraceVariable(var);
				if ( values == null ) {
					return compiler.resolveString(var);
				}
			} else {
				for(int i=0; i<values.length; i++) {
					values[i] = compiler.resolveString(values[i]);
				}
			}
			return values[forindex % values.length];
		} else if (helper.length == 3) {
			HelperStringTokenizer items = new HelperStringTokenizer(helper[0]);
			items.splitComma(true);
			values = items.allTokens();
			for(int i=0; i<values.length; i++) {
				values[i] = compiler.resolveString(values[i]);
			}
			var = helper[2];
			compiler.assignRuntimeScope(var, values);
			return "";
		} else {
			throw new CompilerException("cycle tag expects 1 or 3 helper(s) >>> " + helper.length);
		}
	}

	@Override
	public String getEndTagName() {
		return null;
	}

	@Override
	public String getName() {
		return "cycle";
	}

}
