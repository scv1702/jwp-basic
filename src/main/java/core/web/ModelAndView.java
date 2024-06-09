package core.web;

import core.web.view.View;

public class ModelAndView {
    private Object view;
    private Model model = new Model();

    public ModelAndView() {
    }

    public ModelAndView(final String viewName) {
        this.view = viewName;
    }

    public ModelAndView(final View view, final Model model) {
        this.view = view;
        this.model = model;
    }

    public ModelAndView(final String viewName, final Model model) {
        this.view = viewName;
        this.model = model;
    }

    public String getViewName() {
        if (view instanceof String) {
            return (String) view;
        }
        return null;
    }

    public View getView() {
        if (view instanceof View) {
            return (View) view;
        }
        return null;
    }

    public void setViewName(final String viewName) {
        this.view = viewName;
    }

    public void setView(final View view) {
        this.view = view;
    }

    public Model getModel() {
        return model;
    }


    public void setModel(final Model model) {
        this.model = model;
    }
}
