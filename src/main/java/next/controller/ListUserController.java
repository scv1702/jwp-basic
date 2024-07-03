package next.controller;

import core.bean.annotations.Inject;
import core.web.Model;
import core.bean.annotations.Controller;
import core.web.annotations.RequestMapping;
import next.dao.UserDao;
import next.util.UserSessionUtils;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
@RequestMapping("/users")
public class ListUserController {

    private final UserDao userDao;

    @Inject
    public ListUserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping("/list")
    public String list(HttpSession session, Model model) throws SQLException {
        if (!UserSessionUtils.isLogined(session)) {
            return "redirect:/users/loginForm";
        }
        model.addAttribute("users", userDao.findAll());
        return "/user/list";
    }
}
