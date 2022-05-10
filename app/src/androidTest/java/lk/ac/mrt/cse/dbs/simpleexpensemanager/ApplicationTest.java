/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import androidx.test.core.app.ApplicationProvider;

import org.junit.BeforeClass;
import org.junit.Test;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest  {

    private static ExpenseManager expenseManager;
    private int initial_length;

    @BeforeClass
    public static void addAccount() throws ExpenseManagerException {
        Context context = ApplicationProvider.getApplicationContext();
        DatabaseHelper.createInstance(context);
        expenseManager = new PersistentExpenseManager();
        expenseManager.addAccount("10001", "BOC", "Deshan", 1000);
    }

    @Test
    public void checkAccount() {
        try {
            assertTrue(expenseManager.getAccountsDAO().getAccount("10001").getAccountNo().equals("10001"));
        } catch (InvalidAccountException e) {
            fail();
        }
    }

    @Test
    public void checkTransaction() {
        initial_length = expenseManager.getTransactionsDAO().getAllTransactionLogs().size();
        try {
            expenseManager.updateAccountBalance("10001", 11, 5, 2022, ExpenseType.valueOf("EXPENSE"), "100");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(expenseManager.getTransactionsDAO().getAllTransactionLogs().size() - initial_length == 1);
    }

}