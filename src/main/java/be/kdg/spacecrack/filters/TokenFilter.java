package be.kdg.spacecrack.filters;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.List;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class TokenFilter implements Filter {

    public static final String URLPATTERN = "/api/auth/*";


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, HttpStatusCodeException {
        boolean unauthorized;

        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper((HttpServletResponse) servletResponse);
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) servletRequest);

        String tokenjson = requestWrapper.getHeader("token");


        if(tokenjson == null || tokenjson.isEmpty())
        {
            unauthorized = true;
            System.out.println("token was null");

        }else{
            ObjectMapper objectMapper = new ObjectMapper();
            AccessToken accessToken = objectMapper.readValue(tokenjson, AccessToken.class);
            Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction tx = currentSession.beginTransaction();
            @SuppressWarnings("JpaQlInspection") Query q = currentSession.createQuery("from AccessToken a where a.accessTokenId = :id and a.value = :tokenvalue");
           q.setParameter("id", accessToken.getAccessTokenId());
            q.setParameter("tokenvalue", accessToken.getValue());
            List list = q.list();
            tx.commit();

            if(!list.isEmpty())
            {
                System.out.println(list.size());
                unauthorized = false;

            }else{
                unauthorized = true;

            }
        }

        if(unauthorized == true){
           responseWrapper.sendError(HttpServletResponse.SC_UNAUTHORIZED,"You are unauthorized for this request");
            return;
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
