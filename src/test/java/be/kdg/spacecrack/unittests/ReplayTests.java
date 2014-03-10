package be.kdg.spacecrack.unittests;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.*;
import be.kdg.spacecrack.utilities.IFirebaseUtil;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class ReplayTests {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ApplicationContext applicationContext;

    private User user;
    private IPlayerRepository playerRepository;
    private Profile opponentProfile;

    @Autowired
    IFirebaseUtil firebaseUtil;

    private void makeProfiles() throws Exception {
        playerRepository = new PlayerRepository(sessionFactory);
        ColonyRepository colonyRepository = new ColonyRepository(sessionFactory);
        ShipRepository shipRepository = new ShipRepository(sessionFactory);

        user = new User();
        Profile profile = new Profile();
        opponentProfile = new Profile();
        User opponentUser = new User();
        opponentUser.setProfile(opponentProfile);
        user.setProfile(profile);
    }

    private void createMap() {
        MapFactory mapFactory = new MapFactory(sessionFactory, new PlanetRepository(sessionFactory));
        mapFactory.createPlanets();
    }

    @Test
    public void legeTest() throws Exception {
        assertTrue(true);

    }



    /*   @Test
    public void startReplay_gameWith3Moves_3GameStatusPushedThroughFirebase() throws Exception {
        //region Arrange
        HibernateTransactionManager transactionManager = (HibernateTransactionManager) applicationContext.getBean("transactionManager");
        //region Transaction 1
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        createMap();
        makeProfiles();
        ColonyRepository colonyRepository = new ColonyRepository(sessionFactory);
        ShipRepository shipRepository = new ShipRepository(sessionFactory);
        IFirebaseUtil mockedFirebaseUtil = mock(IFirebaseUtil.class);
        GameService gameService = new GameService(new PlanetRepository(sessionFactory), colonyRepository, shipRepository, playerRepository, new GameRepository(sessionFactory), new MoveShipHandler(colonyRepository), new ViewModelConverter(), mockedFirebaseUtil,new AsyncConfig(), new HibernateTransactionManager(sessionFactory));

        int gameId = gameService.createGame(user.getProfile(), "SpaceCrackName", opponentProfile);


        Game game =  gameService.getGameByGameId(gameId);
        Ship ship = game.getPlayers().get(0).getShips().get(0);
        transactionManager.commit(status);
        //endregion

        //region Transaction 2
        TransactionStatus status2 = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        gameService.moveShip(ship.getShipId(), "b");
        transactionManager.commit(status2);
        //endregion


        //region Transaction 3
        TransactionStatus status3 = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        gameService.moveShip(ship.getShipId(), "c");
        transactionManager.commit(status3);
        //endregion

        //region Transaction 4
        TransactionStatus status4 = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        gameService.moveShip(ship.getShipId(), "b");
        transactionManager.commit(status4);
        //endregion
        //endregion

        //region Transaction 5
        TransactionStatus status5 = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        //region Act
        gameService.startReplay(game.getPlayers().get(0).getPlayerId(),"stubFirebaseadress");
        //endregion
        transactionManager.commit(status5);
        //endregion

        //region Assert
        ArgumentCaptor<GameViewModel> gameArgumentCaptor = ArgumentCaptor.forClass(GameViewModel.class);
        verify(mockedFirebaseUtil, VerificationModeFactory.times(4)).setValue(eq("stubFirebaseadress"), gameArgumentCaptor.capture());

        List<GameViewModel> gameViewModels = gameArgumentCaptor.getAllValues();

        GameViewModel gameViewModel1 = gameViewModels.get(0);
        assertEquals("Actioncounter of gameViewModel should be 1 the first time.", 1, gameViewModel1.getActionNumber());
        GameViewModel gameViewModel2 = gameViewModels.get(1);
        assertEquals("Actioncounter of gameViewModel should be 2 the second time.", 2, gameViewModel2.getActionNumber());
        assertEquals("The colonySize should be 2", 2, gameViewModel2.getPlayer1().getColonies().size());
        GameViewModel gameViewModel3 = gameViewModels.get(2);
        assertEquals("Actioncounter of gameViewModel should be 3 the third time.", 3, gameViewModel3.getActionNumber());
        assertEquals("The colonySize should be 3", 3, gameViewModel3.getPlayer1().getColonies().size());
        GameViewModel gameViewModel4 = gameViewModels.get(3);
        assertEquals("Actioncounter of gameViewModel should be 4 the fourth time.", 4, gameViewModel4.getActionNumber());
        assertEquals("The colonySize should still be 3", 3, gameViewModel4.getPlayer1().getColonies().size());
        //endregion

    }*/


}
