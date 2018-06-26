package digitalcard.digitalcard.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.Model.UserAccount;
import digitalcard.digitalcard.Util.Utilities;

public class AccountDB extends SQLiteOpenHelper {
    public AccountDB(Context context) {
        super(context, Utilities.DATABASE_ACCOUNT_NAME, null, Utilities.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CARD_TABLE = "CREATE TABLE " + Utilities.TABLE_ACCOUNT_LIST + "("
                + Utilities.id_key + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Utilities.account_name + " TEXT, "
                + Utilities.account_email + " TEXT, "
                + Utilities.account_dob + " TEXT, "
                + Utilities.account_identity_number + " TEXT, "
                + Utilities.account_address + " TEXT, "
                + Utilities.account_phone_number + " TEXT " + ")";
        db.execSQL(CREATE_CARD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF " + Utilities.TABLE_ACCOUNT_LIST);
        onCreate(db);
    }

    public void addAccount(UserAccount account){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Utilities.account_name, account.getName());
        values.put(Utilities.account_email, account.getEmail());
        values.put(Utilities.account_dob, account.getDob());
        values.put(Utilities.account_identity_number, account.getIdentityNumber());
        values.put(Utilities.account_address, account.getAddress());
        values.put(Utilities.account_phone_number, account.getPhoneNumber());

        db.insert(Utilities.TABLE_ACCOUNT_LIST,  null, values);
        db.close();
    }

    public void updateAccount(UserAccount account){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Utilities.account_name, account.getName());
        values.put(Utilities.account_email, account.getEmail());
        values.put(Utilities.account_dob, account.getDob());
        values.put(Utilities.account_identity_number, account.getIdentityNumber());
        values.put(Utilities.account_address, account.getAddress());
        values.put(Utilities.account_phone_number, account.getPhoneNumber());

        db.update(Utilities.TABLE_ACCOUNT_LIST, values,"id=" + account.getId(), null );
        db.close();
    }

    public void deleteAccount(int account) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Utilities.TABLE_ACCOUNT_LIST, Utilities.id_key + " = ?",
                new String[]{String.valueOf(account)});
        db.close();
    }

    public List<UserAccount> getAccount(){
        List<UserAccount> data = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Utilities.TABLE_ACCOUNT_LIST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserAccount account = /**/new UserAccount();
                account.setId(Integer.parseInt(cursor.getString(0)));
                account.setName(cursor.getString(2));
                account.setEmail(cursor.getString(1));
                account.setDob(cursor.getString(3));
                account.setIdentityNumber(cursor.getString(4));
                account.setAddress(cursor.getString(5));
                account.setPhoneNumber(cursor.getString(6));

                data.add(account);
            } while (cursor.moveToNext());
        }

        return data;
    }
}
