package be.kdg.spacecrack.utilities;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Tim on 4/02/14.
 */
@Component("generator")
public class TokenStringGenerator implements ITokenStringGenerator {
    long seed;
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
