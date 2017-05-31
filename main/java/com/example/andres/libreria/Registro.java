package com.example.andres.libreria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        setTitle("Registro de usuario");
    }
    public void cancelar(View view){

        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        finish();

    }
    public void registrarse(View view){
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        Toast.makeText(this,"Usuario Registrado",Toast.LENGTH_LONG).show();
        finish();
    }
}
