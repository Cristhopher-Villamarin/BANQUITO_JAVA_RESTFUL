package ec.edu.monster.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ec.edu.monster.R;
import ec.edu.monster.model.Electrodomestico;

public class ElectrodomesticoTableAdapter extends BaseAdapter {
    private Context context;
    private List<Electrodomestico> electrodomesticos;
    private LayoutInflater inflater;

    public ElectrodomesticoTableAdapter(Context context, List<Electrodomestico> electrodomesticos) {
        this.context = context;
        this.electrodomesticos = electrodomesticos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return electrodomesticos != null ? electrodomesticos.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return electrodomesticos != null ? electrodomesticos.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_electrodomestico_table, parent, false);
            holder = new ViewHolder();
            holder.textId = view.findViewById(R.id.textId);
            holder.textNombre = view.findViewById(R.id.textNombre);
            holder.textPrecio = view.findViewById(R.id.textPrecio);
            holder.imgFoto = view.findViewById(R.id.imgFotoCatalogo);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Electrodomestico e = electrodomesticos.get(position);

        holder.textId.setText(String.valueOf(e.getIdElectrodomestico()));
        holder.textNombre.setText(e.getNombre());
        holder.textPrecio.setText(String.format("$%.2f", e.getPrecioVenta()));

        String fotoUrl = e.getFotoUrl();
        if (fotoUrl != null && !fotoUrl.isEmpty()) {
            holder.imgFoto.setVisibility(View.VISIBLE);
            new LoadImageTask(holder.imgFoto).execute(fotoUrl);
        } else {
            holder.imgFoto.setImageBitmap(null);
            holder.imgFoto.setVisibility(View.GONE);
        }

        return view;
    }

    private static class ViewHolder {
        TextView textId;
        TextView textNombre;
        TextView textPrecio;
        ImageView imgFoto;
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

