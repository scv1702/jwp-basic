package core.web;

import core.web.view.InternalResourceView;
import core.web.view.View;

public class InternalResourceViewResolver implements ViewResolver {

    public View resolveViewName(String viewName) {
        return new InternalResourceView(viewName);
    }
}
