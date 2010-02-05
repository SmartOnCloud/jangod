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
package net.asfun.jangod.lib.tag;


import net.asfun.jangod.base.Constants;
import net.asfun.jangod.interpret.InterpretException;
import net.asfun.jangod.interpret.JangodInterpreter;
import net.asfun.jangod.interpret.VariableFilter;
import net.asfun.jangod.lib.Tag;
import net.asfun.jangod.tree.NodeList;
import net.asfun.jangod.util.HelperStringTokenizer;

/**
 * {% set varName post.id|equal:'12' scope %}
 * @author anysome
 *
 */
public class SetTag implements Tag{

	final String TAGNAME = "set";
	final String SCOPE_TOP = "top";
	
	@Override
	public String getName() {
		return TAGNAME;
	}

	@Override
	public String interpreter(NodeList carries, String helpers, JangodInterpreter interpreter) throws InterpretException {
		String[] helper = new HelperStringTokenizer(helpers).allTokens();
		if ( helper.length != 2 || helper.length != 3 ) {
			throw new InterpretException("Tag 'set' expects 2 or 3 helper >>> " + helper.length);
		}
		String scope = SCOPE_TOP;
		if ( helper.length == 3 ) {
			scope = helper[2].toLowerCase();
		}
		Object value = VariableFilter.compute(helper[1], interpreter);
		if ( SCOPE_TOP.equals(scope) ) {
			interpreter.assignRuntimeScope(helper[0], value, 1);
		} else {
			interpreter.assignRuntimeScope(helper[0], value);
		}
		return Constants.STR_BLANK;
	}

	@Override
	public String getEndTagName() {
		return null;
	}

}
