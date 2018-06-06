package digitalcard.digitalcard.Util;

import android.Manifest;

/**
 * Created by viks on 17/03/2018.
 */

public class Utilities {
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

//    Database


//    Bundle
    public final static String BUNDLE_CARD_ID = "card_id";
    public final static String BUNDLE_CARD_NAME = "card_name";
    public final static String BUNDLE_BARCODE_NUMBER = "barcode_number";
    public final static String BUNDLE_CARD_CATEGORY = "card_category";
    public final static String BUNDLE_CARD_LOGO = "card_logo";
    public final static String BUNDLE_CARD_BACKGROUND = "card_background";

    public final static String BUNDLE_PROMO_MERCHANT = "promo_merchant";
    public final static String BUNDLE_PROMO_TITLE = "promo_title";
    public final static String BUNDLE_PROMO_DESCRIPTION = "promo_description";

    public final static String BUNDLE_LOCATION_LATITUDE = "location_latitude";
    public final static String BUNDLE_LOCATION_LONGITUDE = "location_longitude";

//    Merchant
    public final static String MERCHANT_STARBUCKS = "Starbucks";
}
