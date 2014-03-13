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
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private SessionFactory sessionFactory;

    public PlayerRepository() {}

    public PlayerRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Player getPlayerByPlayerId(int playerId) {
        Session session = sessionFactory.getCurrentSession();

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Player p where p.playerId = :playerId");
        q.setParameter("playerId", playerId);

        return (Player) q.uniqueResult();
    }
}
