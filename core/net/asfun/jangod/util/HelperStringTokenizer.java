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

import static net.asfun.jangod.parse.ParserConstants.*;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Whitespace and comma as separator
 * quote to accept them as normal char
 * @author fangchq
 *
 */
public class HelperStringTokenizer implements Iterator<String>{

	private char[] helpers;
	private int currPost = 0;
	private int tokenStart = 0;
	private int length = 0;
	private char lastChar = ' ';
	private int lastStart = 0;
	private boolean useComma = false;
	private char quoteChar = 0;
	private boolean inQuote = false;
	
	public HelperStringTokenizer(String tobeToken) {
		helpers = tobeToken.toCharArray();
		length = tobeToken.length();
	}
	
	/**
	 * use Comma as token split or not
	 * true use it;
	 * false don't use it.
	 * @param onOrOff
	 */
	public void splitComma(boolean onOrOff) {
		useComma = onOrOff;
	}
	
	
	@Override
	public boolean hasNext() {
		return length > currPost;
	}

	@Override
	public String next() {
		String token;
		while( currPost < length ) {
			token = makeToken();
			lastChar = helpers[currPost-1];
			if (token != null) {
				return token;
			}
		}
		return null;
	}
	
	private String makeToken() {
		char c = helpers[currPost++];
		if ( c == DQ | c == SQ ) {
			if ( inQuote ){
				if ( quoteChar == c ) {
					inQuote = false;
				}
			} else {
				inQuote = true;
				quoteChar = c;
			}
		}
		if ( Character.isWhitespace(c) || (useComma && c == CM) ) {
			if ( ! inQuote ) {
				return newToken();
			}
		}
		if ( currPost == length ) {
			return getEndToken();
		}
		return null;
	}

	private String getEndToken() {
		return String.copyValueOf(helpers, tokenStart, currPost-tokenStart);
	}
	
	private String newToken() {
		lastStart = tokenStart;
		tokenStart = currPost;
		if ( Character.isWhitespace(lastChar) || (useComma && lastChar == CM)) {
			return null;
		}
//		startChar = -1;//change to save quote in helper 
		return String.copyValueOf(helpers, lastStart, currPost-lastStart-1);
	}


	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	public String[] allTokens() {
		ArrayList<String> al = new ArrayList<String>();
		while( hasNext() ) {
			al.add(next());
		}
		return al.toArray(new String[]{});
	}
}
