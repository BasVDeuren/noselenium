package be.kdg.spacecrack;

import be.kdg.spacecrack.filters.TokenFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Tim on 5/02/14.
 */

@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TestBeans {

    @Autowired
    private WebApplicationContext ctx;

    public MockMvc getMockMvcWithFilters() {
        return webAppContextSetup(ctx).addFilter(new TokenFilter(), TokenFilter.URLPATTERN).build();
    }

    @Test
    public void testName() throws Exception {


    }
}
