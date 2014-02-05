package be.kdg.spacecrack.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by Ikke on 5-2-14.
 */
@WebFilter(filterName = "TokenFilter",
urlPatterns = "/test")
public class TokenFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("lol");
    }

    @Override
    public void destroy() {

    }
}
