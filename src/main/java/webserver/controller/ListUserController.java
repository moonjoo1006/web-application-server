package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpSession;

import java.util.Collection;
import java.util.Map;

public class ListUserController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(ListUserController.class);

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        if (!isLogin(request.getSession())) {
            response.sendRedirect("/usr/login.html");
            return;
        }
        Collection<User> users = DataBase.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("<Table border='1'>");
        for (User user : users) {
            sb.append("<tr>");
            sb.append("<td>" + user.getUserId() + "</td>");
            sb.append("<td>" + user.getName() + "</td>");
            sb.append("<td>" + user.getEmail() + "</td>");
            sb.append("</tr>");
        }
        response.forwardBody(sb.toString());
    }

    private static boolean isLogin(HttpSession session) {
        Object user = session.getAttribute("user");
        if(user == null) return false;
        return true;
    }

}
