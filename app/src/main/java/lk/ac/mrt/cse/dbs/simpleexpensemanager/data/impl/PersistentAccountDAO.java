package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DatabaseHelper databaseHelper;  // Keeps a database helper object to access database

    public PersistentAccountDAO() {
        this.databaseHelper = DatabaseHelper.getInstance();
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbers = new LinkedList<>();

        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();   // Open database in read only mode
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT account_no FROM accounts", null);

        while (cursor.moveToNext()) {
            accountNumbers.add(cursor.getString(cursor.getColumnIndex("account_no")));
        }

        return accountNumbers;
    }


    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new LinkedList<>();

        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();   // Open database in read only mode
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM accounts", null);

        String accountNo;
        String bankName;
        String accountHolderName;
        double balance;
        while (cursor.moveToNext()) {
            accountNo =  cursor.getString(cursor.getColumnIndex("account_no"));
            bankName =  cursor.getString(cursor.getColumnIndex("bank_name"));
            accountHolderName = cursor.getString(cursor.getColumnIndex("account_holder_name"));
            balance = cursor.getDouble(cursor.getColumnIndex("balance"));
            accounts.add(new Account(accountNo, bankName, accountHolderName, balance));
        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account;

        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();   // Open database in read only mode
        String[] args = {accountNo};
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM accounts WHERE account_no = ?" + " LIMIT 1", args);

        if (cursor.moveToFirst()){
            String accNo = cursor.getString(cursor.getColumnIndex("account_no"));
            String bankName = cursor.getString(cursor.getColumnIndex("bank_name"));
            String accountHolderName = cursor.getString(cursor.getColumnIndex("account_holder_name"));
            double balance = cursor.getDouble(cursor.getColumnIndex("balance"));

            account = new Account(accNo, bankName, accountHolderName, balance);
            return account;
        }

        return null;
    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();   // Open database in write mode.

        ContentValues contentValues = new ContentValues();
        contentValues.put("account_no", account.getAccountNo());
        contentValues.put("bank_name", account.getBankName());
        contentValues.put("account_holder_name", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());

        sqLiteDatabase.insert("accounts", null, contentValues);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();   // Open database in write mode.

        String[] args = {accountNo};
        sqLiteDatabase.delete("accounts", "account_no=?", args);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();   // Open database in write mode.
        Account account = this.getAccount(accountNo);

        ContentValues contentValues = new ContentValues();
        if (expenseType == ExpenseType.EXPENSE)
            contentValues.put("balance", account.getBalance() - amount);
        else if (expenseType == ExpenseType.INCOME)
            contentValues.put("balance", account.getBalance() + amount);

        String[] args = {accountNo};
        sqLiteDatabase.update("accounts", contentValues, "account_no = ?", args);
    }
}
