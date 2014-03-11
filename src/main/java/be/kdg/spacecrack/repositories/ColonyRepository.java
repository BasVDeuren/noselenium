package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Colony;
import be.kdg.spacecrack.model.Game;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("colonyRepository")
public class ColonyRepository implements IColonyRepository {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    SessionFactory sessionFactory;

    public ColonyRepository() {
    }

    public ColonyRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }



    @Override
    public List<Colony> getColoniesByGame(Game game) {
        List<Colony> colonies;
        Session session = sessionFactory.getCurrentSession();

        //   @SuppressWarnings("JpaQlInspection") Query query = session.createQuery("select c from Colony c");

        @SuppressWarnings("JpaQlInspection") Query query = session.createQuery("from Colony c where c.player.game.gameId = :gameId ");
        query.setParameter("gameId", game.getGameId());
        //   List<Player> gameIds = query.list();
        colonies = query.list();
        return colonies;

    }

    @Override
    public Colony getColonyById(Integer colonyId) {

        Session session = sessionFactory.getCurrentSession();
        Colony colony;

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Colony c where c.colonyId = :colonyId");
        q.setParameter("colonyId", colonyId);
        colony = (Colony) q.uniqueResult();


        return colony;


    }


}
