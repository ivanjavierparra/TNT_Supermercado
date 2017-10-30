package com.example.ivan.supermercado;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ivan.supermercado.dao.Producto;
import com.example.ivan.supermercado.dao.Venta;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.ivan.supermercado.MainActivity.productoDao;
import static com.example.ivan.supermercado.MainActivity.ventaDao;

public class AltaVenta extends AppCompatActivity {
    private Spinner cmbProductos;
    private Button btnCancelar;
    private Button btnAceptar;
    private Button btnBuscar;
    private EditText txtFecha;
    private EditText txtCantidad;
    private EditText txtMonto;
    private int dia,mes,ano;
    private float precioFinal;
    private float precio_txt_monto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_venta);

        cmbProductos = (Spinner) findViewById(R.id.cmb_productos);
        btnAceptar = (Button) findViewById(R.id.btn_aceptar);
        btnCancelar = (Button) findViewById(R.id.btn_cancelar);
        btnBuscar = (Button) findViewById(R.id.btn_buscar);
        txtFecha = (EditText) findViewById(R.id.txt_fecha);
        txtCantidad = (EditText) findViewById(R.id.txt_cantidad);
        txtMonto = (EditText) findViewById(R.id.txt_monto);



        /* Inicializo el combo de productos */
        ArrayList<String> valores = new ArrayList<>();
        //txtMonto.setText("$0.0");
        precioFinal = 0;

        List<Producto> productos = productoDao.loadAll();
        for(Producto prod : productos){
                valores.add(prod.getNombre());
                //Toast toast1 = Toast.makeText(getApplicationContext(),"PK: " + productoDao.getKey(prod), Toast.LENGTH_SHORT);
                //toast1.show();
                //break;
        }
        cmbProductos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores));





        btnAceptar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){

                Toast toast1;
                List<Producto> productos = productoDao.loadAll();
                if (productos.isEmpty()) {
                    toast1 = Toast.makeText(getApplicationContext(), "No hay productos en la db", Toast.LENGTH_SHORT);
                    toast1.show();
                    return;
                }
                else if (txtFecha.getText().toString().compareToIgnoreCase("")==0){
                    toast1 = Toast.makeText(getApplicationContext(), "Ingrese fecha...", Toast.LENGTH_SHORT);
                    toast1.show();
                    return;
                }
                else if (txtCantidad.getText().toString().compareToIgnoreCase("")==0){
                    toast1 = Toast.makeText(getApplicationContext(), "Ingrese cantidad...", Toast.LENGTH_SHORT);
                    toast1.show();
                    return;
                }



                String nombreProducto = cmbProductos.getSelectedItem().toString();
                long pk = 0;
                int stock = 0;
                int txtstock = Integer.parseInt(txtCantidad.getText().toString());
                float precio = 0;
                Producto pro = new Producto();









                for(Producto prod : productos){
                    if (prod.getNombre().equals(nombreProducto)){
                        pk = productoDao.getKey(prod);
                        stock = prod.getCantidad();
                        precio = prod.getPrecio();
                        pro = prod;
                        break;
                    }
                }

                if ((stock == 0) || (txtstock>stock)){
                    toast1 = Toast.makeText(getApplicationContext(),"Stock insuficiente...", Toast.LENGTH_SHORT);
                    toast1.show();
                }
                else{

                    Calendar myCal = Calendar.getInstance();
                    myCal.set(Calendar.YEAR, ano);
                    myCal.set(Calendar.MONTH, mes);
                    myCal.set(Calendar.DAY_OF_MONTH, dia);
                    Date fecha = myCal.getTime();

                    Venta venta = new Venta();

                    venta.setFecha_venta(fecha);

                    Integer i = (int) (long) pk;
                    venta.setId_producto(i);
                    venta.setCantidad(txtstock);
                    venta.setMonto_total(precio*txtstock);
                    ventaDao.insertInTx(venta);

                    pro.setCantidad(pro.getCantidad()-txtstock);
                    productoDao.updateInTx(pro);

                    precioFinal = precio*txtstock;
                    //txtMonto.setText(""+precioFinal);

                    AlertDialog.Builder alert = new AlertDialog.Builder(AltaVenta.this);
                    alert.setTitle("Venta Concretada!");
                    alert.setMessage("El monto total es: $" + precioFinal);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Your action here
                        }
                    });



                    alert.show();

                    toast1 = Toast.makeText(getApplicationContext(),"Transaccion finalizada con exito...", Toast.LENGTH_SHORT);
                    toast1.show();

                    limpiarPantalla();
                }






            }
        });



        btnCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(AltaVenta.this, MainActivity.class);
                startActivity(intent);

            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                ano = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AltaVenta.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtFecha.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        dia = dayOfMonth;
                        mes = month + 1;
                        ano = year;
                    }
                }
                ,dia,mes,ano);
                datePickerDialog.show();
            }
        });

        txtCantidad.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                List<Producto> productos = productoDao.loadAll();
                if (!productos.isEmpty()){
                    String nombreProducto = cmbProductos.getSelectedItem().toString();
                    for(Producto prod : productos){
                        if (prod.getNombre().equals(nombreProducto)){
                            precio_txt_monto = prod.getPrecio();
                            break;
                        }
                    }

                    //Toast toast1;
                    //toast1 = Toast.makeText(getApplicationContext(),"Precio de " + nombreProducto + " es: " + precio_txt_monto, Toast.LENGTH_SHORT);
                    //toast1.show();
                }


            }
        });

        txtCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    //Toast toast1;
                    //toast1 = Toast.makeText(getApplicationContext(),"Tengo el Foco!!!" + precio_txt_monto, Toast.LENGTH_SHORT);
                    //toast1.show();
                    List<Producto> productos = productoDao.loadAll();
                    if (!productos.isEmpty()){
                        String nombreProducto = cmbProductos.getSelectedItem().toString();
                        for(Producto prod : productos){
                            if (prod.getNombre().equals(nombreProducto)){
                                precio_txt_monto = prod.getPrecio();
                                break;
                            }
                        }
                    }
                }
            }
        });

        txtCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtCantidad.getText().toString().compareToIgnoreCase("")==0){
                    txtMonto.setText("");
                }else{
                    int number = Integer.parseInt(s.toString());
                    txtMonto.setText("$" + number * precio_txt_monto);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cmbProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                txtCantidad.setText("");
                txtMonto.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    private void limpiarPantalla(){
        cmbProductos.setSelection(0);
        txtFecha.setText("");
        txtCantidad.setText("");
        //txtMonto.setText("");

    }

}
