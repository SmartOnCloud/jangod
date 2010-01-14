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
package net.asfun.jangod.lib;

import java.util.List;

import net.asfun.jangod.interpret.InterpretException;
import net.asfun.jangod.interpret.JangodInterpreter;
import net.asfun.jangod.interpret.Node;


public interface Tag extends Importable{

	public String compile(List<Node> carries, String helpers, JangodInterpreter interpreter) throws InterpretException;

	/**
	 * Get name of end tag
	 * 	lowerCase
	 * 	Null if it's a single tag without content.
	 * @return
	 */
	public String getEndTagName();

}
