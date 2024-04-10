package next.controller;

import core.web.Controller;
import core.web.RequestMapping;
import core.web.RequestMethod;
import next.dao.UserDao;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@Controller
@RequestMapping("/users")
public class UpdateUserController {
    private UserDao userDao = new UserDao();
    private static final Logger log = LoggerFactory.getLogger(UpdateUserController.class);

    @RequestMapping(value = "/updateForm", method = RequestMethod.GET)
    public String updateForm(HttpServletRequest req, HttpServletResponse res) {
        String userId = req.getParameter("userId");
        User user = userDao.findByUserId(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        req.setAttribute("user", user);
        return "/user/updateForm";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(HttpServletRequest req, HttpServletResponse res) throws SQLException {
        User user = userDao.findByUserId(req.getParameter("userId"));
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        User updateUser = new User(req.getParameter("userId"),
            req.getParameter("password"),
            req.getParameter("name"),
            req.getParameter("email"));
        log.debug("Update User : {}", updateUser);
        userDao.update(updateUser);
        return "redirect:/";
    }
}
