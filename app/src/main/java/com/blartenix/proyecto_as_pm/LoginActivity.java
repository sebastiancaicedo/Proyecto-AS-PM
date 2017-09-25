package com.blartenix.proyecto_as_pm;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    /*private static final String[] DUMMY_CREDENTIALS = new String[]{
            "admin@uninorte.edu.co:password"
    };*/
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    File folderUsuarios;
    File pathArchivoCredenciales;
    boolean existeYError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        folderUsuarios = new File(getFilesDir(), "Usuarios");
        if(!folderUsuarios.exists()){
            folderUsuarios.mkdirs();
        }

        pathArchivoCredenciales = new File(getFilesDir(), "Credenciales.txt");
        if(!pathArchivoCredenciales.exists()){
            try {
                pathArchivoCredenciales.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if(!pathArchivoCredenciales.exists()){
            try {
                pathArchivoCredenciales.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        //Log.d("Mensajes", Environment.getExternalStorageDirectory().toString());
        //Log.d("Mensajes", getFilesDir().toString());

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@uninorte.edu.co");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            /*//Verificamos si entro con la cuenta default
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }*/

            //Verificamos si entro con otro usuario
            if(verificarCredenciales()){
                String nombreArchivo = mEmail.split("@")[0];
                String json = leerJSONFile(nombreArchivo, DataBaseHandler.TipoDato.Usuarios);
                Usuario.usuarioLogeado = Constants.GsonHelper.fromJson(json, Usuario.class);
                return true;
            }

            // TODO: register the new account here.
            return false;
        }

        private boolean verificarCredenciales(){

            File archivoUs = new File(folderUsuarios+"/"+mEmail.split("@")[0]+".json");
            if(archivoUs.exists()) {
                try {
                    FileReader fr = new FileReader(pathArchivoCredenciales);
                    BufferedReader bf = new BufferedReader(fr);
                    String credenciales;
                    while ((credenciales = bf.readLine()) != null) {
                        String[] campos = credenciales.split(":");
                        if (TextUtils.equals(campos[0], mEmail) && TextUtils.equals(campos[1], mPassword)) {
                            bf.close();
                            fr.close();
                            return true;
                        }
                        else
                            if(TextUtils.equals(campos[0], mEmail)){
                                //Usuario existe, pero no es su contraseña
                                existeYError = true;
                                return false;

                            }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
                startActivity(new Intent(LoginActivity.this, AsignaturasActivity.class));
            } else {
                if(!isPasswordValid(mPassword) || existeYError) {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
                else {
                    mostartDialogCompletaRegistro(mEmail, mPassword);
                }

            }
        }

        private void mostartDialogCompletaRegistro(final String email, final String password){

            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);

            LayoutInflater linf = LayoutInflater.from(LoginActivity.this);
            final View layout = linf.inflate(R.layout.agregar_usuario_dialog_layout, null);
            dialog.setView(layout);

            dialog.setTitle("Completar Registro");
            dialog.setMessage("El Usuario ingresado no existe, Si desea crear uno nuevo, llene los campos");

            final EditText etCodigoUs = (EditText) layout.findViewById(R.id.etCodigoRegUs);
            final EditText etNombreUs = (EditText) layout.findViewById(R.id.etNombreRegUs);


            dialog.setPositiveButton("Registrar", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    String codigo = etCodigoUs.getText().toString();
                    String nombre = etNombreUs.getText().toString();
                    if(!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(codigo)){

                        Usuario usuarioRegistrado = new Usuario(codigo, nombre, email, password);
                        guardarTodo(usuarioRegistrado);
                        String credenciales = mEmail+":"+mPassword;
                        guardarCredencialUsuario(credenciales);
                        Usuario.usuarioLogeado = usuarioRegistrado;
                        Toast.makeText(LoginActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        onPostExecute(true);

                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Uno o mas campos están vacios", Toast.LENGTH_SHORT).show();
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

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }




        public String leerJSONFile(String nombreArchivo, DataBaseHandler.TipoDato tipoDato) {

            File archivo = new File(getFilesDir()+"/"+tipoDato.toString()+"/", nombreArchivo+".json");
            String json = "";
            try {
                FileReader fr = new FileReader(archivo);
                BufferedReader bf = new BufferedReader(fr);
                json = bf.readLine();
                bf.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
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


        private void guardarCredencialUsuario(String credencial){
            try {
                FileWriter fw = new FileWriter(pathArchivoCredenciales, true);
                PrintWriter pw = new PrintWriter(fw);
                pw.println(credencial);
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
}

