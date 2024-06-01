package core.web;

import core.web.view.JspView;
import core.web.view.View;

public class DefaultViewResolver implements ViewResolver {

    public View resolveViewName(String viewName) {
        return new JspView(viewName);
    }
}
