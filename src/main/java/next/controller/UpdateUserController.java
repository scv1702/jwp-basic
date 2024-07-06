package next.controller;

import core.bean.annotations.Controller;
import core.bean.annotations.Inject;
import core.http.HttpMethod;
import core.web.Model;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import lombok.extern.slf4j.Slf4j;
import next.dao.UserDao;
import next.model.User;
import next.util.UserSessionUtils;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
@Slf4j
public class UpdateUserController {

    private final UserDao userDao;

    @Inject
    public UpdateUserController(UserDao userDao) {
        this.userDao = userDao;
    }

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
