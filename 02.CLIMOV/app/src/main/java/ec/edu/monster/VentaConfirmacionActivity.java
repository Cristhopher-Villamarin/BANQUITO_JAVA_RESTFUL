package ec.edu.monster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ec.edu.monster.model.DetalleVentaResponse;
import ec.edu.monster.model.VentaResponse;

public class VentaConfirmacionActivity extends AppCompatActivity {

    private TextView textFactura;
    private TextView textCliente;
    private TextView textFormaPago;
    private TextView textEstado;
    private TextView textSubtotal;
    private TextView textDescuento;
    private TextView textTotal;
    private ListView listViewDetalles;
    private Button btnVolverMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta_confirmacion);

        setTitle("Venta Procesada");

        textFactura = findViewById(R.id.textFactura);
        textCliente = findViewById(R.id.textCliente);
        textFormaPago = findViewById(R.id.textFormaPago);
        textEstado = findViewById(R.id.textEstado);
        textSubtotal = findViewById(R.id.textSubtotal);
        textDescuento = findViewById(R.id.textDescuento);
        textTotal = findViewById(R.id.textTotal);
        listViewDetalles = findViewById(R.id.listViewDetalles);
        btnVolverMenu = findViewById(R.id.btnVolverMenu);

        btnVolverMenu.setOnClickListener(v -> {
            startActivity(new Intent(this, UserMenuActivity.class));
            finish();
        });

        String cedula = getIntent().getStringExtra("cedula");
        String nombre = getIntent().getStringExtra("nombre");
        
        // Intentar obtener VentaResponse desde JSON (método más confiable)
        VentaResponse venta = null;
        String ventaJson = getIntent().getStringExtra("ventaJson");
        if (ventaJson != null && !ventaJson.isEmpty()) {
            try {
                Gson gson = new Gson();
                venta = gson.fromJson(ventaJson, VentaResponse.class);
            } catch (Exception e) {
                android.util.Log.e("VentaConfirmacion", "Error al deserializar venta desde JSON", e);
            }
        }
        
        // Si no se pudo obtener desde JSON, intentar desde Serializable (fallback)
        if (venta == null) {
            try {
                venta = (VentaResponse) getIntent().getSerializableExtra("venta");
            } catch (Exception e) {
                android.util.Log.e("VentaConfirmacion", "Error al deserializar venta desde Serializable", e);
            }
        }

        if (venta == null) {
            // Si no se recibió la venta, mostrar error y volver
            android.widget.Toast.makeText(this, "Error: No se recibieron los datos de la venta", 
                android.widget.Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (venta.isVentaExitosa()) {
            if (venta.getIdFactura() != null) {
                textFactura.setText("Factura #" + venta.getIdFactura());
            } else {
                textFactura.setText("Venta procesada");
            }
            textCliente.setText("Cliente: " + (nombre != null ? nombre : "") + " (" + (cedula != null ? cedula : "") + ")");
            textFormaPago.setText("Forma de pago: " + (venta.getFormaPago() != null ? venta.getFormaPago() : ""));
            textEstado.setText("Estado: " + (venta.getEstadoFactura() != null ? venta.getEstadoFactura() : ""));
            
            if (venta.getSubtotal() != null) {
                textSubtotal.setText("Subtotal: $" + String.format("%.2f", venta.getSubtotal()));
            } else {
                textSubtotal.setText("Subtotal: $0.00");
            }
            
            if (venta.getDescuento() != null) {
                textDescuento.setText("Descuento: $" + String.format("%.2f", venta.getDescuento()));
            } else {
                textDescuento.setText("Descuento: $0.00");
            }
            
            if (venta.getTotal() != null) {
                textTotal.setText("TOTAL: $" + String.format("%.2f", venta.getTotal()));
            } else {
                textTotal.setText("TOTAL: $0.00");
            }

            List<DetalleVentaResponse> detalles = venta.getDetalles();
            if (detalles != null && !detalles.isEmpty()) {
                List<String> items = new ArrayList<>();
                for (DetalleVentaResponse d : detalles) {
                    if (d != null) {
                        items.add(String.format("Producto: %s\nCantidad: %d | Precio unitario: $%.2f | Subtotal: $%.2f",
                            d.getNombreElectrodomestico() != null ? d.getNombreElectrodomestico() : "",
                            d.getCantidad() != null ? d.getCantidad() : 0,
                            d.getPrecioUnitario() != null ? d.getPrecioUnitario() : java.math.BigDecimal.ZERO,
                            d.getSubtotalLinea() != null ? d.getSubtotalLinea() : java.math.BigDecimal.ZERO));
                    }
                }
                if (!items.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            R.layout.item_detalle_venta, R.id.textDetalle, items);
                    listViewDetalles.setAdapter(adapter);
                } else {
                    List<String> emptyItems = new ArrayList<>();
                    emptyItems.add("No hay detalles disponibles");
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            R.layout.item_detalle_venta, R.id.textDetalle, emptyItems);
                    listViewDetalles.setAdapter(adapter);
                }
            } else {
                List<String> emptyItems = new ArrayList<>();
                emptyItems.add("No hay detalles disponibles");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        R.layout.item_detalle_venta, R.id.textDetalle, emptyItems);
                listViewDetalles.setAdapter(adapter);
            }
        } else {
            // Si la venta no fue exitosa, mostrar mensaje y volver
            String mensaje = venta != null && venta.getMensaje() != null ? venta.getMensaje() : "Error desconocido";
            android.widget.Toast.makeText(this, 
                "La venta no fue exitosa: " + mensaje,
                android.widget.Toast.LENGTH_LONG).show();
            finish();
        }
    }
}

