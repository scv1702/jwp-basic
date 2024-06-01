package core.web;

import core.web.view.JspView;
import core.web.view.View;

public class ModelAndView {
    private final View viewName;
    private final Model model = new Model();

    public ModelAndView(final String viewName) {
        this.viewName = new JspView(viewName);
    }

    public View getView() {
        return viewName;
    }

    public Model getModel() {
        return model;
    }

    public ModelAndView addObject(final String attributeName, final Object attributeValue) {
        model.addAttribute(attributeName, attributeValue);
        return this;
    }
}
