package digitalcard.digitalcard.Model;

import android.support.annotation.Nullable;

/**
 * Created by viks on 17/03/2018.
 */

public class CardList {
    public String cardCategory;
    public int cardIcon;
    public String cardName;
    public String cardType;
    public String barcodeNumber;
    public int id;

    public CardList() {}

    public CardList(String cardName, String cardType, String barcodeNumber) {
        this.cardName = cardName;
        this.cardType = cardType;
        this.barcodeNumber = barcodeNumber;
    }

    public CardList(int id, String cardType, String cardName, String BarcodeNumber) {
        this.cardName = cardName;
        this.cardType = cardType;
        this.barcodeNumber = BarcodeNumber;
        this.id = id;
    }

    public CardList(String cardCategory, int cardIcon) {
        this.cardCategory = cardCategory;
        this.cardIcon = cardIcon;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBarcodeNumber() {
        return barcodeNumber;
    }

    public void setBarcodeNumber(String BarcodeNumber) {
        this.barcodeNumber = BarcodeNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
