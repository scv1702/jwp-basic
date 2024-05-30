package next.controller;

import core.web.Model;
import core.context.annotations.Controller;
import core.web.annotations.RequestMapping;
import next.dao.QuestionDao;
import next.dao.UserDao;

@Controller
@RequestMapping("/")
public class HomeController {
    private UserDao userDao = new UserDao();
    private QuestionDao questionDao = new QuestionDao();

    @RequestMapping
    public String index(Model model) {
        model.addAttribute("users", userDao.findAll());
        model.addAttribute("questions", questionDao.findAll());
        return "/home";
    }
}
