package core.web;

import core.web.view.View;

public interface ViewResolver {

    View resolveViewName(String viewName);
}
