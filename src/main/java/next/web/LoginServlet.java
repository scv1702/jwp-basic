package next.web;

import core.db.DataBase;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            log.debug("user not found : {}", userId);
            resp.sendRedirect("/user/login_failed.jsp");
            return;
        }
        if (!user.getPassword().equals(password)) {
            log.debug("user password not matched : {}", user);
            resp.sendRedirect("/user/login_failed.jsp");
            return;
        }
        req.getSession().setAttribute("user", user);
        log.debug("user login : {}", user);
        resp.sendRedirect("/");
    }
}
