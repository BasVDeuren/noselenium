package be.kdg.spacecrack.utilities;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import com.firebase.client.Firebase;
import org.springframework.stereotype.Component;

@Component("firebaseUtil")
public class FirebaseUtil implements IFirebaseUtil {
    public static final String FIREBASEURLBASE = "https://vivid-fire-9476.firebaseio.com/";

    public FirebaseUtil() {}

    @Override
    public void setValue(String firebaseSuffix, Object object) {
        Firebase ref = new Firebase(FIREBASEURLBASE + firebaseSuffix);
        ref.setValue(object);
    }
}
