package com.expense.manager.smith;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import com.expense.manager.Model.bean.AccountBean;
import com.expense.manager.Model.bean.CategoryBean;
import com.expense.manager.Model.bean.DataBean;
import com.expense.manager.Model.bean.RecurringBean;
import com.expense.manager.Model.bean.TemplateBean;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class MyDatabaseHandler {
    private static final String DATABASE_NAME = "ExpenseManager.db";
    private static final int DATABASE_VERSION = 3;
    private String accountId = "1";
    private Context context;
    /* access modifiers changed from: private */
    public SQLiteDatabase f70db;
    private ManagerDBHelper managerDB;

    public interface AccountTable {
        public static final String CREATE_TABLE_IF_NOT_SQL = "CREATE TABLE IF NOT EXISTS account(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL, currency TEXT NOT NULL )";
        public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS account(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL, currency TEXT NOT NULL )";
        public static final String CURRENCY = "currency";
        public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS account";
        public static final String EMAIL = "email";
        public static final String NAME = "name";
        public static final int SUMMARY_DAILY = 1;
        public static final int SUMMARY_MONTHLY = 3;
        public static final int SUMMARY_WEEKLY = 2;
        public static final String TABLE_NAME = "account";
        public static final String f71ID = "id";
    }

    public interface Category {
        public static final String ALTER_TABLE_CATLIMIT_V2 = "ALTER TABLE categories ADD COLUMN amount_limit TEXT NULL DEFAULT '0'";
        public static final int BOTH = 0;
        public static final String CAT_COLOR = "cat_color";
        public static final String CAT_ID = "cat_id";
        public static final String CAT_LIMIT = "amount_limit";
        public static final String CAT_NAME = "cat_name";
        public static final String CAT_TYPE = "cat_type";
        public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS categories(cat_id INTEGER PRIMARY KEY AUTOINCREMENT, cat_name TEXT NOT NULL, cat_color TEXT NOT NULL, cat_type TEXT NOT NULL, amount_limit TEXT NOT NULL, account_id TEXT NOT NULL,drag_id INTEGER)";
        public static final String DRAG_ID = "drag_id";
        public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS categories";
        public static final int EXPENCE = 1;
        public static final String EXPENSE_TABLE_NAME = "expence_cat";
        public static final int INCOME = 2;
        public static final String INCOME_TABLE_NAME = "income_cat";
        public static final String REF_ACCOUNT = "account_id";
        public static final String TABLE_NAME = "categories";
    }

    public interface ManagerTable {
        public static final String ALTER_TABLE_CATLIMIT_V2 = "ALTER TABLE manager ADD COLUMN amount_limit TEXT NOT NULL DEFAULT '0'";
        public static final String ALTER_TABLE_IMAGE_V2 = "ALTER TABLE manager ADD COLUMN image TEXT NOT NULL DEFAULT 'no image'";
        public static final String ALTER_TABLE_METHOD_V2 = "ALTER TABLE manager ADD COLUMN method TEXT NOT NULL DEFAULT 'Wire Transfer'";
        public static final String ALTER_TABLE_V7 = "ALTER TABLE manager ADD COLUMN account_id TEXT NOT NULL DEFAULT 1";
        public static final String AMOUNT = "amount";
        public static final String CATEGORY = "cat";
        public static final String CAT_LIMIT = "amount_limit";
        public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS manager(id INTEGER PRIMARY KEY AUTOINCREMENT, cat TEXT NOT NULL,sub_cat TEXT NOT NULL,amount_limit TEXT NOT NULL DEFAULT 0,amount TEXT NOT NULL,date TEXT NOT NULL,method TEXT NOT NULL,image TEXT NOT NULL,note TEXT NOT NULL,account_id TEXT NOT NULL)";
        public static final String DATE = "date";
        public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS manager";
        public static final String IMAGE = "image";
        public static final String METHOD = "method";
        public static final String NOTE = "note";
        public static final String REF_ACCOUNT = "account_id";
        public static final String SUB_CATEGORY = "sub_cat";
        public static final String TABLE_NAME = "manager";
        public static final String f72ID = "id";
    }

    public interface RecurringTable {
        public static final String ALTER_TABLE_CATLIMIT_V2 = "ALTER TABLE recurring ADD COLUMN amount_limit TEXT NOT NULL DEFAULT '0'";
        public static final String ALTER_TABLE_IMAGE_V2 = "ALTER TABLE recurring ADD COLUMN image TEXT NOT NULL DEFAULT 'no image'";
        public static final String ALTER_TABLE_METHOD_V2 = "ALTER TABLE recurring ADD COLUMN method TEXT NOT NULL DEFAULT 'Wire Transfer'";
        public static final String ALTER_TABLE_V7 = "ALTER TABLE recurring ADD COLUMN account_id TEXT NOT NULL DEFAULT 1";
        public static final String AMOUNT = "amount";
        public static final String CATEGORY = "cat";
        public static final String CAT_LIMIT = "amount_limit";
        public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS recurring(id INTEGER PRIMARY KEY AUTOINCREMENT, cat TEXT NOT NULL,sub_cat TEXT NOT NULL,amount TEXT NOT NULL,r_type TEXT NOT NULL,last_date TEXT NOT NULL,note TEXT NOT NULL,r_date TEXT NOT NULL,amount_limit TEXT NOT NULL,method TEXT NOT NULL,image TEXT NOT NULL,account_id TEXT NOT NULL )";
        public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS recurring";
        public static final String IMAGE = "image";
        public static final String METHOD = "method";
        public static final String NOTE = "note";
        public static final String RECURRING_DATE = "r_date";
        public static final String RECURRING_LASTDATE = "last_date";
        public static final String RECURRING_TYPE = "r_type";
        public static final CharSequence[] RECURRING_TYPES = {"Weekly", "Biweekly", "Monthly", "Quarterly", "Half Yearly", "Yearly"};
        public static final String REF_ACCOUNT = "account_id";
        public static final String SUB_CATEGORY = "sub_cat";
        public static final String TABLE_NAME = "recurring";
        public static final int TYPE_BIWEEKLY = 2;
        public static final int TYPE_HALF_YEARLY = 4;
        public static final int TYPE_MONTHLY = 1;
        public static final int TYPE_QUARTERLY = 3;
        public static final int TYPE_WEEKLY = 0;
        public static final int TYPE_YEARLY = 5;
        public static final String f73ID = "id";
    }

    public interface TemplatesTable {
        public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS msg_templates(id INTEGER PRIMARY KEY AUTOINCREMENT, template TEXT NOT NULL,account_id TEXT NOT NULL )";
        public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS msg_templates";
        public static final String REF_ACCOUNT = "account_id";
        public static final String TABLE_NAME = "msg_templates";
        public static final String TEMPLATES = "template";
        public static final String f74ID = "id";
    }

    public int getUserSummeryType() {
        return 1;
    }

    public MyDatabaseHandler(Context context2) {
        this.context = context2;
        this.accountId = MyUtils.getCurrentAccount(context2);
        ManagerDBHelper managerDBHelper = new ManagerDBHelper(this.context);
        this.managerDB = managerDBHelper;
        this.f70db = managerDBHelper.getWritableDatabase();
    }

    public void setAccount(String str) {
        this.accountId = str;
    }

    public long addUpdateManagerData(DataBean dataBean) {
        ContentValues contentValuesFromManagerModel = getContentValuesFromManagerModel(dataBean);
        if (TextUtils.isEmpty(dataBean.getId())) {
            return this.f70db.insert(ManagerTable.TABLE_NAME, (String) null, contentValuesFromManagerModel);
        }
        this.f70db.update(ManagerTable.TABLE_NAME, contentValuesFromManagerModel, "id=?", new String[]{dataBean.getId()});
        return 0;
    }

    private ContentValues getContentValuesFromManagerModel(DataBean dataBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("cat", dataBean.getCategoryId());
        contentValues.put("sub_cat", dataBean.getSubCategoryId());
        contentValues.put("amount", dataBean.getAmount());
        contentValues.put("date", dataBean.getDate());
        contentValues.put("note", dataBean.getNote());
        contentValues.put("method", dataBean.getMethod());
        contentValues.put("image", dataBean.getImage());
        contentValues.put("amount_limit", dataBean.getCategoryLimit());
        contentValues.put("account_id", dataBean.getAccountRef());
        return contentValues;
    }

    public void deleteManagerData(DataBean dataBean) {
        if (!TextUtils.isEmpty(dataBean.getId())) {
            this.f70db.delete(ManagerTable.TABLE_NAME, "id=?", new String[]{dataBean.getId()});
        }
    }

    public long addUpdateRecurringData(RecurringBean recurringBean) {
        ContentValues contentValuesFromRecurringModel = getContentValuesFromRecurringModel(recurringBean);
        if (TextUtils.isEmpty(recurringBean.getId())) {
            return this.f70db.insert(RecurringTable.TABLE_NAME, (String) null, contentValuesFromRecurringModel);
        }
        this.f70db.update(RecurringTable.TABLE_NAME, contentValuesFromRecurringModel, "id=?", new String[]{recurringBean.getId()});
        return 0;
    }

    private ContentValues getContentValuesFromRecurringModel(RecurringBean recurringBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("cat", recurringBean.getCategoryId());
        contentValues.put("sub_cat", recurringBean.getSubCategoryId());
        contentValues.put("amount", recurringBean.getAmount());
        contentValues.put("method", recurringBean.getMethod());
        contentValues.put("image", recurringBean.getImage());
        contentValues.put("amount_limit", recurringBean.getCategoryLimit());
        contentValues.put("note", recurringBean.getNote());
        contentValues.put(RecurringTable.RECURRING_TYPE, recurringBean.getRecurringType());
        contentValues.put(RecurringTable.RECURRING_DATE, recurringBean.getRecurringDate());
        contentValues.put(RecurringTable.RECURRING_LASTDATE, recurringBean.getRecurringLastdate());
        contentValues.put("account_id", recurringBean.getAccountRef());
        return contentValues;
    }

    public void deleteRecurringData(RecurringBean recurringBean) {
        if (!TextUtils.isEmpty(recurringBean.getId())) {
            deleteRecurringDataById(recurringBean.getId());
        }
    }

    public void deleteRecurringDataById(String str) {
        this.f70db.delete(RecurringTable.TABLE_NAME, "id=?", new String[]{str});
    }

    public long addUpdateCategoryData(CategoryBean categoryBean) {
        ContentValues contentValuesFromCategoryModel = getContentValuesFromCategoryModel(categoryBean);
        if (TextUtils.isEmpty(categoryBean.getId())) {
            return this.f70db.insert(Category.TABLE_NAME, (String) null, contentValuesFromCategoryModel);
        }
        this.f70db.update(Category.TABLE_NAME, contentValuesFromCategoryModel, "cat_id=?", new String[]{categoryBean.getId()});
        return 0;
    }

    public void addUpdateCategoryByDrod_Id(int i, int i2, int i3, CategoryBean categoryBean, int i4, String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("drag_id", Integer.valueOf(categoryBean.getDrag_id() + i2));
        this.f70db.update(Category.TABLE_NAME, contentValues, "drag_id=? AND cat_type=" + i4 + " AND " + Category.CAT_ID + "=" + i3 + " AND account_id=" + str, new String[]{"" + i});
    }

    public void addUpdateCategoryByDrod_IdtoOthers(int i, int i2, int i3, String str) {
        String[] strArr = {"" + i, "" + i2};
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("UPDATE categories SET drag_id=drag_id-1 where drag_id<=? AND drag_id!=? AND drag_id>=" + i2 + " AND " + Category.CAT_TYPE + "=" + i3 + " AND account_id=" + str, strArr);
        rawQuery.moveToFirst();
        rawQuery.close();
    }

    public void addUpdateCategoryByDrod_IdtoOthersMove(int i, int i2, String str) {
        String[] strArr = {"" + i};
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("UPDATE categories SET drag_id=drag_id-1 where drag_id>? AND cat_type=" + i2 + " AND account_id=" + str, strArr);
        rawQuery.moveToFirst();
        rawQuery.close();
    }

    public void UpdatedeleteCategoryData(int i, int i2, String str) {
        String[] strArr = {"" + i};
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("UPDATE categories SET drag_id=drag_id-1 where drag_id>?  AND cat_type=" + i2 + " AND account_id=" + str, strArr);
        rawQuery.moveToFirst();
        rawQuery.close();
    }

    public void addUpdateCategoryByDrod_Idback(int i, int i2, int i3, CategoryBean categoryBean, int i4, String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("drag_id", Integer.valueOf(categoryBean.getDrag_id() + i2));
        this.f70db.update(Category.TABLE_NAME, contentValues, "drag_id=? AND cat_type=" + i4 + " AND " + Category.CAT_ID + "=" + i3 + " AND account_id=" + str, new String[]{"" + i});
    }

    public int getDragIdfromCategoty(int i, String str) {
        int i2;
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT drag_id FROM  categories WHERE cat_id=" + i + " AND account_id=" + str, (String[]) null);
        if (rawQuery == null || rawQuery.getCount() <= 0) {
            return 0;
        }
        rawQuery.moveToFirst();
        do {
            i2 = rawQuery.getInt(0);
        } while (rawQuery.moveToNext());
        return i2;
    }

    public void addUpdateCategoryByDrod_IdtoOthersback(int i, int i2, int i3, String str) {
        String[] strArr = {"" + i, "" + i2};
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("UPDATE categories SET drag_id= drag_id +1  where drag_id>=? AND drag_id!=? AND drag_id<=" + i2 + " AND " + Category.CAT_TYPE + "=" + i3 + " AND account_id=" + str, strArr);
        rawQuery.moveToFirst();
        rawQuery.close();
    }

    public void UpdateMoveCategory(String str, String str2) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("UPDATE categories SET account_id=? WHERE cat_id=?", new String[]{"" + str2, "" + str});
        rawQuery.moveToFirst();
        rawQuery.close();
    }

    public void UpdateDragIdMoveCategory(String str, int i) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("UPDATE categories SET drag_id=?  WHERE cat_id=?", new String[]{"" + i, "" + str});
        rawQuery.moveToFirst();
        rawQuery.close();
    }

    public int getCategoryCount(String str, int i, String str2) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM  " + str + " WHERE " + Category.CAT_TYPE + "=" + i + " AND account_id=" + str2, (String[]) null);
        int count = rawQuery.getCount();
        rawQuery.close();
        return count;
    }

    public void UpdateManagerAccount_id(int i, String str) {
        String[] strArr = {"" + i};
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("UPDATE manager SET account_id=" + str + " where sub_cat=? ", strArr);
        rawQuery.moveToFirst();
        rawQuery.close();
    }

    public void UpdateRecirringAccount_id(int i, String str) {
        String[] strArr = {"" + i};
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("UPDATE recurring SET account_id=" + str + " where sub_cat=? ", strArr);
        rawQuery.moveToFirst();
        rawQuery.close();
    }

    private ContentValues getContentValuesFromCategoryModel(CategoryBean categoryBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Category.CAT_NAME, categoryBean.getName());
        contentValues.put(Category.CAT_COLOR, categoryBean.getColor());
        contentValues.put(Category.CAT_TYPE, Integer.valueOf(categoryBean.getCategoryGroup()));
        contentValues.put("account_id", categoryBean.getAccountRef());
        contentValues.put("drag_id", Integer.valueOf(categoryBean.getDrag_id()));
        contentValues.put("amount_limit", categoryBean.getCategoryLimit());
        return contentValues;
    }

    public void deleteCategoryData(CategoryBean categoryBean) {
        if (!TextUtils.isEmpty(categoryBean.getId())) {
            this.f70db.delete(ManagerTable.TABLE_NAME, "sub_cat=?", new String[]{categoryBean.getId()});
            this.f70db.delete(RecurringTable.TABLE_NAME, "sub_cat=?", new String[]{categoryBean.getId()});
            this.f70db.delete(Category.TABLE_NAME, "cat_id=?", new String[]{categoryBean.getId()});
        }
    }

    public int getCount(String str) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM " + str, (String[]) null);
        int count = rawQuery.getCount();
        rawQuery.close();
        return count;
    }

    private CategoryBean getCategoryModelFromCursor(Cursor cursor) {
        CategoryBean categoryBean = new CategoryBean();
        categoryBean.setId(cursor.getString(cursor.getColumnIndex(Category.CAT_ID)));
        categoryBean.setName(cursor.getString(cursor.getColumnIndex(Category.CAT_NAME)));
        categoryBean.setColor(cursor.getString(cursor.getColumnIndex(Category.CAT_COLOR)));
        categoryBean.setAccountRef(cursor.getString(cursor.getColumnIndex("account_id")));
        categoryBean.setCategoryLimit(cursor.getString(cursor.getColumnIndex("amount_limit")));
        return categoryBean;
    }

    private CategoryBean getCategoryModelFromCursorToBackup(Cursor cursor) {
        CategoryBean categoryBean = new CategoryBean();
        categoryBean.setId(cursor.getString(cursor.getColumnIndex(Category.CAT_ID)));
        categoryBean.setName(cursor.getString(cursor.getColumnIndex(Category.CAT_NAME)));
        categoryBean.setColor(cursor.getString(cursor.getColumnIndex(Category.CAT_COLOR)));
        categoryBean.setAccountRef(cursor.getString(cursor.getColumnIndex("account_id")));
        categoryBean.setCategoryLimit(cursor.getString(cursor.getColumnIndex("amount_limit")));
        categoryBean.setDrag_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("drag_id"))));
        return categoryBean;
    }

    private DataBean getManagerModelFromCursor(Cursor cursor) {
        DataBean dataBean = new DataBean();
        dataBean.setId(cursor.getString(cursor.getColumnIndex("id")));
        dataBean.setCategoryId(cursor.getString(cursor.getColumnIndex("cat")));
        dataBean.setSubCategoryId(cursor.getString(cursor.getColumnIndex("sub_cat")));
        dataBean.setAmount(cursor.getString(cursor.getColumnIndex("amount")));
        dataBean.setDate(cursor.getString(cursor.getColumnIndex("date")));
        dataBean.setNote(cursor.getString(cursor.getColumnIndex("note")));
        dataBean.setMethod(cursor.getString(cursor.getColumnIndex("method")));
        dataBean.setImage(cursor.getString(cursor.getColumnIndex("image")));
        dataBean.setCategoryLimit(cursor.getString(cursor.getColumnIndex("amount_limit")));
        dataBean.setAccountRef(cursor.getString(cursor.getColumnIndex("account_id")));
        return dataBean;
    }

    private RecurringBean getRecurringModelFromCursor(Cursor cursor) {
        RecurringBean recurringBean = new RecurringBean();
        recurringBean.setId(cursor.getString(cursor.getColumnIndex("id")));
        recurringBean.setCategoryId(cursor.getString(cursor.getColumnIndex("cat")));
        recurringBean.setSubCategoryId(cursor.getString(cursor.getColumnIndex("sub_cat")));
        recurringBean.setAmount(cursor.getString(cursor.getColumnIndex("amount")));
        recurringBean.setNote(cursor.getString(cursor.getColumnIndex("note")));
        recurringBean.setRecurringType(cursor.getString(cursor.getColumnIndex(RecurringTable.RECURRING_TYPE)));
        recurringBean.setRecurringDate(cursor.getString(cursor.getColumnIndex(RecurringTable.RECURRING_DATE)));
        recurringBean.setRecurringLastdate(cursor.getString(cursor.getColumnIndex(RecurringTable.RECURRING_LASTDATE)));
        recurringBean.setAccountRef(cursor.getString(cursor.getColumnIndex("account_id")));
        recurringBean.setMethod(cursor.getString(cursor.getColumnIndex("method")));
        recurringBean.setImage(cursor.getString(cursor.getColumnIndex("image")));
        recurringBean.setCategoryLimit(cursor.getString(cursor.getColumnIndex("amount_limit")));
        return recurringBean;
    }

    public List<CategoryBean> getCategoryList(int i) {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM categories WHERE cat_type=" + i + " AND account_id=" + this.accountId + " ORDER BY drag_id ASC", (String[]) null);
        while (rawQuery.moveToNext()) {
            CategoryBean categoryModelFromCursor = getCategoryModelFromCursor(rawQuery);
            categoryModelFromCursor.setCategoryGroup(i);
            arrayList.add(categoryModelFromCursor);
        }
        rawQuery.close();
        return arrayList;
    }

    public List<CategoryBean> getCategoryDESCList() {
        ArrayList arrayList = new ArrayList();
        Cursor rawQuery = this.f70db.rawQuery("SELECT id,amount,cat,cat_id AS cat_id,cat_name AS cat_name,categories.amount_limit AS amount_limit,categories.account_id AS account_id,cat_color AS cat_color FROM manager INNER JOIN categories ON cat_id = sub_cat ORDER BY id DESC ", (String[]) null);
        while (rawQuery.moveToNext()) {
            CategoryBean categoryModelFromCursor = getCategoryModelFromCursor(rawQuery);
            if (categoryModelFromCursor.getCategoryLimit().equals("")) {
                categoryModelFromCursor.setCategoryLimit("0");
            }
            categoryModelFromCursor.setCategoryGroup(Integer.valueOf(rawQuery.getString(rawQuery.getColumnIndex("cat"))).intValue());
            categoryModelFromCursor.setCategoryTotal(rawQuery.getString(rawQuery.getColumnIndex("amount")));
            arrayList.add(categoryModelFromCursor);
        }
        rawQuery.close();
        return arrayList;
    }

    public List<CategoryBean> getAllCategory() {
        ArrayList arrayList = new ArrayList();
        arrayList.clear();
        arrayList.addAll(getCategoryDESCList());
        return arrayList;
    }

    public List<CategoryBean> getSpeningCategory() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(getCategoryRecentList(1));
        return arrayList;
    }

    public List<CategoryBean> getCategoryRecentList(int i) {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM categories WHERE cat_type=" + i + " AND account_id=" + this.accountId + " ORDER BY drag_id DESC", (String[]) null);
        while (rawQuery.moveToNext()) {
            CategoryBean categoryModelFromCursor = getCategoryModelFromCursor(rawQuery);
            if (categoryModelFromCursor.getCategoryLimit().equals("")) {
                categoryModelFromCursor.setCategoryLimit("0");
            }
            categoryModelFromCursor.setCategoryGroup(i);
            arrayList.add(categoryModelFromCursor);
        }
        rawQuery.close();
        return arrayList;
    }

    public List<CategoryBean> getCategoryListToBackup(int i) {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM categories WHERE cat_type=" + i + " AND account_id=" + this.accountId + " ORDER BY drag_id ASC", (String[]) null);
        while (rawQuery.moveToNext()) {
            CategoryBean categoryModelFromCursorToBackup = getCategoryModelFromCursorToBackup(rawQuery);
            categoryModelFromCursorToBackup.setCategoryGroup(i);
            arrayList.add(categoryModelFromCursorToBackup);
        }
        rawQuery.close();
        return arrayList;
    }

    public List<DataBean> getManagerModelList(int i) {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM manager m INNER JOIN categories e ON e.cat_id=m.sub_cat WHERE m.cat=" + i + " AND m.account_id=" + this.accountId + " ORDER BY date DESC", (String[]) null);
        while (rawQuery.moveToNext()) {
            DataBean managerModelFromCursor = getManagerModelFromCursor(rawQuery);
            CategoryBean categoryModelFromCursor = getCategoryModelFromCursor(rawQuery);
            categoryModelFromCursor.setCategoryGroup(i);
            managerModelFromCursor.setCategoryModel(categoryModelFromCursor);
            if (TextUtils.isEmpty(managerModelFromCursor.getRefRecurring())) {
                TextUtils.isDigitsOnly(managerModelFromCursor.getRefRecurring());
            }
            arrayList.add(managerModelFromCursor);
        }
        rawQuery.close();
        return arrayList;
    }

    public List<RecurringBean> getRecurringModelList(int i) {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM recurring m INNER JOIN categories e ON e.cat_id=m.sub_cat WHERE m.cat=" + i + " AND m.account_id=" + this.accountId + " ORDER BY " + RecurringTable.RECURRING_LASTDATE + " DESC", (String[]) null);
        while (rawQuery.moveToNext()) {
            RecurringBean recurringModelFromCursor = getRecurringModelFromCursor(rawQuery);
            CategoryBean categoryModelFromCursor = getCategoryModelFromCursor(rawQuery);
            categoryModelFromCursor.setCategoryGroup(i);
            recurringModelFromCursor.setCategoryModel(categoryModelFromCursor);
            arrayList.add(recurringModelFromCursor);
        }
        rawQuery.close();
        return arrayList;
    }

    public CategoryBean getCategoryModelById(int i, String str) {
        CategoryBean categoryBean = null;
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM categories WHERE cat_id=" + str, (String[]) null);
        if (rawQuery.moveToFirst()) {
            categoryBean = getCategoryModelFromCursor(rawQuery);
            categoryBean.setCategoryGroup(i);
        }
        rawQuery.close();
        return categoryBean;
    }

    public DataBean getManagerModelById(int i, String str) {
        DataBean dataBean;
        Cursor rawQuery = this.f70db.rawQuery("SELECT * FROM manager WHERE id=?", new String[]{str});
        if (rawQuery.moveToFirst()) {
            dataBean = getManagerModelFromCursor(rawQuery);
            dataBean.setCategoryModel(getCategoryModelById(i, dataBean.getSubCategoryId()));
            if (TextUtils.isEmpty(dataBean.getRefRecurring()) && TextUtils.isDigitsOnly(dataBean.getRefRecurring())) {
                dataBean.setRecurringModel(getRecurringModelById(i, dataBean.getRefRecurring()));
            }
        } else {
            dataBean = null;
        }
        rawQuery.close();
        return dataBean;
    }

    public RecurringBean getRecurringModelById(int i, String str) {
        RecurringBean recurringBean;
        Cursor rawQuery = this.f70db.rawQuery("SELECT * FROM recurring WHERE id=?", new String[]{str});
        if (rawQuery.moveToFirst()) {
            recurringBean = getRecurringModelFromCursor(rawQuery);
            recurringBean.setCategoryModel(getCategoryModelById(i, recurringBean.getSubCategoryId()));
        } else {
            recurringBean = null;
        }
        rawQuery.close();
        return recurringBean;
    }

    public List<DataBean> getManagerDataByCategoryByTime(int i, String str, String str2, String str3) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select * from manager m inner join categories e on e.cat_id = m.sub_cat where ( m.cat= ? and e.cat_id = ?) and ( date BETWEEN ? and ? )  AND m.account_id=" + this.accountId + " order by date desc", new String[]{String.valueOf(i), str, str2, str3});
        ArrayList arrayList = new ArrayList();
        while (rawQuery.moveToNext()) {
            DataBean managerModelFromCursor = getManagerModelFromCursor(rawQuery);
            CategoryBean categoryModelFromCursor = getCategoryModelFromCursor(rawQuery);
            categoryModelFromCursor.setCategoryGroup(i);
            managerModelFromCursor.setCategoryModel(categoryModelFromCursor);
            if (TextUtils.isEmpty(managerModelFromCursor.getRefRecurring())) {
                TextUtils.isDigitsOnly(managerModelFromCursor.getRefRecurring());
            }
            arrayList.add(managerModelFromCursor);
        }
        rawQuery.close();
        return arrayList;
    }

    public List<DataBean> getManagerDataByTime(int i, String str, String str2) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select * from manager m inner join categories e on e.cat_id = m.sub_cat where ( m.cat= ? ) and ( date BETWEEN ? and ? )  AND m.account_id=" + this.accountId + " order by date desc", new String[]{String.valueOf(i), str, str2});
        ArrayList arrayList = new ArrayList();
        while (rawQuery.moveToNext()) {
            DataBean managerModelFromCursor = getManagerModelFromCursor(rawQuery);
            CategoryBean categoryModelFromCursor = getCategoryModelFromCursor(rawQuery);
            categoryModelFromCursor.setCategoryGroup(i);
            managerModelFromCursor.setCategoryModel(categoryModelFromCursor);
            if (TextUtils.isEmpty(managerModelFromCursor.getRefRecurring())) {
                TextUtils.isDigitsOnly(managerModelFromCursor.getRefRecurring());
            }
            arrayList.add(managerModelFromCursor);
        }
        rawQuery.close();
        return arrayList;
    }

    public Boolean existsColumnInTable() {
        try {
            if (this.f70db.rawQuery("SELECT * FROM categories LIMIT 0", (String[]) null).getColumnIndex("drag_id") != -1) {
                return true;
            }
            return false;
        } catch (Exception e) {
            Log.e("...", "- existsColumnInTable");
            return false;
        }
    }

    public String getTotalAmountByTime(int i, String str, String str2) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT SUM(amount) FROM manager m INNER JOIN categories e on e.cat_id = m.sub_cat where ( m.cat= ? ) and ( date BETWEEN ? and ? )  AND m.account_id=" + this.accountId + " order by date desc", new String[]{String.valueOf(i), str, str2});
        String string = rawQuery.moveToFirst() ? rawQuery.getString(0) : null;
        rawQuery.close();
        return string == null ? "0" : string;
    }

    public String getCategoryAmount(int i, String str) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT SUM(amount) FROM manager m INNER JOIN categories e on e.cat_id = m.sub_cat where ( m.cat=? ) and ( e.cat_id=? )  AND m.account_id=" + this.accountId, new String[]{String.valueOf(i), str});
        String string = rawQuery.moveToFirst() ? rawQuery.getString(0) : null;
        rawQuery.close();
        return string == null ? "0" : string;
    }

    public String getCategoryAmount(String str) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT SUM(amount) FROM manager m INNER JOIN categories e on e.cat_id = m.sub_cat where ( e.cat_id=? )  AND m.account_id=" + this.accountId, new String[]{str});
        String string = rawQuery.moveToFirst() ? rawQuery.getString(0) : null;
        rawQuery.close();
        return string == null ? "0" : string;
    }

    public String getCategoryAmountByTime(int i, String str, String str2, String str3) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT SUM(amount) FROM manager m INNER JOIN categories e on e.cat_id = m.sub_cat where ( m.cat=? ) and ( e.cat_id=? ) and ( date BETWEEN ? and ? )  AND m.account_id=" + this.accountId, new String[]{String.valueOf(i), str, str2, str3});
        String string = rawQuery.moveToFirst() ? rawQuery.getString(0) : null;
        rawQuery.close();
        return string == null ? "0" : string;
    }

    public List<RecurringBean> getTodayRecurringData() {
        String format = MyUtils.sdfDatabase.format(Calendar.getInstance().getTime());
        ArrayList arrayList = new ArrayList();
        Cursor rawQuery = this.f70db.rawQuery("SELECT * FROM recurring WHERE last_date <= ?", new String[]{format});
        while (rawQuery.moveToNext()) {
            arrayList.add(getRecurringModelFromCursor(rawQuery));
        }
        rawQuery.close();
        return arrayList;
    }

    public long addUpdateTemplateData(TemplateBean templateBean) {
        ContentValues contentValuesFromTemplateModel = getContentValuesFromTemplateModel(templateBean);
        if (TextUtils.isEmpty(templateBean.getId())) {
            return this.f70db.insert(TemplatesTable.TABLE_NAME, (String) null, contentValuesFromTemplateModel);
        }
        this.f70db.update(TemplatesTable.TABLE_NAME, contentValuesFromTemplateModel, "id=?", new String[]{templateBean.getId()});
        return 0;
    }

    public void addcolumn() {
        this.f70db.execSQL("ALTER TABLE categories ADD COLUMN drag_id INTEGER");
        Cursor rawQuery = this.f70db.rawQuery("SELECT DISTINCT account_id FROM categories", (String[]) null);
        PrintStream printStream = System.out;
        printStream.println("Total account" + rawQuery.getCount());
        Log.e("Total account:", "" + rawQuery.getCount());
        int i = 0;
        while (i < rawQuery.getCount()) {
            i++;
            this.f70db.execSQL(Category.CREATE_TABLE_SQL);
            List<CategoryBean> oldCategories = getOldCategories(Category.TABLE_NAME, 2, "account_id", "" + i);
            List<CategoryBean> oldCategories2 = getOldCategories(Category.TABLE_NAME, 1, "account_id", "" + i);
            for (int i2 = 0; i2 < oldCategories.size(); i2++) {
                CategoryBean categoryBean = oldCategories.get(i2);
                categoryBean.setDrag_id(i2);
                addUpdateCategoryData(categoryBean);
            }
            for (int i3 = 0; i3 < oldCategories2.size(); i3++) {
                CategoryBean categoryBean2 = oldCategories2.get(i3);
                categoryBean2.setDrag_id(i3);
                addUpdateCategoryData(categoryBean2);
            }
        }
    }

    private List<CategoryBean> getOldCategories(String str, int i, String str2, String str3) {
        String[] strArr = {"" + str3};
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM " + str + " WHERE " + Category.CAT_TYPE + " =" + i + " AND account_id=?", strArr);
        while (rawQuery.moveToNext()) {
            CategoryBean categoryBean = new CategoryBean();
            categoryBean.setId(rawQuery.getString(rawQuery.getColumnIndex(Category.CAT_ID)));
            categoryBean.setName(rawQuery.getString(rawQuery.getColumnIndex(Category.CAT_NAME)));
            categoryBean.setColor(rawQuery.getString(rawQuery.getColumnIndex(Category.CAT_COLOR)));
            categoryBean.setAccountRef(rawQuery.getString(rawQuery.getColumnIndex("account_id")));
            categoryBean.setCategoryGroup(i);
            arrayList.add(categoryBean);
        }
        rawQuery.close();
        return arrayList;
    }

    private ContentValues getContentValuesFromTemplateModel(TemplateBean templateBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TemplatesTable.TEMPLATES, templateBean.getTemplate());
        contentValues.put("account_id", templateBean.getAccountRef());
        return contentValues;
    }

    public void deleteTemplateData(TemplateBean templateBean) {
        if (!TextUtils.isEmpty(templateBean.getId())) {
            this.f70db.delete(TemplatesTable.TABLE_NAME, "id=?", new String[]{templateBean.getId()});
        }
    }

    public TemplateBean getTemplateModelById(String str) {
        TemplateBean templateBean = null;
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM msg_templates WHERE id=" + str, (String[]) null);
        if (rawQuery.moveToFirst()) {
            templateBean = getTemplateModelFromCursor(rawQuery);
        }
        rawQuery.close();
        return templateBean;
    }

    private TemplateBean getTemplateModelFromCursor(Cursor cursor) {
        TemplateBean templateBean = new TemplateBean();
        templateBean.setId(cursor.getString(cursor.getColumnIndex("id")));
        templateBean.setTemplate(cursor.getString(cursor.getColumnIndex(TemplatesTable.TEMPLATES)));
        templateBean.setAccountRef(cursor.getString(cursor.getColumnIndex("account_id")));
        return templateBean;
    }

    public long addUpdateAccountData(AccountBean accountBean) {
        ContentValues contentValuesFromAccountModel = getContentValuesFromAccountModel(accountBean);
        if (TextUtils.isEmpty(accountBean.getId())) {
            return this.f70db.insert(AccountTable.TABLE_NAME, (String) null, contentValuesFromAccountModel);
        }
        this.f70db.update(AccountTable.TABLE_NAME, contentValuesFromAccountModel, "id=?", new String[]{accountBean.getId()});
        return 0;
    }

    private ContentValues getContentValuesFromAccountModel(AccountBean accountBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", accountBean.getName());
        contentValues.put("email", accountBean.getEmail());
        contentValues.put(AccountTable.CURRENCY, "" + accountBean.getCurrency());
        return contentValues;
    }

    public void deleteAccountData(AccountBean accountBean) {
        if (!TextUtils.isEmpty(accountBean.getId())) {
            this.f70db.delete(ManagerTable.TABLE_NAME, "account_id=?", new String[]{accountBean.getId()});
            this.f70db.delete(RecurringTable.TABLE_NAME, "account_id=?", new String[]{accountBean.getId()});
            this.f70db.delete(TemplatesTable.TABLE_NAME, "account_id=?", new String[]{accountBean.getId()});
            this.f70db.delete(Category.TABLE_NAME, "account_id=?", new String[]{accountBean.getId()});
            this.f70db.delete(AccountTable.TABLE_NAME, "id=?", new String[]{accountBean.getId()});
        }
    }

    public AccountBean getAccountModelById(String str) {
        AccountBean accountBean = null;
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM account WHERE id=" + str, (String[]) null);
        if (rawQuery.moveToFirst()) {
            accountBean = getAccountModelFromCursor(rawQuery);
        }
        rawQuery.close();
        return accountBean;
    }

    public AccountBean getAccountModelByName(String str) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM account WHERE name=?", new String[]{"" + str});
        AccountBean accountModelFromCursor = rawQuery.moveToFirst() ? getAccountModelFromCursor(rawQuery) : null;
        rawQuery.close();
        return accountModelFromCursor;
    }

    private AccountBean getAccountModelFromCursor(Cursor cursor) {
        AccountBean accountBean = new AccountBean();
        accountBean.setId(cursor.getString(cursor.getColumnIndex("id")));
        accountBean.setName(cursor.getString(cursor.getColumnIndex("name")));
        accountBean.setEmail(cursor.getString(cursor.getColumnIndex("email")));
        String string = cursor.getString(cursor.getColumnIndex(AccountTable.CURRENCY));
        if (TextUtils.isEmpty(string)) {
            accountBean.setCurrency(0);
        } else {
            accountBean.setCurrency(Integer.parseInt(string));
        }
        return accountBean;
    }

    public List<AccountBean> getAccountList() {
        this.f70db.execSQL("CREATE TABLE IF NOT EXISTS account(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL, currency TEXT NOT NULL )");
        ArrayList arrayList = new ArrayList();
        try {
            Cursor rawQuery = this.f70db.rawQuery("SELECT * FROM account", (String[]) null);
            while (rawQuery.moveToNext()) {
                arrayList.add(getAccountModelFromCursor(rawQuery));
            }
            rawQuery.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public List<TemplateBean> getTemplateList() {
        ArrayList arrayList = new ArrayList();
        Cursor rawQuery = this.f70db.rawQuery("SELECT * FROM msg_templates", (String[]) null);
        while (rawQuery.moveToNext()) {
            arrayList.add(getTemplateModelFromCursor(rawQuery));
        }
        rawQuery.close();
        return arrayList;
    }

    public List<TemplateBean> getTemplateListByAccount() {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM msg_templates WHERE account_id=" + this.accountId, (String[]) null);
        while (rawQuery.moveToNext()) {
            arrayList.add(getTemplateModelFromCursor(rawQuery));
        }
        rawQuery.close();
        return arrayList;
    }

    public Collection<? extends RecurringBean> getRecurringModelList(int i, String str) {
        ArrayList arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM recurring m INNER JOIN categories e ON e.cat_id=m.sub_cat WHERE m.cat=" + i + " AND m.account_id=" + this.accountId + " AND e." + Category.CAT_ID + "=" + str + " ORDER BY " + RecurringTable.RECURRING_LASTDATE + " DESC", (String[]) null);
        while (rawQuery.moveToNext()) {
            RecurringBean recurringModelFromCursor = getRecurringModelFromCursor(rawQuery);
            CategoryBean categoryModelFromCursor = getCategoryModelFromCursor(rawQuery);
            categoryModelFromCursor.setCategoryGroup(i);
            recurringModelFromCursor.setCategoryModel(categoryModelFromCursor);
            arrayList.add(recurringModelFromCursor);
        }
        rawQuery.close();
        return arrayList;
    }

    public long getCategoryIdFromData(CategoryBean categoryBean) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        String[] strArr = {Category.CAT_ID};
        Cursor query = sQLiteDatabase.query(Category.TABLE_NAME, strArr, "cat_type=? AND account_id=? AND cat_name=? ", new String[]{"" + categoryBean.getCategoryGroup(), categoryBean.getAccountRef(), categoryBean.getName()}, (String) null, (String) null, (String) null);
        long parseLong = query.moveToFirst() ? Long.parseLong(query.getString(0)) : 0;
        query.close();
        return parseLong;
    }

    public void clearDatabase() {
        this.f70db.execSQL(AccountTable.DROP_TABLE_SQL);
        this.f70db.execSQL(TemplatesTable.DROP_TABLE_SQL);
        this.f70db.execSQL(Category.DROP_TABLE_SQL);
        this.f70db.execSQL(ManagerTable.DROP_TABLE_SQL);
        this.f70db.execSQL(RecurringTable.DROP_TABLE_SQL);
        this.f70db.execSQL("CREATE TABLE IF NOT EXISTS account(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL, currency TEXT NOT NULL )");
        this.f70db.execSQL(TemplatesTable.CREATE_TABLE_SQL);
        this.f70db.execSQL(Category.CREATE_TABLE_SQL);
        this.f70db.execSQL(ManagerTable.CREATE_TABLE_SQL);
        this.f70db.execSQL(RecurringTable.CREATE_TABLE_SQL);
    }

    public int DBVersion() {
        return this.f70db.getVersion();
    }

    public List<DataBean> getManagerDataByTimeAndCategory(int i, int i2, String str, String str2) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("select * from manager m inner join categories e on e.cat_id = m.sub_cat where ( m.cat= ? ) and ( e.cat_id = ? ) and ( date BETWEEN ? and ? )  AND m.account_id=" + this.accountId + " order by date desc", new String[]{String.valueOf(i), String.valueOf(i2), str, str2});
        ArrayList arrayList = new ArrayList();
        while (rawQuery.moveToNext()) {
            DataBean managerModelFromCursor = getManagerModelFromCursor(rawQuery);
            CategoryBean categoryModelFromCursor = getCategoryModelFromCursor(rawQuery);
            categoryModelFromCursor.setCategoryGroup(i);
            managerModelFromCursor.setCategoryModel(categoryModelFromCursor);
            if (TextUtils.isEmpty(managerModelFromCursor.getRefRecurring())) {
                TextUtils.isDigitsOnly(managerModelFromCursor.getRefRecurring());
            }
            arrayList.add(managerModelFromCursor);
        }
        rawQuery.close();
        return arrayList;
    }

    public int isFieldExist(String str) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        return sQLiteDatabase.rawQuery("PRAGMA table_info(" + str + ")", (String[]) null).getColumnCount();
    }

    public boolean isFieldExistName(String str, String str2) {
        SQLiteDatabase sQLiteDatabase = this.f70db;
        Cursor rawQuery = sQLiteDatabase.rawQuery("PRAGMA table_info(" + str + ")", (String[]) null);
        rawQuery.moveToFirst();
        boolean z = false;
        do {
            if (rawQuery.getString(1).equals(str2)) {
                z = true;
            }
        } while (rawQuery.moveToNext());
        return z;
    }

    public void addField(String str) {
        this.f70db.execSQL(str);
    }

    public class ManagerDBHelper extends SQLiteOpenHelper {
        public ManagerDBHelper(Context context) {
            super(context, MyDatabaseHandler.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 3);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            SQLiteDatabase unused = MyDatabaseHandler.this.f70db = sQLiteDatabase;
            MyDatabaseHandler.this.f70db.execSQL(ManagerTable.CREATE_TABLE_SQL);
            MyDatabaseHandler.this.f70db.execSQL(RecurringTable.CREATE_TABLE_SQL);
            MyDatabaseHandler.this.f70db.execSQL(Category.CREATE_TABLE_SQL);
            MyDatabaseHandler.this.f70db.execSQL(TemplatesTable.CREATE_TABLE_SQL);
            MyDatabaseHandler.this.f70db.execSQL("CREATE TABLE IF NOT EXISTS account(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL, currency TEXT NOT NULL )");
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            SQLiteDatabase unused = MyDatabaseHandler.this.f70db = sQLiteDatabase;
            if (!MyDatabaseHandler.this.isFieldExistName(ManagerTable.TABLE_NAME, "amount_limit")) {
                MyDatabaseHandler.this.addField(ManagerTable.ALTER_TABLE_CATLIMIT_V2);
            }
            if (!MyDatabaseHandler.this.isFieldExistName(ManagerTable.TABLE_NAME, "method")) {
                MyDatabaseHandler.this.addField(ManagerTable.ALTER_TABLE_METHOD_V2);
            }
            if (!MyDatabaseHandler.this.isFieldExistName(ManagerTable.TABLE_NAME, "image")) {
                MyDatabaseHandler.this.addField(ManagerTable.ALTER_TABLE_IMAGE_V2);
            }
            if (!MyDatabaseHandler.this.isFieldExistName(RecurringTable.TABLE_NAME, "amount_limit")) {
                MyDatabaseHandler.this.addField(RecurringTable.ALTER_TABLE_CATLIMIT_V2);
            }
            if (!MyDatabaseHandler.this.isFieldExistName(RecurringTable.TABLE_NAME, "method")) {
                MyDatabaseHandler.this.addField(RecurringTable.ALTER_TABLE_METHOD_V2);
            }
            if (!MyDatabaseHandler.this.isFieldExistName(RecurringTable.TABLE_NAME, "image")) {
                MyDatabaseHandler.this.addField(RecurringTable.ALTER_TABLE_IMAGE_V2);
            }
            if (!MyDatabaseHandler.this.isFieldExistName(Category.TABLE_NAME, "amount_limit")) {
                MyDatabaseHandler.this.addField(Category.ALTER_TABLE_CATLIMIT_V2);
            }
            onCreate(MyDatabaseHandler.this.f70db);
        }

        private void updateDatabaseFromVerson6() {
            Cursor rawQuery = MyDatabaseHandler.this.f70db.rawQuery("SELECT DISTINCT account_id FROM categories", (String[]) null);
            PrintStream printStream = System.out;
            printStream.println("Total account" + rawQuery.getCount());
            Log.e("Total account:", "" + rawQuery.getCount());
            int i = 0;
            while (i < rawQuery.getCount()) {
                i++;
                MyDatabaseHandler.this.f70db.execSQL(Category.CREATE_TABLE_SQL);
                List<CategoryBean> oldCategories = getOldCategories(Category.TABLE_NAME, 2, "account_id", "" + i);
                List<CategoryBean> oldCategories2 = getOldCategories(Category.TABLE_NAME, 1, "account_id", "" + i);
                for (int i2 = 0; i2 < oldCategories.size(); i2++) {
                    CategoryBean categoryBean = oldCategories.get(i2);
                    categoryBean.setDrag_id(i2);
                    MyDatabaseHandler.this.addUpdateCategoryData(categoryBean);
                }
                for (int i3 = 0; i3 < oldCategories2.size(); i3++) {
                    CategoryBean categoryBean2 = oldCategories2.get(i3);
                    categoryBean2.setDrag_id(i3);
                    MyDatabaseHandler.this.addUpdateCategoryData(categoryBean2);
                }
            }
        }

        private void updateDatabaseFromVerson5() {
            MyDatabaseHandler.this.f70db.execSQL(TemplatesTable.CREATE_TABLE_SQL);
            MyDatabaseHandler.this.f70db.execSQL("CREATE TABLE IF NOT EXISTS account(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL, currency TEXT NOT NULL )");
            MyDatabaseHandler.this.f70db.execSQL(Category.CREATE_TABLE_SQL);
            MyDatabaseHandler.this.f70db.execSQL(ManagerTable.ALTER_TABLE_V7);
            MyDatabaseHandler.this.f70db.execSQL(RecurringTable.ALTER_TABLE_V7);
            List<CategoryBean> oldCategories = getOldCategories(Category.INCOME_TABLE_NAME, 2, "account_id");
            List<CategoryBean> oldCategories2 = getOldCategories(Category.EXPENSE_TABLE_NAME, 1, "account_id");
            MyDatabaseHandler.this.f70db.execSQL("DROP TABLE IF EXISTS income_cat");
            MyDatabaseHandler.this.f70db.execSQL("DROP TABLE IF EXISTS expence_cat");
            for (CategoryBean categoryBean : oldCategories2) {
                updateCategtoryDataAndReference(categoryBean);
            }
            for (CategoryBean categoryBean2 : oldCategories) {
                updateCategtoryDataAndReference(categoryBean2);
            }
        }

        private void updateCategtoryDataAndReference(CategoryBean categoryBean) {
            int categoryGroup = categoryBean.getCategoryGroup();
            String id = categoryBean.getId();
            categoryBean.setId("");
            String str = "" + MyDatabaseHandler.this.addUpdateCategoryData(categoryBean);
            MyDatabaseHandler.this.f70db.execSQL("UPDATE manager SET sub_cat=" + str + " WHERE cat=" + String.valueOf(categoryGroup) + " AND sub_cat=" + id);
            MyDatabaseHandler.this.f70db.execSQL("UPDATE recurring SET sub_cat=" + str + " WHERE cat=" + String.valueOf(categoryGroup) + " AND sub_cat=" + id);
        }

        private List<CategoryBean> getOldCategories(String str, int i, String str2, String str3) {
            String[] strArr = {"" + str3};
            ArrayList arrayList = new ArrayList();
            SQLiteDatabase a2 = MyDatabaseHandler.this.f70db;
            Cursor rawQuery = a2.rawQuery("SELECT * FROM " + str + " WHERE " + Category.CAT_TYPE + " =" + i + " AND account_id=?", strArr);
            while (rawQuery.moveToNext()) {
                CategoryBean categoryBean = new CategoryBean();
                categoryBean.setId(rawQuery.getString(rawQuery.getColumnIndex(Category.CAT_ID)));
                categoryBean.setName(rawQuery.getString(rawQuery.getColumnIndex(Category.CAT_NAME)));
                categoryBean.setColor(rawQuery.getString(rawQuery.getColumnIndex(Category.CAT_COLOR)));
                categoryBean.setAccountRef(rawQuery.getString(rawQuery.getColumnIndex("account_id")));
                categoryBean.setAccountRef(rawQuery.getString(rawQuery.getColumnIndex("amount_limit")));
                categoryBean.setCategoryGroup(i);
                arrayList.add(categoryBean);
            }
            rawQuery.close();
            return arrayList;
        }

        private List<CategoryBean> getOldCategories(String str, int i, String str2) {
            ArrayList arrayList = new ArrayList();
            SQLiteDatabase a2 = MyDatabaseHandler.this.f70db;
            Cursor rawQuery = a2.rawQuery("SELECT * FROM " + str + " WHERE " + Category.CAT_TYPE + " =" + i + " AND account_id=?", new String[]{"2"});
            while (rawQuery.moveToNext()) {
                CategoryBean categoryBean = new CategoryBean();
                categoryBean.setId(rawQuery.getString(rawQuery.getColumnIndex(Category.CAT_ID)));
                categoryBean.setName(rawQuery.getString(rawQuery.getColumnIndex(Category.CAT_NAME)));
                categoryBean.setColor(rawQuery.getString(rawQuery.getColumnIndex(Category.CAT_COLOR)));
                categoryBean.setAccountRef(rawQuery.getString(rawQuery.getColumnIndex("account_id")));
                categoryBean.setAccountRef(rawQuery.getString(rawQuery.getColumnIndex("amount_limit")));
                categoryBean.setCategoryGroup(i);
                arrayList.add(categoryBean);
            }
            rawQuery.close();
            return arrayList;
        }

        private void updateDatabaseToVersion3() {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(MyDatabaseHandler.this.getRecurringModelList(1));
            arrayList.addAll(MyDatabaseHandler.this.getRecurringModelList(2));
            for (int i = 0; i < arrayList.size(); i++) {
                RecurringBean recurringBean = (RecurringBean) arrayList.get(i);
                Date date = new Date(Long.parseLong(recurringBean.getRecurringLastdate()));
                recurringBean.setRecurringLastdate(MyUtils.sdfDatabase.format(date));
                recurringBean.setRecurringDate("" + date.getDate());
                MyDatabaseHandler.this.addUpdateRecurringData(recurringBean);
            }
        }
    }
}
