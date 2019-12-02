package org.shopzz.dao;

import org.shopzz.model.Connection;
import org.shopzz.shopzzapp.util.SignInRequest;

public interface AuthenticationDAO {
    Connection signIn(SignInRequest request);

    Connection register(SignInRequest request);
}