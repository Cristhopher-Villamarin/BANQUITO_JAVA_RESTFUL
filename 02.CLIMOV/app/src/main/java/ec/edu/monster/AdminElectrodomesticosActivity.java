package ec.edu.monster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ec.edu.monster.adapter.ElectrodomesticoAdapter;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.service.ElectrodomesticoService;
import ec.edu.monster.util.SessionManager;

public class AdminElectrodomesticosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MaterialButton btnNuevo;
    private FloatingActionButton fabAdd;
    private ProgressBar progressBar;
    private ElectrodomesticoService electrodomesticoService;
    private SessionManager sessionManager;
    private List<Electrodomestico> electrodomesticos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_electrodomesticos);

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
        recyclerView = findViewById(R.id.recyclerView);
        btnNuevo = findViewById(R.id.btnNuevo);
        fabAdd = findViewById(R.id.fabAdd);
        progressBar = findViewById(R.id.progressBar);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Configurar botón de nuevo
        View.OnClickListener nuevoListener = v -> {
            Intent intent = new Intent(this, ElectrodomesticoFormActivity.class);
            intent.putExtra("action", "crear");
            startActivity(intent);
        };

        btnNuevo.setOnClickListener(nuevoListener);
        fabAdd.setOnClickListener(nuevoListener);

        // Configurar OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Al presionar back, volver al login (sin cerrar sesión)
                Intent intent = new Intent(AdminElectrodomesticosActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        loadElectrodomesticos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Verificar que la sesión sigue activa
        if (!sessionManager.isLoggedIn() || !sessionManager.isAdmin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        loadElectrodomesticos();
    }

    private void loadElectrodomesticos() {
        new LoadElectrodomesticosTask().execute();
    }

    private void showOptionsDialog(Electrodomestico electrodomestico) {
        String[] options = {"Editar", "Eliminar"};
        new AlertDialog.Builder(this)
                .setTitle("Opciones")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Editar
                        Intent intent = new Intent(this, ElectrodomesticoFormActivity.class);
                        intent.putExtra("action", "editar");
                        intent.putExtra("id", electrodomestico.getIdElectrodomestico());
                        intent.putExtra("nombre", electrodomestico.getNombre());
                        intent.putExtra("precio", electrodomestico.getPrecioVenta().doubleValue());
                        startActivity(intent);
                    } else {
                        // Eliminar
                        new AlertDialog.Builder(this)
                                .setTitle("Confirmar eliminación")
                                .setMessage("¿Está seguro de eliminar este electrodoméstico?")
                                .setPositiveButton("Eliminar", (d, w) -> {
                                    new DeleteElectrodomesticoTask().execute(electrodomestico.getIdElectrodomestico());
                                })
                                .setNegativeButton("Cancelar", null)
                                .show();
                    }
                })
                .show();
    }
    
    private void setupRecyclerView(List<Electrodomestico> electrodomesticos) {
        ElectrodomesticoAdapter adapter = new ElectrodomesticoAdapter(
                this,
                electrodomesticos,
                new ElectrodomesticoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Electrodomestico electrodomestico) {
                        // Mostrar opciones al hacer clic en un elemento
                        showOptionsDialog(electrodomestico);
                    }

                    @Override
                    public void onOptionsClick(Electrodomestico electrodomestico) {
                        // Mostrar opciones al hacer clic en el botón de opciones
                        showOptionsDialog(electrodomestico);
                    }
                });
        recyclerView.setAdapter(adapter);
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

    private class LoadElectrodomesticosTask extends AsyncTask<Void, Void, List<Electrodomestico>> {
        private String errorMessage = null;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Electrodomestico> doInBackground(Void... voids) {
            try {
                return electrodomesticoService.listarElectrodomesticos();
            } catch (Exception e) {
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Electrodomestico> result) {
            progressBar.setVisibility(View.GONE);
            if (result != null) {
                electrodomesticos = result;
                setupRecyclerView(electrodomesticos);
            } else {
                Toast.makeText(AdminElectrodomesticosActivity.this, 
                    "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class DeleteElectrodomesticoTask extends AsyncTask<Integer, Void, Boolean> {
        private String errorMessage = "Error desconocido";

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                int id = params[0];
                Log.d("DeleteTask", "Intentando eliminar electrodoméstico con ID: " + id);
                return electrodomesticoService.eliminar(id);
            } catch (Exception e) {
                errorMessage = e.getMessage();
                Log.e("DeleteTask", "Error al eliminar: " + errorMessage, e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressBar.setVisibility(View.GONE);
            if (success) {
                Log.d("DeleteTask", "Eliminación exitosa, recargando lista...");
                loadElectrodomesticos();
                Toast.makeText(AdminElectrodomesticosActivity.this, 
                    "Electrodoméstico eliminado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("DeleteTask", "Error en la respuesta: " + errorMessage);
                Toast.makeText(AdminElectrodomesticosActivity.this, 
                    "Error al eliminar: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }
}

