package com.blartenix.proyecto_as_pm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class EstudiantesActivity extends AppCompatActivity {

    TextView tvNombreAsignatura;
    TextView tvCodigoAsignatura;
    ListView lvEstudiantes;

    Asignatura asignaturaMostrada;
    //ArrayList<Estudiante> nuevosEstudiantes = new ArrayList<>();
    int indiceAsignatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiantes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something you want
                onBackPressed();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mostarDialogAgregarEstudiante();
            }
        });

        tvNombreAsignatura = (TextView)findViewById(R.id.tvNombreAsignatura);
        tvCodigoAsignatura = (TextView)findViewById(R.id.tvCodigoAsignatura);
        lvEstudiantes = (ListView)findViewById(R.id.lvEstudiantes);

        indiceAsignatura = Integer.parseInt(getIntent().getStringExtra("Indice Asignatura"));
        asignaturaMostrada = Usuario.usuarioLogeado.getAsignaturas().get(indiceAsignatura);
        tvNombreAsignatura.setText(asignaturaMostrada.getNombre());
        tvCodigoAsignatura.setText(asignaturaMostrada.getCodigo());

        mostrarListaEstudiantes();
    }

    void mostrarListaEstudiantes(){

        Estudiante.Adapter adapter = new Estudiante.Adapter(this, asignaturaMostrada.getEstudiantes());
        lvEstudiantes.setAdapter(adapter);
    }

    void mostarDialogAgregarEstudiante(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater linf = LayoutInflater.from(this);
        final View layout = linf.inflate(R.layout.agregar_dialoglayout_default, null);
        dialog.setView(layout);

        dialog.setTitle(R.string.agregar_estudiante_dialog_titulo);
        dialog.setMessage(R.string.agregar_estudiante_dialog_mensaje);

        final EditText etNombre = (EditText) layout.findViewById(R.id.etNombre);
        final EditText etCodigo = (EditText) layout.findViewById(R.id.etCodigo);

        dialog.setPositiveButton(R.string.agregar, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton)
            {
                dialog.dismiss();
                String nombre = etNombre.getText().toString();
                String codigo = etCodigo.getText().toString();

                Estudiante nuevoEstudiante = new Estudiante(nombre, codigo);
                asignaturaMostrada.getEstudiantes().add(nuevoEstudiante);
                guardarTodo(Usuario.usuarioLogeado);

                Toast.makeText(EstudiantesActivity.this,"Estudiante Creado", Toast.LENGTH_SHORT).show();
                mostrarListaEstudiantes();
            }
        });

        dialog.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    /**
     * Guarda y Sobre escribe
     * @param nombreArchivo
     * @param json
     * @param tipoDato
     */
    private void guardarJSONFile(String nombreArchivo, String json, DataBaseHandler.TipoDato tipoDato) {
        File archivo = new File(getFilesDir()+"/"+tipoDato.toString()+"/", nombreArchivo+".json");
        try {
            FileWriter fw = new FileWriter(archivo);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(json);
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarTodo(Usuario usuario){

        String json = Constants.GsonHelper.toJson(usuario, Usuario.class);
        String nombreArchivo = usuario.getEmail().split("@")[0];
        guardarJSONFile(nombreArchivo, json, DataBaseHandler.TipoDato.Usuarios);
        String credenciales = usuario.getEmail()+":"+usuario.getContrasena();
        for (int indice = 0; indice < usuario.getAsignaturas().size(); indice++){
            guardarAsignatura(usuario.getAsignaturas().get(indice));
        }

        for (int indice = 0; indice < usuario.getRubricas().size(); indice++){
            guardarRubrica(usuario.getRubricas().get(indice));
        }

        for (int indice = 0; indice < usuario.getEvaluaciones().size(); indice++){
            guardarEvaluacion(usuario.getEvaluaciones().get(indice));
        }
    }

    private void guardarAsignatura(Asignatura asignatura){

        String json = Constants.GsonHelper.toJson(asignatura, Asignatura.class);
        String nombreArchivo = asignatura.getCodigo()+"_"+asignatura.getNombre();
        guardarJSONFile(nombreArchivo, json, DataBaseHandler.TipoDato.Asignaturas);
        for (int indice = 0; indice < asignatura.getEstudiantes().size(); indice++){
            guardarEstudiante(asignatura.getEstudiantes().get(indice));
        }
    }

    private void guardarEstudiante(Estudiante estudiante){

        String json = Constants.GsonHelper.toJson(estudiante, Estudiante.class);
        String nombreArchivo = estudiante.getCodigo()+"_"+estudiante.getNombre();
        guardarJSONFile(nombreArchivo, json, DataBaseHandler.TipoDato.Estudiantes);
    }

    private void guardarRubrica(Rubrica rubrica){

        String json = Constants.GsonHelper.toJson(rubrica, Rubrica.class);
        String nombreArchivo = rubrica.getCodigo()+"_"+rubrica.getNombreRubrica();
        guardarJSONFile(nombreArchivo, json, DataBaseHandler.TipoDato.Rubricas);
    }

    private void guardarEvaluacion(Evaluacion evaluacion){

        String json = Constants.GsonHelper.toJson(evaluacion, Evaluacion.class);
        String nombreArchivo = evaluacion.getCodigo()+"_"+evaluacion.getNombre();
        guardarJSONFile(nombreArchivo, json, DataBaseHandler.TipoDato.Evaluaciones);
    }

}
