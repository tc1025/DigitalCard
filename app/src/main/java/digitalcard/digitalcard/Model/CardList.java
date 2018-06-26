package digitalcard.digitalcard.Model;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

/**
 * Created by viks on 17/03/2018.
 */

public class CardList {
    public String cardCategory;
    public String cardIcon;
    public int cardBackground;
    public String cardName;
    public String cardType;
    public String barcodeNumber;
    public String cardNote;
    public String cardFrontView;
    public String cardBackView;
    public int id;
    public String logo;

    public CardList() {}

    public CardList(String cardName, String cardType, String barcodeNumber, String cardIcon, int cardBackground) {
        this.cardName = cardName;
        this.cardType = cardType;
        this.barcodeNumber = barcodeNumber;
        this.cardIcon = cardIcon;
        this.cardBackground = cardBackground;
    }

    public CardList(int id, String cardType, String cardName, String BarcodeNumber, String cardIcon, int cardBackground) {
        this.cardName = cardName;
        this.cardType = cardType;
        this.barcodeNumber = BarcodeNumber;
        this.id = id;
        this.cardIcon = cardIcon;
        this.cardBackground = cardBackground;
    }

    public CardList(String cardName, String cardType, String barcodeNumber, String cardIcon, int cardBackground, String cardNote, String cardFrontView, String cardBackView) {
        this.cardName = cardName;
        this.cardType = cardType;
        this.barcodeNumber = barcodeNumber;
        this.cardIcon = cardIcon;
        this.cardBackground = cardBackground;
        this.cardNote = cardNote;
        this.cardFrontView = cardFrontView;
        this.cardBackView = cardBackView;
    }

    public CardList(int id, String cardType, String cardName, String BarcodeNumber, String cardIcon, int cardBackground, String cardNote, String cardFrontView, String cardBackView) {
        this.cardName = cardName;
        this.cardType = cardType;
        this.barcodeNumber = BarcodeNumber;
        this.id = id;
        this.cardIcon = cardIcon;
        this.cardBackground = cardBackground;
        this.cardNote = cardNote;
        this.cardFrontView = cardFrontView;
        this.cardBackView = cardBackView;
    }

    public CardList(String cardCategory, String cardIcon, int cardBackground) {
        this.cardCategory = cardCategory;
        this.cardIcon = cardIcon;
        this.cardBackground = cardBackground;
    }

    public String getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(String cardCategory) {
        this.cardCategory = cardCategory;
    }

    public String getCardNote() {
        return cardNote;
    }

    public void setCardNote(String cardNote) {
        this.cardNote = cardNote;
    }

    public String getCardFrontView() {
        return cardFrontView;
    }

    public void setCardFrontView(String cardFrontView) {
        this.cardFrontView = cardFrontView;
    }

    public String getCardBackView() {
        return cardBackView;
    }

    public void setCardBackView(String cardBackView) {
        this.cardBackView = cardBackView;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getCardIcon() {
        return cardIcon;
    }

    public void setCardIcon(String cardIcon) {
        this.cardIcon = cardIcon;
    }

    public int getCardBackground() {
        return cardBackground;
    }

    public void setCardBackground(int cardBackground) {
        this.cardBackground = cardBackground;
    }
}
