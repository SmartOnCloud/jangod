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
package net.asfun.template.parse;

import static net.asfun.template.parse.ParserConstants.*;

public abstract class Token{

	protected String image;
	//useful for some token type
	protected String content;
	
	public Token(String image2) throws ParserException{
		image = image2;
		parse();
	}

	public String getImage() {
		return image;
	}
	
	@Override
	public String toString() {
		return image;
	}
	
	protected abstract void parse() throws ParserException;
	
	public abstract int getType();

	public static Token newToken(int tokenKind, String image2) throws ParserException {
		switch( tokenKind ) {
		case TOKEN_FIXED : 
			return new FixedToken(image2);
		case TOKEN_NOTE :
			return new NoteToken(image2);
		case TOKEN_ECHO :
			return new EchoToken(image2);
		case TOKEN_TAG :
			return new TagToken(image2);
		case TOKEN_INST :
			return new InstToken(image2);
		default :
			throw new ParserException("creating a token with unknown type" + (char)tokenKind);	
		}
	}
	
}
