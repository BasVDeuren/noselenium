package be.kdg.spacecrack.integrationtests;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.utilities.FirebaseUtil;
import be.kdg.spacecrack.viewmodels.GameActivePlayerWrapper;
import be.kdg.spacecrack.viewmodels.GameViewModel;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IntegrationReplayControllerTests extends BaseFilteredIntegrationTests{
    @Autowired
    private ApplicationContext applicationContext;

    @Test @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void getReplay_validGameId_firebaseURL() throws Exception {
        HibernateTransactionManager transactionManager = (HibernateTransactionManager) applicationContext.getBean("transactionManager");
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));

        ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        String accessToken = loginAndRetrieveAccessToken();
        GameActivePlayerWrapper gameActivePlayerWrapper = createAGame(accessToken);
        GameViewModel game = gameActivePlayerWrapper.getGame();
        int playerId = game.getPlayer1().getPlayerId();

        transactionManager.commit(status);
        TransactionStatus status2 = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        mockMvc.perform(get("/auth/replay/" + playerId)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firebaseUrl", CoreMatchers.equalTo(FirebaseUtil.FIREBASEURLBASE + "oldGame/" + playerId)));
        transactionManager.commit(status2);
    }
}
