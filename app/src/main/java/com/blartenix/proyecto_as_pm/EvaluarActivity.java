package com.blartenix.proyecto_as_pm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EvaluarActivity extends AppCompatActivity {

    LinearLayout loEstudiantes;
    Evaluacion evaluacionARealizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluar);
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

        loEstudiantes = (LinearLayout)findViewById(R.id.loVEstudiantes);

        String codigoEvaluacion = getIntent().getStringExtra("Codigo Evaluacion");
        String nombreEvaluacion = getIntent().getStringExtra("Nombre Evaluacion");
        int indiceAsignatura = Integer.parseInt(getIntent().getStringExtra("Indice Asignatura"));
        int indiceRubrica = Integer.parseInt(getIntent().getStringExtra("Indice Rubrica"));

        evaluacionARealizar = new Evaluacion(codigoEvaluacion, nombreEvaluacion, Usuario.usuarioLogeado.getAsignaturas().get(indiceAsignatura), Usuario.usuarioLogeado.getRubricas().get(indiceRubrica), EvaluarActivity.this, loEstudiantes);

    }

    @Override
    public void onBackPressed() {

        mostrarMsjSalir();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.evaluar, menu);
        return true;
    }

    public void onGuradarEvaluacionClick(MenuItem item) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Guardar");
        dialog.setMessage("Guardar y Salir");

        dialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                if(!evaluacionARealizar.estaIncompleta()){
                    Usuario.usuarioLogeado.getEvaluaciones().add(evaluacionARealizar);
                    guardarTodo(Usuario.usuarioLogeado);
                    setResult(RESULT_OK);
                    finish();
                    Toast.makeText(EvaluarActivity.this, "Evaluación realizada Exitosamente", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(EvaluarActivity.this, "Uno o mas elementos están sin calificar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();

            }
        });

        dialog.show();
        Log.d("Mensajes", evaluacionARealizar.toString());
    }

    private void mostrarMsjSalir() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Salir");
        dialog.setMessage("¿Desea descartar esta evaluación?");

        dialog.setPositiveButton("Salir", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
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
