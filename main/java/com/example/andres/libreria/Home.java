package com.example.andres.libreria;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Home extends AppCompatActivity {
    LinearLayout lhNuevos;
    private RecyclerView recyclerNuevos;
    private RecyclerView recyclerMasVendidos;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private JSONObject informacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Inicio");
        //lhNuevos=(LinearLayout)findViewById(R.id.lhNuevos);
        //lhNuevos.addView(LayoutInflater.from(this).inflate(R.layout.activity_carrito_compras,null));
        //lhNuevos.addView(LayoutInflater.from(this).inflate(R.layout.activity_carrito_compras,null));
        // Inicializar Animes
        List items = new ArrayList();
/*
        items.add(new Libro(1,"nose","tu cucha","Lol","lorem",5000));
        items.add(new Libro(2,"nose2","tu cucha","Lol","lorem",5000));
        items.add(new Libro(3,"nose3","tu cucha","Lol","lorem",5000));
        items.add(new Libro(4,"nose4","tu cucha","Lol","lorem",5000));
*/

// Obtener el Recycler
        recyclerNuevos = (RecyclerView) findViewById(R.id.recicladorNuevos);
        recyclerNuevos.setHasFixedSize(true);
        recyclerMasVendidos = (RecyclerView) findViewById(R.id.recicladorMasVendidos);
        recyclerMasVendidos.setHasFixedSize(true);

// Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerNuevos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerMasVendidos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

// Crear un nuevo adaptador
        adapter = new LibroAdapter(items);
        recyclerNuevos.setAdapter(adapter);
        recyclerMasVendidos.setAdapter(new LibroAdapter(items));
        ObtenerLibros hConexion=new ObtenerLibros();
        hConexion.execute("","");
    }
    @Override public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.inicio){
            //Intent i=new Intent(this,Opcion1.class);
            //i.putExtra("direccion",text1.getText().toString());
            //startActivity(i);
            //Toast.makeText(this,"Se seleccionó la primer opción",Toast.LENGTH_LONG).show();
        }
        if(id==R.id.buscar){
             Intent i=new Intent(this,Busqueda.class);
             startActivity(i);
        }
        if(id==R.id.categorias){
            Intent i=new Intent(this,Categorias.class);
            startActivity(i);
            //Toast.makeText(this,"Se seleccionó la tercera opción",Toast.LENGTH_LONG).show();
        }/*
        if(id==R.id.listaDeseos){
            Intent i=new Intent(this,ListaDeseos.class);
            startActivity(i);
        }*/
        if(id==R.id.carroCompras){
            Intent i=new Intent(this,CarritoCompras.class);
            startActivity(i);
        }
        if(id==R.id.perfil){
            Intent i=new Intent(this,Perfil.class);
            startActivity(i);
        }
        if(id==R.id.acercaDe){
            Intent i=new Intent(this,AcercaDE.class);
            startActivity(i);
        }
        if(id==R.id.cerrarSesion){
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void abrirLibro(View v){
        Intent i =new Intent(this,Vistalibro.class);
        startActivity(i);
        //finish();
    }

    public class ObtenerLibros extends AsyncTask<String,Integer,JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params){
            String cadena=getString(R.string.host)+"/Libros";
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
            informacion=aVoid;
            renderizaInformacion();
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
    public void renderizaInformacion(){
        if(informacion!= null){
            List itemsNuevos = new ArrayList();
            List itemsMasVendidos = new ArrayList();
            try{
                JSONArray ansJason = informacion.getJSONArray("nuevos");
                if (ansJason.length() > 0) {
                    for(int i=0;i<ansJason.length();i++) {
                        itemsNuevos.add(new Libro(ansJason.getJSONObject(i).getInt("isbn"), ansJason.getJSONObject(i).getString("titulo"), ansJason.getJSONObject(i).getString("autor"), "LOL", "lorem0", 50000, ansJason.getJSONObject(i).getString("imgSrc")));
                    }
                }
                ansJason = informacion.getJSONArray("masVendidos");
                if (ansJason.length() > 0) {
                    for(int i=0;i<ansJason.length();i++) {
                        itemsMasVendidos.add(new Libro(ansJason.getJSONObject(i).getInt("isbn"), ansJason.getJSONObject(i).getString("titulo"), ansJason.getJSONObject(i).getString("autor"), "LOL", "lorem0", 0, ansJason.getJSONObject(i).getString("imgSrc")));
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            //adapter = new LibroAdapter(items);
            recyclerNuevos.setAdapter(new LibroAdapter(itemsNuevos));
            recyclerMasVendidos.setAdapter(new LibroAdapter(itemsMasVendidos));
        }
    };
}
