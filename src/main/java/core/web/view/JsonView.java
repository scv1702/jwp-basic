package core.web.view;

import core.util.json.JsonConverter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonView implements View {

    private final Object json;
    private final HttpServletResponse res;
    private static final JsonConverter jsonConverter = new JsonConverter();

    public JsonView(Object json, HttpServletResponse res) {
        this.json = json;
        this.res = res;
    }

    @Override
    public void render() throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(jsonConverter.convertToJson(json));
    }
}
