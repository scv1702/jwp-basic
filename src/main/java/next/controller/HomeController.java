package next.controller;

import core.bean.annotations.Inject;
import core.web.Model;
import core.bean.annotations.Controller;
import core.web.annotations.RequestMapping;
import next.dao.QuestionDao;
import next.dao.UserDao;

@Controller
@RequestMapping("/")
public class HomeController {

    private final UserDao userDao;
    private final QuestionDao questionDao;

    @Inject
    public HomeController(UserDao userDao, QuestionDao questionDao) {
        this.userDao = userDao;
        this.questionDao = questionDao;
    }

    @RequestMapping
    public String index(Model model) {
        model.addAttribute("users", userDao.findAll());
        model.addAttribute("questions", questionDao.findAll());
        return "/home";
    }
}
