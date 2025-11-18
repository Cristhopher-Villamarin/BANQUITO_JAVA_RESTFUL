package ec.edu.monster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ec.edu.monster.R;
import ec.edu.monster.model.DetalleFactura;

public class DetalleFacturaAdapter extends RecyclerView.Adapter<DetalleFacturaAdapter.DetalleViewHolder> {

    private final List<DetalleFactura> detalles;

    public DetalleFacturaAdapter(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }

    @NonNull
    @Override
    public DetalleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detalle_factura, parent, false);
        return new DetalleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetalleViewHolder holder, int position) {
        DetalleFactura detalle = detalles.get(position);

        holder.textProducto.setText(detalle.getNombreElectrodomestico());
        holder.textCantidadPrecio.setText("Cantidad: " + detalle.getCantidad() +
                " | Precio: $" + (detalle.getPrecioUnitario() != null ? detalle.getPrecioUnitario().toPlainString() : "0.00"));
        holder.textSubtotalLinea.setText("Subtotal línea: $" +
                (detalle.getSubtotalLinea() != null ? detalle.getSubtotalLinea().toPlainString() : "0.00"));
    }

    @Override
    public int getItemCount() {
        return detalles != null ? detalles.size() : 0;
    }

    static class DetalleViewHolder extends RecyclerView.ViewHolder {
        TextView textProducto;
        TextView textCantidadPrecio;
        TextView textSubtotalLinea;

        DetalleViewHolder(@NonNull View itemView) {
            super(itemView);
            textProducto = itemView.findViewById(R.id.textProducto);
            textCantidadPrecio = itemView.findViewById(R.id.textCantidadPrecio);
            textSubtotalLinea = itemView.findViewById(R.id.textSubtotalLinea);
        }
    }
}
