package com.blartenix.proyecto_as_pm;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by Usuario on 11/09/2017.
 */

public class Usuario {

    public static Usuario usuarioLogeado;

    @Expose private String codigo;
    @Expose private String nombre;
    @Expose private String email;
    @Expose private String contrasena;
    @Expose private ArrayList<Asignatura> asignaturas = new ArrayList<>();
    @Expose private ArrayList<Rubrica> rubricas = new ArrayList<>();
    @Expose private ArrayList<Evaluacion> evaluaciones = new ArrayList();

    /**
     * Constructor para el registro / creacion de nuevo usuario
     * @param codigo
     * @param nombre
     * @param email
     * @param contrasena
     */
    public Usuario(String codigo, String nombre, String email, String contrasena) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
    }


    /**
     * Constructor para el deserializado
     * @param codigo
     * @param nombre
     * @param email
     * @param contrasena
     * @param asignaturas
     * @param rubricas
     * @param evaluaciones
     */
    public Usuario(String codigo, String nombre, String email, String contrasena, ArrayList<Asignatura> asignaturas, ArrayList<Rubrica> rubricas, ArrayList<Evaluacion> evaluaciones) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.asignaturas = asignaturas;
        this.rubricas = rubricas;
        this.evaluaciones = evaluaciones;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public ArrayList<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    public ArrayList<Rubrica> getRubricas() { return rubricas; }

    public ArrayList<Evaluacion> getEvaluaciones() {
        return evaluaciones;
    }

    public String getNnombreNID(){
        return email.split("@")[0]+":"+codigo;
    }
}
