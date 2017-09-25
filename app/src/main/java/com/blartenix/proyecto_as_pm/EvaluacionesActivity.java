package com.blartenix.proyecto_as_pm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EvaluacionesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView lvEvaluaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluaciones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogCrearEvaluacion();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lvEvaluaciones = (ListView)findViewById(R.id.lvEvaluaciones);
        lvEvaluaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarDialogDeCalificaciones(position);
            }
        });

        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.navheadertvNombreUsuarioEv);
        nav_user.setText(Usuario.usuarioLogeado.getNombre());

        TextView nav_email = (TextView)hView.findViewById(R.id.navheadertvEmailUsuarioEv);
        nav_email.setText(Usuario.usuarioLogeado.getEmail());

        mostrarEvaluaciones();
    }

    public void mostrarEvaluaciones(){

        Evaluacion.Adapter adapter = new Evaluacion.Adapter(this, Usuario.usuarioLogeado.getEvaluaciones());
        lvEvaluaciones.setAdapter(adapter);
    }

    private void mostrarDialogDeCalificaciones(final int indiceEvaluacion){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater linf = LayoutInflater.from(this);
        final View layout = linf.inflate(R.layout.custom_dialoglist_layout , null);
        dialog.setView(layout);

        final ListView lvCalificaciones = (ListView) layout.findViewById(R.id.dialogList_Listview);

        Evaluacion evaluacion = Usuario.usuarioLogeado.getEvaluaciones().get(indiceEvaluacion);

        dialog.setTitle("Calificaciones de "+evaluacion.getNombre());

        dialog.setMessage(" ");

        mostrarCalificaciones(evaluacion, lvCalificaciones);

        dialog.setPositiveButton("Regresar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton)
            {
                dialog.dismiss();
            }
        });

        /*dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();

            }
        });*/

        dialog.show();

    }

    private void mostrarCalificaciones(Evaluacion evalaluacion, ListView dialogListView){

        Evaluacion.Calificacion.Adapter adapter = new Evaluacion.Calificacion.Adapter(this, evalaluacion.getCalificaciones());
        dialogListView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            startActivity(new Intent(EvaluacionesActivity.this, AsignaturasActivity.class));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                ArrayList<String> nombresEvaluaciones = new ArrayList<>();
                for (int i = 0; i< Usuario.usuarioLogeado.getEvaluaciones().size(); i++){
                    nombresEvaluaciones.add(Usuario.usuarioLogeado.getEvaluaciones().get(i).getNombre());
                }
                mostrarMisAsignaturas();
                /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(EvaluacionesActivity.this, android.R.layout.simple_expandable_list_item_1, nombresEvaluaciones);
                lvEvaluaciones.setAdapter(adapter);*/
            }
        }
    }

    void mostrarMisAsignaturas() {

        Evaluacion.Adapter adapter = new Evaluacion.Adapter(this, Usuario.usuarioLogeado.getEvaluaciones());
        lvEvaluaciones.setAdapter(adapter);

    }

    void mostrarDialogCrearEvaluacion(){

        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);

        LayoutInflater linf = LayoutInflater.from(this);
        final View layout = linf.inflate(R.layout.agregar_evaluacion_dialog_layout, null);
        dialog.setView(layout);

        dialog.setTitle("Nueva Evaluación");

        final EditText etCodigoAs = (EditText)layout.findViewById(R.id.etCodigoEvaluacion);
        final EditText etNombre = (EditText) layout.findViewById(R.id.etNombreEvaluacion);
        final Spinner sAsignatura = (Spinner) layout.findViewById(R.id.sAsignatura);
        final Spinner sRubrica = (Spinner)layout.findViewById(R.id.sRubrica);

        final ArrayList<String> asignaturas = new ArrayList<>();
        for (int i = 0; i < Usuario.usuarioLogeado.getAsignaturas().size(); i++){
            asignaturas.add(Usuario.usuarioLogeado.getAsignaturas().get(i).getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, asignaturas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sAsignatura.setAdapter(adapter);


        ArrayList<String> rubricas = new ArrayList<>();
        for (int i = 0; i < Usuario.usuarioLogeado.getRubricas().size(); i++){
            rubricas.add(Usuario.usuarioLogeado.getRubricas().get(i).getNombreRubrica());
        }


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, rubricas);
        sRubrica.setAdapter(adapter);

        dialog.setPositiveButton("Evaluar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton)
            {
                if(!TextUtils.isEmpty(etCodigoAs.getText()) && !TextUtils.isEmpty(etNombre.getText().toString()) &&  sAsignatura.getSelectedItem() != null && sRubrica.getSelectedItem() != null){

                    dialog.dismiss();
                    String codigo = etCodigoAs.getText().toString();
                    String nombre = etNombre.getText().toString();
                    int indiceAsignatura = sAsignatura.getSelectedItemPosition();
                    int indiceRubrica = sRubrica.getSelectedItemPosition();
                    Intent intent = new Intent(EvaluacionesActivity.this, EvaluarActivity.class);
                    intent.putExtra("Codigo Evaluacion", codigo);
                    intent.putExtra("Nombre Evaluacion", nombre);
                    intent.putExtra("Indice Asignatura", String.valueOf(indiceAsignatura));
                    intent.putExtra("Indice Rubrica", String.valueOf(indiceRubrica));
                    startActivityForResult(intent, 1);

                }
                else{

                    Toast.makeText(EvaluacionesActivity.this, "Uno o mas campos están vacios", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }
}
