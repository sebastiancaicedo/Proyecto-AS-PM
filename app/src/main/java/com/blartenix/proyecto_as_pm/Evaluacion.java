package com.blartenix.proyecto_as_pm;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by lusec on 22/09/2017.
 */

public class Evaluacion {

    private final int imagen = 2130837585;

    @Expose private String codigo;
    @Expose private String nombre;
    @Expose private Asignatura asignatura;
    @Expose private Rubrica rubrica;
    @Expose private ArrayList<Calificacion> calificaciones = new ArrayList<>();

    /**
     * Constructor para creacion
     * @param codigo
     * @param nombre
     * @param asignatura
     * @param rubrica
     * @param context
     * @param rootEstudiantesLayout
     */
    public Evaluacion(String codigo, String nombre, Asignatura asignatura, Rubrica rubrica, Context context, LinearLayout rootEstudiantesLayout) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.asignatura = asignatura;
        this.rubrica = rubrica;

        listarEstudiantes(context, rootEstudiantesLayout);
    }

    /**
     * Constructor para el deserializado
     * @param codigo
     * @param nombre
     * @param asignatura
     * @param rubrica
     * @param calificaciones
     */
    public Evaluacion(String codigo, String nombre, Asignatura asignatura, Rubrica rubrica, ArrayList<Calificacion> calificaciones) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.asignatura = asignatura;
        this.rubrica = rubrica;
        this.calificaciones = calificaciones;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public Rubrica getRubrica() {
        return rubrica;
    }

    public ArrayList<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    public int getImagen() {
        return imagen;
    }

    private void listarEstudiantes(Context context, LinearLayout rootEstudiantesLayout){

        for (int indice = 0; indice < asignatura.getEstudiantes().size(); indice++){

            LinearLayout.LayoutParams paramsRootLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            paramsRootLayout.setMargins(0, Constants.convertDpToPixels(15, context), 0, 0);
            LinearLayout rootLayout = new LinearLayout(context);
            rootLayout.setLayoutParams(paramsRootLayout);
            rootLayout.setOrientation(LinearLayout.VERTICAL);

            rootEstudiantesLayout.addView(rootLayout);

            calificaciones.add(new Calificacion(asignatura.getEstudiantes().get(indice),indice+1, rubrica, context, rootLayout));
        }
    }

    public boolean estaIncompleta(){

        for (int i = 0; i < calificaciones.size(); i++){

            if(calificaciones.get(i).estaIncompleta()){
                return true;
            }
        }
        return false;
    }

    public static class Adapter extends BaseAdapter {

        protected Activity activity;
        protected ArrayList<Evaluacion> items;

        public Adapter (Activity activity, ArrayList<Evaluacion> items) {
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

        public void addAll(ArrayList<Evaluacion> category) {
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

            Evaluacion evaluacion = items.get(position);

            TextView title = (TextView) v.findViewById(R.id.tvNombre);
            title.setText(evaluacion.getNombre());

            TextView description = (TextView) v.findViewById(R.id.tvCodigo);
            description.setText(evaluacion.getCodigo());

            ImageView imagen = (ImageView) v.findViewById(R.id.item_imageView);
            imagen.setImageResource(evaluacion.getImagen());

            return v;
        }

    }


    public static class Calificacion {

        //Solo para la creacion;
        private TextView tvCalificacionParcial;
        private ArrayList<RadioGroup> rgElementos = new ArrayList<>();//Con esto verifico que todo este calificado


        @Expose private final Rubrica rubricaNota;
        @Expose private final int indiceEstudiante;
        @Expose private Estudiante estudiante;
        @Expose private float calificacionDef;
        @Expose private Nota[] matrizNotas;

        /**
         * Constructor para creacion
         * @param estudiante
         * @param indiceEstudiante
         * @param rubrica
         * @param context
         * @param rootLayout
         */
        public Calificacion(Estudiante estudiante, int indiceEstudiante, Rubrica rubrica, Context context, LinearLayout rootLayout) {

            this.rubricaNota = rubrica;
            this.indiceEstudiante = indiceEstudiante;
            this.estudiante = estudiante;
            matrizNotas = new Nota[rubrica.getCategorias().size()];

            TextView tvNombreEstud = new TextView(context);
            tvNombreEstud.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tvNombreEstud.setText(estudiante.getNombre());
            if (Build.VERSION.SDK_INT < 23) {
                tvNombreEstud.setTextAppearance(context, R.style.TextAppearance_AppCompat_Headline);
            } else {
                tvNombreEstud.setTextAppearance(R.style.TextAppearance_AppCompat_Headline);
            }
            rootLayout.addView(tvNombreEstud);

            LinearLayout.LayoutParams paramsLoHCalif = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            paramsLoHCalif.setMargins(0, Constants.convertDpToPixels(10, context), 0, 0);
            LinearLayout loHCalificacion = new LinearLayout(context);
            loHCalificacion.setLayoutParams(paramsLoHCalif);
            loHCalificacion.setOrientation(LinearLayout.HORIZONTAL);
            rootLayout.addView(loHCalificacion);

            LinearLayout.LayoutParams paramsTvCalfHead = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsTvCalfHead.weight = 4;
            TextView tvCalifHeader = new TextView(context);
            tvCalifHeader.setLayoutParams(paramsTvCalfHead);
            tvCalifHeader.setText("Calificación Parcial");
            if (Build.VERSION.SDK_INT < 23) {
                tvCalifHeader.setTextAppearance(context, R.style.TextAppearance_AppCompat_Medium);
            } else {
                tvCalifHeader.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
            }
            loHCalificacion.addView(tvCalifHeader);

            LinearLayout.LayoutParams paramsTvCalif = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsTvCalif.weight = 1;
            tvCalificacionParcial = new TextView(context);
            tvCalificacionParcial.setLayoutParams(paramsTvCalif);
            tvCalificacionParcial.setText("0.0");
            if (Build.VERSION.SDK_INT < 23) {
                tvCalificacionParcial.setTextAppearance(context, R.style.TextAppearance_AppCompat_Title);
            } else {
                tvCalificacionParcial.setTextAppearance(R.style.TextAppearance_AppCompat_Title);
            }
            loHCalificacion.addView(tvCalificacionParcial);

            LinearLayout loVCategorias = new LinearLayout(context);
            loVCategorias.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            loVCategorias.setOrientation(LinearLayout.VERTICAL);
            rootLayout.addView(loVCategorias);


            mostrarCategorias(rubrica, context, loVCategorias);
        }

        public Calificacion(Rubrica rubricaNota, int indiceEstudiante, Estudiante estudiante, float calificacionDef, Nota[] matrizNotas) {
            this.rubricaNota = rubricaNota;
            this.indiceEstudiante = indiceEstudiante;
            this.estudiante = estudiante;
            this.calificacionDef = calificacionDef;
            this.matrizNotas = matrizNotas;
        }

        private void mostrarCategorias(Rubrica rubrica, Context context, LinearLayout rootCategoriasLayout) {

            for (int indiceCategoria = 0; indiceCategoria < rubrica.getCategorias().size(); indiceCategoria++) {

                Rubrica.CategoriaRubrica categoria = rubrica.getCategorias().get(indiceCategoria);

                LinearLayout rootLayout = new LinearLayout(context);
                rootLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                rootLayout.setOrientation(LinearLayout.VERTICAL);
                rootCategoriasLayout.addView(rootLayout);

                LinearLayout.LayoutParams paramsLoHCabeza = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                paramsLoHCabeza.setMargins(0, Constants.convertDpToPixels(10, context), 0, 0);
                LinearLayout loHCabeceraCat = new LinearLayout(context);
                loHCabeceraCat.setLayoutParams(paramsLoHCabeza);
                loHCabeceraCat.setOrientation(LinearLayout.HORIZONTAL);
                rootLayout.addView(loHCabeceraCat);

                LinearLayout.LayoutParams paramsTvNombreCat = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                paramsTvNombreCat.weight = 3;
                TextView tvNombreCat = new TextView(context);
                tvNombreCat.setLayoutParams(paramsTvNombreCat);
                tvNombreCat.setText(categoria.getNombreCategoria());
                if (Build.VERSION.SDK_INT < 23) {
                    tvNombreCat.setTextAppearance(context, R.style.TextAppearance_AppCompat_Large);
                } else {
                    tvNombreCat.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
                }
                loHCabeceraCat.addView(tvNombreCat);

                LinearLayout.LayoutParams paramsTvPesoCat = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                paramsTvPesoCat.weight = 1;
                TextView tvPesoCat = new TextView(context);
                tvPesoCat.setLayoutParams(paramsTvPesoCat);
                tvPesoCat.setText(categoria.getPesoCategoria());
                tvPesoCat.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                if (Build.VERSION.SDK_INT < 23) {
                    tvPesoCat.setTextAppearance(context, R.style.TextAppearance_AppCompat_Medium);
                } else {
                    tvPesoCat.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
                }
                loHCabeceraCat.addView(tvPesoCat);

                LinearLayout.LayoutParams paramsLoElement = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                paramsLoElement.setMargins(Constants.convertDpToPixels(40, context), Constants.convertDpToPixels(10, context), 0, 0);
                LinearLayout loElementos = new LinearLayout(context);
                loElementos.setLayoutParams(paramsLoElement);
                loElementos.setOrientation(LinearLayout.VERTICAL);

                rootLayout.addView(loElementos);

                mostrarElementos(categoria, indiceCategoria, context, loElementos);
            }
        }

        private void mostrarElementos(final Rubrica.CategoriaRubrica categoria, final int indiceCategoria, final Context context, LinearLayout rootsElementsLayouts) {

            for (int indiceElemento = 0; indiceElemento < categoria.getElementos().size(); indiceElemento++) {

                Rubrica.ElementoCategoria elemento = categoria.getElementos().get(indiceElemento);
                matrizNotas[indiceCategoria] = new Nota(categoria.getElementos().size());

                LinearLayout.LayoutParams paramsRootLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                paramsRootLayout.setMargins(0, Constants.convertDpToPixels(10, context), 0, 0);
                LinearLayout rootLayout = new LinearLayout(context);
                rootLayout.setLayoutParams(paramsRootLayout);
                rootLayout.setOrientation(LinearLayout.VERTICAL);
                rootsElementsLayouts.addView(rootLayout);

                LinearLayout loHCabezaElem = new LinearLayout(context);
                loHCabezaElem.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                loHCabezaElem.setOrientation(LinearLayout.HORIZONTAL);
                rootLayout.addView(loHCabezaElem);

                LinearLayout.LayoutParams paramsTvNombreElem = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                paramsTvNombreElem.weight = 2.6f;
                TextView tvNombreElemento = new TextView(context);
                tvNombreElemento.setLayoutParams(paramsTvNombreElem);
                tvNombreElemento.setText(elemento.getNombreElemento());
                if (Build.VERSION.SDK_INT < 23) {
                    tvNombreElemento.setTextAppearance(context, R.style.TextAppearance_AppCompat_Title);
                } else {
                    tvNombreElemento.setTextAppearance(R.style.TextAppearance_AppCompat_Title);
                }
                loHCabezaElem.addView(tvNombreElemento);

                LinearLayout.LayoutParams paramsTvPesoElem = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                paramsTvPesoElem.weight = 1;
                TextView tvPesoElemento = new TextView(context);
                tvPesoElemento.setLayoutParams(paramsTvPesoElem);
                tvPesoElemento.setText(elemento.getPesoElemento());
                if (Build.VERSION.SDK_INT < 23) {
                    tvPesoElemento.setTextAppearance(context, R.style.TextAppearance_AppCompat_Medium);
                } else {
                    tvPesoElemento.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
                }
                loHCabezaElem.addView(tvPesoElemento);

                rgElementos.add(new RadioGroup(context));
                rgElementos.get(rgElementos.size()-1).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                rootLayout.addView(rgElementos.get(rgElementos.size()-1));

                final RadioButton[] rbNiveles = new RadioButton[4];
                for (int indiceNivel = 0; indiceNivel < elemento.getNiveles().length; indiceNivel++) {

                    rbNiveles[indiceNivel] = new RadioButton(context);
                    rbNiveles[indiceNivel].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    rbNiveles[indiceNivel].setText(elemento.getNiveles()[indiceNivel]);
                    rbNiveles[indiceNivel].setTag(Constants.TAGS_NIVELES[indiceNivel]);
                    rgElementos.get(rgElementos.size()-1).addView(rbNiveles[indiceNivel]);
                }

                final int finalIndiceElemento = indiceElemento;
                rgElementos.get(rgElementos.size() - 1).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        View rbSelected = group.findViewById(checkedId);
                        String nivelSeleccionado = rbSelected.getTag().toString();//Tag = L1 || L2 || Li...
                        matrizNotas[indiceCategoria].setNota(finalIndiceElemento, nivelSeleccionado);
                        calcularNotaParcial();
                        Log.d("Mensajes", "Estudiante: " + indiceEstudiante + " Categoria: " + indiceCategoria + 1 + " Elemento " + (finalIndiceElemento + 1) + " Nota: " + nivelSeleccionado);
                    }
                });
            }
        }

        public void calcularNotaParcial(){

            float notaParcial = 0;
            for (int indiceCat = 0;indiceCat < this.matrizNotas.length; indiceCat++){

                float sumatoriaCategoria = 0;
                float sumatoriaElementos = 0;
                Nota notasCat = matrizNotas[indiceCat];
                for (int indiceElem = 0; indiceElem < notasCat.getNotas().length; indiceElem++){

                    String[] notasElementos = notasCat.getNotas();
                    //Si ya esta calificado ese elemento
                    if(!TextUtils.isEmpty(notasElementos[indiceElem])){
                        String nivelCalificado = notasElementos[indiceElem];
                        float pesoElemento = Float.parseFloat(rubricaNota.getCategorias().get(indiceCat).getElementos().get(indiceElem).getPesoElemento());
                        float valorNivel = Constants.EQUIVALENCIA_NIVELES.get(nivelCalificado);
                        sumatoriaElementos += valorNivel * (pesoElemento/100);
                    }
                }
                float pesoCategoria = Float.parseFloat(rubricaNota.getCategorias().get(indiceCat).getPesoCategoria());
                sumatoriaCategoria += sumatoriaElementos * (pesoCategoria/100);

                notaParcial += sumatoriaCategoria;
            }
            calificacionDef = notaParcial;
            tvCalificacionParcial.setText(String.valueOf(calificacionDef));
        }

        public boolean estaIncompleta(){

            for (int i = 0; i < rgElementos.size(); i++){

                if(rgElementos.get(i).getCheckedRadioButtonId() == -1){//NO se la selecionado nada
                    return true;
                }
            }
            return false;
        }

        public static class Adapter extends BaseAdapter {

            protected Activity activity;
            protected ArrayList<Calificacion> items;

            public Adapter(Activity activity, ArrayList<Calificacion> items) {
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

            public void addAll(ArrayList<Calificacion> category) {
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
                    v = inf.inflate(R.layout.custom_listview_evaluaciones_layout, null);
                }

                Calificacion calificacion = items.get(position);

                TextView nombreEs = (TextView) v.findViewById(R.id.lvEv_tvNombreEs);
                nombreEs.setText(calificacion.estudiante.getNombre());

                TextView codigoEs = (TextView) v.findViewById(R.id.lvEv_tvCodigEs);
                codigoEs.setText(calificacion.estudiante.getCodigo());

                TextView tvCalif = (TextView) v.findViewById(R.id.lvEv_tvCalif);
                tvCalif.setText(Float.toString(calificacion.calificacionDef));

                return v;
            }
        }

        public static class Nota {

            @Expose private String[] notasElementos;

            /**
             * Constructor para creacion
             * @param size
             */
            public Nota(int size){

                notasElementos = new String[size];

                //Valor default, para poder hacer las verificaciones
                for (int i = 0; i < size; i++){
                    notasElementos[i] = "";
                }
            }

            /**
             * Constructor para el deserializado
             * @param notasElementos
             */
            public Nota(String[] notasElementos) {
                this.notasElementos = notasElementos;
            }

            public String[] getNotas(){ return notasElementos; }

            public void setNota(int indiceElemento, String nivelCalificado){
                notasElementos[indiceElemento] = nivelCalificado;
            }

        }

    }

}
