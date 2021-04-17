package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.controller.Controller;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            //readBuffer(in);
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            if(request.getCookies().getCookie("JSESSIONID") == null) {
                response.addHeader("Set-Cookie", "JSESSIONID=" + UUID.randomUUID());
            }

            Controller controller = RequestMapping.getController(request.getPath());
            if(controller == null) {
                String path = getDefaultPath(request.getPath());
                response.forward(path);
            }else {
                controller.service(request, response);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getDefaultPath(String path) {
        if (path.equals("/")) { return "index.html"; }
        return path;
    }



}
