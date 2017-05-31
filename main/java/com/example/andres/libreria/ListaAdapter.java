package com.example.andres.libreria;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by andres on 13/may/2017.
 */

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ListaViewHolder>{
    private List<Libro> items;
    private ListadoLibros parent;

    public static class ListaViewHolder extends RecyclerView.ViewHolder {
        public ImageView imagen;
        public TextView titulo;
        public TextView autor;
        public TextView precio;
        public ImageButton close;

        public ListaViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            titulo = (TextView) v.findViewById(R.id.TituloTv);
            autor = (TextView) v.findViewById(R.id.AutorTv);
            precio=(TextView) v.findViewById(R.id.precioTv);
            close=(ImageButton) v.findViewById(R.id.closeBtn);
        }
    }

    public ListaAdapter(List<Libro> items,ListadoLibros parent) {
        this.parent=parent;this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ListaAdapter.ListaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lista_card, viewGroup, false);
        return new ListaAdapter.ListaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ListaAdapter.ListaViewHolder viewHolder, final int i) {
        //viewHolder.imagen.setImageResource(items.get(i).getImagen());
        viewHolder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewHolder.titulo.setText(items.get(i).getTitulo());
        viewHolder.autor.setText(items.get(i).getAutor());
        viewHolder.precio.setText("$"+String.valueOf(items.get(i).getPrecio()));
        viewHolder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("nln",v.getParent().getClass().toString());
                //items.remove(i);
                //notifyItemRemoved(i);
                //notifyItemRangeChanged(i,items.size());
                //Intent i =new Intent(v.getContext(),CarritoCompras.class);
                //v.getContext().startActivity(i);
                //v.getContext().finish();
                EliminarLibroCarrito hconexion=new EliminarLibroCarrito();

                hconexion.execute(String.valueOf(items.get(i).getId()));
                //parent.renderizarInformacion();

            }
        });
        new ImageLoadTask("https://iloveread-lenis96.c9users.io/"+items.get(i).getImgSrc(),viewHolder.imagen).execute();
    }
    public class EliminarLibroCarrito extends AsyncTask<String,Integer,JSONObject> {
        boolean ok=false;
        @Override
        protected JSONObject doInBackground(String... params){
            SharedPreferences prefe=((CarritoCompras)parent).getSharedPreferences("datos", Context.MODE_PRIVATE);
            Log.d("if",params[0]);
            String cadena=((CarritoCompras)parent).getString(R.string.host)+"/carritoCompras/"+params[0]+"?user="+prefe.getString("usuario","");
            String devuelve="";
            URL url=null;
            JSONObject resultado=null;
            try{
                url=new URL(cadena);
                HttpURLConnection connection=(HttpsURLConnection) url.openConnection();
                connection.setRequestProperty("user-Agent","Mozilla/5.0.+"+"(Linux: Android 1.5; es-ES) Ejemplo HTTP));");
                connection.setRequestMethod("DELETE");
                connection.setRequestProperty("Content-Type","application/json");
                int respuesta=connection.getResponseCode();
                StringBuilder res1=new StringBuilder();
                if(respuesta== HttpsURLConnection.HTTP_CREATED) {
                    //
                    ok=true;

                }

            }catch(MalformedURLException e){
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
                //Toast.makeText(,"libro agragado al carrito de compras",Toast.LENGTH_LONG).show();
                //comprar.setText("El libro se encuentra en el carrito de compras");
            }
            else{
                //comprar.setEnabled(true);
            }
            //Log.d("info",informacion.toString());
            ((CarritoCompras)parent).actualizarInformacion();
            //parent.renderizarInformacion();

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
