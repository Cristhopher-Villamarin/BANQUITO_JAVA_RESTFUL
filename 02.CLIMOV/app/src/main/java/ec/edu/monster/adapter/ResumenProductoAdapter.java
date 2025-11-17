package ec.edu.monster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.math.BigDecimal;
import java.util.List;
import ec.edu.monster.R;
import ec.edu.monster.VentaActivity;

public class ResumenProductoAdapter extends RecyclerView.Adapter<ResumenProductoAdapter.ViewHolder> {

    private List<VentaActivity.ProductoSeleccionado> productos;
    private OnItemLongClickListener longClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public ResumenProductoAdapter(List<VentaActivity.ProductoSeleccionado> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resumen_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VentaActivity.ProductoSeleccionado p = productos.get(position);
        holder.textNombre.setText(p.getNombre());
        holder.textCantidad.setText("x" + p.getCantidad());
        holder.textPrecioUnitario.setText(String.format("$%.2f", p.getPrecio()));
        BigDecimal subtotal = p.getPrecio().multiply(BigDecimal.valueOf(p.getCantidad()));
        holder.textSubtotal.setText(String.format("$%.2f", subtotal));

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(position);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textCantidad, textPrecioUnitario, textSubtotal;
        ViewHolder(View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.textNombreProducto);
            textCantidad = itemView.findViewById(R.id.textCantidad);
            textPrecioUnitario = itemView.findViewById(R.id.textPrecioUnitario);
            textSubtotal = itemView.findViewById(R.id.textSubtotal);
        }
    }
}