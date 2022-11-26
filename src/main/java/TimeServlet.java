import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

static final String FORMAT_DATA_TIME = "dd.MM.yyyy HH:mm:ss";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");

        resp.getWriter().write("<h3>"+"${timezone}".replace("${timezone}", parseTime(req))+"</h3>");
        resp.setHeader("Refresh", "1");
        resp.getWriter().close();
    }

    private String parseTime(HttpServletRequest request) {
        String timezone = request.getParameter("timezone");

        if(request.getParameterMap().containsKey("timezone")){
            String zoneTimePlus = timezone.replace(" ", "+");
            ZoneId zoneId = ZoneId.of(zoneTimePlus);
            ZonedDateTime zdt = ZonedDateTime.now(zoneId);
            String timeZoneUtc = zdt.format(DateTimeFormatter.ofPattern(FORMAT_DATA_TIME));
            return timeZoneUtc+" "+zoneTimePlus;

        }
        ZoneId zoneId = ZoneId.of("UTC");
        ZonedDateTime zdt = ZonedDateTime.now(zoneId);
        String utc = zdt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        return utc + " UTC";
    }


}