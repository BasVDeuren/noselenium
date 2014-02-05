package be.kdg.spacecrack;

import org.junit.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Ikke on 5-2-14.
 */

public class FilterTest extends TestWithFilteredMockMVC {


    @Test
    public void testTokenFilterHello() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/auth/hello");

        mockMvc.perform(requestBuilder)
            .andExpect(status().isUnauthorized());
    }
}
