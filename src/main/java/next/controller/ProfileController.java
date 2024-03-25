package next.controller;

import core.db.DataBase;
import core.web.Controller;
import core.web.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import next.model.User;

@Controller
@RequestMapping("/users")
public class ProfileController {

    @RequestMapping("/profile")
    public String profile(HttpServletRequest req, HttpServletResponse res) {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        req.setAttribute("user", user);
        return "/user/profile";
    }
}
