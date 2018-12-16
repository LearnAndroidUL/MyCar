package io.ruszkipista.mycar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ActivityCarDetail extends AppCompatActivity implements DialogCarInput.DialogCarInputListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private List<DocumentSnapshot> mCarDocumentSnapshots = new ArrayList<>();
    private List<DocumentSnapshot> mMatDocumentSnapshots = new ArrayList<>();
    private CollectionReference carCollRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_car);
    private CollectionReference matCollRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_material);
    private int mActualDocumentIndex = 0;
    private TextView mCarNameTextView;
    private TextView mPlateNumberTextView;
    private TextView mCountryNameTextView;
    private TextView mCurrencyNameTextView;
    private TextView mDistanceUnitTextView;
    private TextView mOdometerUnitTextView;
    private TextView mFuelMaterialIdTextView;
    private TextView mFuelUnitIdTextView;
    private TextView mFuelEconomyTextView;
    private TextView mCarImageUrlTextView;
    private ImageView mCarImageImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cardetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCarNameTextView = findViewById(R.id.cardetail_carname_field);
        mPlateNumberTextView = findViewById(R.id.cardetail_platenumber_field);
        mCountryNameTextView = findViewById(R.id.cardetail_country_field);
        mCurrencyNameTextView = findViewById(R.id.cardetail_currency_field);
        mDistanceUnitTextView = findViewById(R.id.cardetail_distanceunit_field);
        mOdometerUnitTextView = findViewById(R.id.cardetail_odometerunit_field);
        mFuelUnitIdTextView = findViewById(R.id.cardetail_fuelunit_field);
        mFuelMaterialIdTextView = findViewById(R.id.cardetail_fuelmaterial_field);
        mFuelEconomyTextView = findViewById(R.id.cardetail_fueleconomy_field);
        mCarImageUrlTextView = findViewById(R.id.cardetail_carimageurl_field);
        mCarImageImageView= findViewById(R.id.cardetail_car_image);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogCarInput dialog = DialogCarInput.newInstance(null);
                dialog.show(getSupportFragmentManager(), getString(R.string.cardetail_name));
            }
        });

        loadCars();
    }

    private void loadCars() {
        carCollRef.whereEqualTo(Constants.KEY_USER_ID, mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(Constants.log_tag, "Firebase listening failed!");
                            return;
                        } else {
                            mCarDocumentSnapshots = queryDocumentSnapshots.getDocuments();
                            // get argument from caller activity
                            mActualDocumentIndex = getDocumentIndex(getIntent().getStringExtra(Constants.KEY_CAR_ID));
                            loadMaterials();
                        }
                    }
                });
    }

    private void loadMaterials() {
        matCollRef.whereEqualTo(Constants.KEY_USER_ID, mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(Constants.log_tag, "Firebase listening failed!");
                            return;
                        } else {
                            mMatDocumentSnapshots = queryDocumentSnapshots.getDocuments();
                            displayCar();
                        }
                    }
                });
    }

    private void displayCar() {
        DocumentSnapshot document;
        if (mActualDocumentIndex >= mCarDocumentSnapshots.size()) {
            mActualDocumentIndex = 0;
        }
        if (mCarDocumentSnapshots.size() > 0) {
            document = mCarDocumentSnapshots.get(mActualDocumentIndex);
        } else {
            document = null;
        }
        if (document != null) {
            mCarNameTextView.setText((String) document.get(Constants.KEY_NAME));
            mPlateNumberTextView.setText((String) document.get(Constants.KEY_PLATENUMBER));
            mCountryNameTextView.setText(Country.getColumnValueByFilterValue(Country.COL_ID, (String) document.get(Constants.KEY_COUNTRY_ID), Country.COL_NAME));
            mCurrencyNameTextView.setText(Unit.getColumnValueByFilterValue(Unit.COL_ID, (String) document.get(Constants.KEY_CURRENCY), Unit.COL_NAME ));
            mDistanceUnitTextView.setText(Unit.getColumnValueByFilterValue(Unit.COL_ID,  (String) document.get(Constants.KEY_DISTANCE_UNIT_ID), Unit.COL_NAME ));
            mOdometerUnitTextView.setText(Unit.getColumnValueByFilterValue(Unit.COL_ID,  (String) document.get(Constants.KEY_ODOMETER_UNIT_ID), Unit.COL_NAME ));
            mFuelMaterialIdTextView.setText(ListGenerator.getColumnValueById(mMatDocumentSnapshots, Constants.KEY_NAME ,(String) document.get(Constants.KEY_FUEL_MATERIAL_ID)));
            mFuelUnitIdTextView.setText(Unit.getColumnValueByFilterValue(Unit.COL_ID,  (String) document.get(Constants.KEY_FUEL_UNIT_ID), Unit.COL_NAME ));
            mFuelEconomyTextView.setText(Unit.getColumnValueByFilterValue(Unit.COL_ID,  (String) document.get(Constants.KEY_FUEL_ECONOMY_UNIT_ID), Unit.COL_NAME ));
            String url = (String)document.get(Constants.KEY_CARIMAGEURL);
            mCarImageUrlTextView.setText(url);
            if (!url.isEmpty()) Ion.with(mCarImageImageView).load(url);
            else mCarImageImageView.setImageResource(R.mipmap.ic_launcher);
        }
/*
if (mCarId != null) {
carCollRef.document(mCarId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
@Override
public void onComplete(@NonNull Task<DocumentSnapshot> task) {
if (task.isSuccessful()) {
DocumentSnapshot document = task.getResult();
if (document.exists()) {
mCarNameTextView.setText((String) document.get(Constants.KEY_CARNAME));
mPlateNumberTextView.setText((String) document.get(Constants.KEY_PLATENUMBER));
mCarImageUrlTextView.setText((String) document.get(Constants.KEY_CARIMAGEURL));
} else {
Log.d(Constants.log_tag, "No such document");
}
} else {
Log.d(Constants.log_tag, "get failed with ", task.getException());
}
}
});
}
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_cardetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_list_cars:
                showDialogItemList();
                return true;

            case R.id.action_modify_car:
                DialogCarInput dialog = DialogCarInput.newInstance(mCarDocumentSnapshots.get(mActualDocumentIndex).getId());
                dialog.show(getSupportFragmentManager(), getString(R.string.cardetail_name));
                return true;

            case R.id.action_delete_car:
                showConfirmDelete();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void applyChangesDialogCarInput(String docId) {
        mActualDocumentIndex = getDocumentIndex(docId);
        displayCar();
    }

    private void showDialogItemList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.carlist_title);
        builder.setItems(getDocumentLabels(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActualDocumentIndex = which;
                displayCar();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private String[] getDocumentLabels() {
        String[] names = new String[mCarDocumentSnapshots.size()];
        for (int i = 0; i < mCarDocumentSnapshots.size(); i++) {
            names[i] = (String) mCarDocumentSnapshots.get(i).get(Constants.KEY_NAME);
        }
        return names;
    }

    private int getDocumentIndex(String docId) {
        int found = -1;
        for (int i = 0; i < mCarDocumentSnapshots.size(); i++) {
            if (mCarDocumentSnapshots.get(i).getId().equals(docId)) {
                found = i;
                break;
            }
        }
        return found;
    }


    private void showConfirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.cardialog_delete_title);
        builder.setMessage(R.string.cardialog_delete_message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                carCollRef.document(mCarDocumentSnapshots.get(mActualDocumentIndex).getId()).delete();
                if (mCarDocumentSnapshots.size() == 0){
                    ActivityCarDetail.this.finish();
                } else if (mActualDocumentIndex >= mCarDocumentSnapshots.size()) {
                    mActualDocumentIndex = mCarDocumentSnapshots.size() - 1;
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel,null);
        builder.create().show();
    }
}
