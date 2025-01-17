package com.example.finance.database;  // Ahmet SAZAN and Murat Görkem Kahya worked together for this page

import android.provider.BaseColumns;

public final class DatabaseContract {
    private DatabaseContract() {}

    // Assets Table
    public static class AssetEntry implements BaseColumns {
        public static final String TABLE_NAME = "assets";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_WORTH = "worth";
    }

    // Income Table
    public static class IncomeEntry implements BaseColumns {
        public static final String TABLE_NAME = "income";
        public static final String COLUMN_SOURCE = "source";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_CATEGORY = "category";
    }

    // Expenses Table
    public static class ExpenseEntry implements BaseColumns {
        public static final String TABLE_NAME = "expenses";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IS_RECURRING = "is_recurring";
    }

    // Savings Table
    public static class SavingEntry implements BaseColumns {
        public static final String TABLE_NAME = "savings";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_RETURN_RATE = "return_rate";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_TARGET_DATE = "target_date";
        public static final String COLUMN_TARGET_AMOUNT = "target_amount";
    }

    // Goals Table
    public static class GoalEntry implements BaseColumns {
        public static final String TABLE_NAME = "goals";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TARGET_AMOUNT = "target_amount";
        public static final String COLUMN_CURRENT_AMOUNT = "current_amount";
        public static final String COLUMN_DEADLINE = "deadline";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_PRIORITY = "priority";
    }
}