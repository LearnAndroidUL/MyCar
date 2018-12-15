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

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

public class AdapterTransaction extends RecyclerView.Adapter<AdapterTransaction.TransactionViewHolder>{
    private List<DocumentSnapshot> mTransactionSnapshots = new ArrayList<>();
    private RecyclerView mRecyclerView;

    public AdapterTransaction(){
        CollectionReference transactionRef = FirebaseFirestore.getInstance().collection(Constants.firebase_collection_transaction);
        transactionRef
                .orderBy(Constants.KEY_DOCUMENT_DATE, Query.Direction.DESCENDING).limit(50)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(Constants.log_tag, "Firebase listening failed!");
                            return;
                        } else {
                            mTransactionSnapshots = queryDocumentSnapshots.getDocuments();
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
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemview_transaction,viewGroup, false);
        return new TransactionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder transactionViewHolder, int i) {
        DocumentSnapshot trx = mTransactionSnapshots.get(i);
        String description = (String) trx.get(Constants.KEY_DESCRIPTION);
        transactionViewHolder.mDescriptionTextView.setText(description);
        String date = ((Date) trx.get(Constants.KEY_DOCUMENT_DATE)).toString();
        transactionViewHolder.mDateTextView.setText(date);
    }

    @Override
    public int getItemCount() {
        return mTransactionSnapshots.size();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        private TextView mDescriptionTextView;
        private TextView mDateTextView;

        public TransactionViewHolder(View itemView){
            super(itemView);
            mDescriptionTextView = itemView.findViewById(R.id.itemview_trx_description);
            mDateTextView = itemView.findViewById(R.id.itemview_trx_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context,ActivityCarDetail.class);
                    DocumentSnapshot ds = mTransactionSnapshots.get(getAdapterPosition());
                    intent.putExtra(Constants.EXTRA_DOC_ID, ds.getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
