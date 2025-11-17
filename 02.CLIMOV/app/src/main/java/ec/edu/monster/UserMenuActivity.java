package ec.edu.monster;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import ec.edu.monster.util.SessionManager;

public class UserMenuActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        sessionManager = new SessionManager(this);

        // Verificar autenticación
        if (!sessionManager.isLoggedIn() || !sessionManager.isUser()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Restaurar token de sesión
        String token = sessionManager.getToken();
        if (token != null) {
            ec.edu.monster.db.HttpClientUtil.setAuthToken(token);
        }

        Button btnCatalogo = findViewById(R.id.btnCatalogo);
        Button btnVenta = findViewById(R.id.btnVenta);
        Button btnCreditoEstado = findViewById(R.id.btnCreditoEstado);
        Button btnAmortizacion = findViewById(R.id.btnAmortizacion);

        btnCatalogo.setOnClickListener(v -> {
            startActivity(new Intent(this, CatalogoActivity.class));
        });

        btnVenta.setOnClickListener(v -> {
            startActivity(new Intent(this, VentaActivity.class));
        });

        btnCreditoEstado.setOnClickListener(v -> {
            startActivity(new Intent(this, CreditoEstadoActivity.class));
        });

        btnAmortizacion.setOnClickListener(v -> {
            startActivity(new Intent(this, CreditoAmortizacionActivity.class));
        });

        // Configurar OnBackPressedDispatcher (reemplazo moderno de onBackPressed)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Al presionar back, volver al login (sin cerrar sesión)
                // El usuario puede volver a entrar sin necesidad de loguearse de nuevo
                Intent intent = new Intent(UserMenuActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Verificar que la sesión sigue activa
        if (!sessionManager.isLoggedIn() || !sessionManager.isUser()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            sessionManager.logout();
            ec.edu.monster.db.HttpClientUtil.clearAuthToken();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}