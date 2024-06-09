package next.controller;

import core.web.Model;
import core.http.HttpMethod;
import core.context.annotations.Controller;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import next.dao.UserDao;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UpdateUserController {
    private UserDao userDao = new UserDao();
    private static final Logger log = LoggerFactory.getLogger(UpdateUserController.class);

    @RequestMapping(value = "/updateForm", method = HttpMethod.GET)
    public String updateForm(@RequestParam("userId") String userId, HttpSession session, Model model) {
        User user = userDao.findByUserId(userId);
        if (!UserSessionUtils.isSameUser(session, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        model.addAttribute("user", user);
        return "/user/updateForm";
    }

    @RequestMapping(value = "/update", method = HttpMethod.POST)
    public String update(
        @RequestParam("userId") String userId,
        @RequestParam("password") String password,
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        HttpSession session
    ) {
        User user = userDao.findByUserId(userId);
        if (!UserSessionUtils.isSameUser(session, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        User updateUser = new User(
            userId,
            password,
            name,
            email
        );
        log.debug("Update User : {}", updateUser);
        userDao.update(updateUser);
        return "redirect:/";
    }
}
