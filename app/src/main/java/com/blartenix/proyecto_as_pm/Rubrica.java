package com.blartenix.proyecto_as_pm;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

import static java.lang.System.in;

/**
 * Created by lusec on 21/09/2017.
 */

public class Rubrica {

    private final int imagen = 2130837587;

    @Expose private String codigo = "";
    @Expose private String nombreRubrica = "";
    @Expose private ArrayList<CategoriaRubrica> categorias = new ArrayList<>();

    public Rubrica(Context context, LinearLayout rootlayoutCategorias){

        categorias.add(new CategoriaRubrica(context, rootlayoutCategorias, 1));
    }

    public Rubrica(String codigo, String nombreRubrica, ArrayList<CategoriaRubrica> categorias) {
        this.codigo = codigo;
        this.nombreRubrica = nombreRubrica;
        this.categorias = categorias;
    }

    public void agregarCategoria(Context context, LinearLayout rooLayoutCategorias){

        categorias.add(new CategoriaRubrica(context, rooLayoutCategorias, categorias.size()+1));
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombreRubrica(){ return nombreRubrica; }

    public void setNombreRubrica(String nombre) { this.nombreRubrica = nombre; }

    public ArrayList<CategoriaRubrica> getCategorias(){ return categorias; }

    public int getImagen() {
        return imagen;
    }

    public boolean estaVacia(){
        if(TextUtils.isEmpty(nombreRubrica) || tieneCategoriasVacias()){
            return true;
        }
        return false;
    }

    private boolean tieneCategoriasVacias(){
        for (int i = 0; i < categorias.size(); i++){
            if(categorias.get(i).tieneCamposVacios()){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String miString = "\nNombre: "+nombreRubrica;
        String misCategorias = "";
        for (int i = 0; i< categorias.size(); i++){
            misCategorias += categorias.get(i).toString();
        }
        return miString + misCategorias;
    }

    public static class Adapter extends BaseAdapter {

        protected Activity activity;
        protected ArrayList<Rubrica> items;

        public Adapter (Activity activity, ArrayList<Rubrica> items) {
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

        public void addAll(ArrayList<Rubrica> category) {
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

            Rubrica rubrica = items.get(position);

            TextView title = (TextView) v.findViewById(R.id.tvNombre);
            title.setText(rubrica.getNombreRubrica());

            TextView description = (TextView) v.findViewById(R.id.tvCodigo);
            description.setText(rubrica.getCodigo());

            ImageView imagen = (ImageView) v.findViewById(R.id.item_imageView);
            imagen.setImageResource(rubrica.getImagen());

            return v;
        }

    }

    //----------------------------CATEGORIAS--------------------------------------------------------------------

    public static class CategoriaRubrica {

        //solo para la creacion
        private LinearLayout rootLayoutElementos;

        @Expose private final int miIndice;
        @Expose private String nombreCategoria = "";
        @Expose private String pesoCategoria = "";
        @Expose private ArrayList<ElementoCategoria> elementos = new ArrayList<>();

        //
        public CategoriaRubrica(final Context context, LinearLayout rootlayoutCategorias, int miIndice) {

            this.miIndice = miIndice;

            LinearLayout.LayoutParams rootLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            rootLayoutParams.setMargins(Constants.convertDpToPixels(10, context), Constants.convertDpToPixels(20, context), 0, 0);
            LinearLayout rootLayout = new LinearLayout(context);
            rootLayout.setLayoutParams(rootLayoutParams);
            rootLayout.setOrientation(LinearLayout.VERTICAL);

            rootlayoutCategorias.addView(rootLayout);

            LinearLayout.LayoutParams loCabezaeraParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            LinearLayout loCabecera = new LinearLayout(context);
            loCabecera.setLayoutParams(loCabezaeraParams);
            loCabecera.setOrientation(LinearLayout.HORIZONTAL);

            rootLayout.addView(loCabecera);

            //---------------------Dentro del layout Cabecera
            LinearLayout.LayoutParams tvIndiceParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView tvIndiceCat = new TextView(context);
            tvIndiceCat.setLayoutParams(tvIndiceParams);
            tvIndiceParams.weight = 2;
            tvIndiceCat.setText("Categoria "+ miIndice);
            if (Build.VERSION.SDK_INT < 23) {
                tvIndiceCat.setTextAppearance(context, R.style.TextAppearance_AppCompat_Large);
            } else {
                tvIndiceCat.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
            }
            loCabecera.addView(tvIndiceCat);

            LinearLayout.LayoutParams bAgrElemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Button bAgregarElemento = new Button(context);
            bAgregarElemento.setLayoutParams(bAgrElemParams);
            bAgregarElemento.setText("+ Elemento");

            bAgregarElemento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agregarElemento(context);
                }
            });
            loCabecera.addView(bAgregarElemento);
            //------------------------Afuera del layout Cabezera

            //------------------ DENTRO BASIC INFO-----------------
            LinearLayout loBasicInfo = new LinearLayout(context);
            loBasicInfo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            loBasicInfo.setOrientation(LinearLayout.HORIZONTAL);

            rootLayout.addView(loBasicInfo);

            LinearLayout.LayoutParams paramsEtNombreCatg = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            EditText etNombreCatg = new EditText(context);
            paramsEtNombreCatg.weight = 1;
            etNombreCatg.setLayoutParams(paramsEtNombreCatg);
            etNombreCatg.setHint("Nombre Categoria");
            etNombreCatg.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            etNombreCatg.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    nombreCategoria = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            loBasicInfo.addView(etNombreCatg);

            LinearLayout.LayoutParams paramsEtPesoCatg = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            EditText etPesoCatg = new EditText(context);
            paramsEtPesoCatg.weight = 0.4f;
            etPesoCatg.setLayoutParams(paramsEtPesoCatg);
            etPesoCatg.setHint("Peso");
            etPesoCatg.setInputType(InputType.TYPE_CLASS_NUMBER);
            etPesoCatg.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    pesoCategoria = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            loBasicInfo.addView(etPesoCatg);

            LinearLayout.LayoutParams loElemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            loElemParams.setMargins(Constants.convertDpToPixels(40, context),0,0,0);
            rootLayoutElementos = new LinearLayout(context);
            rootLayoutElementos.setLayoutParams(loElemParams);
            rootLayoutElementos.setOrientation(LinearLayout.VERTICAL);

            rootLayout.addView(rootLayoutElementos);

            elementos.add(new ElementoCategoria(context, rootLayoutElementos, 1));
        }

        public CategoriaRubrica(int miIndice, String nombreCategoria, String pesoCategoria, ArrayList<ElementoCategoria> elementos) {
            this.miIndice = miIndice;
            this.nombreCategoria = nombreCategoria;
            this.pesoCategoria = pesoCategoria;
            this.elementos = elementos;
        }

        public String getNombreCategoria(){ return nombreCategoria; }

        public String getPesoCategoria(){ return pesoCategoria; }

        public ArrayList<ElementoCategoria> getElementos(){ return elementos; }

        private void agregarElemento(Context context){

            elementos.add(new ElementoCategoria(context, rootLayoutElementos, elementos.size()+1));
        }

        public boolean tieneCamposVacios(){

            if(TextUtils.isEmpty(nombreCategoria) || TextUtils.isEmpty(pesoCategoria) || tieneElementosVacios()){
                return true;
            }
            return false;
        }

        private boolean tieneElementosVacios(){
            for (int i = 0;  i < elementos.size(); i++){
                if(elementos.get(i).tieneCamposVacios()){
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            String miString = "\n\tCategoria "+miIndice+"\n\tNombre: "+nombreCategoria+"\tPeso: "+pesoCategoria;
            String misElementos = "";
            for (int i=0; i < elementos.size(); i++){
                misElementos+= elementos.get(i).toString();
            }
            return  miString + misElementos;
        }

    }

    //----------------------------FIN CATEGORIAS--------------------------------------------------------------------







    //----------------------------START ELEMENTOS--------------------------------------------------------------------

    public static class ElementoCategoria {

        //Solo para la creacion de elemento
        @Expose private int miIndice;
        @Expose private String nombreElemento = "";
        @Expose private String pesoElemento = "";
        @Expose private String[] niveles = {"", "", "", ""};


        public ElementoCategoria(Context context, LinearLayout rootLayouElementos, int miIndice){

            this.miIndice = miIndice;

            LinearLayout.LayoutParams rootLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            rootLayoutParams.setMargins(0, Constants.convertDpToPixels(20, context), 0, 0);
            rootLayoutParams.weight = 2;
            LinearLayout rootLayout = new LinearLayout(context);
            rootLayout.setLayoutParams(rootLayoutParams);
            rootLayout.setOrientation(LinearLayout.VERTICAL);

            rootLayouElementos.addView(rootLayout);

            LinearLayout.LayoutParams tvIndiElemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView tvIndiceElem = new TextView(context);
            tvIndiceElem.setLayoutParams(tvIndiElemParams);
            if (Build.VERSION.SDK_INT < 23) {
                tvIndiceElem.setTextAppearance(context, R.style.TextAppearance_AppCompat_Large);
            } else {
                tvIndiceElem.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
            }
            tvIndiceElem.setText("Elemento "+miIndice);

            rootLayout.addView(tvIndiceElem);

            LinearLayout loBasicInfo = new LinearLayout(context);
            loBasicInfo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            loBasicInfo.setOrientation(LinearLayout.HORIZONTAL);

            rootLayout.addView(loBasicInfo);

            LinearLayout.LayoutParams paramsEtNombreElem = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            EditText etNombreElem = new EditText(context);
            paramsEtNombreElem.weight = 1;
            etNombreElem.setLayoutParams(paramsEtNombreElem);
            etNombreElem.setHint("Nombre Elemento");
            etNombreElem.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            etNombreElem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    nombreElemento = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            loBasicInfo.addView(etNombreElem);

            LinearLayout.LayoutParams paramsEtPesoElem = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            EditText etPesoElem = new EditText(context);
            paramsEtPesoElem.weight = 0.4f;
            etPesoElem.setLayoutParams(paramsEtPesoElem);
            etPesoElem.setHint("Peso");
            etPesoElem.setInputType(InputType.TYPE_CLASS_NUMBER);
            etPesoElem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    pesoElemento = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            loBasicInfo.addView(etPesoElem);

            EditText[] etsNiveles = new EditText[4];
            for (int i = 0; i< etsNiveles.length; i++){
                etsNiveles[i] = new EditText(context);
                etsNiveles[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                etsNiveles[i].setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                etsNiveles[i].setHint("L"+(i+1));
                rootLayout.addView(etsNiveles[i]);
            }
            etsNiveles[0].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    niveles[0] = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etsNiveles[1].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    niveles[1] = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etsNiveles[2].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    niveles[2] = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etsNiveles[3].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    niveles[3] = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }

        public ElementoCategoria(int miIndice, String nombreElemento, String pesoElemento, String[] niveles) {
            this.miIndice = miIndice;
            this.nombreElemento = nombreElemento;
            this.pesoElemento = pesoElemento;
            this.niveles = niveles;
        }

        public String getNombreElemento() {
            return nombreElemento;
        }

        public String getPesoElemento() {
            return pesoElemento;
        }

        public String[] getNiveles() {
            return niveles;
        }

        public boolean tieneCamposVacios(){

            if(TextUtils.isEmpty(nombreElemento) || TextUtils.isEmpty(pesoElemento) || tieneNivelesVacios()){
                return true;
            }
            return false;
        }

        private boolean tieneNivelesVacios(){

            for (int i = 0; i < niveles.length; i++){
                if(TextUtils.isEmpty(niveles[i])){
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "\n\t\tElemento "+miIndice+"\n\t\tNombre : "+nombreElemento+"\t\tPeso: "+pesoElemento+"\n\t\tL1: "+niveles[0]+
                    "\n\t\tL2: "+niveles[1]+"\n\t\tL3: "+niveles[2]+"\n\t\tL4: "+niveles[3];
        }
    }
    //----------------------------ELEMENTOS--------------------------------------------------------------------
}
