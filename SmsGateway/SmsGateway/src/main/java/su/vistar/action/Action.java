package su.vistar.action;

import com.google.gson.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {
    JsonObject execute(HttpServletRequest request, HttpServletResponse response);
}
