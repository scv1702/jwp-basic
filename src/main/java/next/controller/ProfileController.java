package next.controller;

import core.web.annotations.Controller;
import core.web.annotations.RequestMapping;
import next.dao.UserDao;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/users")
public class ProfileController {

    private UserDao userDao = new UserDao();

    @RequestMapping("/profile")
    public String profile(HttpServletRequest req, HttpServletResponse res) {
        String userId = req.getParameter("userId");
        User user = userDao.findByUserId(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        req.setAttribute("user", user);
        return "/user/profile";
    }
}
