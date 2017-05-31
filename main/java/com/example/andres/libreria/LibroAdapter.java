package com.example.andres.libreria;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by andres on 08/may/2017.
 */

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.LibroViewHolder> {
    private List<Libro> items;

    public static class LibroViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView titulo;
        public TextView autor;

        public LibroViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            titulo = (TextView) v.findViewById(R.id.titulo);
            autor = (TextView) v.findViewById(R.id.autor);
        }
    }

    public LibroAdapter(List<Libro> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public LibroViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.libro_card, viewGroup, false);
        return new LibroViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LibroViewHolder viewHolder, final int i) {
        //viewHolder.imagen.setImageResource(items.get(i).getImagen());
        viewHolder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),items.get(i).getTitulo(),Toast.LENGTH_LONG).show();
                Intent j=new Intent(v.getContext(),Vistalibro.class);

                Log.d("><",String.valueOf(items.get(i).getId()));
                j.putExtra("id",String.valueOf(items.get(i).getId()));
                v.getContext().startActivity(j);
            }
        });
        viewHolder.titulo.setText(items.get(i).getTitulo());
        viewHolder.autor.setText(items.get(i).getAutor());
        Log.d("imagen",items.get(i).getImgSrc());
        new ImageLoadTask("https://iloveread-lenis96.c9users.io/"+items.get(i).getImgSrc(),viewHolder.imagen).execute();
    }
}
