package ec.edu.monster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ec.edu.monster.R;
import ec.edu.monster.adapter.DetalleFacturaAdapter;
import ec.edu.monster.adapter.FacturaAdapter;
import ec.edu.monster.model.Factura;
import ec.edu.monster.model.FacturaConDetalles;
import ec.edu.monster.service.FacturaService;
import ec.edu.monster.util.SessionManager;

public class FacturasClienteActivity extends AppCompatActivity {

    private EditText editCedula;
    private Button btnBuscarFacturas;
    private ProgressBar progressBar;
    private TextView textMensaje;
    private RecyclerView recyclerViewFacturas;

    private FacturaService facturaService;
    private SessionManager sessionManager;
    private List<Factura> facturas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturas_cliente);

        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn() || !sessionManager.isUser()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String token = sessionManager.getToken();
        if (token != null) {
            ec.edu.monster.db.HttpClientUtil.setAuthToken(token);
        }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Consulta de facturas");
        }

        editCedula = findViewById(R.id.editCedula);
        btnBuscarFacturas = findViewById(R.id.btnBuscarFacturas);
        progressBar = findViewById(R.id.progressBar);
        textMensaje = findViewById(R.id.textMensaje);
        recyclerViewFacturas = findViewById(R.id.recyclerViewFacturas);

        recyclerViewFacturas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFacturas.setAdapter(new FacturaAdapter(facturas, this::mostrarDetalleFactura));

        facturaService = new FacturaService();

        btnBuscarFacturas.setOnClickListener(v -> buscarFacturas());
    }

    private void buscarFacturas() {
        String cedula = editCedula.getText().toString().trim();
        if (cedula.isEmpty()) {
            Toast.makeText(this, "Ingrese la cédula", Toast.LENGTH_SHORT).show();
            return;
        }
        new BuscarFacturasTask().execute(cedula);
    }

    private class BuscarFacturasTask extends AsyncTask<String, Void, List<Factura>> {
        private String errorMessage;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnBuscarFacturas.setEnabled(false);
            textMensaje.setText("");
        }

        @Override
        protected List<Factura> doInBackground(String... params) {
            try {
                return facturaService.listarFacturasPorCedula(params[0]);
            } catch (IOException e) {
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Factura> result) {
            progressBar.setVisibility(View.GONE);
            btnBuscarFacturas.setEnabled(true);

            if (result == null) {
                String mensaje = errorMessage != null ? errorMessage : "Error al buscar facturas";
                Toast.makeText(FacturasClienteActivity.this, mensaje, Toast.LENGTH_LONG).show();
                textMensaje.setText("Ocurrió un error al buscar las facturas");
                facturas.clear();
                recyclerViewFacturas.getAdapter().notifyDataSetChanged();
                return;
            }

            if (result.isEmpty()) {
                textMensaje.setText("No se encontraron facturas para la cédula proporcionada");
            } else {
                textMensaje.setText("");
            }

            facturas.clear();
            facturas.addAll(result);
            recyclerViewFacturas.setAdapter(new FacturaAdapter(facturas, FacturasClienteActivity.this::mostrarDetalleFactura));
        }
    }

    private void mostrarDetalleFactura(Factura factura) {
        if (factura == null || factura.getIdFactura() == null) {
            Toast.makeText(this, "Factura inválida", Toast.LENGTH_SHORT).show();
            return;
        }
        new CargarDetalleFacturaTask().execute(factura.getIdFactura());
    }

    private class CargarDetalleFacturaTask extends AsyncTask<Integer, Void, FacturaConDetalles> {
        private String errorMessage;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected FacturaConDetalles doInBackground(Integer... params) {
            try {
                return facturaService.obtenerFacturaConDetalles(params[0]);
            } catch (IOException e) {
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(FacturaConDetalles result) {
            progressBar.setVisibility(View.GONE);

            if (result == null || result.getFactura() == null || result.getDetalles() == null) {
                String mensaje = errorMessage != null ? errorMessage : "Error al obtener detalle de la factura";
                Toast.makeText(FacturasClienteActivity.this, mensaje, Toast.LENGTH_LONG).show();
                return;
            }

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_detalle_factura, null);
            TextView textInfoFactura = dialogView.findViewById(R.id.textInfoFactura);
            RecyclerView recyclerViewDetalles = dialogView.findViewById(R.id.recyclerViewDetalles);

            Factura factura = result.getFactura();
            StringBuilder info = new StringBuilder();
            info.append("Factura #").append(factura.getIdFactura()).append("\n");
            info.append("Fecha: ").append(factura.getFecha()).append("\n");
            info.append("Forma de pago: ").append(factura.getFormaPago()).append("\n");
            info.append("Total: $").append(factura.getTotal());
            textInfoFactura.setText(info.toString());

            recyclerViewDetalles.setLayoutManager(new LinearLayoutManager(FacturasClienteActivity.this));
            recyclerViewDetalles.setAdapter(new DetalleFacturaAdapter(result.getDetalles()));

            new AlertDialog.Builder(FacturasClienteActivity.this)
                    .setView(dialogView)
                    .setPositiveButton("Cerrar", null)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.menu_back) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
