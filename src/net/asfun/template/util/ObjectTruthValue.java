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
package net.asfun.template.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class ObjectTruthValue {
	
	@SuppressWarnings("unchecked")
	public static boolean evaluate(Object object) {
		
		if ( object == null ) { 
			return false;
		}
		
		if ( object instanceof Boolean ) {
			Boolean b = (Boolean)object;
			return b.booleanValue();
		}
		
		if ( object instanceof Number ) {
			return ((Number)object).intValue() != 0;
		}
		
		if ( object instanceof String ) {
			return !object.toString().equals("");
		}
		
		if ( object.getClass().isArray() ) {
			return Array.getLength(object) != 0;
		}
		
		if ( object instanceof Collection ) {
			return ((Collection<?>)object).size() != 0;
		}
		
		if ( object instanceof Map ) {
			return ((Map<?,?>)object).size() != 0;
		}
		
		return true;
	}
	
}
