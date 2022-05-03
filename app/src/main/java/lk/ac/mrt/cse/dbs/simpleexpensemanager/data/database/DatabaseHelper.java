package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;

    public static void createInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
    }

    public static DatabaseHelper getInstance() {
        return instance;
    }

    public DatabaseHelper(@Nullable Context context) {
        super(context, "190686N", null, 1);
    }

        @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // This method gets called when a new database is created.

        // Use sqLiteDatabase object to create a table
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS accounts (" +
                "account_no VARCHAR(50) PRIMARY KEY," +
                "bank_name VARCHAR(100) NOT NULL," +
                "account_holder_name VARCHAR(50) NOT NULL," +
                "balance DOUBLE(50) NOT NULL" +
                ")");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS transactions(" +
                "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "date DATE NOT NULL," +
                "account_no VARCHAR(50) NOT NULL," +
                "expense_type VARCHAR(20) NOT NULL," +
                "amount DOUBLE(50) NOT NULL," +
                "FOREIGN KEY(account_no) REFERENCES accounts(account_no)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // When upgrading, delete existing tables and crate them again.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS accounts");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS transactions");
        onCreate(sqLiteDatabase);

    }
}
