package next.controller;

import core.db.DataBase;
import core.web.Controller;
import core.web.GetMapping;
import core.web.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String index(HttpServletRequest req, HttpServletResponse res) {
        req.setAttribute("users", DataBase.findAll());
        return "/index";
    }
}
