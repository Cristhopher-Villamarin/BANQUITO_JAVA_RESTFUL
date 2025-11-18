package ec.edu.monster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ec.edu.monster.R;
import ec.edu.monster.model.Electrodomestico;

public class ElectrodomesticoAdapter extends RecyclerView.Adapter<ElectrodomesticoAdapter.ElectrodomesticoViewHolder> {

    private final Context context;
    private final List<Electrodomestico> electrodomesticos;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Electrodomestico electrodomestico);
        void onOptionsClick(Electrodomestico electrodomestico);
    }

    public ElectrodomesticoAdapter(Context context, List<Electrodomestico> electrodomesticos, OnItemClickListener listener) {
        this.context = context;
        this.electrodomesticos = electrodomesticos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ElectrodomesticoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_electrodomestico, parent, false);
        return new ElectrodomesticoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ElectrodomesticoViewHolder holder, int position) {
        Electrodomestico item = electrodomesticos.get(position);

        holder.tvId.setText(String.valueOf(item.getIdElectrodomestico()));
        holder.tvNombre.setText(item.getNombre());
        holder.tvPrecio.setText(String.format("$%.2f", item.getPrecioVenta()));
        
        if (item.getFotoUrl() != null && !item.getFotoUrl().isEmpty()) {
            holder.imgFoto.setVisibility(View.VISIBLE);
            new LoadImageTask(holder.imgFoto).execute(item.getFotoUrl());
        } else {
            holder.imgFoto.setImageBitmap(null);
            holder.imgFoto.setVisibility(View.GONE);
        }

        // Configurar clics
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.btnOpciones.setOnClickListener(v -> listener.onOptionsClick(item));
    }

    @Override
    public int getItemCount() {
        return electrodomesticos.size();
    }

    public void updateData(List<Electrodomestico> newList) {
        electrodomesticos.clear();
        electrodomesticos.addAll(newList);
        notifyDataSetChanged();
    }

    class ElectrodomesticoViewHolder extends RecyclerView.ViewHolder {
        final TextView tvId;
        final TextView tvNombre;
        final TextView tvPrecio;
        final ImageButton btnOpciones;
        final ImageView imgFoto;

        ElectrodomesticoViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnOpciones = itemView.findViewById(R.id.btnOpciones);
            imgFoto = itemView.findViewById(R.id.imgFoto);
        }
    }

    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;

        LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            try {
                java.io.InputStream in = new java.net.URL(url).openStream();
                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
