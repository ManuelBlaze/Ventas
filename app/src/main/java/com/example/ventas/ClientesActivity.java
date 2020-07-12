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
import android.widget.Toast;

public class ClientesActivity extends AppCompatActivity {

    EditText jetName, jetUser, jetPass1, jetPass2, jetCity;
    Button jbtConsultar, jbtAgregar, jbtModificar, jbtEliminar, jbtCancelar;
    MainSQLiteOpenHelper Admin = new MainSQLiteOpenHelper(this, "empresa.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        jetName = findViewById(R.id.etNombre);
        jetUser = findViewById(R.id.etUsuario);
        jetPass1 = findViewById(R.id.etPassword1);
        jetPass2 = findViewById(R.id.etPassword2);
        jetCity = findViewById(R.id.etCiudad);

        jbtConsultar = findViewById(R.id.btConsultar);
        jbtAgregar = findViewById(R.id.btAgregar);
        jbtModificar = findViewById(R.id.btModificar);
        jbtEliminar = findViewById(R.id.btEliminar);
        jbtCancelar = findViewById(R.id.btCancelar);
    }

    public void limpiarCampos () {
        jetUser.setText("");
        jetName.setText("");

        jetPass1.setText("");
        jetPass2.setText("");
        jetCity.setText("");
        jetUser.requestFocus();
    }

    public void consultar (View v) {

        SQLiteDatabase db = Admin.getReadableDatabase();
        String user;
        user = jetUser.getText().toString();
        if (user.isEmpty()) {
            Toast.makeText(this, "El Usuario es Requerido para Consultar", Toast.LENGTH_LONG).show();
            jetUser.requestFocus();
        } else {
            Cursor fila = db.rawQuery("SELECT * FROM cliente WHERE user ='" + user + "'", null);

            if (fila.moveToFirst()) {
                jetName.setText(fila.getString(0));
                jetPass1.setText(fila.getString(2));
                jetCity.setText(fila.getString(3));
            } else {
                Toast.makeText(this, "Usuario No Registrado", Toast.LENGTH_LONG).show();
            }
        }
        db.close();
    }

    public void agregar (View v) {

        SQLiteDatabase db = Admin.getReadableDatabase();
        String name, user, pass1, pass2, city;
        user = jetUser.getText().toString();
        name = jetName.getText().toString();
        pass1 = jetPass1.getText().toString();
        pass2 = jetPass2.getText().toString();
        city = jetCity.getText().toString();

        if (user.isEmpty() || name.isEmpty() || pass1.isEmpty() || pass2.isEmpty() || city.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios!", Toast.LENGTH_LONG).show();
        }
        else if (!pass1.equals(pass2)) {
            Toast.makeText(this, "Las Claves ingresadas no Coinciden!", Toast.LENGTH_LONG).show();
            jetPass2.requestFocus();
        } else {
            ContentValues dato = new ContentValues();
            dato.put("user", user);
            dato.put("name", name);
            dato.put("password", pass1);
            dato.put("city", city);
            long respuesta = db.insert("cliente", null, dato);

            if (respuesta > 0) {
                Toast.makeText(this, "Usuario Registrado Correctamente", Toast.LENGTH_LONG).show();
                limpiarCampos();
            } else {
                Toast.makeText(this, "Ha ocurrido un error, verifique su usuario", Toast.LENGTH_LONG).show();
            }
        }
        db.close();
    }

    public void modificar (View v) {

        SQLiteDatabase db = Admin.getWritableDatabase();
        String name, user, pass1, pass2, city;
        user = jetUser.getText().toString();
        name = jetName.getText().toString();
        pass1 = jetPass1.getText().toString();
        pass2 = jetPass2.getText().toString();
        city = jetCity.getText().toString();

        if (user.isEmpty() || name.isEmpty() || pass1.isEmpty() || pass2.isEmpty() || city.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios!", Toast.LENGTH_LONG).show();
        }
        else if (!pass1.equals(pass2)) {
            Toast.makeText(this, "Las Claves ingresadas no Coinciden!", Toast.LENGTH_LONG).show();
            jetPass2.requestFocus();
        } else {
            ContentValues dato = new ContentValues();
            dato.put("user", user);
            dato.put("name", name);
            dato.put("password", pass1);
            dato.put("city", city);
            long respuesta = db.update("cliente", dato, "user = '"+ user + "'", null);

            if (respuesta > 0) {
                Toast.makeText(this, "Usuario Modificado Correctamente", Toast.LENGTH_LONG).show();
                limpiarCampos();
            } else {
                Toast.makeText(this, "Ha ocurrido un error, verifique su contraseña", Toast.LENGTH_LONG).show();
            }
        }
        db.close();
    }

    public void eliminar ( View v) {
        SQLiteDatabase db = Admin.getWritableDatabase();
        String user;
        user = jetUser.getText().toString();

        if (user.isEmpty()) {
            Toast.makeText(this, "El usuario es obligatorio para realizar esta acción", Toast.LENGTH_LONG).show();
        } else {
            long respuesta = db.delete("cliente","user = '"+ user + "'", null);

            if (respuesta > 0) {
                Toast.makeText(this, "Usuario Eliminado Correctamente", Toast.LENGTH_LONG).show();
                limpiarCampos();
            } else {
                Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
            }
        }
        db.close();
    }

    public void cancelar (View v) {
        limpiarCampos();
    }

    public void regresarLog (View v) {
        Intent IntLog = new Intent(this,MainActivity.class);
        startActivity(IntLog);
    }
}