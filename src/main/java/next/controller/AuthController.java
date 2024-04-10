package next.controller;

import core.web.Controller;
import core.web.RequestMapping;
import core.web.RequestMethod;
import next.dao.UserDao;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class AuthController {

    private UserDao userDao = new UserDao();

    @RequestMapping(value = "/loginForm")
    public String loginForm(HttpServletRequest req, HttpServletResponse res) {
        return "/user/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest req, HttpServletResponse res) {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = userDao.findByUserId(userId);

        if (user == null) {
            req.setAttribute("loginFailed", true);
            return "/user/login";
        }

        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return "redirect:/";
        } else {
            req.setAttribute("loginFailed", true);
            return "/user/login";
        }
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return "redirect:/";
    }
}
