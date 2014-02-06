package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;

/**
 * Created by Ikke on 6-2-14.
 */
public interface IUserRepository {
    void DeleteAccessToken(AccessToken accessToken) throws Exception;

    User getUser(User user) throws Exception;
}
