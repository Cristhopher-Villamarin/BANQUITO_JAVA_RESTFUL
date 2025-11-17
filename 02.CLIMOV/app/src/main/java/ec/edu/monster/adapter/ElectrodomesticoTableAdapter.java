package ec.edu.monster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        if (view == null) {
            view = inflater.inflate(R.layout.item_electrodomestico_table, parent, false);
        }

        Electrodomestico e = electrodomesticos.get(position);

        TextView textId = view.findViewById(R.id.textId);
        TextView textNombre = view.findViewById(R.id.textNombre);
        TextView textPrecio = view.findViewById(R.id.textPrecio);

        textId.setText(String.valueOf(e.getIdElectrodomestico()));
        textNombre.setText(e.getNombre());
        textPrecio.setText(String.format("$%.2f", e.getPrecioVenta()));

        return view;
    }
}

