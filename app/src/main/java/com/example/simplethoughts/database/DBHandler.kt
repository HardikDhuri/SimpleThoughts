package com.example.simplethoughts.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.example.simplethoughts.data.User
import java.io.ByteArrayOutputStream

class DBHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        // here we have defined variables for our database
        // below is variable for database name
        private const val DATABASE_NAME = "SIMPLE_THOUGHTS"

        // below is the variable for database version
        private const val DATABASE_VERSION = 1

        // below is the variable for table name
        private const val TABLE_NAME = "users_details"

        // below is the variable for id column
        private const val ID_COL = "id"

        // below is the variable for username column
        private const val USERNAME_COL = "username"

        // below is the variable for full name column
        private const val FULL_NAME_COL = "full_name"

        // below is the variable for email column
        private const val EMAIL_COL = "email"

        // below is the variable for phone number column
        private const val PHONE_NUMBER_COL = "phone_number"

        // below is the variable for password column
        private const val PASSWORD_COL = "password"

        private const val IMAGE_COL = "profile_picture"


    }

    fun getTableName():String {
        return TABLE_NAME
    }

    fun getUsernameColName():String {
        return USERNAME_COL
    }

    fun getPhoneNumberColName():String {
        return PHONE_NUMBER_COL
    }
    // Creating Database
    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                USERNAME_COL + " TEXT UNIQUE," +
                FULL_NAME_COL + " TEXT," +
                EMAIL_COL + " TEXT," +
                PHONE_NUMBER_COL + " TEXT UNIQUE," +
                PASSWORD_COL + " BINARY(64) NOT NULL," +
                IMAGE_COL + " BLOB NOT NULL" + ")"
                )
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
            // this method is to check if table already exists
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // This method is for adding data in our database
    fun addUser(username : String,
                fullName: String,
                email: String,
                phoneNumber: String,
                password : String,
                profilePicture: ByteArray): String? {

        // creating a instance of writable database
        val db = this.writableDatabase

        // values instance to store values from the user
        val values = ContentValues()



        // Storing data in values
        values.put(USERNAME_COL, username)
        values.put(FULL_NAME_COL, fullName)
        values.put(EMAIL_COL, email)
        values.put(PHONE_NUMBER_COL, phoneNumber)
        values.put(PASSWORD_COL, password)
        values.put(IMAGE_COL, profilePicture)


        // all values are inserted into database
        return try {
            db.insertOrThrow(TABLE_NAME, null, values)
            db.close()
            "User Registered"

        } catch (e: Exception) {
            db.close()
            e.message
        }
    }

    @SuppressLint("Range")
    fun getAllUsers(): List<User> {

        val users: MutableList<User> = mutableListOf()

        val db = this.readableDatabase
        val query = """Select * from $TABLE_NAME"""
        val result:Cursor = db.rawQuery(query, null)

        while (result.moveToNext()){
            var fullName = result.getString(result.getColumnIndex(FULL_NAME_COL))
            var email = result.getString(result.getColumnIndex(EMAIL_COL))
            var username = result.getString(result.getColumnIndex(USERNAME_COL))
            var profilePictureByte: ByteArray = result.getBlob(result.getColumnIndex(IMAGE_COL))
            var profilePicture = BitmapFactory.decodeByteArray(profilePictureByte, 0, profilePictureByte.size);
            var phoneNumber = result.getString(result.getColumnIndex(PHONE_NUMBER_COL))
            users.add(User(fullName,phoneNumber,email,username,profilePicture))
        }
        result.close()
        return users
    }

    @SuppressLint("Range", "Recycle")
    fun validateUser(id: String, password: String): Int {
        val db = this.readableDatabase
        val query = """Select password from $TABLE_NAME where $PHONE_NUMBER_COL=$id"""
        val result:Cursor = db.rawQuery(query, null)
        val row_count = result.count.toString()

        if (row_count == "0") {
            return 0 // returns 0 if user does not exist
        } else {
            while (result.moveToNext()){
                if (password == result.getString(result.getColumnIndex("password"))) {
                    return 1  // returns 1 if user is authourised
                }
            }
            return 2  // returns 2 if user in not authorised
        }
    }

    fun getUser(phone_number: String): Cursor {
        val db = this.readableDatabase
        val query = """Select * from $TABLE_NAME where $PHONE_NUMBER_COL=$phone_number"""
        return db.rawQuery(query, null)
    }

    fun convertImage(id: ImageView): ByteArray {
        val profilePicture = id.drawable.toBitmap()
        val stream = ByteArrayOutputStream()
        profilePicture.compress(Bitmap.CompressFormat.PNG, 50, stream)
        return stream.toByteArray()
    }
}