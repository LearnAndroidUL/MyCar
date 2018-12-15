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
    public static final String KEY_ID                   = "id";
    public static final String KEY_PLATENUMBER          = "plateNumber";
    public static final String KEY_COUNTRY_ID           = "countryId";
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
    public static final String KEY_DESCRIPTION          = "description";
    public static final String KEY_UNIT_TYPE            = "unitType";
    public static final String KEY_MATERIAL_TYPE        = "materialType";
    public static final String KEY_EXPENSE_TYPE         = "expenseType";
    public static final String KEY_PARTNER_ID           = "partnerId";
    public static final String KEY_QUANTITY             = "quantity";
    public static final String KEY_UNIT_PRICE           = "unitPrice";
    public static final String KEY_CREATED              = "created";
    public static final String KEY_ODOMETER             = "odometer";
    public static final String KEY_VALID_FROM           = "validFrom";
    public static final String KEY_VALIDITY_LENGTH      = "validityLength";
    public static final String KEY_VALIDITY_LENGTH_UNIT = "validityLengthUnit";


    //  String uid = FirebaseAuth.getInstance().getUid();


    private static void UploadTrx(String carId){
        AddTrx(carId,"€","20180316","fZXlJhyEUJj9XgzoXKUi","petrol 95","txW3BIH2cJTfqoatQ3jt", 50.74,"L",1.349,178829,"",0,null );
        AddTrx(carId,"€","20180331","fZXlJhyEUJj9XgzoXKUi","petrol 95","txW3BIH2cJTfqoatQ3jt", 41.72,"L",1.451,179446,"",0,null );
        AddTrx(carId,"€","20180402","fZXlJhyEUJj9XgzoXKUi","petrol 95","txW3BIH2cJTfqoatQ3jt", 39.17,"L",1.427,180087,"",0,null );
        AddTrx(carId,"€","20180404","fZXlJhyEUJj9XgzoXKUi","petrol 95","txW3BIH2cJTfqoatQ3jt", 39.50,"L",1.428,180746,"",0,null );
        AddTrx(carId,"€","20180407","fZXlJhyEUJj9XgzoXKUi","petrol 95","txW3BIH2cJTfqoatQ3jt", 42.20,"L",1.388,181473,"",0,null );
        AddTrx(carId,"€","20180509","fZXlJhyEUJj9XgzoXKUi","petrol 95","txW3BIH2cJTfqoatQ3jt", 45.72,"L",1.389,182117,"",0,null );
        AddTrx(carId,"€","20180624","fZXlJhyEUJj9XgzoXKUi","petrol 95","txW3BIH2cJTfqoatQ3jt", 44.72,"L",1.455,182780,"",0,null );
        AddTrx(carId,"€","20180707","fZXlJhyEUJj9XgzoXKUi","petrol 95","txW3BIH2cJTfqoatQ3jt", 45.49,"L",1.439,183567,"",0,null );
        AddTrx(carId,"€","20180915","fZXlJhyEUJj9XgzoXKUi","petrol 95","txW3BIH2cJTfqoatQ3jt", 44.55,"L",1.459,184231,"",0,null );
        AddTrx(carId,"€","20181013","fZXlJhyEUJj9XgzoXKUi","petrol 95","txW3BIH2cJTfqoatQ3jt", 44.86,"L",1.479,184841,"",0,null );
        AddTrx(carId,"€","20181126","fZXlJhyEUJj9XgzoXKUi","petrol 95","txW3BIH2cJTfqoatQ3jt", 48.26,"L",1.469,185458,"",0,null );

//        2018.03.03	Cartell.ie history report	25				Value :	3910.98
//        2018.03.10	Cartell.ie history report	25				Daily cost :	3.77
//        2018.03.10	purchase	2550			178790	100km cost :	8.57
//
//        2018.03.11	12pcs bungee set	3.49
//        2018.03.11	car seat cover set	10.99
//        2018.03.11	L type wheel wrench	4.99
//        2018.03.11	tow rope	4.49
//        2018.03.11	car foot pump	7.02
//        2018.03.20	2nd key w/ programmed chip	60					Parkway Shopping Centre
//
//        2018.03.16	motortax 1 year		385.00				from 1 Mar 2018 until 28 Feb 2019
//        2018.03.20	insurance Allianz 95 days		135.44				€520/year, from 20 Mar 2018 until 22 Jun 2018 (permanent car change on Xedos policy)
//        2018.06.23	insurance Allianz 1 year		407.57				from 23/06/2018 to 22/06/2019
//
//        2018.03.22	service Sebastian's Garage	120			178835		oil filter, air filter, 4 spark plug, engine oil
//        2018.03.16	repairs Sebastian's Garage	830			178821		clutch kit, gearbox bracket, front LHS wishbone, rear brushing, rear axle both side
//        2018.12.14	service Sebastian's Garage	270			185650		break disc front RHS, coil spring rear RHS
    }

    private static void AddTrx(String carId,
                               String currency,
                               String documentDate,
                               String matId,
                               String description,
                               String partnerId,
                               double quantity,
                               String unitOfMeasure,
                               double unitPrice,
                               int    odometer,
                               String validFrom,
                               int    validityLength,
                               String validityLengthUnit
                              ) {
        Map<String, Object> data = new HashMap<>();

        data.put(Constants.KEY_CAR_ID, carId);
        data.put(Constants.KEY_CURRENCY, currency);
        data.put(Constants.KEY_DOCUMENT_DATE, getDate(documentDate));
        data.put(Constants.KEY_MATERIAL_ID, matId);
        data.put(Constants.KEY_DESCRIPTION, description);
        data.put(Constants.KEY_PARTNER_ID, partnerId);
        data.put(Constants.KEY_QUANTITY, quantity);
        data.put(Constants.KEY_UNIT_OF_MEASURE, unitOfMeasure);
        data.put(Constants.KEY_UNIT_PRICE, unitPrice);
        data.put(Constants.KEY_ODOMETER, odometer);
        data.put(Constants.KEY_VALID_FROM, getDate(validFrom));
        data.put(Constants.KEY_VALIDITY_LENGTH, validityLength);
        data.put(Constants.KEY_VALIDITY_LENGTH_UNIT, validityLengthUnit);

        final CollectionReference collRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_transaction);
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
