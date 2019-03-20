package tcc.controller_bt.model;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
    private static String DB_NAME = "DB";
    private static int DB_VERSION = 1;
    public static String BUTTON_TABLE = "BUTTON_TABLE";

    private static DataBase instance; // Singleton

    private DataBase() {
        super(MyApp.getContext(), DB_NAME, null, DB_VERSION);
    }

    public static DataBase getInstance(){
        if(instance==null)
        {
            instance = new DataBase();
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public synchronized void close() {
        instance = null;
        super.close();
    }
}
