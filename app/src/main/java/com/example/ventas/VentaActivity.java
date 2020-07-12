package com.example.ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VentaActivity extends AppCompatActivity {

    EditText jetPlaca, jetColor;
    TextView jtvUsuario, jtvModelo, jtvMarca, jtvValor;
    Button jbtComprar, jbtBuscar;
    MainSQLiteOpenHelper Admin = new MainSQLiteOpenHelper(this, "empresa.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);

        Intent IntVenta = getIntent();
        String user = IntVenta.getStringExtra("User");

        jtvUsuario = findViewById(R.id.tvUsuario);
        jtvUsuario.setText(user);

        jetPlaca = findViewById(R.id.etPlaca);
        jtvModelo = findViewById(R.id.tvModelo);
        jtvMarca = findViewById(R.id.tvMarca);
        jetColor = findViewById(R.id.etColor);
        jtvValor = findViewById(R.id.tvValor);

        jbtBuscar = findViewById(R.id.btBuscar);
        jbtComprar = findViewById(R.id.btComprar);
    }

    public void buscar (View v) {
        SQLiteDatabase db = Admin.getReadableDatabase();
        String placa;
        placa = jetPlaca.getText().toString();

        if ( placa.isEmpty()) {
            Toast.makeText(this, "Placa es Obligatorio", Toast.LENGTH_LONG).show();
        } else {
            Cursor aut  = db.rawQuery("SELECT * FROM auto WHERE placa='" + placa + "'", null );

            if (aut.moveToFirst()) {
                jtvModelo.setText(aut.getString(1));
                jtvMarca.setText(aut.getString(2));
                jtvValor.setText(aut.getString(3));

                jbtBuscar.setVisibility(View.GONE);
                jbtComprar.setVisibility(View.VISIBLE);
                jetColor.requestFocus();
            } else {
                Toast.makeText(this, "Placa NO Encontrada", Toast.LENGTH_LONG).show();
                jetPlaca.requestFocus();
            }
        }
        db.close();
    }

    public void comprar (View v) {
        SQLiteDatabase db = Admin.getReadableDatabase();
        String user, placa, modelo, marca, color, valor;
        user = jtvUsuario.getText().toString();
        placa = jetPlaca.getText().toString();
        modelo = jtvModelo.getText().toString();
        marca = jtvMarca.getText().toString();
        color = jetColor.getText().toString();
        valor = jtvValor.getText().toString();

        if (placa.isEmpty() || color.isEmpty() ) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
            jetColor.requestFocus();
        } else {

            Toast.makeText(this, "Compra realizada Exitosamente", Toast.LENGTH_LONG).show();
            limpiarCampos();
        }
        db.close();
    }

    public void cancelar (View v) {
        limpiarCampos();
        jbtComprar.setVisibility(View.GONE);
        jbtBuscar.setVisibility(View.VISIBLE);
    }

    public void limpiarCampos () {
        jetPlaca.setText("");
        jtvModelo.setText("Modelo del Auto");
        jtvMarca.setText("Marca del Auto");
        jetColor.setText("");
        jtvValor.setText("Ej: 6500000");

        jetPlaca.findFocus();
    }

    public void regresarLog (View v) {
        Intent IntLog = new Intent(this,MainActivity.class);
        startActivity(IntLog);
    }
}