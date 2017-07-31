package com.example.teamengineer.myengineer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pawel on 29.07.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ShoppingListApp";

    // Table Names
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_FRIENDS_LISTS = "friends_lists";
    private static final String TABLE_FRIENDS_LISTS_USER = "friends_lists_user";
    private static final String TABLE_ICONS = "icons";
    private static final String TABLE_NON_STANDARD_PRODUCTS = "non_standard_products";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_SHOPPING_GROUPS = "shopping_groups";
    private static final String TABLE_SHOPPING_GROUPS_USERS = "shopping_groups_users";
    private static final String TABLE_SHOPPING_LISTS = "shopping_lists";
    private static final String TABLE_SHOPPING_LISTS_PRODUCTS = "shopping_lists_products";
    private static final String TABLE_SHOPPING_LISTS_USERS = "shopping_lists_users";
    private static final String TABLE_USERS = "users";


    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ID_USER = "idUser";
    private static final String KEY_ADD_DATE = "addDate";
    private static final String KEY_MEASURE = "measure";
    private static final String KEY_MODIFY_DATE = "modifyDate";
    private static final String KEY_ID_ICON = "idIcon";
    private static final String KEY_CREATE_DATE = "createDate";
    private static final String KEY_ID_SHOPPING_GROUP = "idShoppingGroup";
    private static final String KEY_ID_SHOPPING_LIST = "idShoppingList";

    // CATEGORIES Table - column names
            private static final String KEY_ID_PARENT = "idParent";
            //private static final String KEY_NAME = "name";
            //private static final String KEY_DESCRIPTION = "description";

    // FRIENDS_LISTS Table - column names
            //private static final String KEY_ID_USER = "idUser";

    // FRIENDS_LISTS_USERS Table - column names
            //private static final String KEY_ID_USER = "idUser";
            private static final String KEY_ID_FRIENDS_LIST = "idFriendsList";
            //private static final String KEY_ADD_DATE = "addDate";

    // ICONS Table - column names
            //private static final String KEY_NAME = "name";
            private static final String KEY_ICON = "icon";

    // NON_STANDARD_PRODUCTS Table - column names
            //private static final String KEY_NAME = "name";
            //private static final String KEY_DESCRIPTION = "description";
            //private static final String KEY_MEASURE = "measure";
            //private static final String KEY_ADD_DATE = "addDate";
            //private static final String KEY_MODIFY_DATE = "modifyDate";
            //private static final String KEY_ID_USER = "idUser";
            //private static final String KEY_ID_ICON = "idIcon";

    // PRODUCTS Table - column names
            //private static final String KEY_NAME = "name";
            //private static final String KEY_DESCRIPTION = "description";
            private static final String KEY_ID_CATEGORY = "idCategory";
            //private static final String KEY_MEASURE = "measure";

    // SHOPPING_GROUPS Table - column names
            //private static final String KEY_NAME = "name";
            //private static final String KEY_DESCRIPTION = "description";
            //private static final String KEY_CREATE_DATE = "createDate";
            //private static final String KEY_ID_ICON = "idIcon";

    // SHOPPING_GROUPS_USERS Table - column names
            //private static final String KEY_ID_SHOPPING_GROUP = "idShoppingGroup";
            //private static final String KEY_ID_USER = "idUser";
            private static final String KEY_USER_ADD_DATE = "userAddDate";
            private static final String KEY_PSEUDONYM = "pseudonym";
            private static final String KEY_WHETHER_ADMIN_GROUP = "whetherAdminGroup";

    // SHOPPING_LISTS Table - column names
            //private static final String KEY_ID_SHOPPING_GROUP = "idShoppingGroup";
            //private static final String KEY_ID_ICON = "idIcon";
            //private static final String KEY_CREATE_DATE = "createDate";
            private static final String KEY_LIST_STATUS = "listStatus";
            private static final String KEY_LAST_SYNCHRONIZATION = "lastSynchronization";
            //private static final String KEY_MODIFY_DATE = "modifyDate";

    // SHOPPING_LISTS_PRODUCTS Table - column names
            //private static final String KEY_ID_SHOPPING_LIST = "idShoppingList";
            private static final String KEY_ID_PRODUCT = "idProduct";
            private static final String KEY_PRODUCT_TYPE = "productType";
            private static final String KEY_PRICE = "price";
            //private static final String KEY_DESCRIPTION = "description";
            private static final String KEY_STATUS = "status";
            //private static final String KEY_ADD_DATE = "addDate";
            //private static final String KEY_MODIFY_DATE = "modifyDate";
            private static final String KEY_QUANTITY = "quantity";

    // SHOPPING_LISTS_USERS Table - column names
            //private static final String KEY_ID_SHOPPING_LIST = "idShoppingList";
            //private static final String KEY_ID_USER = "idUser";
            private static final String KEY_SYNC_TIME = "syncTime";

    // USERS Table - column names
            private static final String KEY_EMAIL = "email";
            private static final String KEY_PASSWORD = "password";
            private static final String KEY_REGISTER_DATE = "registerDate";
            private static final String KEY_LAST_LOGIN_DATE = "lastLoginDate";
            //private static final String KEY_ID_ICON = "idIcon";
            private static final String KEY_STAY_LOGGED = "stayLogged";

    // Table Create Statements
    // CATEGORIES table create statement
    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE "
            + TABLE_CATEGORIES + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ID_PARENT
            + " INTEGER," + KEY_NAME + " TEXT," + KEY_DESCRIPTION
            + " TEXT" + ")";

    // FRIENDS_LISTS table create statement
    private static final String CREATE_TABLE_FRIENDS_LISTS = "CREATE TABLE " + TABLE_FRIENDS_LISTS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ID_USER + " INTEGER" + ")";

    // FRIENDS_LISTS_USERS table create statement
    private static final String CREATE_TABLE_FRIENDS_LISTS_USERS = "CREATE TABLE "
            + TABLE_FRIENDS_LISTS_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_USER + " INTEGER," + KEY_ID_FRIENDS_LIST + " INTEGER,"
            + KEY_USER_ADD_DATE + " DATETIME" + ")";
    // ICONS table create statement
    private static final String CREATE_TABLE_ICONS = "CREATE TABLE "
            + TABLE_ICONS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT," + KEY_ICON + " BLOB" + ")";
    // NON_STANDARD_PRODUCTS table create statement
    private static final String CREATE_TABLE_NON_STANDARD_PRODUCTS = "CREATE TABLE "
            + TABLE_NON_STANDARD_PRODUCTS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT," + KEY_DESCRIPTION + " TEXT,"
            + KEY_ID_USER + " INTEGER,"+ KEY_MEASURE + " TEXT," + KEY_ADD_DATE + " DATETIME," + KEY_MODIFY_DATE + " DATETIME" + ")";
    // PRODUCTS table create statement
    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE "
            + TABLE_PRODUCTS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT," + KEY_DESCRIPTION + " TEXT,"
            + KEY_ID_CATEGORY + " INTEGER," + KEY_MEASURE + " TEXT" +")";
    // SHOPPING_GROUPS table create statement
    private static final String CREATE_TABLE_SHOPPING_GROUPS = "CREATE TABLE "
            + TABLE_SHOPPING_GROUPS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT," + KEY_DESCRIPTION + " TEXT,"
            + KEY_ADD_DATE + " DATETIME," + KEY_ID_ICON + " INTEGER" +")";
    // SHOPPING_GROUPS_USERS table create statement
    private static final String CREATE_TABLE_SHOPPING_GROUPS_USERS = "CREATE TABLE "
            + TABLE_SHOPPING_GROUPS_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_SHOPPING_GROUP + " INTEGER," + KEY_ID_USER + " INTEGER,"
            + KEY_USER_ADD_DATE + " DATETIME," + KEY_PSEUDONYM + " TEXT," + KEY_WHETHER_ADMIN_GROUP + " INTEGER" + ")";
    // SHOPPING_LISTS table create statement
    private static final String CREATE_TABLE_SHOPPING_LISTS = "CREATE TABLE "
            + TABLE_SHOPPING_LISTS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_SHOPPING_GROUP + " INTEGER," + KEY_CREATE_DATE + " DATETIME,"
            + KEY_STATUS + " TEXT," + KEY_LAST_SYNCHRONIZATION + " DATETIME," + KEY_MODIFY_DATE + " DATETIME" +")";
    // SHOPPING_LISTS_PRODUCTS table create statement
    private static final String CREATE_TABLE_SHOPPING_LISTS_PRODUCTS = "CREATE TABLE "
            + TABLE_SHOPPING_LISTS_PRODUCTS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_SHOPPING_LIST + " INTEGER," + KEY_ID_PRODUCT + " INTEGER,"
            + KEY_PRODUCT_TYPE + " INTEGER," + KEY_PRICE + " REAL," + KEY_DESCRIPTION + " TEXT," + KEY_STATUS + " INTEGER," + KEY_ADD_DATE + " DATETIME," + KEY_MODIFY_DATE + " DATETIME," + KEY_QUANTITY + " INTEGER" +")";
    // SHOPPING_LISTS_USERS table create statement
    private static final String CREATE_TABLE_SHOPPING_LISTS_USERS = "CREATE TABLE "
            + TABLE_SHOPPING_LISTS_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_SHOPPING_LIST + " INTEGER," + KEY_ID_USER + " INTEGER,"
            + KEY_SYNC_TIME + " DATETIME" +")";
    // USERS table create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_EMAIL + " TEXT," + KEY_PASSWORD + " TEXT,"
            + KEY_REGISTER_DATE + " DATETIME," + KEY_LAST_LOGIN_DATE + " DATETIME," + KEY_ID_ICON + " INTEGER," + KEY_STAY_LOGGED + " INTEGER" +")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_FRIENDS_LISTS);
        db.execSQL(CREATE_TABLE_FRIENDS_LISTS_USERS);
        db.execSQL(CREATE_TABLE_ICONS);
        db.execSQL(CREATE_TABLE_NON_STANDARD_PRODUCTS);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_SHOPPING_GROUPS);
        db.execSQL(CREATE_TABLE_SHOPPING_GROUPS_USERS);
        db.execSQL(CREATE_TABLE_SHOPPING_LISTS);
        db.execSQL(CREATE_TABLE_SHOPPING_LISTS_PRODUCTS);
        db.execSQL(CREATE_TABLE_SHOPPING_LISTS_USERS);
        db.execSQL(CREATE_TABLE_USERS);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS_LISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS_LISTS_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ICONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NON_STANDARD_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_GROUPS_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LISTS_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LISTS_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }
}
