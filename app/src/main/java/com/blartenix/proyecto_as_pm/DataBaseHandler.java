package com.blartenix.proyecto_as_pm;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by lusec on 24/09/2017.
 */

public class DataBaseHandler {

    //Archivos json se guardan como
    // Usuarios codigo_nombre.json
    // Asignaturas codig_nombre.json

    //private Context context;

    /*public static File foldersPath;
    public static File pathArchivoCredenciales = new File(foldersPath, "Credenciales.txt");
    public static File folderUsuarios = new File(foldersPath, "Usuarios");
    public static File folderAsignaturas = new File(foldersPath, "Asignaturas");
    public static File folderEstudiantes = new File(foldersPath, "Estudiantes");
    public static File folderRubricas =  new File(foldersPath, "Rubricas");
    public static File folderEvaluaciones = new File(foldersPath, "Evaluaciones");*/

    /*public DataBaseHandler(Context context){

        this.context = context;

    }*/

    public enum TipoDato{
        Usuarios,
        Asignaturas,
        Estudiantes,
        Rubricas,
        Evaluaciones
    }

    /**
     * Guarda y Sobre escribe
     * @param nombreArchivo
     * @param json
     * @param tipoDato
     */
    /*private void guardarJSONFile(String nombreArchivo, String json, TipoDato tipoDato) {
        File archivo = new File(foldersPath+"/"+tipoDato.toString()+"/", nombreArchivo+".json");
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

    public String leerJSONFile(String nombreArchivo, TipoDato tipoDato) {

        File archivo = new File(foldersPath+"/"+tipoDato.toString()+"/", nombreArchivo+".json");
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

    public boolean verificarCredenciales(String email, String password){

        File archivoUs = new File(folderUsuarios+"/"+email.split("@")[0]+".jason");
        if(archivoUs.exists()) {
            try {
                FileReader fr = new FileReader(pathArchivoCredenciales);
                BufferedReader bf = new BufferedReader(fr);
                String credenciales;
                while ((credenciales = bf.readLine()) != null) {
                    String[] campos = credenciales.split(":");
                    if (TextUtils.equals(campos[0], email) && TextUtils.equals(campos[1], password)) {
                        bf.close();
                        fr.close();
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void guardarTodo(Usuario usuario){

        String json = Constants.GsonHelper.toJson(usuario, Usuario.class);
        String nombreArchivo = usuario.getEmail().split("@")[0];
        guardarJSONFile(nombreArchivo, json, TipoDato.Usuarios);
        String credenciales = usuario.getEmail()+":"+usuario.getContrasena();
        guardarCredencialUsuario(credenciales);
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
        guardarJSONFile(nombreArchivo, json, TipoDato.Asignaturas);
        for (int indice = 0; indice < asignatura.getEstudiantes().size(); indice++){
            guardarEstudiante(asignatura.getEstudiantes().get(indice));
        }
    }

    private void guardarEstudiante(Estudiante estudiante){

        String json = Constants.GsonHelper.toJson(estudiante, Estudiante.class);
        String nombreArchivo = estudiante.getCodigo()+"_"+estudiante.getNombre();
        guardarJSONFile(nombreArchivo, json, TipoDato.Estudiantes);
    }

    private void guardarRubrica(Rubrica rubrica){

        String json = Constants.GsonHelper.toJson(rubrica, Rubrica.class);
        String nombreArchivo = rubrica.getCodigo()+"_"+rubrica.getNombreRubrica();
        guardarJSONFile(nombreArchivo, json, TipoDato.Rubricas);
    }

    private void guardarEvaluacion(Evaluacion evaluacion){

        String json = Constants.GsonHelper.toJson(evaluacion, Evaluacion.class);
        String nombreArchivo = evaluacion.getCodigo()+"_"+evaluacion.getNombre();
        guardarJSONFile(nombreArchivo, json, TipoDato.Rubricas);
    }*/

    /*public String getElementId(String nombre, TipoDato tipoDato){

        String id = "";
        try {
            File path = new File(foldersPath+tipoDato.toString()+"/IDs.txt");
            FileInputStream fis = context.openFileInput(path.getAbsolutePath());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bf = new BufferedReader(isr);
            String linea;
            while ((linea = bf.readLine()) != null){
                String[] campos = linea.split(":");
                if(campos[0] == nombre){
                    id = campos[1];
                }
            }
            bf.close();
            fis.close();
            isr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }*/

    /*public void guardarNombreNid(String nombreNid, TipoDato tipoDato){

        try {
            File path = new File(foldersPath+"/"+tipoDato.toString(), "/IDs.txt");
            FileOutputStream fos = context.openFileOutput(path.getAbsolutePath() , Context.MODE_PRIVATE);
            fos.write(nombreNid.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*public boolean verifExistencia(String nombreNid, TipoDato tipoDato){

        try {
            File path = new File(foldersPath+"/"+tipoDato+"/IDs.txt");
            FileInputStream fis = context.openFileInput(path.getAbsolutePath());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bf = new BufferedReader(isr);
            String nombreNidLeido;
            while ((nombreNidLeido = bf.readLine()) != null){
                if(TextUtils.equals(nombreNidLeido, nombreNid)){
                    bf.close();
                    fis.close();
                    isr.close();
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }*/






    /*private void escribirLineaEnArchivo(File pathArchivo, String texto, boolean append){

        try {
            pathArchivo.createNewFile();
            FileWriter fw = new FileWriter(pathArchivo, append);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(texto);
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*public void leerLineArchivo(File pathArchivo) {
        String message;
        FileReader fr = null;
        try {
            fr = new FileReader(pathArchivo.getPath());
            BufferedReader br = new BufferedReader(fr);
            while ((message = br.readLine()) != null) {
                Log.d("Mensajes", message);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


}
