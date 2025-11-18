package ec.edu.monster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ec.edu.monster.R;
import ec.edu.monster.model.Factura;

public class FacturaAdapter extends RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder> {

    public interface OnVerDetalleClickListener {
        void onVerDetalle(Factura factura);
    }

    private final List<Factura> facturas;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final OnVerDetalleClickListener listener;

    public FacturaAdapter(List<Factura> facturas, OnVerDetalleClickListener listener) {
        this.facturas = facturas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FacturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_factura, parent, false);
        return new FacturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacturaViewHolder holder, int position) {
        Factura factura = facturas.get(position);

        String fechaStr = factura.getFecha() != null
                ? dateFormat.format(factura.getFecha())
                : "";

        holder.textFecha.setText("Fecha: " + fechaStr);
        holder.textFormaPago.setText("Forma de pago: " + (factura.getFormaPago() != null ? factura.getFormaPago() : ""));

        holder.textSubtotal.setText("Subtotal: $" + (factura.getSubtotal() != null ? factura.getSubtotal().toPlainString() : "0.00"));

        if (factura.getDescuento() != null && factura.getDescuento().compareTo(java.math.BigDecimal.ZERO) > 0) {
            holder.textDescuento.setText("Descuento: $" + factura.getDescuento().toPlainString());
        } else {
            holder.textDescuento.setText("Descuento: $0.00");
        }

        holder.textTotal.setText("Total: $" + (factura.getTotal() != null ? factura.getTotal().toPlainString() : "0.00"));
        holder.textEstado.setText("Estado: " + (factura.getEstado() != null ? factura.getEstado() : ""));

        if (listener != null) {
            holder.btnVerDetalle.setOnClickListener(v -> listener.onVerDetalle(factura));
        }
    }

    @Override
    public int getItemCount() {
        return facturas != null ? facturas.size() : 0;
    }

    static class FacturaViewHolder extends RecyclerView.ViewHolder {
        TextView textFecha;
        TextView textFormaPago;
        TextView textSubtotal;
        TextView textDescuento;
        TextView textTotal;
        TextView textEstado;
        Button btnVerDetalle;

        FacturaViewHolder(@NonNull View itemView) {
            super(itemView);
            textFecha = itemView.findViewById(R.id.textFecha);
            textFormaPago = itemView.findViewById(R.id.textFormaPago);
            textSubtotal = itemView.findViewById(R.id.textSubtotal);
            textDescuento = itemView.findViewById(R.id.textDescuento);
            textTotal = itemView.findViewById(R.id.textTotal);
            textEstado = itemView.findViewById(R.id.textEstado);
            btnVerDetalle = itemView.findViewById(R.id.btnVerDetalle);
        }
    }
}
