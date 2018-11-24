package io.ruszkipista.mycar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ActivityHome extends AppCompatActivity {
    private FirebaseFirestore mDB;
    private TextView mCarNameTextView;
    private TextView mPlateNumberTextView;
    private ImageView mCarImageImageView;
    private List<DocumentSnapshot> mCarSnapshots = new ArrayList<>();
    private int mActualCarIndex = -1;
    private DocumentSnapshot mCar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        mCarNameTextView = findViewById(R.id.home_carname_field);
        mPlateNumberTextView = findViewById(R.id.home_platenumber_field);
        mCarImageImageView = findViewById(R.id.home_car_image);
        mDB = FirebaseFirestore.getInstance();

        CollectionReference carRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_car);
        carRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(Constants.log_tag, "Firebase listening failed!");
                            return;
                        } else {
                            mCarSnapshots = queryDocumentSnapshots.getDocuments();
                            displayCar();
                        }
                    }
                });

    }

    private void displayCar() {
        if (mActualCarIndex < 0) {
            if (mCarSnapshots.size() > 0) {
                mActualCarIndex = 0;
                mCar = mCarSnapshots.get(mActualCarIndex);
            }
        } else {
            mCar = mCarSnapshots.get(mActualCarIndex);
        }
        if (mCar != null) {
            mCarNameTextView.setText((String)mCar.get(Constants.KEY_CARNAME));
            mPlateNumberTextView.setText((String)mCar.get(Constants.KEY_PLATENUMBER));
            String url = (String)mCar.get(Constants.KEY_CARIMAGEURL);
            if (!url.isEmpty()) Ion.with(mCarImageImageView).load(url);
            else mCarImageImageView.setImageResource(R.mipmap.ic_launcher);
        }

    }

    private void getCarNext() {
        mActualCarIndex = (mActualCarIndex + 1) % mCarSnapshots.size();
    }

    public void callActivityCarDetail(View view) {
        switch (view.getId()){
            case R.id.home_car_image:
                Context context = view.getContext();
                Intent intent = new Intent(context,ActivityCarDetail.class);
                intent.putExtra(Constants.EXTRA_DOC_ID, mCar.getId());
                context.startActivity(intent);
                return;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (menuItem.getItemId()){
            case R.id.action_swap_car:
                getCarNext();
                displayCar();
                return true;

        };
        return super.onOptionsItemSelected(menuItem);
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_cardetail,null,false);
        builder.setTitle(R.string.cardialog_title_add);
        builder.setView(view);
        final EditText carNameEditTextView = view.findViewById(R.id.cardetail_carname_field);
        final EditText plateNumberEditTextView = view.findViewById(R.id.cardetail_platenumber_field);

        builder.setNegativeButton(android.R.string.cancel,null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String carName = carNameEditTextView.getText().toString();
                String plateNumber = plateNumberEditTextView.getText().toString();

//              create new item with captured details
                Map<String, Object> car = new HashMap< >();
                car.put(Constants.KEY_CARNAME,carName);
                car.put(Constants.KEY_PLATENUMBER,plateNumber);
                car.put(Constants.KEY_CREATED, new Date());
                FirebaseFirestore.getInstance().collection(Constants.firebase_collection_car).add(car);
            }
        });
        builder.create().show();
    }

}
