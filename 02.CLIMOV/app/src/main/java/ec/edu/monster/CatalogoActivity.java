package ec.edu.monster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.service.ElectrodomesticoService;
import ec.edu.monster.util.SessionManager;

public class CatalogoActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressBar progressBar;
    private ElectrodomesticoService electrodomesticoService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Restaurar token de sesión
        String token = sessionManager.getToken();
        if (token != null) {
            ec.edu.monster.db.HttpClientUtil.setAuthToken(token);
        }

        setTitle("Catálogo de Electrodomésticos");
        electrodomesticoService = new ElectrodomesticoService();
        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);

        loadCatalogo();
    }

    private void loadCatalogo() {
        new LoadCatalogoTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_back) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class LoadCatalogoTask extends AsyncTask<Void, Void, List<Electrodomestico>> {
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
                ec.edu.monster.adapter.ElectrodomesticoTableAdapter adapter = 
                    new ec.edu.monster.adapter.ElectrodomesticoTableAdapter(
                        CatalogoActivity.this, result);
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(CatalogoActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }
}

