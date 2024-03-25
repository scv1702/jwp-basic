package next.controller;

import core.db.DataBase;
import core.web.Controller;
import core.web.GetMapping;
import core.web.PostMapping;
import core.web.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import next.model.User;

@Controller
@RequestMapping("/users")
public class AuthController {

    @GetMapping(value = "/loginForm")
    public String loginForm(HttpServletRequest req, HttpServletResponse res) {
        return "/user/login";
    }

    @PostMapping(value = "/login")
    public String login(HttpServletRequest req, HttpServletResponse res) {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);

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

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return "redirect:/";
    }
}
