package io.ruszkipista.mycar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

public class ActivityHome extends AppCompatActivity implements DialogCarInput.DialogCarInputListener{
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextView mCarNameTextView;
    private TextView mPlateNumberTextView;
    private ImageView mCarImageImageView;
    private List<DocumentSnapshot> mCarSnapshots = new ArrayList<>();
    private int mActualCarIndex = 0;
    private DocumentSnapshot mCar = null;
    private CollectionReference carCollRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_car);

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
                showTransactionInputDialog();
            }
        });

        mCarNameTextView = findViewById(R.id.home_carname_field);
        mPlateNumberTextView = findViewById(R.id.home_platenumber_field);
        mCarImageImageView = findViewById(R.id.home_car_image);

        carCollRef.whereEqualTo(Constants.KEY_USER_ID, mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(Constants.log_tag, "Firebase listening failed!");
                            return;
                        } else {
                            mCarSnapshots = queryDocumentSnapshots.getDocuments();
                            displayCar();
                        }
                    }
                });
    }

    private void displayCar() {
        if (mActualCarIndex >= mCarSnapshots.size()) {
            getCarNext();
        }
        if (mCarSnapshots.size() > 0) {
            mCar = mCarSnapshots.get(mActualCarIndex);
        } else {
            mCar = null;
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
        if (mCarSnapshots.size() > 0) {
            mActualCarIndex = (mActualCarIndex + 1) % mCarSnapshots.size();
        }
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

            case R.id.action_modify_car:
                //call static factory method for injecting parameter
                DialogCarInput dialog = DialogCarInput.newInstance(mCar.getId());
                dialog.show(getSupportFragmentManager(),getString(R.string.cardetail_name));
                return true;

            case R.id.action_signout:
                mAuth.signOut();
                finish();
                return true;
        };
        return super.onOptionsItemSelected(menuItem);
    }

    private void showTransactionInputDialog() {
    }

    @Override
    public void applyChangesDialogCarInput(String carId) {
        for (int i=0;i<mCarSnapshots.size();i++) {
            if (mCarSnapshots.get(i).getId().equals(carId)) {
                mActualCarIndex = i;
                displayCar();
                return;
            }
        }
        getCarNext();
        displayCar();
    }
}
