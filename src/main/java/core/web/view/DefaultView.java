package core.web.view;

import javax.servlet.http.HttpServletResponse;

public class DefaultView implements View {

    private final Object result;
    private final HttpServletResponse res;

    public DefaultView(Object result, HttpServletResponse res) {
        this.result = result;
        this.res = res;
    }

    @Override
    public void render() throws Exception {
        res.getWriter().print(result);
    }
}
