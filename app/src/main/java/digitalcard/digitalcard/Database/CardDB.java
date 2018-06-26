package digitalcard.digitalcard.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import digitalcard.digitalcard.Model.CardList;
import digitalcard.digitalcard.Util.Utilities;

public class CardDB extends SQLiteOpenHelper {
    private Context context;

    public CardDB(Context context) {
        super(context, Utilities.DATABASE_NAME, null, Utilities.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CARD_TABLE = "CREATE TABLE " + Utilities.TABLE_CARDLIST + "("
                + Utilities.id_key + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Utilities.card_type + " TEXT, "
                + Utilities.card_name + " TEXT, "
                + Utilities.barcode_number + " TEXT, "
                + Utilities.card_logo + " TEXT, "
                + Utilities.card_background + " INTEGER, "
                + Utilities.notes + " TEXT, "
                + Utilities.front_view + " TEXT, "
                + Utilities.back_view + " TEXT " + ")";
        db.execSQL(CREATE_CARD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF " + Utilities.TABLE_CARDLIST);
        onCreate(db);
    }

    public void addCard(CardList cardList){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Utilities.card_name, cardList.getCardName());
        values.put(Utilities.card_type, cardList.getCardType());
        values.put(Utilities.barcode_number, cardList.getBarcodeNumber());
        values.put(Utilities.card_logo, cardList.getCardIcon());
        values.put(Utilities.card_background, cardList.getCardBackground());
        values.put(Utilities.notes, cardList.getCardNote());
        values.put(Utilities.front_view, cardList.getCardFrontView());
        values.put(Utilities.back_view, cardList.getCardBackView());

        db.insert(Utilities.TABLE_CARDLIST,  null,values);
        db.close();
    }

    public void updateCard(CardList cardList){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Utilities.card_name, cardList.getCardName());
        values.put(Utilities.card_type, cardList.getCardType());
        values.put(Utilities.barcode_number, cardList.getBarcodeNumber());
        values.put(Utilities.card_logo, cardList.getCardIcon());
        values.put(Utilities.card_background, cardList.getCardBackground());
        values.put(Utilities.notes, cardList.getCardNote());
        values.put(Utilities.front_view, cardList.getCardFrontView());
        values.put(Utilities.back_view, cardList.getCardBackView());

        db.update(Utilities.TABLE_CARDLIST, values,"id=" + cardList.getId(), null );
        db.close();
    }

    public void deleteCard(CardList cardList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Utilities.TABLE_CARDLIST, Utilities.id_key + " = ?",
                new String[]{String.valueOf(cardList.getId())});
        db.close();
    }

    public List<CardList> getAllCardList(){
        List<CardList> data = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Utilities.TABLE_CARDLIST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CardList cardList = /**/new CardList();
                cardList.setId(Integer.parseInt(cursor.getString(0)));
                cardList.setCardName(cursor.getString(2));
                cardList.setCardType(cursor.getString(1));
                cardList.setBarcodeNumber(cursor.getString(3));
                cardList.setCardIcon(cursor.getString(4));
                cardList.setCardBackground(cursor.getInt(5));
                cardList.setCardNote(cursor.getString(6));
                cardList.setCardFrontView(cursor.getString(7));
                cardList.setCardBackView(cursor.getString(8));

                data.add(cardList);
            } while (cursor.moveToNext());
        }

        return data;
    }
}
