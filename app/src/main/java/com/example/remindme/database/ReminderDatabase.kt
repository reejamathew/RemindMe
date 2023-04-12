package com.mdev.apsche.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.remindme.model.Reminder
import java.util.*

class ReminderDatabase (context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys = ON")
        db.execSQL("CREATE TABLE $USER_DETAILS_TABLE("+
                "$COL_EMAIL_ID TEXT PRIMARY KEY ,"+
                " $COL_PASSWORD TEXT)")

        db.execSQL("CREATE TABLE $REMINDER_TABLE(" +
                "$COL_REMINDER_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "$COL_TITLE TEXT,"+
                "$COL_DESCRIPTION TEXT,"+
                "$COL_DATE TEXT,"+
                "$COL_IMAGE TEXT,"+
                "$COL_EMAIL_ID TEXT,"+
                "FOREIGN KEY($COL_EMAIL_ID) REFERENCES $USER_DETAILS_TABLE($COL_EMAIL_ID))")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $REMINDER_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $USER_DETAILS_TABLE")
    }

    fun insertReminder(title: String?, description: String?, date: String?, image: String?, email_id: String?): Boolean {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()

        
        contentValues.put(ReminderDatabase.COL_TITLE, title)
        contentValues.put(ReminderDatabase.COL_DESCRIPTION, description)
        contentValues.put(ReminderDatabase.COL_DATE, date)
        contentValues.put(ReminderDatabase.COL_IMAGE, image)
        contentValues.put(ReminderDatabase.COL_EMAIL_ID, email_id)

        Log.d("content",contentValues.toString())

        val cursor = sqLiteDatabase.insert(ReminderDatabase.REMINDER_TABLE, null, contentValues)


       // sqLiteDatabase.close()
        return !cursor.equals(-1)
    }
    fun getReminderDetails(email_id: String?): ArrayList<Reminder> {
        val sqliteDatabase = this.readableDatabase

        val cursor =  sqliteDatabase.rawQuery("SELECT * FROM $REMINDER_TABLE WHERE $COL_EMAIL_ID=?", arrayOf(email_id))

        val reminderList: ArrayList<Reminder> = ArrayList()

        if (cursor.moveToFirst()) {
            do {

                reminderList.add(

                    Reminder(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)


                    )
                )
            } while (cursor.moveToNext())

        }

        return reminderList
    }
    fun getRemindersById(reminderId: String?): ArrayList<Reminder> {

        val sqliteDatabase = this.readableDatabase
        val cursor =  sqliteDatabase.rawQuery("SELECT * FROM $REMINDER_TABLE WHERE $COL_REMINDER_ID=?", arrayOf(reminderId))
        val reminderList: ArrayList<Reminder> = ArrayList()

        if (cursor.moveToFirst()) {
            do {

                reminderList.add(
                    Reminder(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)

                    )
                )
            } while (cursor.moveToNext())

        }

        return reminderList;
        
    }
   

    fun updateReminder(reminderId: String?, apt_no: String?, title: String?, description: String?, image: String?, date: String?, beds: String?, email:String?): Boolean {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_REMINDER_ID, reminderId)
        contentValues.put(COL_TITLE, title)
        contentValues.put(COL_DESCRIPTION, description)
        contentValues.put(COL_IMAGE, image)
        contentValues.put(COL_DATE, date)
        contentValues.put(COL_EMAIL_ID, email)

        Log.d("content", contentValues.toString())

        val cursor = sqLiteDatabase.update(REMINDER_TABLE, contentValues, "$COL_REMINDER_ID =$reminderId",null)

        getRemindersById(reminderId)
        return !cursor.equals(-1)
    }

    fun deleteReminder(reminderId: String): Boolean {
        val sqLiteDatabase = this.writableDatabase

        val result =
            sqLiteDatabase.rawQuery("DELETE  FROM $REMINDER_TABLE WHERE $COL_REMINDER_ID=?", arrayOf(reminderId))
        //sqLiteDatabase.close()
        return !result.count.equals(0)


    }
    fun insertUser(email:String?,  password: String?): Boolean {

        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_EMAIL_ID,email)
        contentValues.put(COL_PASSWORD, password)
        val result = sqLiteDatabase.insert(USER_DETAILS_TABLE, null, contentValues)
     //   sqLiteDatabase.close()
        return !result.equals(-1)
    }

    fun checkEmail(email: String): Boolean {
        val sqLiteDatabase = this.writableDatabase
        Log.d("email in check email", email)
        val result =
            sqLiteDatabase.rawQuery("SELECT * FROM $USER_DETAILS_TABLE WHERE $COL_EMAIL_ID=?", arrayOf(email))
        //sqLiteDatabase.close()
        return !result.count.equals(0)


    }
    fun checkLogin(email: String, password: String): Boolean {
        val sqLiteDatabase = this.readableDatabase
        val result = sqLiteDatabase.rawQuery(
            "SELECT * FROM $USER_DETAILS_TABLE WHERE $COL_EMAIL_ID=? AND $COL_PASSWORD=?",
            arrayOf(email, password)
        )
      //  sqLiteDatabase.close()

        Log.d("result",result.toString())

        return !result.count.equals(0)
    }
    companion object{
        private const val DATABASE_NAME = "Reminder.db"
        private const val DATABASE_VERSION = 1
        private const val REMINDER_TABLE = "reminders"
        private const val USER_DETAILS_TABLE = "user"
        private const val COL_REMINDER_ID = "reminderId"
        private const val COL_TITLE = "title"
        private const val COL_DESCRIPTION = "description"
        private const val COL_DATE = "dateTime"
        private const val COL_IMAGE = "image"
        private const val COL_PASSWORD = "password"
        private const val COL_EMAIL_ID = "email_id"
    }

}