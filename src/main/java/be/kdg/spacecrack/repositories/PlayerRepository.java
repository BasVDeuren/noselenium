package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Player;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("playerRepository")
public class PlayerRepository implements IPlayerRepository {

    @Autowired
    SessionFactory sessionFactory;

    public PlayerRepository() {
    }

    public PlayerRepository(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    @Override
    public void createPlayer(Player player) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(player);
    }

    @Override
    public void updatePlayer(Player player) {
        Session session = sessionFactory.getCurrentSession();

        session.update(player);


    }

    @Override
    public Player getPlayerByPlayerId(int playerId) {
        Session session = sessionFactory.getCurrentSession();
        Player player = null;

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Player p where p.playerId = :playerId");
        q.setParameter("playerId", playerId);
        try{
            player = (Player) q.uniqueResult();

        }catch (Exception ex)
        {
            boolean b = true;
        }



        return player;

    }


}
