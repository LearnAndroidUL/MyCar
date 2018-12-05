package io.ruszkipista.mycar;

import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class FirestoreUpload {

    public static void UploadData(String carId){
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
//      String uid = FirebaseAuth.getInstance().getUid();
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
