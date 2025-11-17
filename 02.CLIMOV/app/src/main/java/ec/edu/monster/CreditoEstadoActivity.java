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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import ec.edu.monster.model.CreditoEvaluacionResponse;
import ec.edu.monster.service.BanquitoService;
import ec.edu.monster.util.SessionManager;

public class CreditoEstadoActivity extends AppCompatActivity {

    private EditText editCedula;
    private Button btnConsultar;
    private ProgressBar progressBar;
    private MaterialCardView cardResultado;
    private TextView textResultado;
    private BanquitoService banquitoService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credito_estado);

        sessionManager = new SessionManager(this);

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

        setTitle("Estado de Crédito");
        banquitoService = new BanquitoService();

        editCedula = findViewById(R.id.editCedula);
        btnConsultar = findViewById(R.id.btnConsultar);
        progressBar = findViewById(R.id.progressBar);
        cardResultado = findViewById(R.id.cardResultado);
        textResultado = findViewById(R.id.textResultado);

        btnConsultar.setOnClickListener(v -> {
            String cedula = editCedula.getText().toString().trim();
            if (cedula.isEmpty()) {
                Toast.makeText(this, "Ingrese la cédula", Toast.LENGTH_SHORT).show();
                return;
            }
            new ConsultarCreditoTask().execute(cedula);
        });
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

    private class ConsultarCreditoTask extends AsyncTask<String, Void, CreditoEvaluacionResponse> {
        private String errorMessage = null;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnConsultar.setEnabled(false);
            textResultado.setText("");
            cardResultado.setVisibility(View.GONE);
        }

        @Override
        protected CreditoEvaluacionResponse doInBackground(String... params) {
            try {
                return banquitoService.verificarSujetoCredito(params[0]);
            } catch (Exception e) {
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(CreditoEvaluacionResponse evaluacion) {
            progressBar.setVisibility(View.GONE);
            btnConsultar.setEnabled(true);
            
            if (evaluacion != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Cédula: ").append(editCedula.getText().toString()).append("\n\n");
                sb.append("Sujeto de crédito: ");
                if (evaluacion.getSujetoCredito() != null && evaluacion.getSujetoCredito()) {
                    sb.append("Sí\n");
                } else {
                    sb.append("No\n");
                }
                
                if (evaluacion.getMontoMaximoCredito() != null) {
                    sb.append("Monto máximo: $").append(evaluacion.getMontoMaximoCredito()).append("\n");
                }
                
                if (evaluacion.getCreditoAprobado() != null) {
                    sb.append("Crédito actual: ");
                    sb.append(evaluacion.getCreditoAprobado() ? "Aprobado\n" : "Rechazado\n");
                }
                
                if (evaluacion.getCuotaMensual() != null) {
                    sb.append("Cuota mensual estimada: $").append(evaluacion.getCuotaMensual()).append("\n");
                }
                
                if (evaluacion.getIdCredito() != null) {
                    sb.append("ID Crédito: ").append(evaluacion.getIdCredito()).append("\n");
                }
                
                if (evaluacion.getMensaje() != null) {
                    sb.append("\n").append(evaluacion.getMensaje());
                }
                
                textResultado.setText(sb.toString());
            } else {
                textResultado.setText("Error: " + errorMessage);
            }

            cardResultado.setVisibility(View.VISIBLE);
        }
    }
}

