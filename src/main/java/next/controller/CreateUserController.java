package next.controller;

import core.db.DataBase;
import core.web.Controller;
import core.web.RequestMapping;
import core.web.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/users")
public class CreateUserController {

    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @RequestMapping(value = "/form")
    public String userForm(HttpServletRequest req, HttpServletResponse res) {
        return "/user/form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createUser(HttpServletRequest req, HttpServletResponse res) {
        User user = new User(req.getParameter("userId"),
            req.getParameter("password"),
            req.getParameter("name"),
            req.getParameter("email"));
        log.debug("Create user : {}", user);
        DataBase.addUser(user);
        return "redirect:/";
    }
}
