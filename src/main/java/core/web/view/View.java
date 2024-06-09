package core.web.view;

import core.web.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
    void render(
        final Model model,
        final HttpServletRequest req,
        final HttpServletResponse res
    ) throws Exception;
}
