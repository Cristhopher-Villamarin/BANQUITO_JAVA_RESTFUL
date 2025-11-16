package ec.edu.monster.service;

import ec.edu.monster.db.HttpClientUtil;
import ec.edu.monster.model.LoginRequest;
import ec.edu.monster.model.LoginResponse;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class AuthService {

    public LoginResponse login(String username, String password) throws IOException, ParseException {
        LoginRequest request = new LoginRequest(username, password);
        LoginResponse response = HttpClientUtil.post("/auth/login", request, LoginResponse.class);

        if (response.isAutenticado()) {
            HttpClientUtil.setAuthToken(response.getToken());
        }

        return response;
    }

    public void logout() {
        HttpClientUtil.clearAuthToken();
    }
}
