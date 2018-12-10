package erisnilton.com.br.testecrud.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import erisnilton.com.br.testecrud.model.Cliente;

public class ClienteDao {

    private final String TABLE_CLIENTES = "Clientes";
    private DbGateway dbGateway;

    public ClienteDao(Context context) {
        dbGateway = DbGateway.getInstance(context);
    }

    public Cliente getUltimoCliente(){
        Cursor cursor = dbGateway.getDatabase().rawQuery("SELECT * FROM Clientes ORDER BY ID DESC",null);

        if(cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            String sexo = cursor.getString(cursor.getColumnIndex("Sexo"));
            String uf = cursor.getString(cursor.getColumnIndex("UF"));
            boolean vip = cursor.getInt(cursor.getColumnIndex("Vip")) > 0;
            return  new Cliente(id,nome,sexo,uf,vip);
        }
        return null;
    }

    public List<Cliente> getTodos(){
        List<Cliente> clientes = new ArrayList<>();
        Cursor cursor =  dbGateway.getDatabase().rawQuery("SELECT * FROM Clientes",null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            String sexo = cursor.getString(cursor.getColumnIndex("Sexo"));
            String uf = cursor.getString(cursor.getColumnIndex("UF"));
            boolean vip = cursor.getInt(cursor.getColumnIndex("Vip")) > 0;

            clientes.add(new Cliente(id,nome,sexo,uf,vip));

        }
        cursor.close();
        return clientes;
    }


    public boolean salvar(String nome, String sexo, String uf, boolean vip){
        return salvar(0,nome,sexo,uf,vip);
    }

    public  boolean salvar(int id, String nome, String sexo, String uf, boolean vip) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Nome",nome);
        contentValues.put("Sexo", sexo);
        contentValues.put("UF", uf);
        contentValues.put("Vip", vip ? 1 : 0);

        if(id > 0 ){
            return dbGateway.getDatabase().update(TABLE_CLIENTES,contentValues,"ID=?",new String[]{ id + "" }) > 0;
        } else {
            return dbGateway.getDatabase().insert(TABLE_CLIENTES,null,contentValues) > 0;
        }

    }

    public  boolean remover(int id){
        return dbGateway.getDatabase().delete(TABLE_CLIENTES,"ID=?",new String[]{ id + "" }) > 0;

    }
}
