package next.controller;

import core.web.Model;
import core.http.HttpMethod;
import core.context.annotations.Controller;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import next.dao.UserDao;
import next.model.User;
import next.util.UserSessionUtils;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class AuthController {

    private final UserDao userDao = UserDao.getInstance();

    @RequestMapping(value = "/loginForm")
    public String loginForm() {
        return "/user/login";
    }

    @RequestMapping(value = "/login", method = HttpMethod.POST)
    public String login(
        @RequestParam("userId") String userId,
        @RequestParam("password") String password,
        HttpSession session,
        Model model
    ) {
        User user = userDao.findByUserId(userId);

        if (user == null) {
            model.addAttribute("loginFailed", true);
            return "/user/login";
        }

        if (user.matchPassword(password)) {
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return "redirect:/";
        } else {
            model.addAttribute("loginFailed", true);
            return "/user/login";
        }
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return "redirect:/";
    }
}
