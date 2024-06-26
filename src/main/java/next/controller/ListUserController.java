package next.controller;

import core.web.Model;
import core.context.annotations.Controller;
import core.web.annotations.RequestMapping;
import next.dao.UserDao;
import next.util.UserSessionUtils;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
@RequestMapping("/users")
public class ListUserController {

    private final UserDao userDao = UserDao.getInstance();

    @RequestMapping("/list")
    public String list(HttpSession session, Model model) throws SQLException {
        if (!UserSessionUtils.isLogined(session)) {
            return "redirect:/users/loginForm";
        }
        model.addAttribute("users", userDao.findAll());
        return "/user/list";
    }
}
