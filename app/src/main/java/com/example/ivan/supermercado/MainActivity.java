package com.example.ivan.supermercado;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ivan.supermercado.dao.DaoMaster;
import com.example.ivan.supermercado.dao.DaoSession;
import com.example.ivan.supermercado.dao.Producto;
import com.example.ivan.supermercado.dao.ProductoDao;
import com.example.ivan.supermercado.dao.Venta;
import com.example.ivan.supermercado.dao.VentaDao;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnAltaProducto;
    private Button btnAltaVenta;
    private Button btnActualizarStock;
    private Button btnConsultarProducto;
    private Button btnConsultarVenta;

    public static ProductoDao productoDao;
    public static VentaDao ventaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* recursos */
        btnAltaProducto = (Button) findViewById(R.id.btn_AltaProducto);
        btnAltaVenta = (Button) findViewById(R.id.btn_AltaVenta);
        btnActualizarStock = (Button) findViewById(R.id.btn_ActualizarStock);
        btnConsultarProducto = (Button) findViewById(R.id.btn_ConsultarProducto);
        btnConsultarVenta = (Button) findViewById(R.id.btn_ConsultarVenta);

        /* si existe la bd entonces la creo, sino abro una sesion con ella */
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"mercadodb",null);
        SQLiteDatabase db = helper.getWritableDatabase(); // abro db para escritura
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        productoDao = daoSession.getProductoDao();
        ventaDao = daoSession.getVentaDao();

        /* insercion de objeto producto
        Producto producto = new Producto();
        producto.setNombre("Clavos");
        producto.setCodigo(100);
        producto.setDescripcion("Clavos lalala");
        producto.setPrecio(3);
        producto.setCantidad(41);

        productoDao.insertInTx(producto);
        setResult(RESULT_OK);
        finish();*/

        /* insercion de objeto venta
        Venta venta = new Venta();
        Date date = new Date();
        venta.setFecha_venta(date);
        venta.setId_producto(1);
        venta.setCantidad(2);
        venta.setMonto_total(6); */

        btnAltaProducto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                //Intent para pasar datos de una pantallita a la otra
                //Intent intent = new Intent(MainActivity.this, Saludo.class);
                //Bundle bundle = new Bundle();
                //bundle.putString("Nombre",txtNombre.getText().toString() );
                //intent.putExtras(bundle);
                //startActivity(intent);
                /* insercion de objeto producto */

                /*
                Producto producto = new Producto();
                producto.setNombre("Tuercas");
                producto.setCodigo(300);
                producto.setDescripcion("Tuercas lalala");
                producto.setPrecio(7);
                producto.setCantidad(77);
                */

                /*
                productoDao.insertInTx(producto);
                //setResult(RESULT_OK);
                //finish();

                /* Consulto a la BD
                List<Producto> productos = productoDao.loadAll();
                for(Producto prod : productos){
                    Log.d(MainActivity.class.getName(),prod.getNombre() + " ... " + prod.getId() + " ... " + prod.getDescripcion());
                }

                */

                Intent intent = new Intent(MainActivity.this, AltaProducto.class);
                //Bundle bundle = new Bundle();
                //bundle.putString("Producto", productoDao );
                //intent.putExtras(bundle);
                startActivity(intent);

            }
        });


        btnAltaVenta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(MainActivity.this, AltaVenta.class);
                startActivity(intent);
            }
        });


        btnActualizarStock.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(MainActivity.this, ActualizarStock.class);
                startActivity(intent);

            }
        });

        btnConsultarProducto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(MainActivity.this, ConsultarProducto.class);
                startActivity(intent);

            }
        });

        btnConsultarVenta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                try{
                    Intent intent = new Intent(MainActivity.this, ConsultarVenta.class);
                    startActivity(intent);
                    Log.i("Entre...", "holaaaaaaaaaaaaaaaaaaaaaa ");

                }catch(Exception e){
                    Log.i("No Entreeeee...", e.getLocalizedMessage());
                    /*AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Hubo un Problema !");
                    alert.setMessage("Detalles: " + e.getMessage());

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Your action here
                        }
                    });



                    alert.show();*/
                }




            }
        });



    }


}
