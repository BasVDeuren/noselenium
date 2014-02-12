package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;

/**
 * Created by Ikke on 7-2-14.
 */
public interface ITokenRepository {

    AccessToken getAccessToken(User dbUser) throws Exception;

    AccessToken getAccessTokenByValue(String value) throws Exception;
}
