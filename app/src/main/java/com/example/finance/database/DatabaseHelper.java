package com.example.finance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.finance.models.Asset;
import com.example.finance.models.Expense;
import com.example.finance.models.Goal;
import com.example.finance.models.Income;
import com.example.finance.models.Saving;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "FinanceTracker.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_EXPENSES = "expenses";

    private static final String COLUMN_CATEGORY = "category";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Assets Table
        final String SQL_CREATE_ASSETS_TABLE = "CREATE TABLE " +
                DatabaseContract.AssetEntry.TABLE_NAME + " (" +
                DatabaseContract.AssetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.AssetEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                DatabaseContract.AssetEntry.COLUMN_AMOUNT + " REAL NOT NULL, " +
                DatabaseContract.AssetEntry.COLUMN_WORTH + " REAL NOT NULL)";

        // Create Income Table
        final String SQL_CREATE_INCOME_TABLE = "CREATE TABLE " +
                DatabaseContract.IncomeEntry.TABLE_NAME + " (" +
                DatabaseContract.IncomeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.IncomeEntry.COLUMN_SOURCE + " TEXT NOT NULL, " +
                DatabaseContract.IncomeEntry.COLUMN_AMOUNT + " REAL NOT NULL, " +
                DatabaseContract.IncomeEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                DatabaseContract.IncomeEntry.COLUMN_CATEGORY + " TEXT)";

        // Create Expenses Table
        final String SQL_CREATE_EXPENSES_TABLE = "CREATE TABLE " +
                DatabaseContract.ExpenseEntry.TABLE_NAME + " (" +
                DatabaseContract.ExpenseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.ExpenseEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                DatabaseContract.ExpenseEntry.COLUMN_AMOUNT + " REAL NOT NULL, " +
                DatabaseContract.ExpenseEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                DatabaseContract.ExpenseEntry.COLUMN_DESCRIPTION + " TEXT, " +
                DatabaseContract.ExpenseEntry.COLUMN_IS_RECURRING + " INTEGER DEFAULT 0)";



        // Create Savings Table
        final String SQL_CREATE_SAVINGS_TABLE = "CREATE TABLE " +
                DatabaseContract.SavingEntry.TABLE_NAME + " (" +
                DatabaseContract.SavingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.SavingEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                DatabaseContract.SavingEntry.COLUMN_AMOUNT + " REAL NOT NULL, " +
                DatabaseContract.SavingEntry.COLUMN_RETURN_RATE + " REAL NOT NULL, " +
                DatabaseContract.SavingEntry.COLUMN_START_DATE + " TEXT, " +
                DatabaseContract.SavingEntry.COLUMN_TARGET_DATE + " TEXT, " +
                DatabaseContract.SavingEntry.COLUMN_TARGET_AMOUNT + " REAL)";

        // Create Goals Table
        final String SQL_CREATE_GOALS_TABLE = "CREATE TABLE " +
                DatabaseContract.GoalEntry.TABLE_NAME + " (" +
                DatabaseContract.GoalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.GoalEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                DatabaseContract.GoalEntry.COLUMN_TARGET_AMOUNT + " REAL NOT NULL, " +
                DatabaseContract.GoalEntry.COLUMN_CURRENT_AMOUNT + " REAL NOT NULL, " +
                DatabaseContract.GoalEntry.COLUMN_DEADLINE + " TEXT, " +
                DatabaseContract.GoalEntry.COLUMN_CATEGORY + " TEXT, " +
                DatabaseContract.GoalEntry.COLUMN_PRIORITY + " INTEGER)";

        try {
            db.execSQL(SQL_CREATE_ASSETS_TABLE);
            db.execSQL(SQL_CREATE_INCOME_TABLE);
            db.execSQL(SQL_CREATE_EXPENSES_TABLE);
            db.execSQL(SQL_CREATE_SAVINGS_TABLE);
            db.execSQL(SQL_CREATE_GOALS_TABLE);
        } catch (Exception e) {
            Log.e(TAG, "Error creating tables", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // Drop older tables if existed
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.AssetEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.IncomeEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.ExpenseEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.SavingEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.GoalEntry.TABLE_NAME);

            // Create tables again
            onCreate(db);
        } catch (Exception e) {
            Log.e(TAG, "Error upgrading database", e);
        }
    }

    // Asset Methods
    public long addOrUpdateAsset(Asset asset) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.AssetEntry.COLUMN_TYPE, asset.getType());
        values.put(DatabaseContract.AssetEntry.COLUMN_AMOUNT, asset.getAmount());
        values.put(DatabaseContract.AssetEntry.COLUMN_WORTH, asset.getWorth());

        long id = -1;
        try {
            Cursor cursor = db.query(
                    DatabaseContract.AssetEntry.TABLE_NAME,
                    null,
                    DatabaseContract.AssetEntry.COLUMN_TYPE + " = ?",
                    new String[]{asset.getType()},
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                // Update existing asset
                id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.AssetEntry._ID));
                db.update(DatabaseContract.AssetEntry.TABLE_NAME, values,
                        DatabaseContract.AssetEntry._ID + " = ?",
                        new String[]{String.valueOf(id)});
            } else {
                // Insert new asset
                id = db.insert(DatabaseContract.AssetEntry.TABLE_NAME, null, values);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error adding/updating asset", e);
        }
        return id;
    }

    public List<Asset> getAllAssets() {
        List<Asset> assetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(
                    DatabaseContract.AssetEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    DatabaseContract.AssetEntry.COLUMN_TYPE + " ASC"
            );

            if (cursor.moveToFirst()) {
                do {
                    Asset asset = new Asset(
                            cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.AssetEntry._ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.AssetEntry.COLUMN_TYPE)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.AssetEntry.COLUMN_AMOUNT)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.AssetEntry.COLUMN_WORTH))
                    );
                    assetList.add(asset);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error getting assets", e);
        }
        return assetList;
    }

    public double getTotalWealth() {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        try {
            String query = "SELECT SUM(" + DatabaseContract.AssetEntry.COLUMN_WORTH +
                    ") as Total FROM " + DatabaseContract.AssetEntry.TABLE_NAME;

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error calculating total wealth", e);
        }
        return total;
    }

    public void deleteAsset(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(DatabaseContract.AssetEntry.TABLE_NAME,
                    DatabaseContract.AssetEntry._ID + " = ?",
                    new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e(TAG, "Error deleting asset", e);
        }
    }

    // Income Methods
    public long addIncome(Income income) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.IncomeEntry.COLUMN_SOURCE, income.getSource());
        values.put(DatabaseContract.IncomeEntry.COLUMN_AMOUNT, income.getAmount());
        values.put(DatabaseContract.IncomeEntry.COLUMN_DATE, income.getDate());
        values.put(DatabaseContract.IncomeEntry.COLUMN_CATEGORY, income.getCategory());

        long id = -1;
        try {
            id = db.insert(DatabaseContract.IncomeEntry.TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error adding income", e);
        }
        return id;
    }

    public List<Income> getAllIncome() {
        List<Income> incomeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(
                    DatabaseContract.IncomeEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    DatabaseContract.IncomeEntry.COLUMN_DATE + " DESC"
            );

            if (cursor.moveToFirst()) {
                do {
                    Income income = new Income(
                            cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.IncomeEntry._ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.IncomeEntry.COLUMN_SOURCE)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.IncomeEntry.COLUMN_AMOUNT)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.IncomeEntry.COLUMN_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.IncomeEntry.COLUMN_CATEGORY))
                    );
                    incomeList.add(income);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error getting income", e);
        }
        return incomeList;
    }

    public double getMonthlyIncome(String yearMonth) {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        try {
            String query = "SELECT SUM(" + DatabaseContract.IncomeEntry.COLUMN_AMOUNT +
                    ") FROM " + DatabaseContract.IncomeEntry.TABLE_NAME +
                    " WHERE strftime('%Y-%m', " + DatabaseContract.IncomeEntry.COLUMN_DATE + ") = ?";

            Cursor cursor = db.rawQuery(query, new String[]{yearMonth});
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error calculating monthly income", e);
        }
        return total;
    }

    public void deleteIncome(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(DatabaseContract.IncomeEntry.TABLE_NAME,
                    DatabaseContract.IncomeEntry._ID + " = ?",
                    new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e(TAG, "Error deleting income", e);
        }
    }

    public double getTotalIncome() {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        try {
            String query = "SELECT SUM(" + DatabaseContract.IncomeEntry.COLUMN_AMOUNT +
                    ") as Total FROM " + DatabaseContract.IncomeEntry.TABLE_NAME;

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error calculating total income", e);
        }
        return total;
    }
    // Expense Methods
    public long addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ExpenseEntry.COLUMN_CATEGORY, expense.getCategory());
        values.put(DatabaseContract.ExpenseEntry.COLUMN_AMOUNT, expense.getAmount());
        values.put(DatabaseContract.ExpenseEntry.COLUMN_DATE, expense.getDate());
        values.put(DatabaseContract.ExpenseEntry.COLUMN_DESCRIPTION, expense.getDescription());
        values.put(DatabaseContract.ExpenseEntry.COLUMN_IS_RECURRING, expense.isRecurring() ? 1 : 0);

        long id = -1;
        try {
            id = db.insert(DatabaseContract.ExpenseEntry.TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error adding expense", e);
        }
        return id;
    }

    public List<Expense> getAllExpenses() {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(
                    DatabaseContract.ExpenseEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    DatabaseContract.ExpenseEntry.COLUMN_DATE + " DESC"
            );

            if (cursor.moveToFirst()) {
                do {
                    Expense expense = new Expense(
                            cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseEntry._ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseEntry.COLUMN_CATEGORY)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseEntry.COLUMN_AMOUNT)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseEntry.COLUMN_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseEntry.COLUMN_DESCRIPTION)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseEntry.COLUMN_IS_RECURRING)) == 1
                    );
                    expenseList.add(expense);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error getting expenses", e);
        }
        return expenseList;
    }

    public double getMonthlyExpensesByCategory(String category, String yearMonth) {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        try {
            String query = "SELECT SUM(" + DatabaseContract.ExpenseEntry.COLUMN_AMOUNT +
                    ") FROM " + DatabaseContract.ExpenseEntry.TABLE_NAME +
                    " WHERE " + DatabaseContract.ExpenseEntry.COLUMN_CATEGORY + " = ? " +
                    "AND strftime('%Y-%m', " + DatabaseContract.ExpenseEntry.COLUMN_DATE + ") = ?";

            Cursor cursor = db.rawQuery(query, new String[]{category, yearMonth});
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error calculating monthly expenses by category", e);
        }
        return total;
    }

    

    // Saving Methods
    public long addOrUpdateSaving(Saving saving) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.SavingEntry.COLUMN_TYPE, saving.getType());
        values.put(DatabaseContract.SavingEntry.COLUMN_AMOUNT, saving.getAmount());
        values.put(DatabaseContract.SavingEntry.COLUMN_RETURN_RATE, saving.getReturnRate());
        values.put(DatabaseContract.SavingEntry.COLUMN_START_DATE, saving.getStartDate());
        values.put(DatabaseContract.SavingEntry.COLUMN_TARGET_DATE, saving.getTargetDate());
        values.put(DatabaseContract.SavingEntry.COLUMN_TARGET_AMOUNT, saving.getTargetAmount());

        long id = -1;
        try {
            Cursor cursor = db.query(
                    DatabaseContract.SavingEntry.TABLE_NAME,
                    null,
                    DatabaseContract.SavingEntry.COLUMN_TYPE + " = ?",
                    new String[]{saving.getType()},
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.SavingEntry._ID));
                db.update(DatabaseContract.SavingEntry.TABLE_NAME, values,
                        DatabaseContract.SavingEntry._ID + " = ?",
                        new String[]{String.valueOf(id)});
            } else {
                id = db.insert(DatabaseContract.SavingEntry.TABLE_NAME, null, values);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error adding/updating saving", e);
        }
        return id;
    }

    public List<Saving> getAllSavings() {
        List<Saving> savingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(
                    DatabaseContract.SavingEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    DatabaseContract.SavingEntry.COLUMN_START_DATE + " DESC"
            );

            if (cursor.moveToFirst()) {
                do {
                    Saving saving = new Saving(
                            cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.SavingEntry._ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.SavingEntry.COLUMN_TYPE)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.SavingEntry.COLUMN_AMOUNT)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.SavingEntry.COLUMN_RETURN_RATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.SavingEntry.COLUMN_START_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.SavingEntry.COLUMN_TARGET_DATE)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.SavingEntry.COLUMN_TARGET_AMOUNT))
                    );
                    savingList.add(saving);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error getting savings", e);
        }
        return savingList;
    }

    // Goal Methods
    public long addOrUpdateGoal(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.GoalEntry.COLUMN_DESCRIPTION, goal.getDescription());
        values.put(DatabaseContract.GoalEntry.COLUMN_TARGET_AMOUNT, goal.getTargetAmount());
        values.put(DatabaseContract.GoalEntry.COLUMN_CURRENT_AMOUNT, goal.getCurrentAmount());
        values.put(DatabaseContract.GoalEntry.COLUMN_DEADLINE, goal.getDeadline());
        values.put(DatabaseContract.GoalEntry.COLUMN_CATEGORY, goal.getCategory());
        values.put(DatabaseContract.GoalEntry.COLUMN_PRIORITY, goal.getPriority());

        long id = -1;
        try {
            if (goal.getId() > 0) {
                db.update(DatabaseContract.GoalEntry.TABLE_NAME, values,
                        DatabaseContract.GoalEntry._ID + " = ?",
                        new String[]{String.valueOf(goal.getId())});
                id = goal.getId();
            } else {
                id = db.insert(DatabaseContract.GoalEntry.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error adding/updating goal", e);
        }
        return id;
    }

    public List<Goal> getAllGoals() {
        List<Goal> goalList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(
                    DatabaseContract.GoalEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    DatabaseContract.GoalEntry.COLUMN_PRIORITY + " DESC"
            );

            if (cursor.moveToFirst()) {
                do {
                    Goal goal = new Goal(
                            cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.GoalEntry._ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.GoalEntry.COLUMN_DESCRIPTION)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.GoalEntry.COLUMN_TARGET_AMOUNT)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.GoalEntry.COLUMN_CURRENT_AMOUNT)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.GoalEntry.COLUMN_DEADLINE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.GoalEntry.COLUMN_CATEGORY)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.GoalEntry.COLUMN_PRIORITY))
                    );
                    goalList.add(goal);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error getting goals", e);
        }
        return goalList;
    }

    // Utility Methods
    public Map<String, Double> getMonthlyExpenseSummary(String yearMonth) {
        Map<String, Double> summary = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String query = "SELECT " + DatabaseContract.ExpenseEntry.COLUMN_CATEGORY +
                    ", SUM(" + DatabaseContract.ExpenseEntry.COLUMN_AMOUNT + ") as total" +
                    " FROM " + DatabaseContract.ExpenseEntry.TABLE_NAME +
                    " WHERE strftime('%Y-%m', " + DatabaseContract.ExpenseEntry.COLUMN_DATE + ") = ?" +
                    " GROUP BY " + DatabaseContract.ExpenseEntry.COLUMN_CATEGORY;

            Cursor cursor = db.rawQuery(query, new String[]{yearMonth});
            if (cursor.moveToFirst()) {
                do {
                    String category = cursor.getString(0);
                    double total = cursor.getDouble(1);
                    summary.put(category, total);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error getting monthly expense summary", e);
        }
        return summary;
    }

    public double getTotalAssetWorth() {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        try {
            Cursor cursor = db.rawQuery("SELECT SUM(" +
                    DatabaseContract.AssetEntry.COLUMN_WORTH + ") FROM " +
                    DatabaseContract.AssetEntry.TABLE_NAME, null);

            if (cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error calculating total asset worth", e);
        }
        return total;
    }

    public void clearAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(DatabaseContract.AssetEntry.TABLE_NAME, null, null);
            db.delete(DatabaseContract.IncomeEntry.TABLE_NAME, null, null);
            db.delete(DatabaseContract.ExpenseEntry.TABLE_NAME, null, null);
            db.delete(DatabaseContract.SavingEntry.TABLE_NAME, null, null);
            db.delete(DatabaseContract.GoalEntry.TABLE_NAME, null, null);
        } catch (Exception e) {
            Log.e(TAG, "Error clearing all data", e);
        }
    }

    // Inside DatabaseHelper class

    public void setGoal(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.GoalEntry.COLUMN_DESCRIPTION, goal.getDescription());
        values.put(DatabaseContract.GoalEntry.COLUMN_TARGET_AMOUNT, goal.getTargetAmount());
        values.put(DatabaseContract.GoalEntry.COLUMN_CURRENT_AMOUNT, goal.getCurrentAmount());
        values.put(DatabaseContract.GoalEntry.COLUMN_DEADLINE, goal.getDeadline());
        values.put(DatabaseContract.GoalEntry.COLUMN_CATEGORY, goal.getCategory());
        values.put(DatabaseContract.GoalEntry.COLUMN_PRIORITY, goal.getPriority());

        try {
            db.insert(DatabaseContract.GoalEntry.TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error setting goal", e);
        }
    }

    public Goal getCurrentGoal() {
        SQLiteDatabase db = this.getReadableDatabase();
        Goal goal = null;

        try {
            Cursor cursor = db.query(
                    DatabaseContract.GoalEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    DatabaseContract.GoalEntry.COLUMN_PRIORITY + " DESC",
                    "1"
            );

            if (cursor.moveToFirst()) {
                goal = new Goal(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.GoalEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.GoalEntry.COLUMN_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.GoalEntry.COLUMN_TARGET_AMOUNT)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.GoalEntry.COLUMN_CURRENT_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.GoalEntry.COLUMN_DEADLINE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.GoalEntry.COLUMN_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.GoalEntry.COLUMN_PRIORITY))
                );
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error getting current goal", e);
        }
        return goal;
    }


    public double getTotalSavings() {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        try {
            String query = "SELECT SUM(" + DatabaseContract.SavingEntry.COLUMN_AMOUNT + ") as Total FROM " +
                    DatabaseContract.SavingEntry.TABLE_NAME;

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error calculating total savings", e);
        }
        return total;
    }




}