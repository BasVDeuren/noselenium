package be.kdg.spacecrack.filters;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.Exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.services.IAuthorizationService;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class TokenHandlerInterceptor extends HandlerInterceptorAdapter {




    private IAuthorizationService authorizationService;

    public TokenHandlerInterceptor() {
    }

    public TokenHandlerInterceptor(IAuthorizationService authorizationService) {

        this.authorizationService = authorizationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        boolean unauthorized;

        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper( httpServletResponse);
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(( httpServletRequest));
        //Checking if the request url should really pass through this interceptor,
        // this is a small workaround for the integrationtests because,
        // in the production environment this can't occur
        if(!requestWrapper.getRequestURI().contains("/auth")){
            return true;
        }

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

            return false;
        }
        return  true;
    }


    public void setAuthorizationService(IAuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public IAuthorizationService getAuthorizationService() {
        return authorizationService;
    }
}
