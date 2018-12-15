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
        AddTrx(carId,"€","20180303","RrjnIoTgSqm79yNSYmnE","Cartell.ie history report","rB19QkTiSw4qpyfuw6uJ", 1,"pc",25,0,"",0,null );
        AddTrx(carId,"€","20180310","RrjnIoTgSqm79yNSYmnE","Cartell.ie history report","rB19QkTiSw4qpyfuw6uJ", 1,"pc",25,0,"",0,null );
        AddTrx(carId,"€","20180310","QD3DN24T7dfwd3vgU2xe","car purchase","rB19QkTiSw4qpyfuw6uJ", 1,"pc",2550,178790,"",0,null );
        AddTrx(carId,"€","20180311","LEo7N90zUmbHish3WW7e","bungee set 12pcs","rB19QkTiSw4qpyfuw6uJ", 1,"pc",3.49,0,"",0,null );
        AddTrx(carId,"€","20180311","LEo7N90zUmbHish3WW7e","car seat cover set","rB19QkTiSw4qpyfuw6uJ", 1,"pc",10.99,0,"",0,null );
        AddTrx(carId,"€","20180311","QD3DN24T7dfwd3vgU2xe","wheel wrench L shape","rB19QkTiSw4qpyfuw6uJ", 1,"pc",4.99,0,"",0,null );
        AddTrx(carId,"€","20180311","QD3DN24T7dfwd3vgU2xe","tow rope","rB19QkTiSw4qpyfuw6uJ", 1,"pc",4.49,0,"",0,null );
        AddTrx(carId,"€","20180311","QD3DN24T7dfwd3vgU2xe","car foot pump","rB19QkTiSw4qpyfuw6uJ", 1,"pc",7.02,0,"",0,null );
        AddTrx(carId,"€","20180320","QD3DN24T7dfwd3vgU2xe","door key w/ programmed chip","9LyDCD6WTtvKFNKu2OPF", 1,"pc",60,0,"",0,null );
        AddTrx(carId,"€","20180316","D2iqqgLodfN7M94ueLX8","motortax","MGLe4PxCsevIgZHmpwfs", 1,"year",385,0,"20180301",1,"year");
        AddTrx(carId,"€","20180320","jX9rCSS1r5vfOyguSIkv","insurance (permanent car change on Xedos policy)","KjfGmzYUbrgoow3TyCr3", 95,"day",1.4256, 0,"20180320",95,"day");
        AddTrx(carId,"€","20180623","jX9rCSS1r5vfOyguSIkv","insurance","KjfGmzYUbrgoow3TyCr3", 1,"year",407.57, 0,"20180623",1,"year");
        AddTrx(carId,"€","20180316","QD3DN24T7dfwd3vgU2xe","clutch kit, gearbox bracket, front LHS wishbone, rear brushing, rear axle both side","qUvg7SvtDUrTkPq7mJ5H", 1,"pc",830, 178821,"",0,null);
        AddTrx(carId,"€","20180322","LEo7N90zUmbHish3WW7e","oil filter, air filter, 4 spark plug, engine oil","qUvg7SvtDUrTkPq7mJ5H", 1,"pc",120, 178835,"",0,null);
        AddTrx(carId,"€","20181214","QD3DN24T7dfwd3vgU2xe","break disc front RHS, coil spring rear RHS","qUvg7SvtDUrTkPq7mJ5H", 1,"pc",270, 185700,"",0,null);
        AddTrx(carId,"€","20181208","RrjnIoTgSqm79yNSYmnE","car test","GQOUbDGZ1p2jZ8I2ap2B", 1,"pc",55, 185687,"",0,null);
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
