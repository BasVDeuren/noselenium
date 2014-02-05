package be.kdg.spacecrack.filters;

import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Created by Ikke on 5-2-14.
 */
@WebFilter(filterName = "TokenFilter",
urlPatterns = TokenFilter.URLPATTERN)
public class TokenFilter implements Filter {

    public static final String URLPATTERN = "/auth/*";


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, HttpStatusCodeException {

        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper((HttpServletResponse) servletResponse);
        responseWrapper.setStatus(401);
    }

    @Override
    public void destroy() {

    }
}
