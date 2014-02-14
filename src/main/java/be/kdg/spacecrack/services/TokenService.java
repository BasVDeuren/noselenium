package be.kdg.spacecrack.services;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenService implements ITokenService {

    @Autowired
    TokenRepository tokenRepository;

    public TokenService() {
    }

    public TokenService(TokenRepository tokenRepository){
        this.tokenRepository = tokenRepository;
    }

    @Override
    public AccessToken getAccessTokenByValue(String accessTokenValue) throws Exception {
        return tokenRepository.getAccessTokenByValue(accessTokenValue);
    }
}
