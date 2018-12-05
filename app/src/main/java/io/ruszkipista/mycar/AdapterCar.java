package io.ruszkipista.mycar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class AdapterCar extends RecyclerView.Adapter<AdapterCar.CarViewHolder>{
    private List<DocumentSnapshot> mCarSnapshots = new ArrayList<>();
    private RecyclerView mRecyclerView;

    public AdapterCar(){
        CollectionReference carRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_car);
        carRef
                .orderBy(Constants.KEY_CREATED, Query.Direction.DESCENDING).limit(50)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(Constants.log_tag, "Firebase listening failed!");
                            return;
                        } else {
                            mCarSnapshots = queryDocumentSnapshots.getDocuments();
                            notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemview_car,viewGroup, false);
        return new CarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder carViewHolder, int i) {
        DocumentSnapshot car = mCarSnapshots.get(i);
        String carName = (String) car.get(Constants.KEY_NAME);
        carViewHolder.mCarNameTextView.setText(carName);
        String plateNumber = (String) car.get(Constants.KEY_PLATENUMBER);
        carViewHolder.mPlateNumberTextView.setText(plateNumber);
    }

    @Override
    public int getItemCount() {
        return mCarSnapshots.size();
    }

    class CarViewHolder extends RecyclerView.ViewHolder {
        private TextView mCarNameTextView;
        private TextView mPlateNumberTextView;

        public CarViewHolder(View itemView){
            super(itemView);
            mCarNameTextView = itemView.findViewById(R.id.itemview_carname);
            mPlateNumberTextView = itemView.findViewById(R.id.itemview_platenumber);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context,ActivityCarDetail.class);
                    DocumentSnapshot ds = mCarSnapshots.get(getAdapterPosition());
                    intent.putExtra(Constants.EXTRA_DOC_ID, ds.getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
