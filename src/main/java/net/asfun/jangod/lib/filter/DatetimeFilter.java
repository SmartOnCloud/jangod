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
package net.asfun.jangod.lib.filter;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import net.asfun.jangod.interpret.InterpretException;
import net.asfun.jangod.interpret.JangodInterpreter;
import net.asfun.jangod.lib.Filter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.format.datetime.joda.JodaTimeContext;
import org.springframework.format.datetime.joda.JodaTimeContextHolder;

public class DatetimeFilter implements Filter {

    @Override
    public Object filter(Object object, JangodInterpreter interpreter,
	    String... arg) throws InterpretException {
	if (object == null) {
	    return object;
	}
	if (object instanceof DateTime) { // joda DateTime
	    DateTimeFormatter formatter;
	    DateTimeFormatter a = DateTimeFormat.forPattern(interpreter
		    .resolveString(arg[0]));
	    if (arg.length == 1) {
		DateTimeFormatter forPattern = a;
		JodaTimeContext jodaTimeContext = JodaTimeContextHolder
			.getJodaTimeContext();
		if (jodaTimeContext==null) {
		    jodaTimeContext = new JodaTimeContext();
		}
		formatter = jodaTimeContext.getFormatter(forPattern);
	    } else if (arg.length == 2) {
		formatter = a.withChronology(ISOChronology
			.getInstance(DateTimeZone.forID(interpreter
				.resolveString(arg[1]))));
	    } else {
		throw new InterpretException(
			"filter date expects 1 or 2 args >>> " + arg.length);
	    }
	    return formatter.print((DateTime) object);
	} else {
	    SimpleDateFormat sdf;
	    if (arg.length == 1) {
		sdf = new SimpleDateFormat(interpreter.resolveString(arg[0]));
		sdf.setTimeZone(interpreter.getConfiguration().getTimezone());
	    } else if (arg.length == 2) {
		sdf = new SimpleDateFormat(interpreter.resolveString(arg[0]));
		sdf.setTimeZone(TimeZone.getTimeZone(interpreter
			.resolveString(arg[1])));
	    } else {
		throw new InterpretException(
			"filter date expects 1 or 2 args >>> " + arg.length);
	    }
	    return sdf.format(object);
	}
    }

    @Override
    public String getName() {
	return "date";
    }

}
