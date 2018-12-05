package io.ruszkipista.mycar;

//import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

public class Material extends AppCompatActivity implements CodeList{
//    private String Id;
//    private String name;
//    private String type;
//    private String unitOfMeasure;
    public String[] codeList;
    public String[] nameList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CollectionReference collRef = FirebaseFirestore.getInstance()
                                         .collection(Constants.firebase_collection_material);
    private List<DocumentSnapshot> docSnapshots = new ArrayList<>();

    public static final String TYPE_FUEL        = "FUEL";
    public static final String TYPE_PART        = "PART";
    public static final String TYPE_CONSUMABLE  = "CONSUMABLE";
    public static final String TYPE_SERVICE     = "SERVICE";
    public static final String TYPE_INSURANCE   = "INSURANCE";
    public static final String TYPE_TAX         = "TAX";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        collRef.whereEqualTo(Constants.KEY_USER_ID, mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(Constants.log_tag, "Firebase listening failed!");
                            return;
                        } else {
                            docSnapshots = queryDocumentSnapshots.getDocuments();
                        }
                    }
                });
    }

    @Override
    public void produceLists(String filterType) {
        DocumentSnapshot ds;
        int i;
        int count = 0;
        for (i=0;i<docSnapshots.size();i++) {
            ds = docSnapshots.get(i);
            if (filterType.equals((String)ds.get(Constants.KEY_MATERIAL_TYPE))) count++;
        }

        if (count > 0) {
            codeList = new String[count];
            nameList = new String[count];
            for (i=0;i<docSnapshots.size();i++) {
                ds = docSnapshots.get(i);
                if (filterType.equals((String)ds.get(Constants.KEY_MATERIAL_TYPE))) {
                    codeList[i] = ds.getId();
                    nameList[i] = (String) ds.get(Constants.KEY_NAME);
                }
            }
        }
    }

    @Override
    public void updateLists(String newMemberId) {

    }

    public static String getNameById(List<DocumentSnapshot> documents, String docId){
        String name = null;
        DocumentSnapshot ds;
        int i;
        for (i=0;i<documents.size();i++) {
            ds = documents.get(i);
            if (docId.equals(ds.getId())) {
                name = (String)ds.get(Constants.KEY_NAME);
                break;
            }
        }
        return name;
    }
}
