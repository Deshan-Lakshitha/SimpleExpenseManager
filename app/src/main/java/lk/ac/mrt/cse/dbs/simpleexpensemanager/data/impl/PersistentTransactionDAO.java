package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import java.text.SimpleDateFormat;

public class PersistentTransactionDAO implements TransactionDAO {
    private DatabaseHelper databaseHelper;  // Keeps a database helper object to access database

    public PersistentTransactionDAO() {
        this.databaseHelper = DatabaseHelper.getInstance();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();   // Open database in write mode.

        ContentValues contentValues = new ContentValues();
        contentValues.put("date", new SimpleDateFormat("dd/MM/yyyy").format(date));
        contentValues.put("account_no", accountNo);
        contentValues.put("expense_type", String.valueOf(expenseType));
        contentValues.put("amount", amount);

        sqLiteDatabase.insert("transactions", null, contentValues);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();   // Open database in read only mode
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM transactions", null);

        return generateTransactionList(cursor);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();   // Open database in read only mode
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM transactions LIMIT " + limit, null);

        return generateTransactionList(cursor);

    }

    // A common method to generate transaction object list
    private List<Transaction> generateTransactionList(Cursor cursor) {
        List<Transaction> transactions = new LinkedList<>();

        Date date;
        String accountNo;
        ExpenseType expenseType;
        double amount;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        while (cursor.moveToNext()) {
            try {
                date = format.parse(cursor.getString(cursor.getColumnIndex("date")));
                System.out.println(date);
            } catch (ParseException e) {
                date = null;
                e.printStackTrace();
            }
            accountNo = cursor.getString(cursor.getColumnIndex("account_no"));
            expenseType = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex("expense_type")));
            amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            transactions.add(new Transaction(date, accountNo, expenseType, amount));
        }
        return transactions;
    }
}
