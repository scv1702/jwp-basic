package next.controller;

import core.bean.annotations.Inject;
import core.http.HttpMethod;
import core.bean.annotations.Controller;
import core.web.annotations.RequestMapping;
import core.web.annotations.RequestParam;
import next.dao.UserDao;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/users")
public class CreateUserController {

    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    private final UserDao userDao;

    @Inject
    public CreateUserController(UserDao userDao) {
        this.userDao = userDao;
    }

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
