package be.kdg.spacecrack;

import be.kdg.spacecrack.utilities.ITokenStringGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Ikke on 5-2-14.
 */
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class FilterTest {
    @Autowired
    private ServletContext servletContext;
    private WebApplicationContext ctx;
    private MockMvc mockMvc;
    private ITokenStringGenerator mockTokenGenerator;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();


        ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        mockMvc = webAppContextSetup(ctx).addFilters().build();

        mockTokenGenerator = Mockito.mock(ITokenStringGenerator.class);
    }

    @Test
    public void testTokenFilterHello() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/hello");

        mockMvc.perform(requestBuilder)
            .andExpect(status().isUnauthorized());
    }
}
