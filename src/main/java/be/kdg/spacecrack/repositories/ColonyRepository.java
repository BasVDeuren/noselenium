package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Colony;
import be.kdg.spacecrack.services.IColonyRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

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
}
