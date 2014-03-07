package be.kdg.spacecrack.filters;

import be.kdg.spacecrack.exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.services.AuthorizationService;
import be.kdg.spacecrack.services.IAuthorizationService;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */


public class TokenFilter implements Filter {


    private IAuthorizationService authorizationService;

    public TokenFilter() {
    }

    public TokenFilter(IAuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();


        authorizationService = (IAuthorizationService) autowireCapableBeanFactory.autowire(AuthorizationService.class, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);

        // authorizationService = new AuthorizationService(new TokenRepository(), new UserRepository(), new TokenStringGenerator());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, HttpStatusCodeException {
        boolean unauthorized;

        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper((HttpServletResponse) servletResponse);
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) servletRequest);


        if (requestWrapper.getCookies() == null || requestWrapper.getCookies().length < 1) {
            unauthorized = true;
        } else {
            String tokenValue = requestWrapper.getCookies()[0].getValue();
            //  tokenValue =  tokenValue.replaceAll("%22", "");
            tokenValue = tokenValue.substring(3, tokenValue.length() - 3);
            requestWrapper.getCookies()[0].setValue(tokenValue);
            if (requestWrapper.getCookies().length < 1 || tokenValue == null || tokenValue.isEmpty()) {
                unauthorized = true;
                System.out.println("token was null");

            } else {

                AccessToken token = null;
                try {
                    token = authorizationService.getAccessTokenByValue(tokenValue);
                } catch (SpaceCrackUnauthorizedException ex) {
                    responseWrapper.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are unauthorized for this request");
                }

                if (token != null) {
                    unauthorized = false;

                } else {
                    unauthorized = true;

                }
            }
        }
        if (unauthorized == true) {
            responseWrapper.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are unauthorized for this request");

            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {

    }
}
