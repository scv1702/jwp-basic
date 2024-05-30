package next.controller;

import core.web.annotations.Controller;
import core.web.annotations.RequestMapping;
import next.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@Controller
@RequestMapping("/users")
public class ListUserController {
    private UserDao userDao = new UserDao();

    @RequestMapping("/list")
    public String list(HttpServletRequest req, HttpServletResponse res) throws SQLException {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return "redirect:/users/loginForm";
        }
        req.setAttribute("users", userDao.findAll());
        return "/user/list";
    }
}
