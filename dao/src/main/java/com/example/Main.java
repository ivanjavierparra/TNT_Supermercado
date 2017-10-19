package com.example;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Main {


    public static void main(String [] args) throws Exception {
        Schema schema = new Schema(2,"com.example.ivan.supermercado.dao");
        schema.enableKeepSectionsByDefault(); //permite modificar codigo que me da por defecto
        createDatabase(schema);
        DaoGenerator generator = new DaoGenerator(); // puede lanzar IOException
        generator.generateAll(schema,args[0]); // puede lanzar Exception

    }

    /* aqui modelo mi base  de datos */
    private static void createDatabase(Schema schema){
        /* creo entidad Producto */
        Entity producto = schema.addEntity("Producto");
        producto.addIdProperty().autoincrement();
        producto.addStringProperty("nombre").notNull();
        producto.addIntProperty("codigo").notNull();
        producto.addStringProperty("descripcion").notNull();
        producto.addByteArrayProperty("imagen");
        producto.addFloatProperty("precio").notNull();
        producto.addIntProperty("cantidad").notNull();

        /* creo entidad Venta */
        Entity venta = schema.addEntity("Venta");
        venta.addIdProperty().autoincrement();
        venta.addDateProperty("fecha_venta").notNull();
        venta.addIntProperty("id_producto").notNull();
        venta.addIntProperty("cantidad").notNull();
        venta.addFloatProperty("monto_total").notNull();


    }

}
