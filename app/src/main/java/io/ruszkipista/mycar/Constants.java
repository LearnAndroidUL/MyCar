package io.ruszkipista.mycar;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Constants {
//  internal technical constants
    public static final String log_tag = "MyCarLog";
    public static final String EXTRA_DOC_ID = "document_id";
    public static final String CARSHAPEURL = "https://visualpharm.com/assets/881/Car-595b40b75ba036ed117d57e9.svg";
//  firestore collection names
    public static final String firebase_collection_car         = "car";
    public static final String firebase_collection_transaction = "transaction";
    public static final String firebase_collection_unit        = "unit";
    public static final String firebase_collection_material    = "material";
    public static final String firebase_collection_country     = "country";
    public static final String firebase_collection_partner     = "partner";
    public static final String firebase_collection_conversion  = "conversion";
//  database field (column) names
    public static final String KEY_CAR_ID           = "carId";
    public static final String KEY_USER_ID          = "uid";
    public static final String KEY_NAME             = "name";
    public static final String KEY_CARIMAGEURL      = "carImageUrl";
    public static final String KEY_PLATENUMBER      = "plateNumber";
    public static final String KEY_CREATED          = "created";
    public static final String KEY_COUNTRY          = "country";
    public static final String KEY_DISTANCE_UNIT_ID = "distanceUnitId";
    public static final String KEY_CURRENCY         = "currency";
    public static final String KEY_UNIT_TYPE        = "unitType";
    public static final String VALUE_UNIT_TYPE_VOLUME       = "VOL";
    public static final String VALUE_UNIT_TYPE_MASS         = "MASS";
    public static final String VALUE_UNIT_TYPE_DISTANCE     = "DIST";
    public static final String VALUE_UNIT_TYPE_FUEL_ECONOMY = "FECO";
    public static final String KEY_UNIT_OF_MEASURE       = "unitOfMeasure";
    public static final String KEY_FROM_UNIT_OF_MEASURE  = "fromUnitOfMeasure";
    public static final String KEY_TO_UNIT_OF_MEASURE    = "toUnitOfMeasure";
    public static final String KEY_FUEL_ID          = "fuelId";
    public static final String KEY_FUEL_UNIT_ID     = "fuelUnitId";
    public static final String KEY_DOCUMENT_DATE    = "documentDate";
    public static final String KEY_MATERIAL_ID      = "matId";
    public static final String KEY_PARTNER_ID       = "partnerId";
    public static final String KEY_QUANTITY         = "quantity";
    public static final String KEY_UNIT_PRICE       = "unitPrice";

    //  String uid = FirebaseAuth.getInstance().getUid();

    public static void UploadUnit(){
        AddUnit("L",      "litre",           Constants.VALUE_UNIT_TYPE_VOLUME);
        AddUnit("galUK",  "gallon UK",       Constants.VALUE_UNIT_TYPE_VOLUME);
        AddUnit("L100km", "L/100km",         Constants.VALUE_UNIT_TYPE_FUEL_ECONOMY);
        AddUnit("mpgUK",  "miles/gallon UK", Constants.VALUE_UNIT_TYPE_FUEL_ECONOMY);
        AddUnit("kg",     "kilogramm",       Constants.VALUE_UNIT_TYPE_MASS);
        AddUnit("g",      "grammm",          Constants.VALUE_UNIT_TYPE_MASS);
        AddUnit("lb",     "pound",           Constants.VALUE_UNIT_TYPE_MASS);
        AddUnit("km",     "kilometre",       Constants.VALUE_UNIT_TYPE_DISTANCE);
        AddUnit("m",      "metre",           Constants.VALUE_UNIT_TYPE_DISTANCE);
        AddUnit("cm",     "centimetre",      Constants.VALUE_UNIT_TYPE_DISTANCE);
        AddUnit("mm",     "millimetre",      Constants.VALUE_UNIT_TYPE_DISTANCE);
        AddUnit("mi",     "mile",            Constants.VALUE_UNIT_TYPE_DISTANCE);
    }
    private static void AddUnit( String unitOfMeasure,
                                 String name,
                                 String unitType) {
        CollectionReference collRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_unit);
        Map<String, Object> data = new HashMap<>();

        data.put(Constants.KEY_UNIT_OF_MEASURE, unitOfMeasure);
        data.put(Constants.KEY_NAME, name);
        data.put(Constants.KEY_UNIT_TYPE, unitType);

        collRef.add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(Constants.log_tag, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Constants.log_tag, "Error adding document", e);
                    }
                });
    }

    public static void UploadConv(){
        AddConv("lb",     "kg",   0.45359237);
        AddConv("L",      "galUK",0.2199692482990878);
        AddConv("L100km", "mpgUK",282.48093627967);
        AddConv("kg",     "g",    1000);
        AddConv("km",     "m",    1000);
        AddConv("m",      "cm",   100);
        AddConv("m",      "mm",   1000);
        AddConv("cm",     "mm",   10);
        AddConv("km",     "mi",   0.621371);
    }
    private static void AddConv( String fromUnitOfMeasure,
                                 String toUnitOfMeasure,
                                 double quantity) {
        CollectionReference collRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_conversion);
        Map<String, Object> data = new HashMap<>();

        data.put(Constants.KEY_FROM_UNIT_OF_MEASURE, fromUnitOfMeasure);
        data.put(Constants.KEY_TO_UNIT_OF_MEASURE,   toUnitOfMeasure);
        data.put(Constants.KEY_QUANTITY,             quantity);

        collRef.add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(Constants.log_tag, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Constants.log_tag, "Error adding document", e);
                    }
                });
    }

    public static void UploadTrx(String carId){
        AddTrx(carId,"€", "20181124", "fZXlJhyEUJj9XgzoXKUi","txW3BIH2cJTfqoatQ3jt", 48.25, "L",1.469 );
    }

    private static void AddTrx(String carId,
                               String currency,
                               String documentDate,
                               String matId,
                               String partnerId,
                               double quantity,
                               String unitOfMeasure,
                               double unitPrice) {
        CollectionReference collRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_transaction);
        Map<String, Object> data = new HashMap<>();

        data.put(Constants.KEY_CAR_ID, carId);
        data.put(Constants.KEY_CURRENCY, currency);
        data.put(Constants.KEY_DOCUMENT_DATE, getDate(documentDate));
        data.put(Constants.KEY_MATERIAL_ID, matId);
        data.put(Constants.KEY_PARTNER_ID, partnerId);
        data.put(Constants.KEY_QUANTITY, quantity);
        data.put(Constants.KEY_UNIT_OF_MEASURE, unitOfMeasure);
        data.put(Constants.KEY_UNIT_PRICE, unitPrice);

        collRef.add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(Constants.log_tag, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Constants.log_tag, "Error adding document", e);
                    }
                });
    }

    public static void UploadData(String carId){
        UploadUnit();
        UploadConv();
//        UploadTrx(carId);
    }

    private static Date getDate(String date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        TimeZone timezone = TimeZone.getTimeZone("Europe/London");
        formatter.setTimeZone(timezone);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
