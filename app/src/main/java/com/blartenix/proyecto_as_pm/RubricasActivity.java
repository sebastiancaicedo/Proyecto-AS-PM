package com.blartenix.proyecto_as_pm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RubricasActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView lvRubricas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubricas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RubricasActivity.this, CrearRubricaActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lvRubricas = (ListView)findViewById(R.id.lvRubcricas);

        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.navheadertvNombreUsuarioRu);
        nav_user.setText(Usuario.usuarioLogeado.getNombre());

        TextView nav_email = (TextView)hView.findViewById(R.id.navheadertvEmailUsuarioRu);
        nav_email.setText(Usuario.usuarioLogeado.getEmail());

        mostrartRubricas();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                mostrartRubricas();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            startActivity(new Intent(RubricasActivity.this, AsignaturasActivity.class));
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

    private void mostrartRubricas() {


        Rubrica.Adapter adapter = new Rubrica.Adapter(this, Usuario.usuarioLogeado.getRubricas());
        lvRubricas.setAdapter(adapter);

        /*ArrayList<String> nombresRubricas = new ArrayList<>();
        for (int i = 0; i < Usuario.usuarioLogeado.getRubricas().size(); i++){
            nombresRubricas.add(Usuario.usuarioLogeado.getRubricas().get(i).getNombreRubrica());
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresRubricas);
        lvRubricas.setAdapter(adapter);*/
    }
}
