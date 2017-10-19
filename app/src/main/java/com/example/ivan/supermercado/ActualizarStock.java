package com.example.ivan.supermercado;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ivan.supermercado.dao.Producto;

import java.util.ArrayList;
import java.util.List;

import static com.example.ivan.supermercado.MainActivity.productoDao;

public class ActualizarStock extends AppCompatActivity {
    private Spinner cmbProductos;
    private Button btnCancelar;
    private Button btnAceptar;
    private EditText txtCantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_stock);

        cmbProductos = (Spinner) findViewById(R.id.cmb_productos);
        btnAceptar = (Button) findViewById(R.id.btn_aceptar);
        btnCancelar = (Button) findViewById(R.id.btn_cancelar);
        txtCantidad = (EditText) findViewById(R.id.txt_cantidad);

        /* Inicializo el combo de productos */
        ArrayList<String> valores = new ArrayList<>();
        List<Producto> productos = productoDao.loadAll();
        for(Producto prod : productos){
            valores.add(prod.getNombre());
        }
        cmbProductos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores));




        btnAceptar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Toast toast1;
                int cantidad = 0;
                if(txtCantidad.getText().toString().compareToIgnoreCase("")==0){
                    toast1 = Toast.makeText(getApplicationContext(),"Ingrese cantidad...", Toast.LENGTH_SHORT);
                    toast1.show();
                    return;
                }
                else{
                    cantidad = Integer.parseInt(txtCantidad.getText().toString());
                }


                List<Producto> productos = productoDao.loadAll();
                if (cantidad<=0){
                    toast1 = Toast.makeText(getApplicationContext(),"Ingrese cantidad positiva...", Toast.LENGTH_SHORT);
                    toast1.show();
                }
                else if (productos.isEmpty()){
                    toast1 = Toast.makeText(getApplicationContext(),"No hay productos en la db", Toast.LENGTH_SHORT);
                    toast1.show();


                }
                else{
                    String nombreProducto = cmbProductos.getSelectedItem().toString();
                    Producto p = new Producto();
                    long pk = 0;
                    int stockactual = 0;


                    /* Busco en db el producto seleccionado*/
                    //List<Producto> productos = productoDao.loadAll();
                    for(Producto prod : productos){
                        if (prod.getNombre().equals(nombreProducto)){
                            p = prod;
                            stockactual = prod.getCantidad();
                            break;
                        }
                    }


                    /* Actualizo el producto en la db */
                    stockactual = stockactual +  Integer.parseInt(txtCantidad.getText().toString());
                    p.setCantidad(stockactual);
                    productoDao.updateInTx(p);



                    /* Me fijo si salio todo bien */
                    List<Producto> consulta_productos = productoDao.loadAll();
                    int cantidad_apariciones = 0;
                    int cantidad_ingresada = 0;
                    for(Producto prod : consulta_productos){
                        if (prod.getNombre().equals(p.getNombre())){
                            cantidad_apariciones++;
                            cantidad_ingresada = prod.getCantidad();
                        }
                    }

                    //limpio pantalla
                    cmbProductos.setSelection(0);
                    txtCantidad.setText("");

                    toast1 = Toast.makeText(getApplicationContext(),"Cantidad de stock: " + cantidad_ingresada, Toast.LENGTH_SHORT);
                    toast1.show();
                }



            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(ActualizarStock.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
