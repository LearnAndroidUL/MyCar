package io.ruszkipista.mycar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DialogCarInput extends AppCompatDialogFragment {
    private FirebaseAuth mAuth;
    private CollectionReference carCollRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_car);
    private EditText carNameEditTextView;
    private EditText plateNumberEditTextView;
    private EditText carImageUrlEditTextView;
    private DialogCarInputListener listener;
    private String carId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_cardetail,null);

        carNameEditTextView = dialogView.findViewById(R.id.cardialog_carname_field);
        plateNumberEditTextView = dialogView.findViewById(R.id.cardialog_platenumber_field);
        carImageUrlEditTextView = dialogView.findViewById(R.id.cardialog_imageurl_field);

        mAuth = FirebaseAuth.getInstance();

        if (carId != null) {
            builder.setTitle(R.string.cardialog_title_edit);
            carCollRef.document(carId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            carNameEditTextView.setText((String) document.get(Constants.KEY_CARNAME));
                            plateNumberEditTextView.setText((String) document.get(Constants.KEY_PLATENUMBER));
                            carImageUrlEditTextView.setText((String) document.get(Constants.KEY_CARIMAGEURL));
                        } else {
                            Log.d(Constants.log_tag, "No such document");
                        }
                    } else {
                        Log.d(Constants.log_tag, "get failed with ", task.getException());
                    }
                }
            });
        } else {
            builder.setTitle(R.string.cardialog_title_add);
        }

        builder.setView(dialogView)
               .setNegativeButton(android.R.string.cancel,null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String carName = carNameEditTextView.getText().toString();
                    String plateNumber = plateNumberEditTextView.getText().toString();
                    String carImageUrl = carImageUrlEditTextView.getText().toString();
//                  create new item with captured details
                    Map<String, Object> car = new HashMap<>();
                    car.put(Constants.KEY_USER_ID, mAuth.getCurrentUser().getUid());
                    car.put(Constants.KEY_CARNAME, carName);
                    car.put(Constants.KEY_PLATENUMBER, plateNumber);
                    car.put(Constants.KEY_CARIMAGEURL, carImageUrl);
                    if ( carId != null) {
                        carCollRef.document(carId).set(car);
                    } else {
                        carId = carCollRef.add(car).getResult().getId();
                    }
                    listener.applyChanges(carId);
                }
            });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogCarInputListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement DialogCarInputListener");
        }
    }

    public interface DialogCarInputListener{
        void applyChanges(String carId);
    }
}