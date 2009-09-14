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

import java.util.logging.Logger;

public final class logging {

	public static final Logger JangodLogger = Logger.getLogger("asfun.jandog");
	
	public static class Level {
		public static final java.util.logging.Level SEVERE = java.util.logging.Level.SEVERE;
		public static final java.util.logging.Level WARNING = java.util.logging.Level.WARNING;
	}
}
