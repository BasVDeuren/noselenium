package be.kdg.spacecrack.validators;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.viewmodels.GameParameters;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class GameParametersValidator implements Validator{
    @Override
    public boolean supports(Class<?> clazz) {
        return GameParameters.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        GameParameters gameParameters = (GameParameters) o;
        //.$[]#/
        if(!gameParameters.getGameName().matches("^[a-zA-Z0-9]+$"))
        {
            throw new SpaceCrackNotAcceptableException("Insufficient commandPoints");
        }
    }
}
