package io.ruszkipista.mycar;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.HashMap;
import java.util.Map;

public class DialogCarInput {

    public void showCarInputDialog(android.content.Context context,
                                   View view, DocumentSnapshot car,
                                   CollectionReference carRef) {

        final DocumentSnapshot oldCar = car;
        final CollectionReference carCollectionReference = carRef;
        final EditText carNameEditTextView = view.findViewById(R.id.cardialog_carname_field);
        final EditText plateNumberEditTextView = view.findViewById(R.id.cardialog_platenumber_field);
        final EditText carImageUrlEditTextView = view.findViewById(R.id.cardialog_imageurl_field);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        if (oldCar != null) {
            builder.setTitle(R.string.cardialog_title_edit);
            carNameEditTextView.setText((String) oldCar.get(Constants.KEY_CARNAME));
            plateNumberEditTextView.setText((String) oldCar.get(Constants.KEY_PLATENUMBER));
            carImageUrlEditTextView.setText((String) oldCar.get(Constants.KEY_CARIMAGEURL));
        } else {
            builder.setTitle(R.string.cardialog_title_add);
        }

        builder.setNegativeButton(android.R.string.cancel,null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String carName = carNameEditTextView.getText().toString();
                String plateNumber = plateNumberEditTextView.getText().toString();
                String carImageUrl = carImageUrlEditTextView.getText().toString();
//              create new item with captured details
                Map<String, Object> car = new HashMap<>();
                car.put(Constants.KEY_CARNAME, carName);
                car.put(Constants.KEY_PLATENUMBER, plateNumber);
                car.put(Constants.KEY_CARIMAGEURL, carImageUrl);
                if (oldCar != null) {
                    carCollectionReference.document(oldCar.getId()).update(car);
                } else {
                    carCollectionReference.add(car);
                }
            }
        });
        builder.create().show();
    }
}
