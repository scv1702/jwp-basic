package next.controller;

import core.db.DataBase;
import core.web.Controller;
import core.web.RequestMapping;
import next.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@Controller
@RequestMapping("/")
public class HomeController {
    private UserDao userDao = new UserDao();

    @RequestMapping
    public String index(HttpServletRequest req, HttpServletResponse res) throws SQLException {
        req.setAttribute("users", userDao.findAll());
        return "/index";
    }
}
