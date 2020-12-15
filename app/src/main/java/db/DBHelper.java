package db;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Results.db";
    public static final String TABLE_NAME = "results";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BMI = "bmi";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_DATE = "date";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME + " (" +
                        COLUMN_ID + "integer primary key," +
                        COLUMN_BMI + " text ," +
                        COLUMN_WEIGHT + " text, "+
                        COLUMN_HEIGHT + " text, "+
                        COLUMN_DATE + " text )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertResult (String bmi, String weight, String height, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BMI, bmi);
        contentValues.put(COLUMN_WEIGHT, weight);
        contentValues.put(COLUMN_HEIGHT, height);
        contentValues.put(COLUMN_DATE , date);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME +" where id=" + id + "", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateResults (int id, String bmi, String weight, String height, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BMI, bmi);
        contentValues.put(COLUMN_WEIGHT, weight);
        contentValues.put(COLUMN_HEIGHT, height);
        contentValues.put(COLUMN_DATE , date);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteResult (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public void deleteAllRows(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }

    public ArrayList<Result> getAllResults() {
        ArrayList<Result> array_list = new ArrayList<Result>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME + "", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Result ret = new Result(
                    res.getString(res.getColumnIndex(COLUMN_WEIGHT)),
                    res.getString(res.getColumnIndex(COLUMN_HEIGHT)),
                    res.getString(res.getColumnIndex(COLUMN_BMI)),
                    res.getString(res.getColumnIndex(COLUMN_DATE))
            );
            array_list.add(ret);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Result> getResults(int size) {
        ArrayList<Result> array_list = new ArrayList<Result>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME + " ORDER BY " + COLUMN_DATE
                + " DESC LIMIT " + size + "", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Result ret = new Result(
                    res.getString(res.getColumnIndex(COLUMN_WEIGHT)),
                    res.getString(res.getColumnIndex(COLUMN_HEIGHT)),
                    res.getString(res.getColumnIndex(COLUMN_BMI)),
                    res.getString(res.getColumnIndex(COLUMN_DATE))
            );
            array_list.add(ret);
            res.moveToNext();
        }
        return array_list;
    }
}