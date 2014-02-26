package be.kdg.spacecrack.integrationtests;

import be.kdg.spacecrack.filters.TokenFilter;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

//import org.codehaus.jackson.map.ObjectMapper;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class BaseFilteredIntegrationTests {
    protected MockMvc mockMvc;
    @Autowired
    private ServletContext servletContext;
    private WebApplicationContext ctx;
    protected ObjectMapper objectMapper;

    @Before
    public void setupMockMVC() throws Exception {
               ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        TokenFilter filter = new TokenFilter();
        FilterConfig filterConfig = new MockFilterConfig(servletContext);

        filter.init(filterConfig);
        mockMvc = webAppContextSetup(ctx).addFilter(filter, "/auth/*" ).build();
        objectMapper = new ObjectMapper();
    }

    protected String login() throws Exception {


      User testUser = new User("test","test", "test@gmail.com");
        String userjson =  objectMapper.writeValueAsString(testUser);

        MockHttpServletRequestBuilder requestBuilder = post("/accesstokens");
        String accessTokenJson = mockMvc.perform(requestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(userjson)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        AccessToken accessToken = objectMapper.readValue(accessTokenJson, AccessToken.class);
        return "%22"+accessToken.getValue()+"%22";

    }
}
