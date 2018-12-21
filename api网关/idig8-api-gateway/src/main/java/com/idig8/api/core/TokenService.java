package com.idig8.api.core;

/**
 * Created by idig8.com
 */
public interface TokenService {

    public Token createToken();

    public Token getToken(String token);
}
