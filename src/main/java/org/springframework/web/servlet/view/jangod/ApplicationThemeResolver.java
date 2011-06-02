/**********************************************************************
Copyright (c) 2010 Asfun Net.

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
package org.springframework.web.servlet.view.jangod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.theme.AbstractThemeResolver;

public class ApplicationThemeResolver extends AbstractThemeResolver {

    private String theme = "default";

    @Override
    public String resolveThemeName(HttpServletRequest req) {
	return theme;
    }

    @Override
    public void setThemeName(HttpServletRequest req, HttpServletResponse resp,
	    String theme) {
	this.theme = theme;
    }

}
