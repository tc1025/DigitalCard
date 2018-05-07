package digitalcard.digitalcard.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import digitalcard.digitalcard.Model.CardList;

public class CardDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tableCardList";
    private static final String TABLE_CARDLIST = "cardList";

    private static final String id_key = "id";
    private static final String card_name = "card_names";
    private static final String card_type = "card_types";
    private static final String barcode_number = "barcodeNumber";
    private static final String notes = "notes";
    private static final String front_view = "frontView";
    private static final String back_view = "backView";

    private Context context;

    public CardDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CARD_TABLE = "CREATE TABLE " + TABLE_CARDLIST + "("
                + id_key + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + card_type + " TEXT, "
                + card_name + " TEXT, "
                + barcode_number + " TEXT"+ ")";
        db.execSQL(CREATE_CARD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF " + TABLE_CARDLIST);
        onCreate(db);
    }

    public void addCard(CardList cardList){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(card_name, cardList.getCardName());
        values.put(card_type, cardList.getCardType());
        values.put(barcode_number, cardList.getBarcodeNumber());

        db.insert(TABLE_CARDLIST,  null,values);
        db.close();
    }

    public void updateCard(CardList cardList){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(card_name, cardList.getCardName());
        values.put(card_type, cardList.getCardType());
        values.put(barcode_number, cardList.getBarcodeNumber());

        db.update(TABLE_CARDLIST, values,"id=" + id_key, null );
        db.close();
    }

    public void deleteCard(CardList cardList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARDLIST, id_key + " = ?",
                new String[]{String.valueOf(cardList.getId())});
        db.close();
    }

    public List<CardList> getAllCardList(){
        List<CardList> data = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CARDLIST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CardList cardList = /**/new CardList();
                cardList.setId(Integer.parseInt(cursor.getString(0)));
                cardList.setCardName(cursor.getString(2));
                cardList.setCardType(cursor.getString(1));
                cardList.setBarcodeNumber(cursor.getString(3));

                data.add(cardList);
            } while (cursor.moveToNext());
        }

        return data;
    }
}
