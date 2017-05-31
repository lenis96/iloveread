package com.example.andres.libreria;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    EditText user;
    EditText password;
    String name;
    String lastname;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Inicio de Sesión");
        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
        user=(EditText)findViewById(R.id.usuarioInput);
        password=(EditText)findViewById(R.id.passwordInput);
        user.setText(prefe.getString("usuario",""));
    }


    public void registrarse(View view){
        Intent i=new Intent(this,Registro.class);
        startActivity(i);
        finish();
    }
    public void ingresar(View view){

        if(!user.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
            ((Button)findViewById(R.id.loginButton)).setEnabled(false);
            ValidarUsuario hConexio=new ValidarUsuario();
            hConexio.execute(user.getText().toString(),password.getText().toString());
            //SharedPreferences preferences=getSharedPreferences("datos", Context.MODE_PRIVATE);
            //SharedPreferences.Editor editor=preferences.edit();
            //editor.putString("usuario",user.getText().toString());
            //Intent i=new Intent(this,Home.class);
            //editor.commit();
            //startActivity(i);
            //finish();
        }
        else{
            Toast.makeText(this,"Ingrese un usuario valido y contraseña",Toast.LENGTH_LONG).show();
        }

    }
    public void INGRESAR(){
        SharedPreferences preferences=getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("usuario",user.getText().toString());
        Intent i=new Intent(this,Home.class);
        editor.commit();
        startActivity(i);
        finish();
    }
    public  void RECHAZAR(){
        Toast.makeText(this,"el usuario o la contraseña son incorrectas",Toast.LENGTH_LONG).show();
    }

    public class ValidarUsuario extends AsyncTask<String,Integer,JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params){
            SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
            String cadena=getString(R.string.host)+"/user?user="+params[0]+"&password="+params[1];
            String devuelve="";
            URL url=null;
            JSONObject resultado=null;
            try{
                url=new URL(cadena);
                HttpURLConnection connection=(HttpsURLConnection) url.openConnection();
                connection.setRequestProperty("user-Agent","Mozilla/5.0.+"+"(Linux: Android 1.5; es-ES) Ejemplo HTTP));");
                int respuesta=connection.getResponseCode();

                StringBuilder res1=new StringBuilder();
                if(respuesta== HttpsURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader leer = new BufferedReader(new InputStreamReader(in));

                    String linea=leer.readLine();

                    while (linea!= null) {
                        res1.append(linea);
                        Log.d(">",linea);
                        devuelve+=linea;
                        linea=leer.readLine();
                    }
                    resultado=new JSONObject(res1.toString());

                }


                if(respuesta==HttpsURLConnection.HTTP_INTERNAL_ERROR){
                    Log.d("******",String.valueOf(respuesta));
                    Toast.makeText(getApplicationContext(),"el servidor no genera respuesta intentelo mas tarde",Toast.LENGTH_LONG).show();
                }
            }catch(MalformedURLException e){
                e.printStackTrace();
                devuelve="oo";
            }catch(JSONException e){
                e.printStackTrace();
                //devuelve="ool";
            }catch(IOException e){
                e.printStackTrace();
                devuelve="ooll";
            }
            if(resultado!=null) {
                //Log.d("antes", resultado.toString());
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
            try{
                //Log.d(aVoid.getString("validate"),"");
                Log.d(aVoid.getString("validate"),">>");
                if(aVoid.getString("validate").equals("ok")){
                    Log.d("tu cucha","");
                    SharedPreferences preferences=getSharedPreferences("datos", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("nombre",aVoid.getString("name"));
                    editor.putString("apellidos",aVoid.getString("lastname"));
                    editor.putString("email",aVoid.getString("email"));
                    editor.commit();
                    INGRESAR();

                }
                else{
                    Log.d("tu cnkuhbkhuucha","");
                    RECHAZAR();
                }
                Log.d("",">>lll");

            }catch (Exception e){
                e.printStackTrace();
                Log.d("",">>njkxdabjsk");
            }
            ((Button)findViewById(R.id.loginButton)).setEnabled(true);
            //renderizaInformacion();
        }
        @Override
        protected  void onPreExecute(){
            super.onCancelled();
            //tv1.setText("pidiendo");
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            Log.d("lll",String.valueOf(values));
        }


    }
}
