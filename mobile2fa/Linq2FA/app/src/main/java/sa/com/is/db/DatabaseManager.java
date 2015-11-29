package sa.com.is.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.UUID;

import sa.com.is.models.EnvelopedData;

/**
 * Created by snouto on 26/11/15.
 */
public class DatabaseManager {

    private FADBHelper dbHelper;

    public DatabaseManager(Context context){


        dbHelper = new FADBHelper(context,FADBHelper.DATABASE_NAME,null,FADBHelper.DATABASE_VERSION);
    }


    public boolean createAccount(EnvelopedData data)
    {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try
        {
            ContentValues cv = new ContentValues();
            cv.put(FADBHelper.PRIMARY_KEY,UUID.randomUUID().toString());
            cv.put(FADBHelper.ACTIVATED,1);
            cv.put(FADBHelper.NUM_OF_DIGITS,data.getNumDigits());
            cv.put(FADBHelper.ACCOUNT_NAME,data.getAccountName());
            cv.put(FADBHelper.BIN_PASSWD,data.getBinPassword());
            cv.put(FADBHelper.NUMBER_OF_SECONDS,data.getSeconds());
            cv.put(FADBHelper.SEED_VALUE, data.getSeed());

           long rows =  db.insert(FADBHelper.TABLE_NAME,null,cv);

            if(rows > 0)
                return true;
            else return false;




        }catch (Exception s)
        {
            s.printStackTrace();
            return false;
        }
        finally {

            db.close();
        }
    }

    public boolean isAccountActivated(){

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try
        {

            String[] AllColumns = dbHelper.getAllColumns();
            String WhereClause = FADBHelper.ACTIVATED + "=" + 1;
            String[] whereArgs = null;
            String groupBy = null;
            String having = null;
            String order = null;

            Cursor cursor = db.query(FADBHelper.TABLE_NAME, AllColumns, WhereClause, whereArgs, groupBy, having, order);

            return cursor.moveToNext();



        }catch (Exception s)
        {
            s.printStackTrace();
            return false;
        }

        finally {

            db.close();
        }
    }


    public boolean verifyPinPassword(String password)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try
        {

            String[] AllColumns = dbHelper.getAllColumns();
            String WhereClause = FADBHelper.ACTIVATED + "=" + 1 + " AND " + FADBHelper.BIN_PASSWD + "='" + password + "'";
            String[] whereArgs = null;
            String groupBy = null;
            String having = null;
            String order = null;

            Cursor cursor = db.query(FADBHelper.TABLE_NAME, AllColumns, WhereClause, whereArgs, groupBy, having, order);

            return cursor.moveToNext();



        }catch (Exception s)
        {
            s.printStackTrace();
            return false;
        }

        finally {

            db.close();
        }
    }


    public EnvelopedData getActiveAccount()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        try
        {
            String[] AllColumns = dbHelper.getAllColumns();
            String WhereClause = FADBHelper.ACTIVATED + "=" + 1;
            String[] whereArgs = null;
            String groupBy = null;
            String having = null;
            String order = null;
            Cursor cursor = db.query(FADBHelper.TABLE_NAME, AllColumns, WhereClause, whereArgs, groupBy, having, order);

            EnvelopedData envelopedData = new EnvelopedData();

            while(cursor.moveToNext())
            {
                //Primary Key
                int columnIndex = cursor.getColumnIndex(FADBHelper.PRIMARY_KEY);
                envelopedData.setKey(cursor.getString(columnIndex));

                //Bin Password
                columnIndex = cursor.getColumnIndex(FADBHelper.BIN_PASSWD);
                envelopedData.setBinPassword(cursor.getString(columnIndex));

                //Seconds
                columnIndex = cursor.getColumnIndex(FADBHelper.NUMBER_OF_SECONDS);
                envelopedData.setSeconds(cursor.getInt(columnIndex));

                //Seed
                columnIndex = cursor.getColumnIndex(FADBHelper.SEED_VALUE);
                envelopedData.setSeed(cursor.getString(columnIndex));


                //Account Name
                columnIndex = cursor.getColumnIndex(FADBHelper.ACCOUNT_NAME);
                envelopedData.setAccountName(cursor.getString(columnIndex));

                //Num of digits
                columnIndex = cursor.getColumnIndex(FADBHelper.NUM_OF_DIGITS);
                envelopedData.setNumDigits(cursor.getInt(columnIndex));
            }


            return envelopedData;

        }catch (Exception s)
        {
            s.printStackTrace();
            return null;
        }

        finally
        {
            db.close();
        }
    }


}
