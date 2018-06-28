package digitalcard.digitalcard.Util;

import android.Manifest;

/**
 * Created by viks on 17/03/2018.
 */

public class Utilities {
//    URL
    public static final String URL_CARD = "https://enlivening-ratio.000webhostapp.com/cardcategory.php";
    public static final String URL_PROMO = "https://enlivening-ratio.000webhostapp.com/promolist.php";

//    Permission
    public static final String[] ALL_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS
    };
    public static final int VP_ALL = 0;
    public static final int VP_LOCATION = 1;

//    Account
    public static final String CHECK_ACCOUNT = "check_account";
    public static final String LOGIN_STATUS = "login_status";

//    Database - card
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "tableCardList";
    public static final String TABLE_CARDLIST = "cardList";
    public static final String id_key = "id";
    public static final String card_name = "card_names";
    public static final String card_type = "card_types";
    public static final String barcode_number = "barcode_number";
    public static final String card_logo = "card_logo";
    public static final String card_background = "card_background";
    public static final String notes = "notes";
    public static final String front_view = "frontView";
    public static final String back_view = "backView";

//    Database - account
    public static final String DATABASE_ACCOUNT_NAME = "tableAccount";
    public static final String TABLE_ACCOUNT_LIST = "accountList";
    public static final String account_name = "account_names";
    public static final String account_email = "account_email";
    public static final String account_dob = "account_dob";
    public static final String account_address = "account_address";
    public static final String account_identity_number = "account_identity_number";
    public static final String account_phone_number = "account_phone_number";

//    Bundle
    public final static String BUNDLE_CARD_ID = "card_id";
    public final static String BUNDLE_CARD_NAME = "card_name";
    public final static String BUNDLE_BARCODE_NUMBER = "barcode_number";
    public final static String BUNDLE_CARD_CATEGORY = "card_category";
    public final static String BUNDLE_CARD_LOGO = "card_logo";
    public final static String BUNDLE_CARD_BACKGROUND = "card_background";
    public final static String BUNDLE_CARD_NOTES = "notes";
    public final static String BUNDLE_CARD_FRONT_VIEW = "frontView";
    public final static String BUNDLE_CARD_BACK_VIEW = "backView";

    public final static String BUNDLE_PROMO_MERCHANT = "promo_merchant";
    public final static String BUNDLE_PROMO_TITLE = "promo_title";
    public final static String BUNDLE_PROMO_BANNER = "promo_banner";
    public final static String BUNDLE_PROMO_ORIGINAL_PRICE = "promo_original_price";
    public final static String BUNDLE_PROMO_DISCOUNTED_PRICE = "promo_discounted_price";
    public final static String BUNDLE_PROMO_DESCRIPTION = "promo_description";
    public final static String BUNDLE_PROMO_LINK = "promo_link";

    public final static String BUNDLE_LOCATION_LATITUDE = "location_latitude";
    public final static String BUNDLE_LOCATION_LONGITUDE = "location_longitude";

//    Merchant
    public final static String MERCHANT_STARBUCKS = "Starbucks";
}
