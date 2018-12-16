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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class DialogTransactionInput extends AppCompatDialogFragment {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CollectionReference trxCollRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_transaction);
    private CollectionReference carCollRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_car);
    private CollectionReference matCollRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_material);
    private CollectionReference parCollRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_partner);
    private List<DocumentSnapshot> mTrxDocumentSnapshots = new ArrayList<>();
    private List<DocumentSnapshot> mCarDocumentSnapshots = new ArrayList<>();
    private List<DocumentSnapshot> mMatDocumentSnapshots = new ArrayList<>();
    private List<DocumentSnapshot> mParDocumentSnapshots = new ArrayList<>();

    private Spinner  mMaterialNameSpinner;
    private EditText mDescriptionEditText;
    private CalendarView mDocDateView;
    private EditText mOdometerEditText;
    private Spinner  mPartnerNameSpinner;
    private EditText mQuantityEditText;
    private Spinner  mQuantityUomSpinner;
    private EditText mPriceEditText;
    private TextView mCurrencyTextView;
    private CalendarView mValidFromDateView;
    private EditText mValidityLengthEditText;
    private Spinner  mValLengthUomSpinner;

    private GregorianCalendar mDocumentDate = new GregorianCalendar();
    private GregorianCalendar mValFromDate = new GregorianCalendar();

    private String mCarId;
    private String mTrxId;

    private DialogTransactionInputListener mListener;

    public interface DialogTransactionInputListener{
        void applyChangesDialogTransactionInput(String carId);
    }

    public static DialogTransactionInput newInstance(String carId, String transactionId) {
        DialogTransactionInput dialog = new DialogTransactionInput();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_CAR_ID, carId);
        args.putString(Constants.KEY_TRANSACTION_ID, transactionId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_transactiondetail,null);

        mMaterialNameSpinner = dialogView.findViewById(R.id.transactiondialog_material_field);
        mDescriptionEditText = dialogView.findViewById(R.id.transactiondialog_description_field);
        mDocDateView = dialogView.findViewById(R.id.transactiondialog_docdate_field);
        mOdometerEditText = dialogView.findViewById(R.id.transactiondialog_odometer_field);
        mPartnerNameSpinner = dialogView.findViewById(R.id.transactiondialog_partner_field);
        mQuantityEditText = dialogView.findViewById(R.id.transactiondialog_quantity_field);
        mQuantityUomSpinner = dialogView.findViewById(R.id.transactiondialog_quantityuom_field);
        mPriceEditText = dialogView.findViewById(R.id.transactiondialog_price_field);
        mCurrencyTextView = dialogView.findViewById(R.id.transactiondialog_currency_field);
        mValidFromDateView = dialogView.findViewById(R.id.transactiondialog_validfrom_field);
        mValidityLengthEditText = dialogView.findViewById(R.id.transactiondialog_validitylength_field);
        mValLengthUomSpinner = dialogView.findViewById(R.id.transactiondialog_vallengthuom_field);

        mDocDateView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                mDocumentDate.set(year,month,dayOfMonth);
            }
        });
        mValidFromDateView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                mValFromDate.set(year,month,dayOfMonth);
            }
        });


        ArrayAdapter<String> quantityUomAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Unit.getColumnValueList(null,null, Unit.COL_NAME));
        mQuantityUomSpinner.setAdapter(quantityUomAdapter);

        ArrayAdapter<String> valLengthUomAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Unit.getColumnValueList(null,null, Unit.COL_NAME));
        mValLengthUomSpinner.setAdapter(valLengthUomAdapter);

//      TODO: proper currency derivation from partner
        mCurrencyTextView.setText( "euro");

        //attempt to load passed arguments
        if (getArguments() != null) {
            mCarId = getArguments().getString(Constants.KEY_CAR_ID);
            mTrxId = getArguments().getString(Constants.KEY_TRANSACTION_ID);
        }

//      get car
        carCollRef
                .whereEqualTo(Constants.KEY_CAR_ID, mCarId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(Constants.log_tag, "Firebase listening failed!");
                            return;
                        } else {
                            mCarDocumentSnapshots = queryDocumentSnapshots.getDocuments();
                        }
                    }
                });

//      get transaction
        trxCollRef
                .whereEqualTo(Constants.KEY_CAR_ID, mCarId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(Constants.log_tag, "Firebase listening failed!");
                            return;
                        } else {
                            mTrxDocumentSnapshots = queryDocumentSnapshots.getDocuments();
                        }
                    }
                });

//      get material master
        matCollRef
                .whereEqualTo(Constants.KEY_USER_ID, mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(Constants.log_tag, "Firebase listening failed!");
                            return;
                        } else {
                            mMatDocumentSnapshots = queryDocumentSnapshots.getDocuments();
                            ArrayAdapter<String> materialNameAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,
                                    ListGenerator.getColumnValueList(mMatDocumentSnapshots,null,null,Material.COL_NAME));
                            mMaterialNameSpinner.setAdapter(materialNameAdapter);
                        }
                    }
                });

