
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    static final String FORMAT_DATA_TIME = "dd.MM.yyyy HH:mm:ss";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        String timezone = req.getParameter("timezone");

        Cookie cookie = new Cookie("lastTimezone", timezone);

        if(timezone!=null){
            resp.addCookie(cookie);
        }

        resp.getWriter().write("<h3>" + "${timezone}".replace("${timezone}", parseTime(req, timezone) + "</h3>"));
        resp.getWriter().close();
    }

    private String parseTime(HttpServletRequest req, String timezone) {
        Map<String, String> cookies = getCookies(req);

        if(cookies.containsKey("lastTimezone") && timezone==null){
          timezone=cookies.get("lastTimezone");
        }

        if (timezone!=null) {
            ZoneId zoneId = ZoneId.of(timezone);
            ZonedDateTime zdt = ZonedDateTime.now(zoneId);
            String timeZoneUtc = zdt.format(DateTimeFormatter.ofPattern(FORMAT_DATA_TIME));
            return timeZoneUtc + " " + timezone;
        }
        ZoneId zoneId = ZoneId.of("UTC");
        ZonedDateTime zdt = ZonedDateTime.now(zoneId);
        String utc = zdt.format(DateTimeFormatter.ofPattern(FORMAT_DATA_TIME));

        return utc + " UTC";
    }
    private Map<String, String> getCookies(HttpServletRequest req) {
        String cookies = req.getHeader("Cookie");

        if (cookies == null) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<>();

        String[] separateCookies = cookies.split(";");
        for (String pair : separateCookies) {
            String[] keyValue = pair.split("=");
            result.put(keyValue[0], keyValue[1]);
        }
        return result;
    }
}

