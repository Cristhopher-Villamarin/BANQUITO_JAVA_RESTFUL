package ec.edu.monster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.edu.monster.adapter.ProductoDisponibleAdapter;
import ec.edu.monster.adapter.ResumenProductoAdapter;
import ec.edu.monster.model.DetalleVentaRequest;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.VentaRequest;
import ec.edu.monster.model.VentaResponse;
import ec.edu.monster.service.BanquitoService;
import ec.edu.monster.service.ElectrodomesticoService;
import ec.edu.monster.service.VentaService;
import ec.edu.monster.util.SessionManager;

public class VentaActivity extends AppCompatActivity {

    private EditText editCedula;
    private EditText editNombre;
    private EditText editPlazo;
    private TextInputLayout layoutPlazo;
    private RadioGroup radioFormaPago;
    private LinearLayout containerProductos;
    private RecyclerView recyclerViewResumen;
    private Button btnProcesar;
    private ProgressBar progressBar;
    
    private ElectrodomesticoService electrodomesticoService;
    private VentaService ventaService;
    private BanquitoService banquitoService;
    private SessionManager sessionManager;
    
    private List<Electrodomestico> electrodomesticos;
    private Map<Integer, Integer> cantidades = new HashMap<>();
    private List<ProductoSeleccionado> productosSeleccionados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);

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

        setTitle("Registrar Venta");
        electrodomesticoService = new ElectrodomesticoService();
        ventaService = new VentaService();
        banquitoService = new BanquitoService();

        editCedula = findViewById(R.id.editCedula);
        editNombre = findViewById(R.id.editNombre);
        editPlazo = findViewById(R.id.editPlazo);
        layoutPlazo = findViewById(R.id.layoutPlazo);
        radioFormaPago = findViewById(R.id.radioFormaPago);
        containerProductos = findViewById(R.id.containerProductos);
        recyclerViewResumen = findViewById(R.id.recyclerViewResumen);
        btnProcesar = findViewById(R.id.btnProcesar);
        progressBar = findViewById(R.id.progressBar);

        radioFormaPago.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selected = findViewById(checkedId);
            if (selected != null && selected.getText().toString().contains("CRÉDITO")) {
                layoutPlazo.setVisibility(View.VISIBLE);
            } else {
                layoutPlazo.setVisibility(View.GONE);
            }
        });

        btnProcesar.setOnClickListener(v -> procesarVenta());

        loadProductos();
    }

    private void mostrarDialogoCantidad(Electrodomestico producto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar cantidad: " + producto.getNombre());

        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setHint("Cantidad");
        Integer cantidadActual = cantidades.get(producto.getIdElectrodomestico());
        if (cantidadActual != null && cantidadActual > 0) {
            input.setText(String.valueOf(cantidadActual));
        }
        builder.setView(input);

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            String cantidadStr = input.getText().toString().trim();
            if (!cantidadStr.isEmpty()) {
                try {
                    int cantidad = Integer.parseInt(cantidadStr);
                    if (cantidad > 0) {
                        // Agregar o actualizar en el resumen
                        agregarAlResumen(producto, cantidad);
                        Toast.makeText(VentaActivity.this, 
                            "Producto agregado: " + producto.getNombre() + " x" + cantidad, Toast.LENGTH_SHORT).show();
                    } else if (cantidad == 0) {
                        // Eliminar del resumen si la cantidad es 0
                        eliminarDelResumen(producto.getIdElectrodomestico());
                        Toast.makeText(VentaActivity.this, 
                            "Producto eliminado del resumen", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VentaActivity.this, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(VentaActivity.this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void agregarAlResumen(Electrodomestico producto, int cantidad) {
        // Buscar si ya existe en el resumen
        ProductoSeleccionado existente = null;
        for (ProductoSeleccionado ps : productosSeleccionados) {
            if (ps.getIdElectrodomestico().equals(producto.getIdElectrodomestico())) {
                existente = ps;
                break;
            }
        }

        if (existente != null) {
            // Actualizar cantidad existente
            existente.setCantidad(cantidad);
        } else {
            // Agregar nuevo producto
            productosSeleccionados.add(new ProductoSeleccionado(
                producto.getIdElectrodomestico(),
                producto.getNombre(),
                producto.getPrecioVenta(),
                cantidad
            ));
        }

        // Actualizar el mapa de cantidades
        cantidades.put(producto.getIdElectrodomestico(), cantidad);
        
        // Actualizar el resumen visual
        actualizarResumen();
    }

    private void eliminarDelResumen(Integer idElectrodomestico) {
        productosSeleccionados.removeIf(ps -> ps.getIdElectrodomestico().equals(idElectrodomestico));
        cantidades.put(idElectrodomestico, 0);
        actualizarResumen();
    }

    private void actualizarResumen() {
        // Configura el LayoutManager (siempre)
        recyclerViewResumen.setLayoutManager(new LinearLayoutManager(this));

        if (productosSeleccionados.isEmpty()) {
            // === CASO: LISTA VACÍA (mensaje centrado) ===
            recyclerViewResumen.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

                @NonNull
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    TextView textView = new TextView(parent.getContext());
                    textView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    textView.setPadding(48, 64, 48, 64);
                    textView.setText("No hay productos seleccionados");
                    textView.setTextColor(getResources().getColor(R.color.text_muted, getTheme()));
                    textView.setTextSize(16);
                    textView.setGravity(android.view.Gravity.CENTER);
                    return new RecyclerView.ViewHolder(textView) {};
                }

                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                    // No hay datos que bindear
                }

                @Override
                public int getItemCount() {
                    return 1; // Solo el mensaje
                }
            });
        } else {
            // === CASO: HAY PRODUCTOS ===
            ResumenProductoAdapter adapter = new ResumenProductoAdapter(productosSeleccionados);
            adapter.setOnItemLongClickListener(position -> {
                ProductoSeleccionado ps = productosSeleccionados.get(position);
                new AlertDialog.Builder(VentaActivity.this)
                        .setTitle("Eliminar producto")
                        .setMessage("¿Deseas eliminar " + ps.getNombre() + " del resumen?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            eliminarDelResumen(ps.getIdElectrodomestico());
                            Toast.makeText(this, ps.getNombre() + " eliminado", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            });
            recyclerViewResumen.setAdapter(adapter);
        }
    }

    private void loadProductos() {
        new LoadProductosTask().execute();
    }

    private void procesarVenta() {
        String cedula = editCedula.getText().toString().trim();
        String nombre = editNombre.getText().toString().trim();
        int selectedId = radioFormaPago.getCheckedRadioButtonId();
        
        if (cedula.isEmpty() || nombre.isEmpty() || selectedId == -1) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selected = findViewById(selectedId);
        String formaPago = selected.getText().toString().contains("EFECTIVO") ? "EFECTIVO" : "CREDITO_DIRECTO";
        
        Integer plazoMeses = null;
        if ("CREDITO_DIRECTO".equals(formaPago)) {
            String plazoStr = editPlazo.getText().toString().trim();
            if (plazoStr.isEmpty()) {
                Toast.makeText(this, "Ingrese el plazo en meses", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                plazoMeses = Integer.parseInt(plazoStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Plazo inválido", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Usar los productos del resumen en lugar del mapa de cantidades
        if (productosSeleccionados.isEmpty()) {
            Toast.makeText(this, "Debe agregar al menos un producto al resumen", Toast.LENGTH_SHORT).show();
            return;
        }

        List<DetalleVentaRequest> detalles = new ArrayList<>();
        for (ProductoSeleccionado ps : productosSeleccionados) {
            if (ps.getCantidad() > 0) {
                detalles.add(new DetalleVentaRequest(ps.getIdElectrodomestico(), ps.getCantidad()));
            }
        }

        if (detalles.isEmpty()) {
            Toast.makeText(this, "Debe agregar al menos un producto", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("CREDITO_DIRECTO".equals(formaPago)) {
            new VerificarCreditoTask().execute(cedula, nombre, formaPago, plazoMeses, detalles);
        } else {
            VentaRequest ventaRequest = new VentaRequest();
            ventaRequest.setCedulaCliente(cedula);
            ventaRequest.setNombreCliente(nombre);
            ventaRequest.setFormaPago(formaPago);
            ventaRequest.setPlazoMeses(plazoMeses);
            ventaRequest.setDetalles(detalles);
            new ProcesarVentaTask().execute(ventaRequest);
        }
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

    private class LoadProductosTask extends AsyncTask<Void, Void, List<Electrodomestico>> {
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
                for (Electrodomestico e : result) {
                    cantidades.put(e.getIdElectrodomestico(), 0);
                }

                // ← Cambio: Infla ítems dinámicamente en LinearLayout
                containerProductos.removeAllViews(); // Limpia por si acaso
                for (Electrodomestico producto : result) {
                    View itemView = getLayoutInflater().inflate(
                            R.layout.item_producto_disponible, containerProductos, false);

                    TextView textProducto = itemView.findViewById(R.id.textProducto);
                    textProducto.setText(String.format("%s - $%.2f", producto.getNombre(), producto.getPrecioVenta()));

                    // Agrega click listener directamente
                    itemView.setOnClickListener(v -> mostrarDialogoCantidad(producto));

                    containerProductos.addView(itemView);
                }

                actualizarResumen(); // inicial vacío
            } else {
                Toast.makeText(VentaActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class VerificarCreditoTask extends AsyncTask<Object, Void, Boolean> {
        private String errorMessage = null;
        private Object[] params;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnProcesar.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            this.params = params;
            try {
                String cedula = (String) params[0];
                ec.edu.monster.model.CreditoEvaluacionResponse evaluacion = 
                    banquitoService.verificarSujetoCredito(cedula);
                return evaluacion != null && evaluacion.getSujetoCredito() != null && 
                       evaluacion.getSujetoCredito();
            } catch (Exception e) {
                errorMessage = e.getMessage();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean esSujeto) {
            if (esSujeto) {
                VentaRequest ventaRequest = new VentaRequest();
                ventaRequest.setCedulaCliente((String) params[0]);
                ventaRequest.setNombreCliente((String) params[1]);
                ventaRequest.setFormaPago((String) params[2]);
                ventaRequest.setPlazoMeses((Integer) params[3]);
                ventaRequest.setDetalles((List<DetalleVentaRequest>) params[4]);
                new ProcesarVentaTask().execute(ventaRequest);
            } else {
                progressBar.setVisibility(View.GONE);
                btnProcesar.setEnabled(true);
                String mensaje = errorMessage != null ? errorMessage : "El cliente no es sujeto de crédito";
                Toast.makeText(VentaActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ProcesarVentaTask extends AsyncTask<VentaRequest, Void, VentaResponse> {
        private String errorMessage = null;

        @Override
        protected VentaResponse doInBackground(VentaRequest... params) {
            try {
                return ventaService.procesarVenta(params[0]);
            } catch (Exception e) {
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(VentaResponse response) {
            progressBar.setVisibility(View.GONE);
            btnProcesar.setEnabled(true);
            
            if (response == null) {
                String mensaje = errorMessage != null ? errorMessage : "Error al procesar venta: respuesta nula";
                Toast.makeText(VentaActivity.this, mensaje, Toast.LENGTH_LONG).show();
                return;
            }
            
            if (response.isVentaExitosa()) {
                try {
                    Intent intent = new Intent(VentaActivity.this, VentaConfirmacionActivity.class);
                    // Usar Gson para serializar a JSON (más confiable que Serializable con BigDecimal)
                    Gson gson = new Gson();
                    String ventaJson = gson.toJson(response);
                    intent.putExtra("ventaJson", ventaJson);
                    intent.putExtra("cedula", editCedula.getText().toString());
                    intent.putExtra("nombre", editNombre.getText().toString());
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(VentaActivity.this, 
                        "Error al abrir pantalla de confirmación: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    // En caso de error, no hacer finish para que el usuario pueda intentar de nuevo
                }
            } else {
                String mensaje = errorMessage != null ? errorMessage : 
                    (response.getMensaje() != null ? response.getMensaje() : "Venta rechazada");
                Toast.makeText(VentaActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        }
    }

    // Clase interna para representar un producto seleccionado en el resumen
    public static class ProductoSeleccionado {
        private Integer idElectrodomestico;
        private String nombre;
        private BigDecimal precio;
        private Integer cantidad;

        public ProductoSeleccionado(Integer idElectrodomestico, String nombre, BigDecimal precio, Integer cantidad) {
            this.idElectrodomestico = idElectrodomestico;
            this.nombre = nombre;
            this.precio = precio;
            this.cantidad = cantidad;
        }

        public Integer getIdElectrodomestico() {
            return idElectrodomestico;
        }

        public void setIdElectrodomestico(Integer idElectrodomestico) {
            this.idElectrodomestico = idElectrodomestico;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public BigDecimal getPrecio() {
            return precio;
        }

        public void setPrecio(BigDecimal precio) {
            this.precio = precio;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
}

