package com.example.ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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
    TextView jtvUsuario, jtvModelo, jtvMarca, jtvValor, jtvStatus, jtvColor;
    Button jbtComprar, jbtBuscar, jbtVerVenta;
    MainSQLiteOpenHelper Admin = new MainSQLiteOpenHelper(this, "empresa.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);

        this.setTitle("Realizar Venta");

        Intent IntVenta = getIntent();
        String user = IntVenta.getStringExtra("User");

        jtvUsuario = findViewById(R.id.tvUsuario);
        jtvUsuario.setText(user);

        jetPlaca = findViewById(R.id.etPlaca);
        jtvModelo = findViewById(R.id.tvModelo);
        jtvMarca = findViewById(R.id.tvMarca);
        jetColor = findViewById(R.id.etColor);
        jtvColor = findViewById(R.id.tvColor);
        jtvValor = findViewById(R.id.tvValor);
        jtvStatus = findViewById(R.id.tvStatus);

        jbtBuscar = findViewById(R.id.btBuscar);
        jbtComprar = findViewById(R.id.btComprar);
        jbtVerVenta = findViewById(R.id.btVerVenta);
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
                jtvStatus.setText(aut.getString(4));

                String status = jtvStatus.getText().toString();
                if (status.equals("Disponible")) {
                    jbtBuscar.setVisibility(View.GONE);
                    jbtComprar.setVisibility(View.VISIBLE);
                    jetColor.requestFocus();
                } else {
                    subConsul(placa);
                    jbtBuscar.setVisibility(View.GONE);
                    jbtVerVenta.setVisibility(View.VISIBLE);
                }

            } else {
                Toast.makeText(this, "Placa NO Encontrada", Toast.LENGTH_LONG).show();
                jetPlaca.requestFocus();
            }
            aut.close();
        }
        db.close();
    }

    public void comprar (View v) {
        SQLiteDatabase db = Admin.getReadableDatabase();
        String user, placa, modelo, marca, color, valor, status;
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
            status = "Vendido";
            ContentValues dato = new ContentValues();
            dato.put("placa", placa);
            dato.put("user", user);
            dato.put("modelo", modelo);
            dato.put("marca", marca);
            dato.put("color", color);
            dato.put("valor", valor);
            dato.put("status", status);

            ContentValues auto = new ContentValues();
            dato.put("placa", placa);
            dato.put("modelo", modelo);
            dato.put("marca", marca);
            dato.put("valor", valor);
            dato.put("status", status);

            long respuesta = db.insert("venta", null, dato);
            if (respuesta > 0) {
                long res = actualizar();

                if (res > 0) {
                    Toast.makeText(this, "Compra realizada Exitosamente", Toast.LENGTH_LONG).show();
                    limpiarCampos();
                } else {
                    Toast.makeText(this, "Ha Ocurrido un Error al Actualizar", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "Ha Ocurrido un Error", Toast.LENGTH_LONG).show();
                jetPlaca.requestFocus();
            }
        }
        db.close();
    }

    public void subConsul (String placa) {
        SQLiteDatabase db = Admin.getReadableDatabase();
        Cursor col = db.rawQuery("SELECT * FROM venta WHERE placa = '"+placa +"'", null);
        if (col.moveToFirst()) {
            jtvColor.setText(col.getString(4));

            jetColor.setVisibility(View.GONE);
            jtvColor.setVisibility(View.VISIBLE);
        }
        db.close();
    }

    public long actualizar () {
        SQLiteDatabase db = Admin.getWritableDatabase();
        String placa = jetPlaca.getText().toString();
        String modelo = jtvModelo.getText().toString();
        String marca = jtvMarca.getText().toString();
        String valor = jtvValor.getText().toString();
        String status = "Vendido";

        ContentValues dato = new ContentValues();
        dato.put("placa", placa);
        dato.put("modelo", modelo);
        dato.put("marca", marca);
        dato.put("valor", valor);
        dato.put("status", status);

        long res = db.update("auto", dato, "placa = '"+ placa + "'", null);

        if (res > 0) {
            return res;
        } else {
            res = 0;
        }
        db.close();
        return res;
    }

    public void verVenta (View v) {
        String placa = jetPlaca.getText().toString();
        Intent IntInfoVenta = new Intent(this,InfoVentaActivity.class);
        IntInfoVenta.putExtra("Placa", placa);
        startActivity(IntInfoVenta);
    }

    public void cancelar (View v) {
        limpiarCampos();
    }

    public void limpiarCampos () {
        jetPlaca.setText("");
        jtvModelo.setText("Modelo del Auto");
        jtvMarca.setText("Marca del Auto");
        jetColor.setText("");
        jtvColor.setText("");
        jtvColor.setVisibility(View.GONE);
        jetColor.setVisibility(View.VISIBLE);
        jtvValor.setText("Ej: 6500000");
        jtvStatus.setText("----------");

        jbtComprar.setVisibility(View.GONE);
        jbtVerVenta.setVisibility(View.GONE);
        jbtBuscar.setVisibility(View.VISIBLE);
        jetPlaca.findFocus();
    }

    public void regresarLog (View v) {
        Intent IntLog = new Intent(this,MainActivity.class);
        startActivity(IntLog);
    }
}