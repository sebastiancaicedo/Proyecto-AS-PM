package com.blartenix.proyecto_as_pm;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Usuario on 11/09/2017.
 */

public class Estudiante implements Serializable{

    private final int imagen = 2130837584;

    @Expose private String codigo;
    @Expose private String nombre;

    public Estudiante(String nombre, String codigo){

        this.nombre = nombre;
        this.codigo = codigo;

        //this.imagen = R.drawable.ic_custom_estudiante;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getImagen() { return imagen; }


    public static class Adapter extends BaseAdapter {

        protected Activity activity;
        protected ArrayList<Estudiante> items;

        public Adapter (Activity activity, ArrayList<Estudiante> items) {
            this.activity = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        public void clear() {
            items.clear();
        }

        public void addAll(ArrayList<Estudiante> category) {
            for (int i = 0; i < category.size(); i++) {
                items.add(category.get(i));
            }
        }

        @Override
        public Object getItem(int arg0) {
            return items.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.custom_listview_layout_default, null);
            }

            Estudiante estudiante = items.get(position);

            TextView title = (TextView) v.findViewById(R.id.tvNombre);
            title.setText(estudiante.getNombre());

            TextView description = (TextView) v.findViewById(R.id.tvCodigo);
            description.setText(estudiante.getCodigo());

            ImageView imagen = (ImageView) v.findViewById(R.id.item_imageView);
            imagen.setImageResource(estudiante.getImagen());

            return v;
        }

    }



}
