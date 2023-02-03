package com.ugb.miapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView temp;
    Button btn;
    Spinner spn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btnCalcular);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = (EditText)findViewById(R.id.txtNum1);
                double num1, num2, resp;
                num1 = Double.parseDouble(temp.getText().toString());

                temp = (EditText)findViewById(R.id.txtNum2);
                num2 = Double.parseDouble(temp.getText().toString());

                spn = (Spinner) findViewById(R.id.spnOpciones);
                String msg = "";
                switch (spn.getSelectedItemPosition()){
                    case 0://suma
                        resp = num1 + num2;
                        msg = "La suma es: "+ resp;
                        break;
                    case 1://REsta
                        resp = num1 - num2;
                        msg = "La resta es: "+ resp;
                        break;
                    case 2://Multi
                        resp = num1*num2;
                        msg="La Multiplicacion es: "+ resp;
                        break;
                    case 3://division
                        resp = num1/num2;
                        msg = "La division es: "+ resp;
                        break;
                    case 4://exponente
                        resp = Math.pow(num1, num2);
                        msg = "La exponenciacion es: "+ resp;
                        break;
                }
                temp = (TextView) findViewById(R.id.lblRespuesta);
                temp.setText(msg);
            }
        });
    }
}