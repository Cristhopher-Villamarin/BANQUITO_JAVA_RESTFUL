package ec.edu.monster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import ec.edu.monster.model.LoginResponse;
import ec.edu.monster.service.AuthService;
import ec.edu.monster.util.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private AuthService authService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        authService = new AuthService();

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        // Si ya está logueado, auto-completar campos pero mostrar pantalla
        if (sessionManager.isLoggedIn()) {
            String username = sessionManager.getUsername();
            if (username != null) {
                editUsername.setText(username);
            }
            // Mostrar mensaje informativo
            Toast.makeText(this, "Sesión activa. Presiona 'Entrar' para continuar o cierra sesión desde el menú",
                    Toast.LENGTH_LONG).show();
        }

        btnLogin.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Si hay sesión activa y las credenciales coinciden, redirigir sin hacer login
            if (sessionManager.isLoggedIn()) {
                String savedUsername = sessionManager.getUsername();
                if (savedUsername != null && savedUsername.equals(username)) {
                    // Credenciales coinciden, redirigir directamente
                    redirectByRole();
                    return;
                }
            }

            new LoginTask().execute(username, password);
        });

        // Configurar OnBackPressedDispatcher (reemplazo moderno de onBackPressed)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Si hay sesión activa, redirigir según rol
                // Si no hay sesión, salir de la app
                if (sessionManager.isLoggedIn()) {
                    redirectByRole();
                } else {
                    moveTaskToBack(true);
                }
            }
        });
    }

    private void redirectByRole() {
        if (sessionManager.isAdmin()) {
            startActivity(new Intent(this, AdminElectrodomesticosActivity.class));
        } else if (sessionManager.isUser()) {
            startActivity(new Intent(this, UserMenuActivity.class));
        }
        finish();
    }

    private class LoginTask extends AsyncTask<String, Void, LoginResponse> {
        private String errorMessage = null;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
        }

        @Override
        protected LoginResponse doInBackground(String... params) {
            try {
                return authService.login(params[0], params[1]);
            } catch (Exception e) {
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(LoginResponse response) {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);

            if (response != null && response.isAutenticado()) {
                sessionManager.createSession(response);
                // El token ya se estableció en AuthService, pero lo guardamos también en SessionManager
                ec.edu.monster.db.HttpClientUtil.setAuthToken(response.getToken());
                Toast.makeText(LoginActivity.this, "Bienvenido " + response.getUsername(), Toast.LENGTH_SHORT).show();
                redirectByRole();
            } else {
                String mensaje = errorMessage != null ? errorMessage :
                        (response != null ? response.getMensaje() : "Credenciales inválidas");
                Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        }
    }
}