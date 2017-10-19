package com.example.ivan.supermercado;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ivan.supermercado.dao.Producto;
import com.example.ivan.supermercado.dao.Venta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.ivan.supermercado.MainActivity.productoDao;
import static com.example.ivan.supermercado.MainActivity.ventaDao;

public class ConsultarVenta extends AppCompatActivity {
    private Spinner cmbProductos;
    private Button btnFecha;
    private Button btnCancelar;
    private Button btnBuscar;
    private EditText txtFecha;
    private int dia,mes,ano;
    private List<Producto> productos;
    private ListView lv_ventas;
    private ArrayList lista_ventas;
    private ArrayAdapter adaptador;
    private String nombre_producto_venta;
    private ArrayList lista_mostrar;
    private CheckBox ckbProducto;
    private CheckBox ckbFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_venta);

        Log.i("Entre...", "estoy en ventas");



        cmbProductos = (Spinner) findViewById(R.id.cmb_productos);
        btnFecha = (Button) findViewById(R.id.btn_fecha);
        btnCancelar = (Button) findViewById(R.id.btn_cancelar);
        btnBuscar = (Button) findViewById(R.id.btn_buscar);
        txtFecha = (EditText) findViewById(R.id.txt_fecha);
        lv_ventas = (ListView) findViewById(R.id.lv_ventas);
        ckbProducto = (CheckBox) findViewById(R.id.ckb_productos);
        ckbFecha = (CheckBox) findViewById(R.id.ckb_fecha);

        inicializar_combo();
        lista_mostrar = new ArrayList();
        lista_ventas = new ArrayList();
        ckbProducto.setChecked(true);
        ckbFecha.setChecked(true);
        nombre_producto_venta = "";

        Log.i("Entre...", "termineeeee de inicializar.....");

        btnFecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                ano = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ConsultarVenta.this, new DatePickerDialog.OnDateSetListener() {
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

        btnCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(ConsultarVenta.this, MainActivity.class);
                startActivity(intent);

            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Toast toast1;
                boolean bandera = false;
                productos = productoDao.loadAll();

                if ((!ckbFecha.isChecked()) && (!ckbProducto.isChecked())){//no selecciono nada
                    toast1 = Toast.makeText(getApplicationContext(),"Los 2 campos esta vacios...", Toast.LENGTH_SHORT);
                    toast1.show();
                    return;
                }
                else{
                    //selecciono los dos filtros
                    if ((ckbFecha.isChecked()) && (ckbProducto.isChecked())){
                        if (fechaProductoValidos()){
                            bandera = buscarFechaProducto();
                        }
                        else{
                            return;
                        }
                    }
                    else if(ckbProducto.isChecked()){//selecciono solo producto
                        if(cmbProductos.getCount()==0){
                            toast1 = Toast.makeText(getApplicationContext(),"el combo esta vacio...", Toast.LENGTH_SHORT);
                            toast1.show();
                            return;
                        }
                        else{
                            bandera = buscarProducto();
                        }
                    }
                    else if(ckbFecha.isChecked()){
                        if ((txtFecha.getText().toString().compareToIgnoreCase("")==0)){
                            toast1 = Toast.makeText(getApplicationContext(),"el txt_fecha esta vacio...", Toast.LENGTH_SHORT);
                            toast1.show();
                            return;
                        }
                        else{
                            bandera = buscarFecha();
                        }
                    }
                }
                /*
                if ((cmbProductos.getCount()==0) && (txtFecha.getText().toString().compareToIgnoreCase("")==0)){
                    toast1 = Toast.makeText(getApplicationContext(),"Los 2 campos esta vacios...", Toast.LENGTH_SHORT);
                    toast1.show();
                }
                else if ((cmbProductos.getCount()!=0) && (txtFecha.getText().toString().compareToIgnoreCase("")!=0)){
                    //toast1 = Toast.makeText(getApplicationContext(),"Los 2 campos esta llenos...", Toast.LENGTH_SHORT);
                    //toast1.show();

                    //armar fecha en base a los datos que me ingreso
                    Calendar myCal = Calendar.getInstance();
                    myCal.set(Calendar.YEAR, ano);
                    myCal.set(Calendar.MONTH, mes);
                    myCal.set(Calendar.DAY_OF_MONTH, dia);
                    Date fecha = myCal.getTime();


                    //buscar producto
                    nombre_producto_venta = cmbProductos.getSelectedItem().toString();
                    long pk_producto = 0;
                    Date fecha_producto = new Date();
                    for(Producto prod : productos){
                        if (prod.getNombre().equals(nombre_producto_venta)){
                            pk_producto = productoDao.getKey(prod);
                            break;
                        }
                    }


                    //busco las ventas
                    int cantidad = 0;
                    lista_ventas = new ArrayList();
                    lista_mostrar = new ArrayList();
                    List<Venta> ventas = ventaDao.loadAll();
                    for(Venta ven : ventas){
                        if ((ven.getId_producto()==pk_producto) && (fecha.before(ven.getFecha_venta()))){

                            //aca voy guardado las ventas en una lista....
                            lista_ventas.add(ven);
                            lista_mostrar.add("Venta: " + ven.getId());
                            ArrayAdapter ad = new ArrayAdapter(ConsultarVenta.this,android.R.layout.simple_expandable_list_item_1,lista_mostrar);
                            lv_ventas.setAdapter(ad);
                            cantidad++;


                        }
                    }

                    if (!lista_ventas.isEmpty())bandera=true;




                    toast1 = Toast.makeText(getApplicationContext(),"cantidad: " + cantidad, Toast.LENGTH_LONG);
                    toast1.show();


                }
                else if (cmbProductos.getCount()!=0){

                    //buscar producto
                    nombre_producto_venta = cmbProductos.getSelectedItem().toString();
                    long pk_producto = 0;

                    for(Producto prod : productos){
                        if (prod.getNombre().equals(nombre_producto_venta)){
                            pk_producto = productoDao.getKey(prod);
                            break;
                        }
                    }


                    //Busco ventas
                    int cantidad = 0;
                    List<Venta> ventas = ventaDao.loadAll();
                    lista_ventas = new ArrayList();
                    lista_mostrar = new ArrayList();
                    for(Venta ven : ventas){
                        if ((ven.getId_producto()==pk_producto)){

                            //aca voy guardado las ventas en una lista....
                            lista_ventas.add(ven);
                            lista_mostrar.add("Venta: " + ven.getId());
                            ArrayAdapter ad = new ArrayAdapter(ConsultarVenta.this,android.R.layout.simple_expandable_list_item_1,lista_mostrar);
                            lv_ventas.setAdapter(ad);
                            cantidad++;


                        }
                    }

                    if (!lista_ventas.isEmpty())bandera=true;



                    toast1 = Toast.makeText(getApplicationContext(),"cantidad: " + cantidad, Toast.LENGTH_LONG);
                    toast1.show();
                }
                else{//entra aca si solo ingreso la fecha....

                    //armar fecha en base a los datos que me ingreso
                    Calendar myCal = Calendar.getInstance();
                    myCal.set(Calendar.YEAR, ano);
                    myCal.set(Calendar.MONTH, mes);
                    myCal.set(Calendar.DAY_OF_MONTH, dia);
                    Date fecha = myCal.getTime();

                    //Seteo producto
                    nombre_producto_venta = "";


                    //Busco ventas
                    int cantidad = 0;
                    List<Venta> ventas = ventaDao.loadAll();
                    lista_ventas = new ArrayList();
                    lista_mostrar = new ArrayList();
                    for(Venta ven : ventas){
                        if (fecha.before(ven.getFecha_venta())){

                            //aca voy guardado las ventas en una lista....
                            lista_ventas.add(ven);
                            lista_mostrar.add("Venta: " + ven.getId());
                            ArrayAdapter ad = new ArrayAdapter(ConsultarVenta.this,android.R.layout.simple_expandable_list_item_1,lista_mostrar);
                            cantidad++;


                        }
                    }

                    if (!lista_ventas.isEmpty())bandera=true;

                    toast1 = Toast.makeText(getApplicationContext(),"cantidad: " + cantidad, Toast.LENGTH_LONG);
                    toast1.show();
                }*/


                if (!bandera){
                    borrarLista();
                    toast1 = Toast.makeText(getApplicationContext(),"Nada que mostrar...", Toast.LENGTH_SHORT);
                    toast1.show();
                }

            }
        });

        //hacer el item
        lv_ventas.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Toast toast1;
                int cantidad = lista_ventas.size();
                toast1 = Toast.makeText(getApplicationContext(),"a ver...." + adapter.getItemIdAtPosition(position) + ", cantidad: " + cantidad, Toast.LENGTH_SHORT);
                toast1.show();

                //Intent intent = new Intent(Activity.this,destinationActivity.class);
                //based on item add info to intent
                //startActivity(intent);




                AlertDialog.Builder alert = new AlertDialog.Builder(ConsultarVenta.this);
                alert.setTitle(""+adapter.getItemAtPosition(position));
                int posicion = (int) (long) adapter.getItemIdAtPosition(position);
                Venta venta = (Venta) lista_ventas.get(posicion);
                if(nombre_producto_venta.compareToIgnoreCase("")==0)nombre_producto_venta = buscarNombreProducto(venta);
                String mensaje = "Venta: " + venta.getId() + "\n";
                mensaje += "Fecha: " + venta.getFecha_venta() + "\n";
                mensaje += "Producto: " + nombre_producto_venta + "\n";
                mensaje += "Cantidad: " + venta.getCantidad() + "\n";
                mensaje += "Monto: $" + venta.getMonto_total();
                alert.setMessage(mensaje);
                //
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Your action here
                    }
                });



                alert.show();
                nombre_producto_venta="";


            }
        });


        ckbProducto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                cmbProductos.setSelection(0);
                borrarLista();

            }
        });

        ckbFecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                txtFecha.setText("");
                borrarLista();
            }
        });

       /* El codigo siguiente tira error en los celulares pero no en las tables.....wtf??? */

        /*cmbProductos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                try{
                    cmbProductos.setSelection(0);
                    borrarLista();
                }
                catch(Exception e){
                    Log.i("Errorrrrrrrr... : ", e.getMessage());
                    //setOnItemClickListener
                }

            }
        });*/

        /*cmbProductos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                try{
                    if (cmbProductos.getCount()!=0){
                        cmbProductos.setSelection(0);
                        borrarLista();
                    }


                }
                catch(Exception e){
                    Log.i("Errorrrrrrrr... : ", e.getMessage());
                    //setOnItemClickListener
                }

            }
        });*/


    }


    private void inicializar_combo(){
        ArrayList<String> valores = new ArrayList<>();
        productos = productoDao.loadAll();
        for(Producto prod : productos){
            valores.add(prod.getNombre());
        }
        cmbProductos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores));
    }

    private void borrarLista(){
        lista_mostrar = new ArrayList();
        lista_ventas = new ArrayList();
        ArrayAdapter ad = new ArrayAdapter(ConsultarVenta.this,android.R.layout.simple_expandable_list_item_1,lista_mostrar);
        lv_ventas.setAdapter(ad);

    }

    private boolean fechaProductoValidos(){
        boolean flag = true;
        Toast toast1;
        if (cmbProductos.getCount()==0) {
            toast1 = Toast.makeText(getApplicationContext(),"No hay producto seleccionado...", Toast.LENGTH_SHORT);
            toast1.show();
            flag=false;
        }
        else if (txtFecha.getText().toString().compareToIgnoreCase("")==0){
            toast1 = Toast.makeText(getApplicationContext(),"No hay fecha seleccionada...", Toast.LENGTH_SHORT);
            toast1.show();
            flag=false;
        }
        return flag;
    }

    private boolean buscarFechaProducto(){
        boolean flag = false;

        //armar fecha en base a los datos que me ingreso
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, ano);
        myCal.set(Calendar.MONTH, mes);
        myCal.set(Calendar.DAY_OF_MONTH, dia);
        Date fecha = myCal.getTime();

        //buscar producto
        nombre_producto_venta = cmbProductos.getSelectedItem().toString();
        long pk_producto = 0;
        //Date fecha_producto = new Date();
        for(Producto prod : productos){
            if (prod.getNombre().equals(nombre_producto_venta)){
                pk_producto = productoDao.getKey(prod);
                break;
            }
        }


        //busco las ventas
        int cantidad = 0;
        lista_ventas = new ArrayList();
        lista_mostrar = new ArrayList();
        List<Venta> ventas = ventaDao.loadAll();
        for(Venta ven : ventas){
            if ((ven.getId_producto()==pk_producto) && (fecha.before(ven.getFecha_venta()))){

                //aca voy guardado las ventas en una lista....
                lista_ventas.add(ven);
                lista_mostrar.add("Venta: " + ven.getId());
                ArrayAdapter ad = new ArrayAdapter(ConsultarVenta.this,android.R.layout.simple_expandable_list_item_1,lista_mostrar);
                lv_ventas.setAdapter(ad);
                cantidad++;


            }
        }

        if (!lista_ventas.isEmpty())flag=true;


        return flag;
    }

    private boolean buscarProducto(){
        boolean flag = false;

        //buscar producto
        nombre_producto_venta = cmbProductos.getSelectedItem().toString();
        long pk_producto = 0;

        for(Producto prod : productos){
            if (prod.getNombre().equals(nombre_producto_venta)){
                pk_producto = productoDao.getKey(prod);
                break;
            }
        }


        //Busco ventas
        int cantidad = 0;
        List<Venta> ventas = ventaDao.loadAll();
        lista_ventas = new ArrayList();
        lista_mostrar = new ArrayList();
        for(Venta ven : ventas){
            if ((ven.getId_producto()==pk_producto)){

                //aca voy guardado las ventas en una lista....
                lista_ventas.add(ven);
                lista_mostrar.add("Venta: " + ven.getId());
                ArrayAdapter ad = new ArrayAdapter(ConsultarVenta.this,android.R.layout.simple_expandable_list_item_1,lista_mostrar);
                lv_ventas.setAdapter(ad);
                cantidad++;


            }
        }

        if (!lista_ventas.isEmpty())flag=true;

        return flag;
    }

    private boolean buscarFecha(){
        boolean flag = false;

        //armar fecha en base a los datos que me ingreso
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, ano);
        myCal.set(Calendar.MONTH, mes);
        myCal.set(Calendar.DAY_OF_MONTH, dia);
        Date fecha = myCal.getTime();

        //Seteo producto
        nombre_producto_venta = "";//ojo...


        //Busco ventas
        int cantidad = 0;
        List<Venta> ventas = ventaDao.loadAll();
        lista_ventas = new ArrayList();
        lista_mostrar = new ArrayList();
        for(Venta ven : ventas){
            if (fecha.before(ven.getFecha_venta())){

                //aca voy guardado las ventas en una lista....
                lista_ventas.add(ven);
                lista_mostrar.add("Venta: " + ven.getId());
                ArrayAdapter ad = new ArrayAdapter(ConsultarVenta.this,android.R.layout.simple_expandable_list_item_1,lista_mostrar);
                lv_ventas.setAdapter(ad);
                cantidad++;


            }
        }

        if (!lista_ventas.isEmpty())flag=true;

        return flag;
    }


    private String buscarNombreProducto(Venta venta){
        String nombre = "";
        //buscar producto



        for(Producto prod : productos){
            if (venta.getId_producto()==prod.getId()){
                nombre = prod.getNombre();
                break;
            }
        }

        return nombre;
    }

}
