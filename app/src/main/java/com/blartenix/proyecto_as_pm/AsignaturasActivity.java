package com.blartenix.proyecto_as_pm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AsignaturasActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //---------------- Variables globales ------------------------------

    LinearLayout layoutContent;
    ListView lvMisAsignaturas;

    //-------------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        File f = new File(getFilesDir(), "Prueba");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignaturas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mostarDialogAgregarAsignatura();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //--------------------Codigo agregado----------------------------

        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.navheadertvNombreUsuarioAs);
        nav_user.setText(Usuario.usuarioLogeado.getNombre());

        TextView nav_email = (TextView)hView.findViewById(R.id.navheadertvEmailUsuarioAs);
        nav_email.setText(Usuario.usuarioLogeado.getEmail());

        layoutContent = (LinearLayout)findViewById(R.id.content_asignaturas);
        lvMisAsignaturas = (ListView)findViewById(R.id.lvMisAsignaturas);

        lvMisAsignaturas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(AsignaturasActivity.this, EstudiantesActivity.class);
                intent.putExtra("Indice Asignatura", String.valueOf(position));
                startActivity(intent);
            }
        });
        mostrarMisAsignaturas();

        //----------------------------------------------------------------
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Usuario.usuarioLogeado = null;
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        Intent intent = null;

        if (id == R.id.nav_asignaturas) {
            intent = new Intent(this, AsignaturasActivity.class);

        } else if (id == R.id.nav_evaluaciones) {
            intent = new Intent(this, EvaluacionesActivity.class);

        } else if (id == R.id.nav_rubricas) {
            intent = new Intent(this, RubricasActivity.class);

        } else if (id == R.id.nav_sign_out) {
            Usuario.usuarioLogeado = null;
        }

        if(intent != null) {
            startActivity(intent);
        }
        finish();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //------------------ Metodos ----------------------------------------------

    void mostrarMisAsignaturas(){

        Asignatura.Adapter adapter = new Asignatura.Adapter(this, Usuario.usuarioLogeado.getAsignaturas());
        lvMisAsignaturas.setAdapter(adapter);
    }

    void mostarDialogAgregarAsignatura(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater linf = LayoutInflater.from(this);
        final View layout = linf.inflate(R.layout.agregar_dialoglayout_default, null);
        dialog.setView(layout);

        dialog.setTitle(R.string.agregar_asignatura_dialog_titulo);
        dialog.setMessage(R.string.agregar_asignatura_dialog_mensaje);

        final EditText etNombre = (EditText) layout.findViewById(R.id.etNombre);
        final EditText etCodigo = (EditText) layout.findViewById(R.id.etCodigo);

        dialog.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton)
            {
                dialog.dismiss();
                String nombre = etNombre.getText().toString();
                String codigo = etCodigo.getText().toString();

                Usuario.usuarioLogeado.getAsignaturas().add(new Asignatura(nombre, codigo));
                guardarTodo(Usuario.usuarioLogeado);
                Toast.makeText(AsignaturasActivity.this,"Asignatura Creada", Toast.LENGTH_SHORT).show();
                mostrarMisAsignaturas();
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
    //-------------------------------------------------------------------------
}
