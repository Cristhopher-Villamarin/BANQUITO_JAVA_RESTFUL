package ec.edu.monster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ec.edu.monster.R;
import ec.edu.monster.model.Electrodomestico;

public class ProductoDisponibleAdapter extends RecyclerView.Adapter<ProductoDisponibleAdapter.ViewHolder> {

    private List<Electrodomestico> productos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Electrodomestico producto);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProductoDisponibleAdapter(List<Electrodomestico> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto_disponible, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Electrodomestico p = productos.get(position);
        holder.textProducto.setText(String.format("%s - $%.2f", p.getNombre(), p.getPrecioVenta()));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(p);
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textProducto;
        ViewHolder(View itemView) {
            super(itemView);
            textProducto = itemView.findViewById(R.id.textProducto);
        }
    }
}