package org.shopzz.shopzzapp.web.controller;

import org.shopzz.dao.AuthenticationImpl;
import org.shopzz.model.Connection;
import org.shopzz.shopzzapp.util.RegisterRequest;
import org.shopzz.shopzzapp.util.SignInRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private AuthenticationImpl authentication = new AuthenticationImpl();

    @PostMapping(value = "/sign-in")
    public Connection signIn(@RequestBody SignInRequest request) {
        return authentication.signIn(request);
    }

    @PostMapping(value = "/register")
    public Connection register(@RequestBody RegisterRequest request) {
        return authentication.register(request);
    }
}