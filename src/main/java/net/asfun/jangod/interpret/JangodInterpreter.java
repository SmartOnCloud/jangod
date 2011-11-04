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

import static net.asfun.jangod.util.logging.JangodLogger;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import net.asfun.jangod.base.Application;
import net.asfun.jangod.base.Configuration;
import net.asfun.jangod.base.Constants;
import net.asfun.jangod.base.Context;
import net.asfun.jangod.base.ResourceManager;
import net.asfun.jangod.parse.TokenParser;
import net.asfun.jangod.tree.Node;
import net.asfun.jangod.tree.TreeParser;
import net.asfun.jangod.util.ListOrderedMap;
import net.asfun.jangod.util.ListOrderedMap.Item;

public class JangodInterpreter implements Cloneable {

    public static final String CHILD_FLAG = "'IS\"CHILD";
    public static final String PARENT_FLAG = "'IS\"PARENT";
    public static final String INSERT_FLAG = "'IS\"INSERT";
    public static final String SEMI_RENDER = "'SEMI\"FORMAL";
    public static final String BLOCK_LIST = "'BLK\"LIST";
    public static final String SEMI_BLOCK = "<K2C9OL7B>";

    static final String VAR_DATE = "now";
    static final String VAR_PATH = "workspace";

    private int level = 1;
    private FloorBindings runtime;
    private Context context;
    String file = null;
    private Locale locale;

    public JangodInterpreter(Context context) {
	this.context = context;
	runtime = new FloorBindings();
    }

    private JangodInterpreter() {
    }

    public Configuration getConfiguration() {
	return context.getConfiguration();
    }

    @Override
    public JangodInterpreter clone() {
	JangodInterpreter compiler = new JangodInterpreter();
	compiler.context = context;
	compiler.runtime = runtime.clone();
	return compiler;
    }

    public void init(Locale l) {
	runtime = new FloorBindings();
	level = 1;
	locale = l;
    }

    public String render(TokenParser parser) throws InterpretException {
	return render(TreeParser.parser(parser));
    }

    public String render(Node root) throws InterpretException {
	StringBuffer buff = new StringBuffer();
	for (Node node : root.children()) {
	    buff.append(node.render(this));
	}
	if (runtime.get(CHILD_FLAG, 1) != null
		&& runtime.get(INSERT_FLAG, 1) == null) {
	    StringBuilder sb = new StringBuilder((String) fetchRuntimeScope(
		    SEMI_RENDER, 1));
	    // replace the block identify with block content
	    ListOrderedMap blockList = (ListOrderedMap) fetchRuntimeScope(
		    BLOCK_LIST, 1);
	    Iterator<Item> mi = blockList.iterator();
	    int index;
	    String replace;
	    Item item;
	    while (mi.hasNext()) {
		item = mi.next();
		replace = SEMI_BLOCK + item.getKey() + SEMI_BLOCK;
		while ((index = sb.indexOf(replace)) > 0) {
		    sb.delete(index, index + replace.length());
		    sb.insert(index, item.getValue());
		}
	    }
	    return sb.toString();
	}
	return buff.toString();
    }

    public Object evaluateExpression(String expr) {
	if (expr == null || expr.trim().length() == 0) {
	    JangodLogger.severe("variable name is required.");
	    return Constants.STR_BLANK;
	}
	// find from runtime(tree scope) > engine > global
	Object obj = runtime.get(expr, level);
	int lvl = level;
	while (obj == null && lvl > 1) {
	    obj = runtime.get(expr, --lvl);
	}
	if (obj == null) {
	    obj = context.getAttribute(expr);
	}
	JangodLogger.finer(expr + " can't resolve variable >>> " + expr);
	return obj;
    }

    public String evaluateExpressionAsString(String exp) {
	Object res = evaluateExpression(exp);
	return res == null ? exp : String.valueOf(res);
    }

    /**
     * save variable to runtime tree scope space
     * 
     * @param name
     * @param item
     */
    public void assignRuntimeScope(String name, Object item) {
	runtime.put(name, item, level);
    }

    public void assignRuntimeScope(String name, Object item, int level) {
	runtime.put(name, item, level);
    }

    public Object fetchRuntimeScope(String name, int level) {
	return runtime.get(name, level);
    }

    public Object fetchRuntimeScope(String name) {
	return runtime.get(name, level);
    }

    public void setLevel(int lvl) {
	level = lvl;
    }

    public int getLevel() {
	return level;
    }

    public Context getContext() {
	return context;
    }

    public Application getApplication() {
	return context.getApplication();
    }

    public String getWorkspace() {
	if (file != null) {
	    try {
		return ResourceManager.getDirectory(file);
	    } catch (IOException e) {
		return context.getConfiguration().getWorkspace();
	    }
	} else {
	    return context.getConfiguration().getWorkspace();
	}
    }

    public void setFile(String fullName) {
	this.file = fullName;
    }

    public Locale getLocale() {
	return locale;
    }

}
