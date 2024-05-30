package next.controller;

import core.http.HttpMethod;
import core.context.annotations.Controller;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import next.dao.UserDao;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/users")
public class CreateUserController {

    private UserDao userDao = new UserDao();
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @RequestMapping(value = "/form")
    public String userForm() {
        return "/user/form";
    }

    @RequestMapping(value = "/create", method = HttpMethod.POST)
    public String createUser(
        @RequestParam("userId") String userId,
        @RequestParam("password") String password,
        @RequestParam("name") String name,
        @RequestParam("email") String email
    ) {
        User user = new User(
            userId,
            password,
            name,
            email
        );
        log.debug("Create user : {}", user);
        userDao.insert(user);
        return "redirect:/";
    }
}