//      get partner master
        parCollRef
                .whereEqualTo(Constants.KEY_USER_ID, mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(Constants.log_tag, "Firebase listening failed!");
                            return;
                        } else {
                            mParDocumentSnapshots = queryDocumentSnapshots.getDocuments();
                            ArrayAdapter<String> partnerNameAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,
                                    ListGenerator.getColumnValueList(mParDocumentSnapshots,null,null,Partner.COL_NAME));
                            mPartnerNameSpinner.setAdapter(partnerNameAdapter);
                        }
                    }
                });

        if (mTrxId != null) {
            builder.setTitle(R.string.transactiondialog_title_edit);
            trxCollRef.document(mTrxId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            mMaterialNameSpinner.setSelection(ListGenerator.getIndexByColumnValue(mMatDocumentSnapshots,Material.COL_ID, (String)document.get(Constants.KEY_MATERIAL_ID)));
                            mDescriptionEditText.setText((String) document.get(Constants.KEY_DESCRIPTION));
                            try {
                                mDocDateView.setDate(((Date)document.get(Constants.KEY_DOCUMENT_DATE)).getTime());
                            } catch (Exception e) {}
                            try {
                                mOdometerEditText.setText(Integer.toString((int) document.get(Constants.KEY_ODOMETER)));
                            } catch (Exception e) {}
                            mPartnerNameSpinner.setSelection(ListGenerator.getIndexByColumnValue(mParDocumentSnapshots,Partner.COL_ID, (String)document.get(Constants.KEY_PARTNER_ID)));
                            try {
                                mQuantityEditText.setText(Double.toString((double) document.get(Constants.KEY_QUANTITY)));
                            } catch (Exception e) {}
                            mQuantityUomSpinner.setSelection(Unit.getIndexByColumnValue(Unit.COL_ID, (String)document.get(Constants.KEY_QUANTITY_UNIT)));
                            try {
                                mPriceEditText.setText(Double.toString((double) document.get(Constants.KEY_UNIT_PRICE)));
                            } catch (Exception e) {}
                            mCurrencyTextView.setText( Unit.getColumnValueByFilterValue(Unit.COL_ID,(String) document.get(Constants.KEY_CURRENCY),Unit.COL_NAME));
                            try {
                                mValidFromDateView.setDate(((Date)document.get(Constants.KEY_VALID_FROM)).getTime());
                            } catch (Exception e) {}
                            try {
                                mValidityLengthEditText.setText(Double.toString((double) document.get(Constants.KEY_VALIDITY_LENGTH)));
                            } catch (Exception e) {}
                            mValLengthUomSpinner.setSelection(Unit.getIndexByColumnValue(Unit.COL_ID, (String)document.get(Constants.KEY_VALIDITY_LENGTH_UNIT)));
                        } else {
                            Log.d(Constants.log_tag, "No such document");
                        }
                    } else {
                        Log.d(Constants.log_tag, "get failed with ", task.getException());
                    }
                }
            });
        } else {
            builder.setTitle(R.string.transactiondialog_title_add);
            mDocDateView.setDate(System.currentTimeMillis(),false,false);
        }

        builder.setView(dialogView)
               .setNegativeButton(android.R.string.cancel,null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                  create new item with captured details
                    Map<String, Object> trx = new HashMap<>();
                    trx.put(Constants.KEY_CAR_ID, mCarId);
                    trx.put(Constants.KEY_MATERIAL_ID, ListGenerator.getColumnValueByIndex(mMatDocumentSnapshots, (int) mMaterialNameSpinner.getSelectedItemId(), Material.COL_ID));
                    trx.put(Constants.KEY_DESCRIPTION, mDescriptionEditText.getText().toString());
                    trx.put(Constants.KEY_DOCUMENT_DATE, mDocumentDate.getTime());
                    trx.put(Constants.KEY_ODOMETER, mOdometerEditText.getText().toString());
                    trx.put(Constants.KEY_PARTNER_ID, ListGenerator.getColumnValueByIndex(mParDocumentSnapshots, (int) mPartnerNameSpinner.getSelectedItemId(), Partner.COL_ID));
                    try {
                        trx.put(Constants.KEY_QUANTITY, Double.parseDouble(mQuantityEditText.getText().toString()));
                    } catch (NumberFormatException e) {}
                    trx.put(Constants.KEY_QUANTITY_UNIT, Unit.getColumnValueByIndex((int) mQuantityUomSpinner.getSelectedItemId(),Unit.COL_ID));
                    try {
                        trx.put(Constants.KEY_UNIT_PRICE, Double.parseDouble(mPriceEditText.getText().toString()));
                    } catch (NumberFormatException e) {}
                    trx.put(Constants.KEY_CURRENCY, Unit.getColumnValueByFilterValue(Unit.COL_NAME, mCurrencyTextView.getText().toString(),Unit.COL_ID));
                    trx.put(Constants.KEY_VALID_FROM, mValFromDate.getTime());
                    try {
                        trx.put(Constants.KEY_VALIDITY_LENGTH, Double.parseDouble(mValidityLengthEditText.getText().toString()));
                    } catch (NumberFormatException e) {}
                    trx.put(Constants.KEY_VALIDITY_LENGTH_UNIT, Unit.getColumnValueByIndex((int) mValLengthUomSpinner.getSelectedItemId(),Unit.COL_ID));

                    if ( mTrxId != null) {
                        trxCollRef.document(mTrxId).set(trx).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mListener.applyChangesDialogTransactionInput(mTrxId);
                                } else {
                                    Log.d(Constants.log_tag, "update failed with ", task.getException());
                                }
                            }
                        });
                    } else {
                         trxCollRef.add(trx).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                             @Override
                             public void onComplete(@NonNull Task<DocumentReference> task) {
                                 if (task.isSuccessful()) {
                                     DocumentReference documentReference = task.getResult();
                                     if (documentReference != null) {
                                         mTrxId = documentReference.getId();
                                         mListener.applyChangesDialogTransactionInput(mTrxId);
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
            mListener = (DialogTransactionInputListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement DialogTransactionInputListener");
        }
    }

}