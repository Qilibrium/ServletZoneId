import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.TimeZone;

@WebFilter(value = "/time/*")
public class TimezoneValidateFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        String timeValue = req.getParameter("timezone");

        if(timeValue==null){
            chain.doFilter(req, resp);
        }
        else if (timeValue.contains("UTC")) {
            try {
                chain.doFilter(req, resp);
            }catch (Exception exception){
                resp.setStatus(400);
                resp.getWriter().write("Invalid timezone");
                resp.getWriter().close();
            }
        } else {
            resp.setStatus(400);
            resp.getWriter().write("Invalid timezone");
            resp.getWriter().close();
        }
    }
}

