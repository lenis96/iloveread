package com.example.andres.libreria;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.widget.Toast.makeText;

public class Perfil extends AppCompatActivity {
    TextView usuarioView;
    EditText nombreInput;
    EditText apellidosInput;
    EditText fechaInput;
    EditText ciudadInput;
    EditText direccionInput;
    EditText paisInput;
    EditText emailInput;
    RadioButton masculino,femenino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        usuarioView=(TextView)findViewById((R.id.usuarioView));
        nombreInput=(EditText)findViewById(R.id.nombreInput);
        apellidosInput=(EditText)findViewById(R.id.apellidosInput);
        fechaInput=(EditText)findViewById(R.id.fechaInput);
        masculino=(RadioButton)findViewById(R.id.masculino);
        femenino=(RadioButton)findViewById(R.id.femenino);
        ciudadInput=(EditText)findViewById(R.id.ciudadInput);
        direccionInput=(EditText)findViewById(R.id.direccionInput);
        emailInput=(EditText)findViewById(R.id.emailInput);
        paisInput=(EditText)findViewById(R.id.paisInput);
        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
        usuarioView.setText("usuario: "+prefe.getString("usuario",""));
        nombreInput.setText(prefe.getString("nombre",""));
        apellidosInput.setText(prefe.getString("apellidos",""));
        fechaInput.setText(prefe.getString("fecha",""));
        String sexo=prefe.getString("sexo","");
        if(sexo.equals("masculino")){
            masculino.setChecked(true);
        }
        else if(sexo.equals("femenino")){
            femenino.setChecked(true);
        }
        paisInput.setText(prefe.getString("pais",""));
        ciudadInput.setText(prefe.getString("ciudad",""));
        direccionInput.setText(prefe.getString("direccion",""));
        emailInput.setText(prefe.getString("email",""));

    }
    public void cargarPerfil(View v){

    }
    public void guardarPerfil(View v){
        SharedPreferences preferences=getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("nombre",nombreInput.getText().toString());
        editor.putString("apellidos",apellidosInput.getText().toString());
        editor.putString("fecha",fechaInput.getText().toString());
        if(masculino.isChecked()){
            editor.putString("sexo","masculino");
        }
        else if(femenino.isChecked()){
            editor.putString("sexo","femenino");
        }
        editor.putString("pais",paisInput.getText().toString());
        editor.putString("ciudad",ciudadInput.getText().toString());
        editor.putString("direccion",direccionInput.getText().toString());
        editor.putString("email",emailInput.getText().toString());
        //Toast.makeText(this,"Datos guardados",Toast.LENGTH_LONG).show();
        editor.commit();
        ActualizarPefil hConexion=new ActualizarPefil();
        hConexion.execute();

    }



    public class ActualizarPefil extends AsyncTask<String,Integer,JSONObject> {
        boolean ok=false;
        @Override
        protected JSONObject doInBackground(String... params){
            String cadena=getString(R.string.host)+"/userE/";
            String devuelve="";
            URL url=null;
            JSONObject resultado=null;
            try{
                url=new URL(cadena);
                HttpURLConnection connection=(HttpsURLConnection) url.openConnection();
                connection.setRequestProperty("user-Agent","Mozilla/5.0.+"+"(Linux: Android 1.5; es-ES) Ejemplo HTTP));");
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type","application/json");
                JSONObject body=new JSONObject();
                SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
                body.put("user",prefe.getString("usuario",""));
                /*body.put("password","");
                body.put("name",prefe.getString("nombre",""));
                body.put("lastname",prefe.getString("apellidos",""));
                body.put("date",prefe.getString("fecha",""));
                String sexo=prefe.getString("sexo","");
                if(sexo.equals("masculino")){
                    body.put("sex",1);
                }
                else if(sexo.equals("femenino")){
                    body.put("sex",2);
                }
                body.put("city",prefe.getString("ciudad",""));
                body.put("country",prefe.getString("pais",""));
                //paisInput.setText(prefe.getString("pais",""));
                //ciudadInput.setText(prefe.getString("ciudad",""));
                body.put("direccion",prefe.getString("direccion",""));*/
                body.put("email",prefe.getString("email",""));
                //Log.d("--",informacion.toString());
                //body.put("libroId",informacion.getInt("idLibro"));
                OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
                wr.write(body.toString());
                wr.flush();
                wr.close();
                Log.d("body",body.toString());
                int respuesta=connection.getResponseCode();
                StringBuilder res1=new StringBuilder();
                if(respuesta== HttpsURLConnection.HTTP_CREATED) {
                    //
                    ok=true;

                }

            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch(JSONException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();

            }
            if(resultado!=null) {
                Log.d("antes", resultado.toString());
            }
            return resultado;

        }
        @Override
        protected void onCancelled(JSONObject aVoid){
            super.onCancelled();
        }
        @Override
        protected  void onPostExecute(JSONObject aVoid){
            //informacion=aVoid;
            //Log.d("inframacion libro",aVoid.toString());
            if(ok){
                Toast.makeText(getApplicationContext(),"Datos guardados",Toast.LENGTH_LONG).show();
                //CarritoCompras.ObtenerLibros hconexio=new CarritoCompras.ObtenerLibros();
                //hconexio.execute();
            }

        }
        @Override
        protected  void onPreExecute(){
            super.onCancelled();
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
        }


    }
}
