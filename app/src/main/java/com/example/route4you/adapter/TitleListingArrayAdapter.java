package com.example.route4you.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.route4you.R;
import com.example.route4you.model.Ruta;

import java.util.List;

/**
 * Clase ArrayAdapter customizado para mostrar en cada fila la información de una ruta
 * y que se liste con un estilo establecido, fue necesario para hacer el filtrado personalizado
 */
public class TitleListingArrayAdapter extends ArrayAdapter<Ruta> {

    private List<Ruta> items;
    private Context context;

    public TitleListingArrayAdapter(Context context, int textViewResourceId, List<Ruta> items) {
        super(context, textViewResourceId, items);
        this.items = items;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.titlelisting_single_row, null);
        }
        Ruta item = items.get(position);
        if (item!= null) {
            TextView rutaView = (TextView) view.findViewById(R.id.content);
            if (rutaView != null) {
                rutaView.setText(item.toString());
            }
        }
        return view;
    }
}