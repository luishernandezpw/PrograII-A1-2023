package com.ugb.miapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class lista_amigos extends AppCompatActivity {
    Bundle parametros = new Bundle();
    DB db_agenda;
    ListView lts;
    Cursor cAMigos;
    FloatingActionButton btn;
    final ArrayList<String> alAmigos = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_amigos);

        obtenerDatosAmigos();
        btn = findViewById(R.id.btnAgregarAmigos);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parametros.putString("accion", "nuevo");
                abrirAgregarAmigos(parametros);
            }
        });
    }
    public void abrirAgregarAmigos(Bundle parametros){
        Intent iAgregarAmigos = new Intent(lista_amigos.this, MainActivity.class);
        iAgregarAmigos.putExtras(parametros);
        startActivity(iAgregarAmigos);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        cAMigos.moveToPosition(info.position);
        menu.setHeaderTitle(cAMigos.getString(1)); //1=> Nombre del amigo...
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try{
            switch (item.getItemId()){
                case R.id.mnxAgregar:
                    parametros.putString("accion", "nuevo");
                    abrirAgregarAmigos(parametros);
                    return true;
                case R.id.mnxModificar:
                    String amigos[] = {
                            cAMigos.getString(0), //idAmigo
                            cAMigos.getString(1), //nombre
                            cAMigos.getString(2), //direccion
                            cAMigos.getString(3), //telefono
                            cAMigos.getString(4), //email
                    };
                    parametros.putString("accion", "modificar");
                    parametros.putStringArray("amigos", amigos);
                    abrirAgregarAmigos(parametros);
                    return true;
                case R.id.mnxEliminar:
                    eliminarAmigos();
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }catch (Exception e){
            return super.onContextItemSelected(item);
        }
    }
    void eliminarAmigos(){
        AlertDialog.Builder confirmacion =  new AlertDialog.Builder(lista_amigos.this);
        confirmacion.setTitle("Esta seguro de eliminar a?: ");
        confirmacion.setMessage(cAMigos.getString(1)); //nombre
        confirmacion.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db_agenda.administrar_agenda(cAMigos.getString(0), "", "", "","",
                        "eliminar");
                obtenerDatosAmigos();
                dialogInterface.dismiss();//cerrar la cuadro de dialogo
            }
        });
        confirmacion.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        confirmacion.create().show();
    }
    public void obtenerDatosAmigos(){
        try {
            alAmigos.clear();
            db_agenda = new DB(lista_amigos.this, "", null, 1);
            cAMigos = db_agenda.consultar_agenda();
            if(cAMigos.moveToFirst()){
                lts = findViewById(R.id.ltsAmigos);
                final ArrayAdapter<String> adAmigos = new ArrayAdapter<String>(lista_amigos.this,
                        android.R.layout.simple_expandable_list_item_1, alAmigos);
                lts.setAdapter(adAmigos);
                do{
                    alAmigos.add(cAMigos.getString(1));//1 es el nombre del amigo, pues 0 es el idAmigo.
                }while(cAMigos.moveToNext());
                adAmigos.notifyDataSetChanged();
                registerForContextMenu(lts);
            }else{
                Toast.makeText(this, "NO HAY datos que mostrar", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "Error al obtener amigos: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}