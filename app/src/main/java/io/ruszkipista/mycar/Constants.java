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
    public static final String firebase_collection_material    = "material";
    public static final String firebase_collection_partner     = "partner";
//  database field (column) names
    public static final String KEY_USER_ID              = "uid";
    public static final String KEY_CAR_ID               = "carId";
    public static final String KEY_NAME                 = "name";
    public static final String KEY_PLATENUMBER          = "plateNumber";
    public static final String KEY_COUNTRY              = "country";
    public static final String KEY_DISTANCE_UNIT_ID     = "distanceUnitId";
    public static final String KEY_ODOMETER_UNIT_ID     = "odometerUnitId";
    public static final String KEY_CURRENCY             = "currency";
    public static final String KEY_UNIT_OF_MEASURE      = "unitOfMeasure";
    public static final String KEY_FUEL_ECONOMY_UNIT_ID = "fuelEconomyUnitId";
    public static final String KEY_FUEL_UNIT_ID         = "fuelUnitId";
    public static final String KEY_FUEL_MATERIAL_ID     = "fuelMaterialId";
    public static final String KEY_DOCUMENT_DATE        = "documentDate";
    public static final String KEY_CARIMAGEURL          = "carImageUrl";
    public static final String KEY_MATERIAL_ID          = "materialId";
    public static final String KEY_MATERIAL_TYPE        = "materialType";
    public static final String KEY_PARTNER_ID           = "partnerId";
    public static final String KEY_QUANTITY             = "quantity";
    public static final String KEY_UNIT_PRICE           = "unitPrice";
    public static final String KEY_CREATED              = "created";

    //  String uid = FirebaseAuth.getInstance().getUid();


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
        UploadTrx(carId);
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
