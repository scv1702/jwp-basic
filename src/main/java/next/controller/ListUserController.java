package next.controller;

import core.db.DataBase;
import core.web.Controller;
import core.web.GetMapping;
import core.web.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/users")
public class ListUserController {

    @GetMapping("/list")
    public String list(HttpServletRequest req, HttpServletResponse res) {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return "redirect:/users/loginForm";
        }
        req.setAttribute("users", DataBase.findAll());
        return "/user/list";
    }
}
