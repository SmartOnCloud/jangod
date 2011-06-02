package net.asfun.jangod.lib.tag;

import net.asfun.jangod.interpret.InterpretException;
import net.asfun.jangod.interpret.JangodInterpreter;
import net.asfun.jangod.interpret.VariableFilter;
import net.asfun.jangod.lib.Tag;
import net.asfun.jangod.tree.NodeList;
import net.asfun.jangod.util.HelperStringTokenizer;

import org.springframework.context.MessageSource;

public class MessageTag implements Tag {

    final String TAGNAME = "message";

    @Override
    public String interpreter(NodeList carries, String helpers,
	    JangodInterpreter interpreter) throws InterpretException {
	String key;
	String[] args;

	String[] tokens = new HelperStringTokenizer(helpers).allTokens();
	if (tokens.length < 1) {
	    throw new InterpretException(
		    "Tag 'message' expects at least 1 helper >>> "
			    + tokens.length);
	} else {
	    key = resolveToken(tokens[0], interpreter);
	    if (tokens.length == 1) {
		args = new String[0];
	    } else {
		args = new String[tokens.length - 1];
		System.arraycopy(tokens, 1, args, 0, tokens.length - 1);
	    }
	}

	for (int i = 0; i < args.length; i++) {
	    args[i] = resolveToken(args[i], interpreter);
	}

	String message = key;
	MessageSource messageSource = interpreter.getApplication()
		.getMessageSource();
	if (messageSource != null) {
	    message = messageSource.getMessage(key, args,
		    interpreter.getLocale());
	}

	return message;
    }

    private String resolveToken(String token, JangodInterpreter interpreter)
	    throws InterpretException {
	Object val = VariableFilter.compute(token, interpreter);
	return (val == null) ? token : String.valueOf(val);
    }

    @Override
    public String getEndTagName() {
	return null;
    }

    @Override
    public String getName() {
	return TAGNAME;
    }

}
