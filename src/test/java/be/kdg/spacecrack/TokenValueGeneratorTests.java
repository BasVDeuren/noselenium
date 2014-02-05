package be.kdg.spacecrack;

import be.kdg.spacecrack.utilities.ITokenStringGenerator;
import be.kdg.spacecrack.utilities.TokenStringGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Tim on 4/02/14.
 */
public class TokenValueGeneratorTests {

    @Test
    public void testGenerator() throws Exception {
        ITokenStringGenerator tokenValueGenerator = new TokenStringGenerator(1234L);
        String actual = tokenValueGenerator.generateTokenString();
        assertEquals("38gqit55s18bd2r37jf3lsgf7l", actual);


    }
}
