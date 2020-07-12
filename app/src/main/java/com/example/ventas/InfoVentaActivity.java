package com.example.ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InfoVentaActivity extends AppCompatActivity {
    TextView jtvPlaca, jtvUser, jtvModelo, jtvMarca, jtvColor, jtvValor;
    MainSQLiteOpenHelper Admin = new MainSQLiteOpenHelper(this, "empresa.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_venta);

        this.setTitle("Información de Venta");

        Intent IntInfoVenta = getIntent();
        String Placa = IntInfoVenta.getStringExtra("Placa");

        jtvPlaca = findViewById(R.id.tvPlaca);
        jtvPlaca.setText(Placa);

        jtvUser = findViewById(R.id.tvUser);
        jtvModelo = findViewById(R.id.tvModelo);
        jtvMarca = findViewById(R.id.tvMarca);
        jtvColor = findViewById(R.id.tvColor);
        jtvValor = findViewById(R.id.tvValor);

        mostrarDatos();
    }

    public void mostrarDatos () {
        SQLiteDatabase db = Admin.getReadableDatabase();
        String placa;
        placa = jtvPlaca.getText().toString();
        Cursor dat = db.rawQuery("SELECT * FROM venta WHERE placa = '"+placa+"'",null);

        if (dat.moveToFirst()) {
            jtvUser.setText(dat.getString(1));
            jtvModelo.setText(dat.getString(2));
            jtvMarca.setText(dat.getString(3));
            jtvColor.setText(dat.getString(4));
            jtvValor.setText(dat.getString(5));
        }
    }


    public void regresar (View v) {
        Intent IntBack = new Intent(this,VentaActivity.class);
        startActivity(IntBack);
    }
}