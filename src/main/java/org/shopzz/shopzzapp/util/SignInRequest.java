package org.shopzz.shopzzapp.util;

public class SignInRequest {
    protected String email, password;
    protected Location location;
    public SignInRequest(String email, String password, Location location) {
        this.email = email;
        this.password = password;
        this.location = location;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Location getLocation() { return this.location; }

    public void setLocation() { this.location = location; }


}