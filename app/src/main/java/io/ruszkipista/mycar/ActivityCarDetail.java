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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ActivityCarDetail extends AppCompatActivity implements DialogCarInput.DialogCarInputListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private List<DocumentSnapshot> mDocumentSnapshots = new ArrayList<>();
    private CollectionReference docCollRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_car);
    private int mActualDocumentIndex = 0;
    private TextView mCarNameTextView;
    private TextView mPlateNumberTextView;
    private TextView mCarImageUrlTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cardetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCarNameTextView = findViewById(R.id.cardetail_carname_field);
        mPlateNumberTextView = findViewById(R.id.cardetail_platenumber_field);
        mCarImageUrlTextView = findViewById(R.id.cardetail_carimageurl_field);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogCarInput dialog = DialogCarInput.newInstance(null);
                dialog.show(getSupportFragmentManager(), getString(R.string.cardetail_name));
            }
        });

        docCollRef.whereEqualTo(Constants.KEY_USER_ID, mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(Constants.log_tag, "Firebase listening failed!");
                            return;
                        } else {
                            mDocumentSnapshots = queryDocumentSnapshots.getDocuments();
                            // get argument from caller activity
                            mActualDocumentIndex = getDocumentIndex(getIntent().getStringExtra(Constants.EXTRA_DOC_ID));
                            displayCar();
                        }
                    }
                });
    }

    private void displayCar() {
        DocumentSnapshot document;
        if (mActualDocumentIndex >= mDocumentSnapshots.size()) {
            mActualDocumentIndex = 0;
        }
        if (mDocumentSnapshots.size() > 0) {
            document = mDocumentSnapshots.get(mActualDocumentIndex);
        } else {
            document = null;
        }
        if (document != null) {
            mCarNameTextView.setText((String) document.get(Constants.KEY_CARNAME));
            mPlateNumberTextView.setText((String) document.get(Constants.KEY_PLATENUMBER));
            mCarImageUrlTextView.setText((String) document.get(Constants.KEY_CARIMAGEURL));
        }
//        if (mCarId != null) {
//            docCollRef.document(mCarId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            mCarNameTextView.setText((String) document.get(Constants.KEY_CARNAME));
//                            mPlateNumberTextView.setText((String) document.get(Constants.KEY_PLATENUMBER));
//                            mCarImageUrlTextView.setText((String) document.get(Constants.KEY_CARIMAGEURL));
//                        } else {
//                            Log.d(Constants.log_tag, "No such document");
//                        }
//                    } else {
//                        Log.d(Constants.log_tag, "get failed with ", task.getException());
//                    }
//                }
//            });
//        }
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
                DialogCarInput dialog = DialogCarInput.newInstance(mDocumentSnapshots.get(mActualDocumentIndex).getId());
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
        String[] names = new String[mDocumentSnapshots.size()];
        for (int i = 0; i < mDocumentSnapshots.size(); i++) {
            names[i] = (String) mDocumentSnapshots.get(i).get(Constants.KEY_CARNAME);
        }
        return names;
    }

    private int getDocumentIndex(String docId) {
        int found = -1;
        for (int i = 0; i < mDocumentSnapshots.size(); i++) {
            if (mDocumentSnapshots.get(i).getId().equals(docId)) {
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
                docCollRef.document(mDocumentSnapshots.get(mActualDocumentIndex).getId()).delete();
                if (mDocumentSnapshots.size() == 0){
                    ActivityCarDetail.this.finish();
                } else if (mActualDocumentIndex >= mDocumentSnapshots.size()) {
                    mActualDocumentIndex = mDocumentSnapshots.size() - 1;
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel,null);
        builder.create().show();
    }
}
