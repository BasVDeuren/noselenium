package be.kdg.spacecrack.unittests;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.repositories.MapFactory;
import be.kdg.spacecrack.repositories.PlanetRepository;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations = {"classpath:application-context.xml"})

public abstract class BaseUnitTest {
    @Autowired
    SessionFactory sessionFactory;


    @Before
    public void createMap()
    {
            MapFactory mapFactory = new MapFactory(sessionFactory,new PlanetRepository(sessionFactory));
            mapFactory.createPlanets();
    }

}
