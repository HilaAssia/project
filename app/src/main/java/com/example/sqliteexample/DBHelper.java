package com.example.sqliteexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASENAME = "user.db";
    private static final String TABLE_RECORD = "tbluser";
    private static final int DATABASEVERSION = 2;
    // ?
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PICTURE = "image";

    private static final String[] allColumns = {COLUMN_ID, COLUMN_EMAIL, COLUMN_PASSWORD,COLUMN_PICTURE};

    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " +
            TABLE_RECORD + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_EMAIL + " TEXT," +
            COLUMN_PASSWORD + " TEXT," +
            COLUMN_PICTURE + " BLOB );";

    private SQLiteDatabase database; // access to table

    public DBHelper(@Nullable Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }


    // creating the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
    }

    // in case of version upgrade -> new schema
    // database version
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);
        onCreate(sqLiteDatabase);
    }


    // get the user back with the id
    // also possible to return only the id
    public User insert(User user)
    {
        database = getWritableDatabase(); // get access to write the database
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());

        // stored as Binary Large OBject ->  BLOB
        Bitmap b = user.getBitmap();
        if(b!=null)
            values.put(COLUMN_PICTURE, getBytes(b));
      //  else
      //      values.put(COLUMN_PICTURE,null);
        long id = database.insert(TABLE_RECORD, null, values);
        user.setId(id);
        database.close();
        return user;
    }

    public void deleteById(long id )
    {
        database = getWritableDatabase(); // get access to write e data
        database.delete(TABLE_RECORD, COLUMN_ID + " = " + id, null);
        database.close(); // close the database
    }

    // update a specific user
    public void update(User user)
    {
        database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, user.getId());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        // stored as Binary Large OBject ->  BLOB
        values.put(COLUMN_PICTURE, getBytes(user.getBitmap()));
        database.update(TABLE_RECORD, values, COLUMN_ID + "=" + user.getId(), null);
        database.close();

    }

    // return all rows in table
    public ArrayList<User> selectAll()
    {
        database = getReadableDatabase(); // get access to read the database
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = database.query(TABLE_RECORD, allColumns, null, null, null, null, null); // cursor points at a certain row
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));

                byte[] bytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PICTURE));
                Bitmap bitmap = null;
                if(bytes!=null)
                    bitmap = getImage(bytes);
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                User user= new User(id,bitmap, email,password);
                users.add(user);
            }
        }
        database.close();
        return users;
    }


    //
    // I prefer using this one...
    //
    public ArrayList<User> genericSelectByEmail(String email)
    {
        String[] vals = { email };
        // if using the rawQuery
        // String query = "SELECT * FROM " + TABLE_RECORD + " WHERE " + COLUMN_EMAIL + " = ?";
        String column = COLUMN_EMAIL;
        return select(column,vals);
    }

    public User selectById(long id)
    {
        String[] vals = { ""+id };
        // if using the rawQuery
        // String query = "SELECT * FROM " + TABLE_RECORD + " WHERE " + COLUMN_EMAIL + " = ?";
        String column = COLUMN_ID;
        // there is a single case
        return select(column,vals).get(0);

    }

    // INPUT: notice two options rawQuery should look like
    // rawQuery("SELECT id, email FROM people WHERE email = ? AND id = ?", new String[] {"David", "2"});
    // OUTPUT: arraylist - number of elements accordingly
    public ArrayList<User> select(String column, String[] values)
    {
        database = getReadableDatabase(); // get access to read the database
        ArrayList<User> users = new ArrayList<>();
        // Two options,
        // since query cannot be created in compile time there is no difference
        //Cursor cursor = database.rawQuery(query, values);
        Cursor cursor= database.query(TABLE_RECORD, allColumns, column +" = ? ", values, null, null, null); // cursor points at a certain row
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
                byte[] bytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PICTURE));

                Bitmap bitmap = null;
                if(bytes!=null)
                    bitmap = getImage(bytes);

                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                User user= new User(id,bitmap,email,password);
                users.add(user);
            }// end while
        } // end if
        database.close();
        return users;
    }


    // convert from bitmap to byte array
    private  byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    private  Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
