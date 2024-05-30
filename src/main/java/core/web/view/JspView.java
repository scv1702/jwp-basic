package core.web.view;

import core.web.Model;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JspView implements View {

    private final String viewName;
    private final Model model;
    private final HttpServletRequest req;
    private final HttpServletResponse res;

    public JspView(
        final String viewName,
        final Model model,
        final HttpServletRequest req,
        final HttpServletResponse res
    ) {
        this.viewName = viewName;
        this.model = model;
        this.req = req;
        this.res = res;
    }

    @Override
    public void render() throws IOException, ServletException {
        model.getAttributes().forEach(req::setAttribute);
        if (viewName.startsWith("redirect:")) {
            res.sendRedirect(viewName.substring("redirect:".length()));
            return ;
        }
        req.getRequestDispatcher(viewName + ".jsp").forward(req, res);
    }
}
