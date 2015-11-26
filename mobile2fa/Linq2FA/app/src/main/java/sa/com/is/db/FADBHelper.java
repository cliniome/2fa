package sa.com.is.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by snouto on 26/11/15.
 */
public class FADBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Linqfa";
    public static final String PRIMARY_KEY = "ID";
    public static final String ACTIVATED = "ACTIVATED";
    public static final String BIN_PASSWD = "BINPASS";
    public static final String SEED_VALUE = "SEEDVAL";
    public static final String NUMBER_OF_SECONDS = "SECONDS_NUM";
    public static final String TABLE_NAME ="ACCOUNT";
    private static final String CREATE_DATABASE_TABLE = "create table "+ TABLE_NAME + " ( "
            +PRIMARY_KEY +" text primary key," + ACTIVATED + " int not null , " +
            BIN_PASSWD + " text not null," + SEED_VALUE + " text not null , "+
            NUMBER_OF_SECONDS + " int not null)";

    public String[] getAllColumns(){

        return new String[] {PRIMARY_KEY,ACTIVATED,BIN_PASSWD,SEED_VALUE,NUMBER_OF_SECONDS};
    }





    public FADBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_DATABASE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(newVersion > DATABASE_VERSION){

            db.execSQL("drop table if exists " + TABLE_NAME);
        }

        onCreate(db);
    }
}
