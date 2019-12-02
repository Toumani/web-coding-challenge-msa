package org.shopzz.shopzzapp.util;

/**
 * It makes sens to extend SignInRequest sign a registration is right followed by a signing in
 */
public class RegisterRequest extends SignInRequest {
    private String name;

    public RegisterRequest(String name, String email, String password) {
        super(email, password);
        this.name = name;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}