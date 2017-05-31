package com.example.andres.libreria;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by andres on 13/may/2017.
 */

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>{
    private List<Categoria> items;

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public Button btn;

        public CategoriaViewHolder(View v) {
            super(v);
            btn=(Button)v.findViewById(R.id.btn);
        }
    }

    public CategoriaAdapter(List<Categoria> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public CategoriaAdapter.CategoriaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.categoria_card, viewGroup, false);
        return new CategoriaAdapter.CategoriaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoriaAdapter.CategoriaViewHolder viewHolder, final int i) {
        viewHolder.btn.setText(items.get(i).getDescripcion());
        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(v.getContext(),CategoriaVista.class);
                j.putExtra("id",String.valueOf(items.get(i).getId()));
                j.putExtra("categoria",String.valueOf(items.get(i).getDescripcion()));
                v.getContext().startActivity(j);
            }
        });
        //viewHolder.imagen.setImageResource(itms.get(i).getImagen());
        /*
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
        new ImageLoadTask("https://iloveread-lenis96.c9users.io/img/img1.jpg",viewHolder.imagen).execute();
        */
    }
}
