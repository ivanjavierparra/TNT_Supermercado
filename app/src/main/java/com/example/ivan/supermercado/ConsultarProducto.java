package com.example.ivan.supermercado;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ivan.supermercado.dao.Producto;
import com.example.ivan.supermercado.dao.ProductoDao;

import java.util.ArrayList;
import java.util.List;

import static com.example.ivan.supermercado.MainActivity.productoDao;

public class ConsultarProducto extends AppCompatActivity {
    private Button btnBuscar;
    private Button btnCancelar;
    private EditText txtNombre;
    private EditText txtCodigo;
    private ListView lv;
    private ArrayList lista_mostrar;
    private ArrayAdapter adaptador;
    private CheckBox ckbNombre;
    private CheckBox ckbCodigo;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_producto);

        btnBuscar = (Button) findViewById(R.id.btn_buscar);
        btnCancelar = (Button) findViewById(R.id.btn_cancelar);
        txtNombre = (EditText) findViewById(R.id.txt_nombre);
        txtCodigo = (EditText) findViewById(R.id.txt_codigo);
        lv = (ListView)findViewById(R.id.list_productos);
        ckbNombre = (CheckBox) findViewById(R.id.ckb_nombre);
        ckbCodigo = (CheckBox) findViewById(R.id.ckb_codigo);
        img = (ImageView) findViewById(R.id.iv_imagen);
        //adaptador = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,lista_mostrar);
        //lv.setAdapter(adaptador);

        ckbNombre.setChecked(true);
        ckbCodigo.setChecked(true);
        img.setVisibility(View.INVISIBLE);
        //img.setImageResource(R.drawable.ic_menu_camera);
        //https://stackoverflow.com/questions/13397709/android-hide-imageview
        lista_mostrar = new ArrayList();

        btnBuscar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                boolean bandera = false;
                /* busco todos los productos */
                List<Producto> productos = productoDao.loadAll();
                Toast toast1;
                if (productos.isEmpty()){
                    toast1 = Toast.makeText(getApplicationContext(),"No hay productos en la db", Toast.LENGTH_SHORT);
                    toast1.show();
                }
                else{
                    //toast1 = Toast.makeText(getApplicationContext(),"Hay productos en la db", Toast.LENGTH_SHORT);
                    //toast1.show();
                    if ((!ckbNombre.isChecked()) && (!ckbCodigo.isChecked())){
                        toast1 = Toast.makeText(getApplicationContext(),"Seleccione un parametro de busqueda.", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                    else if ((ckbNombre.isChecked()) && (ckbCodigo.isChecked())){
                        if(validarNombreyCodigo()){
                            bandera = obtenerNombreyCodigo(productos);
                            toast1 = Toast.makeText(getApplicationContext(),"Los 2 seleccionados", Toast.LENGTH_SHORT);
                            toast1.show();
                        }

                    }
                    else if (ckbNombre.isChecked()){
                        if (txtNombre.getText().toString().compareToIgnoreCase("")==0){
                            toast1 = Toast.makeText(getApplicationContext(),"Ingrese Nombre...", Toast.LENGTH_SHORT);
                            toast1.show();
                        }
                        else{
                            bandera = obtenerNombre(productos);
                        }
                    }
                    else if (ckbCodigo.isChecked()){
                        if(txtCodigo.getText().toString().compareToIgnoreCase("")==0){
                            toast1 = Toast.makeText(getApplicationContext(),"Ingrese Codigo...", Toast.LENGTH_SHORT);
                            toast1.show();
                        }
                        else{
                            bandera = obtenerCodigo(productos);
                        }
                    }









                    /*else if ((txtNombre.getText().toString().equals("")) && (txtCodigo.getText().toString().equals(""))){
                        toast1 = Toast.makeText(getApplicationContext(),"Ingrese algo...", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                    else if (!(txtNombre.getText().toString().equals("")) && (!(txtCodigo.getText().toString().equals("")))){
                        toast1 = Toast.makeText(getApplicationContext(),"Ingreso los 2...", Toast.LENGTH_SHORT);
                        toast1.show();
                        bandera = obtenerNombreyCodigo(productos);

                    }
                    else if (!(txtNombre.getText().toString().equals(""))){
                        toast1 = Toast.makeText(getApplicationContext(),"Ingreso el nombre...", Toast.LENGTH_SHORT);
                        toast1.show();
                        bandera = obtenerNombre(productos);

                    }
                    else{
                        toast1 = Toast.makeText(getApplicationContext(),"Ingreso el codigo...", Toast.LENGTH_SHORT);
                        toast1.show();
                        bandera = obtenerCodigo(productos);

                    }/*
                    /*for(Producto prod : productos){
                        if (prod.getNombre().equals(txtNombre.getText().toString())){

                            lista_mostrar = new ArrayList();
                            lista_mostrar.add(prod.getNombre());
                            lista_mostrar.add(prod.getCodigo());
                            lista_mostrar.add(prod.getDescripcion());
                            lista_mostrar.add(prod.getCantidad());
                            lista_mostrar.add(prod.getPrecio());

                            ArrayAdapter ad = new ArrayAdapter(ConsultarProducto.this,android.R.layout.simple_expandable_list_item_1,lista_mostrar);
                            lv.setAdapter(ad);

                            bandera = true;
                            break;
                        }
                    }*/
                }

                if (!bandera){
                    borrarLista();

                    toast1 = Toast.makeText(getApplicationContext(),"Nada que mostrar...", Toast.LENGTH_SHORT);
                    toast1.show();
                }

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(ConsultarProducto.this, MainActivity.class);
                startActivity(intent);

            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Toast toast1;
                toast1 = Toast.makeText(getApplicationContext(),"a ver...." + adapter.getItemAtPosition(position), Toast.LENGTH_SHORT);
                toast1.show();

                //Intent intent = new Intent(Activity.this,destinationActivity.class);
                //based on item add info to intent
                //startActivity(intent);

                AlertDialog.Builder alert = new AlertDialog.Builder(ConsultarProducto.this);
                alert.setTitle("Has tocado: " + adapter.getItemAtPosition(position));
                // alert.setMessage("Message");

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Your action here
                    }
                });

                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });

                alert.show();


            }
        });

        ckbNombre.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                txtNombre.setText("");
                borrarLista();

            }
        });

        ckbCodigo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                txtCodigo.setText("");
                borrarLista();

            }
        });

    }


    private void borrarLista(){
        lista_mostrar = new ArrayList();
        ArrayAdapter ad = new ArrayAdapter(ConsultarProducto.this,android.R.layout.simple_expandable_list_item_1,lista_mostrar);
        lv.setAdapter(ad);
        img.setVisibility(View.INVISIBLE);
    }


    private boolean obtenerNombreyCodigo(List<Producto> productos){
        boolean bandera = false;

        int codigo = Integer.parseInt(txtCodigo.getText().toString());
        for(Producto prod : productos){
            if ((prod.getNombre().compareToIgnoreCase(txtNombre.getText().toString())==0) && (prod.getCodigo()==codigo)){

                lista_mostrar = new ArrayList();
                lista_mostrar.add("Nombre: " + prod.getNombre());
                lista_mostrar.add("Codigo: " + prod.getCodigo());
                lista_mostrar.add("Descripcion: " + prod.getDescripcion());
                lista_mostrar.add("Cantidad: " + prod.getCantidad());
                lista_mostrar.add("Precio: $" + prod.getPrecio());
                if (prod.getImagen() != null){
                    //Pasando byte array a bitmap
                    byte[] bitmapdata = prod.getImagen(); // let this be your byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata .length);
                    img.setImageBitmap(bitmap);
                    img.setVisibility(View.VISIBLE);
                }
                ArrayAdapter ad = new ArrayAdapter(ConsultarProducto.this,android.R.layout.simple_expandable_list_item_1,lista_mostrar);
                lv.setAdapter(ad);

                bandera = true;
                break;
            }
        }

        return bandera;
    }

    private boolean obtenerNombre(List<Producto> productos){
        boolean bandera = false;


        for(Producto prod : productos){
            if (prod.getNombre().compareToIgnoreCase(txtNombre.getText().toString())==0){

                lista_mostrar = new ArrayList();
                lista_mostrar.add("Nombre: " + prod.getNombre());
                lista_mostrar.add("Codigo: " + prod.getCodigo());
                lista_mostrar.add("Descripcion: " + prod.getDescripcion());
                lista_mostrar.add("Cantidad: " + prod.getCantidad());
                lista_mostrar.add("Precio: $" + prod.getPrecio());
                if (prod.getImagen() != null){
                    //Pasando byte array a bitmap
                    byte[] bitmapdata = prod.getImagen(); // let this be your byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata .length);
                    img.setImageBitmap(bitmap);
                    img.setVisibility(View.VISIBLE);
                }
                ArrayAdapter ad = new ArrayAdapter(ConsultarProducto.this,android.R.layout.simple_expandable_list_item_1,lista_mostrar);
                lv.setAdapter(ad);

                bandera = true;
                break;
            }
        }

        return bandera;
    }

    private boolean obtenerCodigo(List<Producto> productos){
        boolean bandera = false;


        int codigo = Integer.parseInt(txtCodigo.getText().toString());
        for(Producto prod : productos){
            if ((prod.getCodigo()==codigo)){

                lista_mostrar = new ArrayList();
                lista_mostrar.add("Nombre: " + prod.getNombre());
                lista_mostrar.add("Codigo: " + prod.getCodigo());
                lista_mostrar.add("Descripcion: " + prod.getDescripcion());
                lista_mostrar.add("Cantidad: " + prod.getCantidad());
                lista_mostrar.add("Precio: $" + prod.getPrecio());
                if (prod.getImagen() != null){
                    //Pasando byte array a bitmap
                    byte[] bitmapdata = prod.getImagen(); // let this be your byte array
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata .length);
                    img.setImageBitmap(bitmap);
                    img.setVisibility(View.VISIBLE);
                }
                ArrayAdapter ad = new ArrayAdapter(ConsultarProducto.this,android.R.layout.simple_expandable_list_item_1,lista_mostrar);
                lv.setAdapter(ad);

                bandera = true;
                break;
            }
        }

        return bandera;
    }

    private boolean validarNombreyCodigo(){
        boolean flag = true;
        Toast toast1;
        if (txtNombre.getText().toString().compareToIgnoreCase("") == 0){
            flag = false;
            toast1 = Toast.makeText(getApplicationContext(),"Ingrese Nombre...", Toast.LENGTH_SHORT);
            toast1.show();
        }
        else if (txtCodigo.getText().toString().compareToIgnoreCase("") == 0){
            flag = false;
            toast1 = Toast.makeText(getApplicationContext(),"Ingrese Codigo...", Toast.LENGTH_SHORT);
            toast1.show();
        }
        return flag;
    }

}




