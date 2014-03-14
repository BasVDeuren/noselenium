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
    public static final String FIREBASECHATURLBASE = "https://amber-fire-3394.firebaseio.com/";

    public FirebaseUtil() {}

    @Override
    public void setValue(String firebaseSuffix, Object object) {
        Firebase ref = new Firebase(FIREBASEURLBASE + firebaseSuffix);
        ref.setValue(object);
    }

    @Override
    public void clearSpaceCrack() {
        Firebase ref = new Firebase(FIREBASEURLBASE);
        ref.removeValue();
    }

    @Override
    public void clearChat() {
        Firebase ref = new Firebase(FIREBASECHATURLBASE);
        ref.removeValue();

    }
}
