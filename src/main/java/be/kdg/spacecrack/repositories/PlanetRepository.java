package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Planet;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

@Component("planetRepository")
public class PlanetRepository implements IPlanetRepository {
    @Override
    public Planet getPlanetByName(String planetName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Planet planet;
        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Planet p where p.name = :name");
                q.setParameter("name", planetName);
                planet = (Planet) q.uniqueResult();
                tx.commit();
            } catch (RuntimeException ex) {
                tx.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.close(session);
        }
        return planet;
    }
}
