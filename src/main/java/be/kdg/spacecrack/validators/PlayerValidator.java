package be.kdg.spacecrack.validators;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.model.Player;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PlayerValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Player.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Player player = (Player) o;
        if(player.getCommandPoints() < 0)
        {
           throw new SpaceCrackNotAcceptableException("Insufficient commandPoints");
        }
    }
}
