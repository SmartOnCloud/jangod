package org.springframework.web.servlet.view.jangod;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class JangodViewResolver extends AbstractTemplateViewResolver {
	
	private JangodConfig jangodConfig;
	private boolean isConfigUnset = true;
	private ModelDataProvider commonAttributes;
	
	public JangodViewResolver() {
		setViewClass(requiredViewClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class requiredViewClass() {
		return JangodView.class;
	}
	
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		JangodView view = (JangodView) super.buildView(viewName);
		if ( isConfigUnset ) {
			jangodConfig.setRoot(getPrefix());
			if ( commonAttributes != null ) {
				jangodConfig.getTemplate().setCommonBindings(commonAttributes.getModel());
			}
			isConfigUnset = false;
		}
		view.setJangodConfig(jangodConfig);
		view.setUrl(viewName + getSuffix());
		return view;
	}

	public JangodConfig getJangodConfig() {
		return jangodConfig;
	}

	public void setJangodConfig(JangodConfig jangodConfig) {
		this.jangodConfig = jangodConfig;
	}

	public ModelDataProvider getCommonAttributes() {
		return commonAttributes;
	}

	public void setCommonAttributes(ModelDataProvider commonAttributes) {
		this.commonAttributes = commonAttributes;
	}

}
