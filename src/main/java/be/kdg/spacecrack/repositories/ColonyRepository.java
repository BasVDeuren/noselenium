package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Colony;
import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.services.IColonyRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("colonyRepository")
public class ColonyRepository implements IColonyRepository {

    @Override
    public void createColony(Colony colony) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.saveOrUpdate(colony);
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
    public void updateColony(Colony colony) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.saveOrUpdate(colony);
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
    public List<Colony> getColoniesByGame(Game game) {
        List<Colony> colonies;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                //   @SuppressWarnings("JpaQlInspection") Query query = session.createQuery("select c from Colony c");

                @SuppressWarnings("JpaQlInspection") Query query = session.createQuery("from Colony c where c.player.game.gameId = :gameId ");
                query.setParameter("gameId", game.getGameId());
                //   List<Player> gameIds = query.list();
                colonies = query.list();

                tx.commit();
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        } finally {
            HibernateUtil.close(session);
        }
        return colonies;
    }

    @Override
    public Colony getColonyById(Integer colonyId) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Colony colony;
        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Colony c where c.colonyId = :colonyId");
                q.setParameter("colonyId", colonyId);
                colony = (Colony) q.uniqueResult();
                tx.commit();
            } catch (RuntimeException e) {
                throw e;
            }
        } finally {
            HibernateUtil.close(session);
        }

        return colony;


    }
}
