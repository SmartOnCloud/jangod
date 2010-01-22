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
package net.asfun.jangod.interpret;

import static net.asfun.jangod.parse.ParserConstants.*;
import static net.asfun.jangod.util.logging.*;

import java.util.LinkedList;
import java.util.List;

import net.asfun.jangod.parse.EchoToken;
import net.asfun.jangod.parse.FixedToken;
import net.asfun.jangod.parse.InstToken;
import net.asfun.jangod.parse.JangodParser;
import net.asfun.jangod.parse.TagToken;
import net.asfun.jangod.parse.Token;


public class NodeList {
	
	/**
	 * general the node tree
	 * @param parser
	 * @param endTagName
	 * @param path
	 * @return
	 */
	public static List<Node> makeList(JangodParser parser, String endTagName, int level) {
		List<Node> nodes = new LinkedList<Node>();
		Token token;
		TagToken tag;
		while( parser.hasNext() ) {
			token = parser.next();
			switch(token.getType()) {
				case TOKEN_FIXED :
					TextNode xn = new TextNode((FixedToken)token);
					nodes.add(xn);
					break;
				case TOKEN_NOTE :		
					//even not need to add this node, or, it could be add like a fixedtoken
					break;
				case TOKEN_INST :
					try {
						InstNode in = new InstNode((InstToken) token);
						nodes.add(in);
					} catch (InterpretException e) {
						JangodLogger.log(Level.WARNING, "can't create node with token >>> " + token, e.getCause());
					}	
					break;
				case TOKEN_ECHO :
					VariableNode vn = new VariableNode((EchoToken) token, level);
					nodes.add(vn);
					break;
				case TOKEN_TAG :
					tag = (TagToken) token;
					if ( tag.getTagName().equals(endTagName) ) {
						return nodes;
					}
					try {
						TagNode tn = new TagNode((TagToken) token, parser, level);
						nodes.add(tn);
					} catch (InterpretException e) {
						JangodLogger.log(Level.WARNING, "can't create node with token >>> " + token,e.getCause());
					}
					break;
				default :
					JangodLogger.warning("unknown type token >>> " + token);
			}
		}
		//can't reach end tag
		if(endTagName != null ) {
			JangodLogger.severe("lost end tag >>> " + endTagName);
		}
		return nodes;
	}
}
