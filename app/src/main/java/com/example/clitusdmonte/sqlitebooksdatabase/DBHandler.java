package com.example.clitusdmonte.sqlitebooksdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

/**
 * Created by clitus dmonte on 3/30/2018.
 */

public class DBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "BOOKS";
    // Contacts table name
    private static final String TABLE_NAME = "BOOKSINFO";
    // Shops Table Columns names
    private static final String ID = "ID";
    private static final String NAME = "BOOKNAME";
    private static final String AUTHOR = "AUTHOR";
    private static final String RATINGS = "RATINGS";
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOKS_TABLE = "CREATE TABLE "+TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY,"+ NAME + " TEXT,"
                + AUTHOR + " TEXT, "+ RATINGS + " INTEGER"+")";
        db.execSQL(CREATE_BOOKS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
// Creating tables again
        onCreate(db);
    }
    // Adding new shop
    public void addBook(BookModel book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, book.getBookName()); // Shop Name
        values.put(AUTHOR, book.getAuthorName()); // Shop Phone Number
        values.put(RATINGS, book.getRating());
// Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }
    // Getting one shop
    public BookModel getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{ID,
                        NAME, AUTHOR, RATINGS}, ID + "=?" ,
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        BookModel book = new BookModel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),
                Integer.parseInt(cursor.getString(3)));
        return book;
    }
    public ArrayList<BookModel> getAllbooks() {
        ArrayList<BookModel> arrList = new ArrayList<BookModel>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                BookModel book = new BookModel(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3)));
                arrList.add(book);
            } while (cursor.moveToNext());
        }
        return arrList;
    }
    public ArrayList<BookModel> getsearchedBooks(String searchText) {
        ArrayList<BookModel> arrList = new ArrayList<BookModel>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + NAME + " LIKE " + "'%" + searchText+ "%'";
        Log.i("fdfdfd", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                BookModel book = new BookModel(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3)));
                arrList.add(book);
            } while (cursor.moveToNext());
        }
        return arrList;
    }
    // Getting shops Count
    public int getBooksCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }
    // Updating a shop
    public int updateBook(BookModel book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, book.getBookName());
        values.put(AUTHOR, book.getAuthorName());
        values.put(RATINGS, book.getRating());
// updating row
        return db.update(TABLE_NAME, values, ID + " = ?",
                new String[]{String.valueOf(book.getId())});
    }
    // Deleting a shop
    public void deleteBook(BookModel book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?",
                new String[] { String.valueOf(book.getId()) });
        db.close();
    }
}