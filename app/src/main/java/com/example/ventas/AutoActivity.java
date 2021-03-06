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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AutoActivity extends AppCompatActivity {
    EditText jetPlaca, jetModelo, jetMarca, jetValor;
    Button jbtConsultar, jbtAgregar, jbtModificar, jbtEliminar, jbtCancelar;
    TextView jtvStatus, jtvModelo, jtvMarca, jtvValor;
    LinearLayout botones;
    char div = '-';
    MainSQLiteOpenHelper Admin = new MainSQLiteOpenHelper(this, "empresa.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);

        this.setTitle("Control De Autos");

        jetPlaca = findViewById(R.id.etPlaca);
        jetModelo = findViewById(R.id.etModelo);
        jetMarca = findViewById(R.id.etMarca);
        jetValor = findViewById(R.id.etValor);

        jbtConsultar = findViewById(R.id.btConsultar);
        jbtAgregar = findViewById(R.id.btAgregar);
        jbtModificar = findViewById(R.id.btModificar);
        jbtEliminar = findViewById(R.id.btEliminar);
        jbtCancelar = findViewById(R.id.btCancelar);

        jtvModelo = findViewById(R.id.tvModelo);
        jtvMarca = findViewById(R.id.tvMarca);
        jtvValor = findViewById(R.id.tvValor);
        jtvStatus = findViewById(R.id.tvStatus);

        botones = findViewById(R.id.lyBotones);
    }

    public  void agregar (View v){
        SQLiteDatabase db = Admin.getWritableDatabase();

        String placa = jetPlaca.getText().toString();
        String modelo = jetModelo.getText().toString();
        String marca = jetMarca.getText().toString();
        String valor = jetValor.getText().toString();
        String status = "Disponible";

        if(placa.isEmpty() || modelo.isEmpty() || marca.isEmpty() || valor.isEmpty()) {
            Toast.makeText(this, "Todos los Campos son Requeridos", Toast.LENGTH_LONG).show();
        } else if (!placa.contains("-") || (placa.charAt(3) != div) || (placa.length() != 7)) {
            Toast.makeText(this, "Ingrese una placa válida Ej: IPC-541", Toast.LENGTH_LONG).show();
        }else{
            ContentValues dato  = new ContentValues();
            dato.put("placa", placa);
            dato.put("modelo", modelo);
            dato.put("marca", marca);
            dato.put("valor", valor);
            dato.put("status", status);

            long respuesta = db.insert("auto", null, dato);
            if(respuesta > 0){
                Toast.makeText(this, "Auto Agregado Exitosamente", Toast.LENGTH_LONG).show();
                limpiarCampos();
            }else{
                Toast.makeText(this, "Error. No se pudo guardar ", Toast.LENGTH_LONG).show();
            }
        }
        db.close();
    }

    public void consultar (View v) {
        SQLiteDatabase db = Admin.getReadableDatabase();
        String placa;
        placa = jetPlaca.getText().toString();

        if (placa.isEmpty()) {
            Toast.makeText(this, "La Placa es Obligatoria para Consultar", Toast.LENGTH_LONG).show();
        } else if (!placa.contains("-") || (placa.charAt(3) != div) || (placa.length() != 7)) {
            Toast.makeText(this, "Ingrese una placa válida Ej: IPC-541", Toast.LENGTH_LONG).show();
        } else {
            Cursor fila  = db.rawQuery("SELECT * FROM auto WHERE placa='" + placa + "'", null );
            if(fila.moveToFirst()){
                jtvStatus.setText(fila.getString(4));
                String status = jtvStatus.getText().toString();

                if (status.equals("Disponible")) {
                    jtvModelo.setVisibility(View.GONE);
                    jetModelo.setVisibility(View.VISIBLE);
                    jtvMarca.setVisibility(View.GONE);
                    jetMarca.setVisibility(View.VISIBLE);
                    jtvValor.setVisibility(View.GONE);
                    jetValor.setVisibility(View.VISIBLE);

                    jbtAgregar.setVisibility(View.VISIBLE);
                    botones.setVisibility(View.VISIBLE);

                    jetModelo.setText(fila.getString(1));
                    jetMarca.setText(fila.getString(2));
                    jetValor.setText(fila.getString(3));
                } else {
                    jetModelo.setVisibility(View.GONE);
                    jtvModelo.setVisibility(View.VISIBLE);
                    jetMarca.setVisibility(View.GONE);
                    jtvMarca.setVisibility(View.VISIBLE);
                    jetValor.setVisibility(View.GONE);
                    jtvValor.setVisibility(View.VISIBLE);

                    jbtAgregar.setVisibility(View.GONE);
                    botones.setVisibility(View.GONE);

                    jtvModelo.setText(fila.getString(1));
                    jtvMarca.setText(fila.getString(2));
                    jtvValor.setText(fila.getString(3));
                }
            } else {
                Toast.makeText(this, "Auto no Registrado ", Toast.LENGTH_LONG).show();
            }
            fila.close();
        }
        db.close();
    }

    public void modificar (View v) {
        SQLiteDatabase db = Admin.getWritableDatabase();
        String placa = jetPlaca.getText().toString();
        String modelo = jetModelo.getText().toString();
        String marca = jetMarca.getText().toString();
        String valor = jetValor.getText().toString();
        String status = jtvStatus.getText().toString();

        if(placa.isEmpty() || modelo.isEmpty() || marca.isEmpty() || valor.isEmpty()) {
            Toast.makeText(this, "Todos los Campos son Requeridos", Toast.LENGTH_LONG).show();
        } else {
            ContentValues dato = new ContentValues();
            dato.put("placa", placa);
            dato.put("modelo", modelo);
            dato.put("marca", marca);
            dato.put("valor", valor);
            dato.put("status", status);

            long respuesta = db.update("auto", dato, "placa = '"+ placa + "'", null);

            if (respuesta > 0) {
                Toast.makeText(this, "Auto Modificado Correctamente", Toast.LENGTH_LONG).show();
                limpiarCampos();
            } else {
                Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
            }
        }
        db.close();
    }

    public void eliminar (View v) {
        SQLiteDatabase db = Admin.getWritableDatabase();
        String placa = jetPlaca.getText().toString();
        if (placa.isEmpty()) {
            Toast.makeText(this, "La Placa es Obligatoria para Eliminar", Toast.LENGTH_LONG).show();
        } else {
            long respuesta = db.delete("auto", "placa = '" + placa + "'", null);

            if (respuesta > 0) {
                Toast.makeText(this, "Auto Eliminado Correctamente", Toast.LENGTH_LONG).show();
                limpiarCampos();
            } else {
                Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
            }
        }
        db.close();
    }

    public void limpiarCampos () {
        jetPlaca.setText("");
        jetModelo.setText("");
        jetMarca.setText("");
        jetValor.setText("");
        jtvStatus.setText("----------");
        jtvMarca.setText("");
        jtvModelo.setText("");
        jtvValor.setText("");

        jtvModelo.setVisibility(View.GONE);
        jetModelo.setVisibility(View.VISIBLE);
        jtvMarca.setVisibility(View.GONE);
        jetMarca.setVisibility(View.VISIBLE);
        jtvValor.setVisibility(View.GONE);
        jetValor.setVisibility(View.VISIBLE);

        jbtAgregar.setVisibility(View.VISIBLE);
        botones.setVisibility(View.VISIBLE);

        jetPlaca.requestFocus();
    }

    public void cancelar (View v) {
        limpiarCampos();
    }

    public void regresarLog (View v) {
        Intent IntLog = new Intent(this,MainActivity.class);
        startActivity(IntLog);
    }
}