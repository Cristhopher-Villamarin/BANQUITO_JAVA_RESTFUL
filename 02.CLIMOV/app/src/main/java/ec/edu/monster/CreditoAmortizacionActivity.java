package ec.edu.monster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ec.edu.monster.model.Credito;
import ec.edu.monster.model.CuotaAmortizacion;
import ec.edu.monster.model.TablaAmortizacion;
import ec.edu.monster.service.BanquitoService;
import ec.edu.monster.util.SessionManager;

public class CreditoAmortizacionActivity extends AppCompatActivity {

    private EditText editCedula;
    private EditText editIdCredito;
    private Button btnBuscarCreditos;
    private Button btnVerTabla;
    private ProgressBar progressBar;
    private TextView textInfo;
    private ListView listView;           // Para créditos activos
    private androidx.recyclerview.widget.RecyclerView recyclerViewAmortizacion;// Para tabla
    private View layoutCreditos;         // Contenedor dinámico
    private View cardInfo;
    private View cardTabla;

    private BanquitoService banquitoService;
    private SessionManager sessionManager;
    private List<Credito> creditos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credito_amortizacion);

        // ... (session check igual)

        setTitle("Tabla de Amortización");
        banquitoService = new BanquitoService();

        // Vistas
        editCedula = findViewById(R.id.editCedula);
        editIdCredito = findViewById(R.id.editIdCredito);
        btnBuscarCreditos = findViewById(R.id.btnBuscarCreditos);
        btnVerTabla = findViewById(R.id.btnVerTabla);
        progressBar = findViewById(R.id.progressBar);
        textInfo = findViewById(R.id.textInfo);
        listView = findViewById(R.id.listView);
        recyclerViewAmortizacion = findViewById(R.id.recyclerViewAmortizacion);
        layoutCreditos = findViewById(R.id.layoutCreditos);
        cardInfo = findViewById(R.id.cardInfo);
        cardTabla = findViewById(R.id.cardTabla);

        btnBuscarCreditos.setOnClickListener(v -> {
            String cedula = editCedula.getText().toString().trim();
            if (cedula.isEmpty()) {
                Toast.makeText(this, "Ingrese la cédula", Toast.LENGTH_SHORT).show();
                return;
            }
            new BuscarCreditosTask().execute(cedula);
        });

        btnVerTabla.setOnClickListener(v -> {
            String idCreditoStr = editIdCredito.getText().toString().trim();
            if (idCreditoStr.isEmpty()) {
                Toast.makeText(this, "Ingrese el ID del crédito", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                Integer idCredito = Integer.parseInt(idCreditoStr);
                new CargarTablaTask().execute(idCredito);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // === TAREA: Buscar créditos activos ===
    private class BuscarCreditosTask extends AsyncTask<String, Void, List<Credito>> {
        private String errorMessage = null;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnBuscarCreditos.setEnabled(false);
            // Ocultar resultados previos
            cardInfo.setVisibility(View.GONE);
            cardTabla.setVisibility(View.GONE);
            recyclerViewAmortizacion.setVisibility(View.GONE);
        }

        @Override
        protected List<Credito> doInBackground(String... params) {
            try {
                return banquitoService.obtenerCreditosActivos(params[0]);
            } catch (Exception e) {
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Credito> result) {
            progressBar.setVisibility(View.GONE);
            btnBuscarCreditos.setEnabled(true);

            if (result != null && !result.isEmpty()) {
                creditos = result;
                List<String> items = new ArrayList<>();
                for (Credito c : result) {
                    items.add(String.format("ID: %d | Monto: $%.2f | Plazo: %d meses | Cuota: $%.2f",
                            c.getIdCredito(), c.getMontoAprobado(), c.getPlazoMeses(), c.getCuotaFija()));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreditoAmortizacionActivity.this,
                        R.layout.item_credito_activo, R.id.textItemCredito, items);
                listView.setAdapter(adapter);
                // Mostrar sección de créditos + formulario de ID
                layoutCreditos.setVisibility(View.VISIBLE);

                listView.setOnItemClickListener((parent, view, position, id) -> {
                    Credito credito = creditos.get(position);
                    editIdCredito.setText(String.valueOf(credito.getIdCredito()));
                });
            } else {
                layoutCreditos.setVisibility(View.GONE);
                String mensaje = errorMessage != null ? "Error: " + errorMessage : "No se encontraron créditos activos";
                Toast.makeText(CreditoAmortizacionActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        }
    }

    // === TAREA: Cargar tabla de amortización ===
    private class CargarTablaTask extends AsyncTask<Integer, Void, TablaAmortizacion> {
        private String errorMessage = null;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnVerTabla.setEnabled(false);
        }

        @Override
        protected TablaAmortizacion doInBackground(Integer... params) {
            try {
                return banquitoService.obtenerTablaAmortizacion(params[0]);
            } catch (Exception e) {
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(TablaAmortizacion tabla) {
            progressBar.setVisibility(View.GONE);
            btnVerTabla.setEnabled(true);

            if (tabla != null && tabla.getCuotas() != null && !tabla.getCuotas().isEmpty()) {
                StringBuilder info = new StringBuilder();
                info.append("ID Factura: ").append(tabla.getIdFactura()).append("\n");
                info.append("Cliente: ").append(tabla.getNombreCliente())
                        .append(" (").append(tabla.getCedulaCliente()).append(")\n");
                info.append("Monto financiado: $").append(tabla.getMontoTotal()).append("\n");
                info.append("Cuota mensual: $").append(tabla.getCuotaMensual()).append("\n");
                textInfo.setText(info.toString());

                cardInfo.setVisibility(View.VISIBLE);
                cardTabla.setVisibility(View.VISIBLE);

                ec.edu.monster.adapter.AmortizacionTableAdapter adapter =
                        new ec.edu.monster.adapter.AmortizacionTableAdapter(
                                CreditoAmortizacionActivity.this, tabla.getCuotas());

                recyclerViewAmortizacion.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(
                        CreditoAmortizacionActivity.this));
                recyclerViewAmortizacion.setAdapter(adapter);
                recyclerViewAmortizacion.setVisibility(View.VISIBLE);
            } else {
                cardInfo.setVisibility(View.GONE);
                cardTabla.setVisibility(View.GONE);
                //listViewAmortizacion.setVisibility(View.GONE);
                String mensaje = errorMessage != null ? "Error: " + errorMessage : "No se encontró la tabla de amortización";
                Toast.makeText(CreditoAmortizacionActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        }
    }
}

