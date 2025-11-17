package ec.edu.monster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.service.ElectrodomesticoService;
import ec.edu.monster.util.SessionManager;

public class ElectrodomesticoFormActivity extends AppCompatActivity {

    private EditText editNombre;
    private EditText editPrecio;
    private Button btnGuardar;
    private ProgressBar progressBar;
    private ElectrodomesticoService electrodomesticoService;
    private SessionManager sessionManager;
    private String action;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electrodomestico_form);
        // En el método onCreate, después de setContentView
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Manejar el botón de retroceso
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Actualizar el título según la acción
        if ("editar".equals(action)) {
            id = getIntent().getIntExtra("id", 0);
            editNombre.setText(getIntent().getStringExtra("nombre"));
            editPrecio.setText(String.valueOf(getIntent().getDoubleExtra("precio", 0)));
            setTitle("Editar Electrodoméstico");
            // Actualizar el título en el TextView también
            TextView titleText = findViewById(R.id.titleText);
            if (titleText != null) {
                titleText.setText("Editar Electrodoméstico");
            }
        } else {
            setTitle("Nuevo Electrodoméstico");
        }
        sessionManager = new SessionManager(this);

        // Verificar autenticación
        if (!sessionManager.isLoggedIn() || !sessionManager.isAdmin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Restaurar token de sesión
        String token = sessionManager.getToken();
        if (token != null) {
            ec.edu.monster.db.HttpClientUtil.setAuthToken(token);
        }

        electrodomesticoService = new ElectrodomesticoService();
        editNombre = findViewById(R.id.editNombre);
        editPrecio = findViewById(R.id.editPrecio);
        btnGuardar = findViewById(R.id.btnGuardar);
        progressBar = findViewById(R.id.progressBar);

        action = getIntent().getStringExtra("action");
        if ("editar".equals(action)) {
            id = getIntent().getIntExtra("id", 0);
            editNombre.setText(getIntent().getStringExtra("nombre"));
            editPrecio.setText(String.valueOf(getIntent().getDoubleExtra("precio", 0)));
            setTitle("Editar Electrodoméstico");
        } else {
            setTitle("Nuevo Electrodoméstico");
        }

        btnGuardar.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();
            String precioStr = editPrecio.getText().toString().trim();

            if (nombre.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double precio = Double.parseDouble(precioStr);
                if ("editar".equals(action)) {
                    new UpdateElectrodomesticoTask().execute(id, nombre, precio);
                } else {
                    new CreateElectrodomesticoTask().execute(nombre, precio);
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class CreateElectrodomesticoTask extends AsyncTask<Object, Void, Electrodomestico> {
        private String errorMessage = null;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnGuardar.setEnabled(false);
        }

        @Override
        protected Electrodomestico doInBackground(Object... params) {
            try {
                return electrodomesticoService.crear((String) params[0], (Double) params[1]);
            } catch (Exception e) {
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Electrodomestico result) {
            progressBar.setVisibility(View.GONE);
            btnGuardar.setEnabled(true);
            if (result != null) {
                Toast.makeText(ElectrodomesticoFormActivity.this, "Electrodoméstico creado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ElectrodomesticoFormActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class UpdateElectrodomesticoTask extends AsyncTask<Object, Void, Electrodomestico> {
        private String errorMessage = null;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnGuardar.setEnabled(false);
        }

        @Override
        protected Electrodomestico doInBackground(Object... params) {
            try {
                return electrodomesticoService.actualizar((Integer) params[0], (String) params[1], (Double) params[2]);
            } catch (Exception e) {
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Electrodomestico result) {
            progressBar.setVisibility(View.GONE);
            btnGuardar.setEnabled(true);
            if (result != null) {
                Toast.makeText(ElectrodomesticoFormActivity.this, "Electrodoméstico actualizado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ElectrodomesticoFormActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }
}

