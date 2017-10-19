package com.example.ivan.supermercado.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.example.ivan.supermercado.dao.Producto;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PRODUCTO.
*/
public class ProductoDao extends AbstractDao<Producto, Long> {

    public static final String TABLENAME = "PRODUCTO";

    /**
     * Properties of entity Producto.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Nombre = new Property(1, String.class, "nombre", false, "NOMBRE");
        public final static Property Codigo = new Property(2, int.class, "codigo", false, "CODIGO");
        public final static Property Descripcion = new Property(3, String.class, "descripcion", false, "DESCRIPCION");
        public final static Property Imagen = new Property(4, byte[].class, "imagen", false, "IMAGEN");
        public final static Property Precio = new Property(5, float.class, "precio", false, "PRECIO");
        public final static Property Cantidad = new Property(6, int.class, "cantidad", false, "CANTIDAD");
    };


    public ProductoDao(DaoConfig config) {
        super(config);
    }
    
    public ProductoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PRODUCTO' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'NOMBRE' TEXT NOT NULL ," + // 1: nombre
                "'CODIGO' INTEGER NOT NULL ," + // 2: codigo
                "'DESCRIPCION' TEXT NOT NULL ," + // 3: descripcion
                "'IMAGEN' BLOB," + // 4: imagen
                "'PRECIO' REAL NOT NULL ," + // 5: precio
                "'CANTIDAD' INTEGER NOT NULL );"); // 6: cantidad
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PRODUCTO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Producto entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getNombre());
        stmt.bindLong(3, entity.getCodigo());
        stmt.bindString(4, entity.getDescripcion());
 
        byte[] imagen = entity.getImagen();
        if (imagen != null) {
            stmt.bindBlob(5, imagen);
        }
        stmt.bindDouble(6, entity.getPrecio());
        stmt.bindLong(7, entity.getCantidad());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Producto readEntity(Cursor cursor, int offset) {
        Producto entity = new Producto( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // nombre
            cursor.getInt(offset + 2), // codigo
            cursor.getString(offset + 3), // descripcion
            cursor.isNull(offset + 4) ? null : cursor.getBlob(offset + 4), // imagen
            cursor.getFloat(offset + 5), // precio
            cursor.getInt(offset + 6) // cantidad
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Producto entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNombre(cursor.getString(offset + 1));
        entity.setCodigo(cursor.getInt(offset + 2));
        entity.setDescripcion(cursor.getString(offset + 3));
        entity.setImagen(cursor.isNull(offset + 4) ? null : cursor.getBlob(offset + 4));
        entity.setPrecio(cursor.getFloat(offset + 5));
        entity.setCantidad(cursor.getInt(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Producto entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Producto entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}