package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Ship;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("shipRepository")
public class ShipRepository implements IShipRepository {

    @Autowired
    SessionFactory sessionFactory;

    public ShipRepository() {
    }

    public ShipRepository(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }



    @Override
    public Ship getShipByShipId(int shipId) {
        Session session = sessionFactory.getCurrentSession();
        Ship ship;

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Ship s where s.shipId = :shipId");
        q.setParameter("shipId", shipId);
        ship = (Ship) q.uniqueResult();


        return ship;
    }


}
