package be.kdg.spacecrack.filters;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.repositories.ITokenRepository;
import be.kdg.spacecrack.repositories.TokenRepository;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
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
@Component
public class TokenFilter implements Filter {

    public static final String URLPATTERN = "auth/*";
    private static final String BEAN_NAME = "tokenfilterbean";

    public ITokenRepository tokenRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();

//        autowireCapableBeanFactory.configureBean(this, BEAN_NAME);
        tokenRepository = (ITokenRepository) autowireCapableBeanFactory.autowire(TokenRepository.class, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, HttpStatusCodeException {
        boolean unauthorized;

        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper((HttpServletResponse) servletResponse);
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) servletRequest);

        if(requestWrapper.getCookies() == null || requestWrapper.getCookies().length < 1)
        {
            unauthorized = true;
        }else{
            String tokenValue = requestWrapper.getCookies()[0].getValue();
            tokenValue = tokenValue.substring(3, tokenValue.length()-3);
            requestWrapper.getCookies()[0].setValue(tokenValue);
            if(requestWrapper.getCookies().length < 1||tokenValue == null || tokenValue.isEmpty())
            {
                unauthorized = true;
                System.out.println("token was null");

            }else{

                AccessToken token = tokenRepository.getAccessTokenByValue(tokenValue);

                if(token != null)
                {
                    unauthorized = false;

                }else{
                    unauthorized = true;

                }
            }
        }
        if(unauthorized == true){
            responseWrapper.sendError(HttpServletResponse.SC_UNAUTHORIZED,"You are unauthorized for this request");

            return;
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

//    private AccessToken getAccessTokenByValue(String tokenValue) {
//        Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
//        Transaction tx = currentSession.beginTransaction();
//        @SuppressWarnings("JpaQlInspection") Query q = currentSession.createQuery("from AccessToken a where a.value = :tokenvalue");
//        q.setParameter("tokenvalue", tokenValue);
//        AccessToken token = (AccessToken) q.uniqueResult();
//        tx.commit();
//        return token;
//    }

    @Override
    public void destroy() {

    }
}
