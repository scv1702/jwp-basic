package next.web;

import core.db.DataBase;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/user/update")
public class UpdateUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(UpdateUserServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        User loginUser = (User) req.getSession().getAttribute("user");
        if (!userId.equals(loginUser.getUserId())) {
            log.debug("user update failed. userId : {}, loginUser : {}", userId, loginUser);
            resp.sendRedirect("/user/list");
            return;
        }
        User user = DataBase.findUserById(userId);
        user.setName(req.getParameter("name"));
        user.setPassword(req.getParameter("password"));
        user.setEmail(req.getParameter("email"));
        log.debug("user update : {}", user);
        resp.sendRedirect("/user/list");
    }
}
