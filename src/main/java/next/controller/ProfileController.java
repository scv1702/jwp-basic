package next.controller;

import core.web.Model;
import core.context.annotations.Controller;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import next.dao.UserDao;
import next.model.User;

@Controller
@RequestMapping("/users")
public class ProfileController {

    private final UserDao userDao = UserDao.getInstance();

    @RequestMapping("/profile")
    public String profile(@RequestParam("userId") String userId, Model model) {
        User user = userDao.findByUserId(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        model.addAttribute("user", user);
        return "/user/profile";
    }
}
