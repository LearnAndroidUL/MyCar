package io.ruszkipista.mycar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DialogCarInput extends AppCompatDialogFragment {
    private FirebaseAuth mAuth;
    private CollectionReference carCollRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_car);
    private EditText mCarNameEditText;
    private EditText mPlateNumberEditText;
    private Spinner  mCountryIdSpinner;
    private EditText mCurrencyIdEditText;
    private EditText mDistanceUnitIdEditText;
    private EditText mOdometerUnitIdEditText;
    private EditText mFuelMaterialIdEditText;
    private EditText mFuelUnitIdEditText;
    private EditText mFuelEconomyIdEditText;

    private EditText mCarImageUrlEditText;


    private DialogCarInputListener mListener;
    private String mCarId;

    public static DialogCarInput newInstance(String carId) {
        DialogCarInput dialog = new DialogCarInput();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_CAR_ID, carId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_cardetail,null);

        mCarNameEditText = dialogView.findViewById(R.id.cardialog_carname_field);
        mPlateNumberEditText = dialogView.findViewById(R.id.cardialog_platenumber_field);
        mCurrencyIdEditText = dialogView.findViewById(R.id.cardialog_CurrencyId_field);
        mDistanceUnitIdEditText = dialogView.findViewById(R.id.cardialog_DistanceUnitId_field);
        mOdometerUnitIdEditText = dialogView.findViewById(R.id.cardialog_OdometerUnitId_field);
        mFuelMaterialIdEditText = dialogView.findViewById(R.id.cardialog_FuelMaterialId_field);
        mFuelUnitIdEditText = dialogView.findViewById(R.id.cardialog_FuelUnitId_field);
        mFuelEconomyIdEditText = dialogView.findViewById(R.id.cardialog_FuelEconomyId_field);
        mCarImageUrlEditText = dialogView.findViewById(R.id.cardialog_imageurl_field);

        mCountryIdSpinner = dialogView.findViewById(R.id.cardialog_CountryId_field);
        ArrayAdapter<String> countryIdAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item, Country.getNameList());
        mCountryIdSpinner.setAdapter(countryIdAdapter);

        mAuth = FirebaseAuth.getInstance();

        //attempt to load passed arguments
        if (getArguments() != null) {
            mCarId = getArguments().getString(Constants.KEY_CAR_ID);
        }

        if (mCarId != null) {
            builder.setTitle(R.string.cardialog_title_edit);
            carCollRef.document(mCarId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            mCarNameEditText.setText((String) document.get(Constants.KEY_NAME));
                            mPlateNumberEditText.setText((String) document.get(Constants.KEY_PLATENUMBER));
                            mCountryIdSpinner.setSelection(Country.getIndexById((String)document.get(Constants.KEY_COUNTRY)));
                            mCurrencyIdEditText.setText((String) document.get(Constants.KEY_CURRENCY));
                            mDistanceUnitIdEditText.setText((String) document.get(Constants.KEY_DISTANCE_UNIT_ID));
                            mOdometerUnitIdEditText.setText((String) document.get(Constants.KEY_ODOMETER_UNIT_ID));
                            mFuelMaterialIdEditText.setText((String) document.get(Constants.KEY_FUEL_MATERIAL_ID));
                            mFuelUnitIdEditText.setText((String) document.get(Constants.KEY_FUEL_UNIT_ID));
                            mFuelEconomyIdEditText.setText((String) document.get(Constants.KEY_FUEL_ECONOMY_UNIT_ID));
                            mCarImageUrlEditText.setText((String) document.get(Constants.KEY_CARIMAGEURL));
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
//                  create new item with captured details
                    Map<String, Object> car = new HashMap<>();
                    car.put(Constants.KEY_USER_ID, mAuth.getCurrentUser().getUid());
                    car.put(Constants.KEY_NAME, mCarNameEditText.getText().toString());
                    car.put(Constants.KEY_PLATENUMBER, mPlateNumberEditText.getText().toString());
                    car.put(Constants.KEY_COUNTRY, Country.getIdByIndex((int)mCountryIdSpinner.getSelectedItemId()));
                    car.put(Constants.KEY_CURRENCY, mCurrencyIdEditText.getText().toString());
                    car.put(Constants.KEY_DISTANCE_UNIT_ID, mDistanceUnitIdEditText.getText().toString());
                    car.put(Constants.KEY_ODOMETER_UNIT_ID, mOdometerUnitIdEditText.getText().toString());
                    car.put(Constants.KEY_FUEL_MATERIAL_ID, mFuelMaterialIdEditText.getText().toString());
                    car.put(Constants.KEY_FUEL_UNIT_ID, mFuelUnitIdEditText.getText().toString());
                    car.put(Constants.KEY_FUEL_ECONOMY_UNIT_ID, mFuelEconomyIdEditText.getText().toString());
                    car.put(Constants.KEY_CARIMAGEURL, mCarImageUrlEditText.getText().toString());
                    if ( mCarId != null) {
                        carCollRef.document(mCarId).set(car).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mListener.applyChangesDialogCarInput(mCarId);
                                } else {
                                    Log.d(Constants.log_tag, "update failed with ", task.getException());
                                }
                            }
                        });
                    } else {
                         carCollRef.add(car).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                             @Override
                             public void onComplete(@NonNull Task<DocumentReference> task) {
                                 if (task.isSuccessful()) {
                                     DocumentReference documentReference = task.getResult();
                                     if (documentReference != null) {
                                         mCarId = documentReference.getId();
                                         mListener.applyChangesDialogCarInput(mCarId);
                                     } else {
                                         Log.d(Constants.log_tag, "No such document");
                                     }
                                 } else {
                                     Log.d(Constants.log_tag, "insert failed with ", task.getException());
                                 }
                             }
                         });
                    }
                }
            });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogCarInputListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement DialogCarInputListener");
        }
    }

    public interface DialogCarInputListener{
        void applyChangesDialogCarInput(String carId);
    }
}