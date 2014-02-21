package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Player;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

@Component("playerRepository")
public class PlayerRepository implements IPlayerRepository {

    @Override
    public void createPlayer(Player player) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            try {
                session.saveOrUpdate(player);
                tx.commit();
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        } finally {
            HibernateUtil.close(session);
        }
    }

    @Override
    public void updatePlayer(Player player) {
        createPlayer(player);
    }

    @Override
    public Player getPlayerByPlayerId(int playerId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Player player;
        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Player p where p.playerId = :playerId");
                q.setParameter("playerId", playerId);
                player = (Player) q.uniqueResult();
                tx.commit();
            } catch (RuntimeException e) {
                throw e;
            }
        } finally {
            HibernateUtil.close(session);
        }

        return player;

    }


}
