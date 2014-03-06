package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Planet;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("planetRepository")
public class PlanetRepository implements IPlanetRepository {
    @Autowired
    SessionFactory sessionFactory;

    public PlanetRepository() {
    }

    public PlanetRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Planet getPlanetByName(String planetName) {
        Session session = sessionFactory.getCurrentSession();
        Planet planet;

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Planet p where p.name = :name");
        q.setParameter("name", planetName);
        planet = (Planet) q.uniqueResult();


        return planet;
    }


    public void createPlanets(Planet[] planets) {
        Session session = sessionFactory.getCurrentSession();

        for (Planet planet : planets) {
            session.saveOrUpdate(planet);
        }


    }


    public Planet[] getAll() {
        Session session = sessionFactory.getCurrentSession();
        Planet[] planets;
        List result;

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Planet");
        result = q.list();


        int size = result.size();
        planets = new Planet[size];
        for (int i = 0; i < size; i++) {
            planets[i] = (Planet) result.get(i);
        }
        return planets;
    }
}
