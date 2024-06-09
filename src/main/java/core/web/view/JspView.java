package core.web.view;

import core.web.Model;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JspView implements View {

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Model model, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException {
        model.getAttributes().forEach(req::setAttribute);
        if (viewName.startsWith("redirect:")) {
            res.sendRedirect(viewName.substring("redirect:".length()));
            return ;
        }
        req.getRequestDispatcher(viewName + ".jsp").forward(req, res);
    }
}
