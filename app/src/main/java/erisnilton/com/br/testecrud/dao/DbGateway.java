package erisnilton.com.br.testecrud.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbGateway {

    private static DbGateway dbGateway;
    private SQLiteDatabase db;

    public DbGateway(Context context) {
        DbHelper helper = new DbHelper(context);
        db = helper.getWritableDatabase();
    }

    public static DbGateway getInstance(Context ctx){
        if(dbGateway == null)
            dbGateway = new DbGateway(ctx);
        return dbGateway;
    }

    public SQLiteDatabase getDatabase(){
        return this.db;
    }
}
