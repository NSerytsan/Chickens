package com.example.chickens;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String CHICKENS_TABLE = "CHICKENS_TABLE";
    public static final String COLUMN_CHICKEN_NAME = "CHICKEN_NAME";
    public static final String COLUMN_CHICKEN_EGG_NUM = "CHICKEN_EGG_NUM";
    public static final String COLUMN_CHICKEN_TYPE = "CHICKEN_TYPE";
    public static final String COLUMN_ID = "ID";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "chickens.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CHICKENS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CHICKEN_NAME + " TEXT, " + COLUMN_CHICKEN_EGG_NUM + " INT, " + COLUMN_CHICKEN_TYPE + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(ChickenModel chickenModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CHICKEN_NAME, chickenModel.getName());
        cv.put(COLUMN_CHICKEN_EGG_NUM, chickenModel.getEgg_num());
        cv.put(COLUMN_CHICKEN_TYPE, chickenModel.getType());

        long insert = db.insert(CHICKENS_TABLE, null, cv);
        if(insert == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean deleteOne(ChickenModel chickenModel){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CHICKENS_TABLE + " WHERE " + COLUMN_ID + " = " + chickenModel.getId();

        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }

    }

    public List<ChickenModel> getAll(){
        List<ChickenModel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + CHICKENS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            do{
                int chickenID = cursor.getInt(0);
                String chickenName = cursor.getString(1);
                int chickenEggNum = cursor.getInt(2);
                String chickenType = cursor.getString(3);

                ChickenModel newChicken = new ChickenModel(chickenID, chickenName, chickenEggNum, chickenType);
                returnList.add(newChicken);
            }while (cursor.moveToNext());
        }
        else {

        }

        cursor.close();
        db.close();

        return returnList;
    }

    public List<ChickenModel> getMoreThen(){
        List<ChickenModel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + CHICKENS_TABLE + " WHERE " + COLUMN_CHICKEN_EGG_NUM + " >= 300";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            do{
                int chickenID = cursor.getInt(0);
                String chickenName = cursor.getString(1);
                int chickenEggNum = cursor.getInt(2);
                String chickenType = cursor.getString(3);

                ChickenModel newChicken = new ChickenModel(chickenID, chickenName, chickenEggNum, chickenType);
                returnList.add(newChicken);
            }while (cursor.moveToNext());
        }
        else {

        }

        cursor.close();
        db.close();

        return returnList;
    }

    public int getAverage(){

        String queryString = "SELECT AVG(" + COLUMN_CHICKEN_EGG_NUM + ") AS EGG_AVG FROM " + CHICKENS_TABLE + " WHERE " + COLUMN_CHICKEN_TYPE + " = 'Яєчна'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        int EGG_AVG;
        if (cursor.moveToFirst()){
            EGG_AVG = cursor.getInt(0);
        }
        else {
            EGG_AVG = 0;
        }

        cursor.close();
        db.close();

        return EGG_AVG;
    }
}
