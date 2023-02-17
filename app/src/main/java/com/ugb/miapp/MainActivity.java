package com.ugb.miapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TabHost tbh;
    TextView temp;
    Spinner spn;
    Button btn;
    conversores miConversor= new conversores();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbh = findViewById(R.id.tbhConversores);
        tbh.setup();

        tbh.addTab(tbh.newTabSpec("longitud").setContent(R.id.tbLongitud).setIndicator("", getDrawable(R.drawable.longitud)));
        tbh.addTab(tbh.newTabSpec("monedas").setContent(R.id.tbMonedas).setIndicator("", getDrawable(R.drawable.money)));
        tbh.addTab(tbh.newTabSpec("masa").setContent(R.id.tbMasa).setIndicator("", getDrawable(R.drawable.masa)));

        btn=findViewById(R.id.btnConvertir);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spn = findViewById(R.id.spnDeMonedas);
                int de = spn.getSelectedItemPosition();

                spn = findViewById(R.id.spnAMonedas);
                int a = spn.getSelectedItemPosition();

                temp = (TextView) findViewById(R.id.txtCantidad);
                double cantidad = Double.parseDouble(temp.getText().toString());

                double respuesta = miConversor.convertir(0, de, a, cantidad);
                temp = findViewById(R.id.lblRespuesta);
                temp.setText("Respuesta: "+ respuesta);
            }
        });
    }
}
class conversores{
    double[][] valores = {
            {1, 7.84, 24.66, 36.56, 580.23, 8.75, 0.94, 131.33, 82.54},//moendas
            {}, //Longitudes
            {}, //Peso
            {}, //almacenamiento
            {}, //transferencia de datos (internet)
    };
    public double convertir(int opcion, int de, int a, double cantidad){
        return valores[opcion][a]/ valores[opcion][de] * cantidad;
    }
}