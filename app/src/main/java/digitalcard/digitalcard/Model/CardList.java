package digitalcard.digitalcard.Model;

import android.support.annotation.Nullable;

/**
 * Created by viks on 17/03/2018.
 */

public class CardList {
    private String cardCategory;
    private String cardName;
    private String cardBarcodeNumber;
    private int thumbnail;
    private String cardBarcodeType;

    public CardList(String cardCategory, String cardName, String cardBarcodeNumber, int thumbnail) {
        this.cardCategory = cardCategory;
        this.cardName = cardName;
        this.cardBarcodeNumber = cardBarcodeNumber;
        this.thumbnail = thumbnail;
    }

    public CardList(String cardCategory, @Nullable int thumbnail) {
        this.cardCategory = cardCategory;
        this.thumbnail = thumbnail;
    }

    public String getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(String cardCategory) {
        this.cardCategory = cardCategory;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardBarcodeNumber() {
        return cardBarcodeNumber;
    }

    public void setCardBarcodeNumber(String cardBarcodeNumber) {
        this.cardBarcodeNumber = cardBarcodeNumber;
    }

    public String getCardBarcodeType() {
        return cardBarcodeType;
    }

    public void setCardBarcodeType(String cardBarcodeType) {
        this.cardBarcodeType = cardBarcodeType;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
