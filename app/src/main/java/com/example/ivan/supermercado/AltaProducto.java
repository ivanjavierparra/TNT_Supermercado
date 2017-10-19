package com.example.ivan.supermercado;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ivan.supermercado.dao.DaoMaster;
import com.example.ivan.supermercado.dao.DaoSession;
import com.example.ivan.supermercado.dao.Producto;
import com.example.ivan.supermercado.dao.ProductoDao;
import com.example.ivan.supermercado.dao.VentaDao;

import java.util.List;

import static com.example.ivan.supermercado.MainActivity.productoDao;


public class AltaProducto extends AppCompatActivity {
    private Button btnAceptar;
    private Button btnCancelar;

    private EditText txt_nombre;
    private EditText txt_codigo;
    private EditText txt_descripcion;
    private EditText txt_precio;
    private EditText txt_cantidad;
    private String mensaje;
    private ImageView img;
    private EditText txt_imagen;
    private Button btnImagen;

    //private ProductoDao productoDao;
    //private VentaDao ventaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_producto);

        /* recursos */
        btnAceptar = (Button) findViewById(R.id.btn_Aceptar);
        btnCancelar = (Button) findViewById(R.id.btn_Cancelar);

        txt_nombre = (EditText) findViewById(R.id.txt_nombre);
        txt_codigo = (EditText) findViewById(R.id.txt_Codigo);
        txt_descripcion = (EditText) findViewById(R.id.txt_Descripcion);
        txt_precio = (EditText) findViewById(R.id.txt_Precio);
        txt_cantidad = (EditText) findViewById(R.id.txt_Cantidad);

        txt_imagen = (EditText) findViewById(R.id.txt_Imagen);
        btnImagen = (Button) findViewById(R.id.btn_Imagen);
        img = (ImageView) findViewById(R.id.iv_imagen);

        /* si existe la bd entonces la creo, sino abro una sesion con ella */
        /*DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"mercadodb",null);
        SQLiteDatabase db = helper.getWritableDatabase(); // abro db para escritura
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        productoDao = daoSession.getProductoDao();
        ventaDao = daoSession.getVentaDao();
        System.out.println("llegue a abrir la bd...");*/

        btnAceptar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){

                Toast toast1;
                /* Creo el producto */

                try{

                    if (validarCampos()){
                        toast1 = Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_SHORT);
                        toast1.show();
                        return;
                    }


                    if(verificarExistencia()){
                        toast1 = Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_SHORT);
                        toast1.show();
                        return;
                    }
                    else{
                        Producto producto = new Producto();
                        producto.setNombre(txt_nombre.getText().toString());
                        producto.setCodigo(Integer.parseInt(txt_codigo.getText().toString()));
                        producto.setDescripcion(txt_descripcion.getText().toString());
                        producto.setPrecio(Float.parseFloat(txt_precio.getText().toString()));
                        producto.setCantidad(Integer.parseInt(txt_cantidad.getText().toString()));
                        Log.d(MainActivity.class.getName(),producto.getNombre() + " ... " + producto.getId() + " ... " + producto.getDescripcion());


                        productoDao.insertInTx(producto);

                        limpiarCampos();

                        toast1 = Toast.makeText(getApplicationContext(),"Has ingresado: " + " " + producto.getNombre(), Toast.LENGTH_SHORT);
                        toast1.show();


                    }





                }catch(Exception e){
                    toast1 = Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_SHORT);
                    toast1.show();

                }
                







            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(AltaProducto.this, MainActivity.class);
                startActivity(intent);

            }
        });

        btnImagen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                //Creamos el Intent para llamar a la Camara
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
                //https://www.youtube.com/watch?v=IMomzqwTuKA
            }
        });


    }

    protected void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        //Comprovamos que la foto se a realizado
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Creamos un bitmap con la imagen recientemente
            // almacenada en la memoria
            Bundle extras = data.getExtras();
            Bitmap bMap = (Bitmap) extras.get("data");
            //Añadimos el bitmap al imageView para
            // mostrarlo por pantalla
            img.setImageBitmap(bMap);


        }
    }



    private boolean verificarExistencia(){
        boolean bandera = false;
        List<Producto> productos = productoDao.loadAll();
        for(Producto prod : productos){
            if (prod.getNombre().compareToIgnoreCase(txt_nombre.getText().toString())==0){
                bandera = true;
                mensaje = "El nombre ya existe...";
                break;
            }
            else if (prod.getCodigo()==Integer.parseInt(txt_codigo.getText().toString())){
                bandera = true;
                mensaje = "El codigo ya existe...";
                break;
            }
        }
        return bandera;
    }

    private boolean validarCampos(){
        boolean bandera = false;
        if (txt_nombre.getText().toString().compareToIgnoreCase("")==0){
            mensaje = "Ingrese nombre...";
            bandera  = true;
        }
        else if (txt_codigo.getText().toString().compareToIgnoreCase("")==0){
            mensaje = "Ingrese codigo...";
            bandera  = true;
        }
        else if (txt_descripcion.getText().toString().compareToIgnoreCase("")==0){
            mensaje = "Ingrese descripcion...";
            bandera  = true;
        }
        else if (txt_precio.getText().toString().compareToIgnoreCase("")==0){
            mensaje = "Ingrese precio...";
            bandera  = true;
        }
        else if (txt_cantidad.getText().toString().compareToIgnoreCase("")==0){
            mensaje = "Ingrese cantidad...";
            bandera  = true;
        }
        else if (txt_precio.getText().toString().compareToIgnoreCase("")!=0){
            try{
               float precio = Float.parseFloat(txt_precio.getText().toString());
            }catch(Exception e){
                mensaje = "Ingrese precio valido...";
                bandera  = true;
            }

        }


        return bandera;
    }

    private void limpiarCampos(){
        txt_nombre.setText("");
        txt_codigo.setText("");
        txt_descripcion.setText("");
        txt_precio.setText("");
        txt_cantidad.setText("");
    }

}

