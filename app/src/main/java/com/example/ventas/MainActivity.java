package com.example.ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText jetUser, jetPassword;
    Button jbtLogin, jbtRegister, jbtRegisCar;
    MainSQLiteOpenHelper Admin = new MainSQLiteOpenHelper(this, "empresa.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("Venta de Autos | Inicio");

        jetUser = findViewById(R.id.etUser);
        jetPassword = findViewById(R.id.etPassword);
        jbtLogin = findViewById(R.id.btLogin);
        jbtRegister = findViewById(R.id.btRegister);
        jbtRegisCar = findViewById(R.id.btRegisCar);
    }

    public void logIn (View v) {
        SQLiteDatabase db = Admin.getReadableDatabase();
        String user, pass;
        user = jetUser.getText().toString();
        pass = jetPassword.getText().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Ambos campos son obligatorios", Toast.LENGTH_LONG).show();
            jetUser.requestFocus();
        } else {
            Cursor fila = db.rawQuery("SELECT * FROM cliente WHERE user = '"+ user + "' AND password = '"+ pass +"'", null);

            if (fila.moveToFirst()) {
                Intent IntVenta = new Intent(this, VentaActivity.class);
                IntVenta.putExtra("User", user);
                startActivity(IntVenta);
            } else {
                Toast.makeText(this, "Usuario o Contraseña Incorrectos", Toast.LENGTH_LONG).show();
            }
            fila.close();
        }
        db.close();
    }

    public void clientes (View v) {
        Intent IntClientes = new Intent(this, ClientesActivity.class);
        startActivity(IntClientes);
    }

    public void autos (View v) {
        Intent IntAutos = new Intent(this, AutoActivity.class);
        startActivity(IntAutos);
    }
}