package be.kdg.spacecrack.utilities;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Random;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Component("generator")
public class TokenStringGenerator implements ITokenStringGenerator {
 Random random;
    public TokenStringGenerator(long seed) {
        random = new Random(seed);
    }

    public TokenStringGenerator() {
         random = new Random();
    }


    @Override
    public String generateTokenString() {
        return new BigInteger(130, random ).toString(32);
    }
}
