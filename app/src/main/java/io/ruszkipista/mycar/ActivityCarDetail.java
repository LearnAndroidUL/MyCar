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
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class ActivityCarDetail extends AppCompatActivity {
    private TextView mCarNameTextView;
    private TextView mPlateNumberTextView;
    private DocumentReference mDocRef;
    private DocumentSnapshot mDocSnapshot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCarNameTextView = findViewById(R.id.cardetail_carname_field);
        mPlateNumberTextView = findViewById(R.id.cardetail_platenumber_field);

        String docId = getIntent().getStringExtra(Constants.EXTRA_DOC_ID);
        mDocRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_car).document(docId);
        mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(Constants.log_tag, "Firebase detail listening failed!");
                    return;
                } else {
                    if (documentSnapshot.exists()){
                        mCarNameTextView.setText((String)documentSnapshot.get(Constants.KEY_CARNAME));
                        mPlateNumberTextView.setText((String)documentSnapshot.get(Constants.KEY_PLATENUMBER));
                        mDocSnapshot = documentSnapshot;
                    }
                }


            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_cardetail,null,false);
        builder.setTitle(R.string.cardialog_title_edit);
        builder.setView(view);
        final EditText carNameEditTextView = view.findViewById(R.id.cardetail_carname_field);
        final EditText plateNumberEditTextView = view.findViewById(R.id.cardetail_platenumber_field);
        carNameEditTextView.setText((String) mDocSnapshot.get(Constants.KEY_CARNAME));
        plateNumberEditTextView.setText((String) mDocSnapshot.get(Constants.KEY_PLATENUMBER));

        builder.setNegativeButton(android.R.string.cancel,null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String carName = carNameEditTextView.getText().toString();
                String plateNumber = plateNumberEditTextView.getText().toString();

//              update item with captured details
                Map<String, Object> movieQuote = new HashMap< >();
                movieQuote.put(Constants.KEY_CARNAME,carName);
                movieQuote.put(Constants.KEY_PLATENUMBER,plateNumber);
                movieQuote.put(Constants.KEY_CREATED, new Date());
                mDocRef.update(movieQuote);
            }
        });
        builder.create().show();

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
            case R.id.action_delete:
                mDocRef.delete();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
