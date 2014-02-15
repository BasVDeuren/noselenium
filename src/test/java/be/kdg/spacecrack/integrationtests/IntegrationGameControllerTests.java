package be.kdg.spacecrack.integrationtests;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.AccessToken;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class IntegrationGameControllerTests extends BaseFilteredIntegrationTests {

    private AccessToken token;

  /*  @Before
    public void setUp() throws Exception {
        UserRepository repository = new UserRepository();
        repository.addUser("test","test","test@test.com");
        User test = repository.getUserByUsername("test");
        TokenController tokenController = new TokenController();
        token = tokenController.login(test);

  



    }**/

    @Test
    public void testTrue() throws Exception {
        assertTrue(true);

    }
/* @Test
    public void Post_ValidAccessTokenandContact_ResponseOkwithGame() throws Exception {

        mockMvc.perform(post("/api/auth/game").cookie(new Cookie("accessTokenValue", token.getValue())));
    }
*/

}
