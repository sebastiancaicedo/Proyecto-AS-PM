package com.blartenix.proyecto_as_pm;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.*;

/**
 * Created by lusec on 22/09/2017.
 */

public class Constants {

    public static Gson GsonHelper = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static String[] TAGS_NIVELES = new String[4];
    static {
        TAGS_NIVELES[0]= "L1";
        TAGS_NIVELES[1]= "L2";
        TAGS_NIVELES[2]= "L3";
        TAGS_NIVELES[3]= "L4";
    }

    public static HashMap<String, Float> EQUIVALENCIA_NIVELES;
    static {
        EQUIVALENCIA_NIVELES = new HashMap<>();
        EQUIVALENCIA_NIVELES.put(TAGS_NIVELES[0], 5.0f);
        EQUIVALENCIA_NIVELES.put(TAGS_NIVELES[1], 3.5f);
        EQUIVALENCIA_NIVELES.put(TAGS_NIVELES[2], 2.0f);
        EQUIVALENCIA_NIVELES.put(TAGS_NIVELES[3], 1.0f);
    }

    public static String singleDivider = "|";

    //para vistas
    public static int convertDpToPixels(float dp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    //Para tamaño de textos
    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static Usuario getUsuarioDeserializado(String serial){
        String c = ";u;";
        String[] campos = serial.split(c);
        String codigo = campos[0];
        String nombre = campos[1];
        String email = campos[2];
        String contrasena = campos[3];

        ArrayList<Asignatura> asignaturas = getAsignturasDeserializadas(campos[4]);//Falta verificar si son nulas;
        ArrayList<Rubrica> rubricas = getRubricasDeserializadas(campos[5]);
        ArrayList<Evaluacion> evaluaciones = getEvaluacionesDeserializadas(campos[6]);

        return new Usuario(codigo, nombre, email, contrasena, asignaturas, rubricas, evaluaciones);
    }

    public static ArrayList<Asignatura> getAsignturasDeserializadas(String serial){

        ArrayList<Asignatura> result = new ArrayList<>();
        if(!TextUtils.equals(serial, "-")) {
            String[] asignaturas = serial.split(singleDivider);
            for (int indice = 0; indice < asignaturas.length; indice++) {

                result.add(getAsignaturaDeserializada(asignaturas[indice]));
            }
        }
        return result;
    }

    public static Asignatura getAsignaturaDeserializada(String serial){
        String c = ";a;";
        String[] campos = serial.split(c);
        String codigo = campos[0];
        String nombre = campos[1];
        ArrayList<Estudiante> estudiantes = getEstudiantesDeserializados(campos[2]);

        return new Asignatura(codigo, nombre, estudiantes);
    }

    public static ArrayList<Estudiante> getEstudiantesDeserializados(String serial){

        ArrayList<Estudiante> result = new ArrayList<>();
        if(TextUtils.equals(serial, "-")){
            String[] estudiantes = serial.split(singleDivider);
            for (int indice = 0; indice < estudiantes.length; indice++){

                result.add(getEstudianteDeserializado(estudiantes[indice]));
            }
        }
        return result;
    }

    public static Estudiante getEstudianteDeserializado(String serial){
        String c = ";es;";
        String[] campos = serial.split(c);
        String codigo = campos[0];
        String nombre = campos[1];
        return new Estudiante(codigo, nombre);
    }

    public static ArrayList<Rubrica> getRubricasDeserializadas(String serial){

        ArrayList<Rubrica> result = new ArrayList<>();
        if(!TextUtils.equals(serial, "-")){
            String[] rubricas = serial.split(singleDivider);
            for (int indice = 0; indice < rubricas.length; indice++){

                result.add(getRubricaDeserializada(rubricas[indice]));
            }
        }
        return result;
    }

    public static Rubrica getRubricaDeserializada(String serial){
        String c = ";r;";
        String[] campos = serial.split(c);
        String codigo = campos[0];
        String nombre = campos[1];

        //Siempre tenra por lo menos 1 cat y un  elem
        ArrayList<Rubrica.CategoriaRubrica> categorias = new ArrayList<>();
        String[] serialesCategs = campos[2].split(singleDivider);
        for (int indiceCat = 0; indiceCat < serialesCategs.length; indiceCat++){
            String cC = ";cr;";
            String[] camposCatg = serialesCategs[indiceCat].split(cC);
            String nombreCatg = camposCatg[0];
            String pesoCatg = camposCatg[1];

            ArrayList<Rubrica.ElementoCategoria> elementos = new ArrayList<>();
            String[] serialesElems = camposCatg[2].split(singleDivider);
            for (int indiceElem = 0; indiceElem < serialesElems.length; indiceElem++){
                String cE = ";ec;";
                String[] camposElem = serialesElems[indiceElem].split(cE);
                String nombreElem = camposElem[0];
                String pesoElem = camposElem[1];
                String[] niveles = camposElem[2].split(";;");

                elementos.add(new Rubrica.ElementoCategoria(indiceElem, nombreElem, pesoElem, niveles));

            }
            categorias.add(new Rubrica.CategoriaRubrica(indiceCat, nombre, pesoCatg, elementos));
        }

        return new Rubrica(codigo, nombre, categorias);
    }

    public static ArrayList<Evaluacion> getEvaluacionesDeserializadas(String serial){
        ArrayList<Evaluacion> result = new ArrayList<>();
        if(!TextUtils.equals(serial, "-")){
            String[] serialesEvals = serial.split(singleDivider);
            for (int indice = 0;indice < serialesEvals.length; indice++){
                String c = ";ev;";
                String[] camposEval = serialesEvals[indice].split(c);
                String codigo = camposEval[0];
                String nombre = camposEval[1];
                Asignatura asignatura = getAsignaturaDeserializada(camposEval[2]);
                Rubrica rubrica = getRubricaDeserializada(camposEval[3]);
                //Siempre tendra calificaciones, sino no existira;

                ArrayList<Evaluacion.Calificacion> calificaciones = new ArrayList<>();
                String[] serialesCalifs = camposEval[4].split(singleDivider);
                for (int indiceCalif= 0; indiceCalif < serialesCalifs.length; indiceCalif++){
                    String cC= ";ce;";
                    String[] camposCalif = serialesCalifs[indiceCalif].split(cC);

                    Estudiante estudiante = getEstudianteDeserializado(camposCalif[0]);
                    float califDef = Float.parseFloat(camposCalif[1]);
                    String cMN = ";cn;";

                    String[] serialesMatrizNotas = camposCalif[2].split(cMN);
                    Evaluacion.Calificacion.Nota[] matrizNotas = new Evaluacion.Calificacion.Nota[serialesMatrizNotas.length];
                    for (int indiceMatNota = 0; indiceMatNota < serialesMatrizNotas.length; indiceMatNota++){
                        String cN = ";;";
                        matrizNotas[indiceMatNota] = new Evaluacion.Calificacion.Nota(serialesMatrizNotas[indiceMatNota].split(cN));
                    }

                    calificaciones.add(new Evaluacion.Calificacion(rubrica,indice, estudiante, califDef, matrizNotas));
                }
                result.add(new Evaluacion(codigo, nombre, asignatura, rubrica, calificaciones));
            }
        }
        return result;
    }

}
