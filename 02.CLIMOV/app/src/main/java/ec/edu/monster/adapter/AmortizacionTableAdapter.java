// AmortizacionTableAdapter.java
package ec.edu.monster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ec.edu.monster.R;
import ec.edu.monster.model.CuotaAmortizacion;

public class AmortizacionTableAdapter extends RecyclerView.Adapter<AmortizacionTableAdapter.ViewHolder> {

    private Context context;
    private List<CuotaAmortizacion> cuotas;

    public AmortizacionTableAdapter(Context context, List<CuotaAmortizacion> cuotas) {
        this.context = context;
        this.cuotas = cuotas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_amortizacion_table, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CuotaAmortizacion cuota = cuotas.get(position);

        holder.textCuota.setText(String.valueOf(cuota.getNumeroCuota()));
        holder.textCapital.setText(String.format("$%.2f", cuota.getCapital()));
        holder.textInteres.setText(String.format("$%.2f", cuota.getInteres()));
        holder.textValorCuota.setText(String.format("$%.2f", cuota.getCuota()));
        holder.textSaldo.setText(String.format("$%.2f", cuota.getSaldoFinal()));
    }

    @Override
    public int getItemCount() {
        return cuotas != null ? cuotas.size() : 0;
    }

    // ViewHolder interno
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCuota, textCapital, textInteres, textValorCuota, textSaldo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCuota = itemView.findViewById(R.id.textCuota);
            textCapital = itemView.findViewById(R.id.textCapital);
            textInteres = itemView.findViewById(R.id.textInteres);
            textValorCuota = itemView.findViewById(R.id.textValorCuota);
            textSaldo = itemView.findViewById(R.id.textSaldo);
        }
    }
}