package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Ship;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

@Component("shipRepository")
public class ShipRepository implements IShipRepository {
    @Override
    public void createShip(Ship ship) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            try {
                session.saveOrUpdate(ship);
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
    public Ship getShipByShipId(int shipId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Ship ship;
        try {
            Transaction tx =session.beginTransaction();
            try {

                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Ship s where s.shipId = :shipId");
                q.setParameter("shipId", shipId);
                ship = (Ship) q.uniqueResult();
                tx.commit();
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        } finally {
            HibernateUtil.close(session);
        }

        return ship;
    }

    @Override
    public void updateShip(Ship ship) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            try {
                session.saveOrUpdate(ship);
                tx.commit();
            } catch (RuntimeException e) {
                tx.rollback();;
                e.printStackTrace();
            }
        } finally {
            HibernateUtil.close(session);
        }
    }
}
