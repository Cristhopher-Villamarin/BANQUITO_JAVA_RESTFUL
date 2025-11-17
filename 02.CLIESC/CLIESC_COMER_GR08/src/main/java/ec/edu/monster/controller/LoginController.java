package ec.edu.monster.controller;

import ec.edu.monster.model.LoginResponse;
import ec.edu.monster.service.AuthService;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class LoginController {
    private final AuthService authService;

    public LoginController() {
        this.authService = new AuthService();
    }

    public LoginResponse login(String username, String password) throws IOException, ParseException {
        return authService.login(username, password);
    }

    public void logout() {
        authService.logout();
    }
}
