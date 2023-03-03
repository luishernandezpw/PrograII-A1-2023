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

        tbh.addTab(tbh.newTabSpec("AREA").setContent(R.id.tbLongitud).setIndicator("AREA"));
        tbh.addTab(tbh.newTabSpec("CAJERO").setContent(R.id.tbCajero).setIndicator("CAJERO"));

        btn=findViewById(R.id.btnRetirar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    temp = findViewById(R.id.txtCantidadCajero);
                    double cantidad = Double.parseDouble(temp.getText().toString());

                    double j = 0;
                    double denominaciones[] = {100, 50, 20, 10, 5, 1, 0.5, 0.25, 0.1, 0.05, 0.01};
                    String resp = "";
                    for (int i = 0; i < denominaciones.length; i++) {
                        while (cantidad/100*100 >= denominaciones[i]) {
                            cantidad = cantidad/100*100 - denominaciones[i];
                            j++;
                        }
                        if (j > 0) {
                            if (denominaciones[i] > 1) {
                                resp += j + " billetes de " + denominaciones[i] + "\n";
                            } else {
                                resp += j + " monedas de " + denominaciones[i] + "\n";
                            }
                        }
                        j = 0;
                    }
                    temp = findViewById(R.id.lblRespuestaCajero);
                    temp.setText(resp);
                }catch (Exception e){
                    temp = findViewById(R.id.lblRespuestaCajero);
                    temp.setText("Error: "+ e.getMessage());
                }
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