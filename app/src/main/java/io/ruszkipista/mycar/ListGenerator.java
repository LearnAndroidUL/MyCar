package io.ruszkipista.mycar;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class ListGenerator {

    public static String getColumnValueById(List<DocumentSnapshot> documents, String returnColumnName, String id){
        String value = null;
        DocumentSnapshot ds;
        for (int i=0;i<documents.size();i++) {
            ds = documents.get(i);
            if (id.equals(ds.getId())) {
                value = ds.get(returnColumnName).toString();
                break;
            }
        }
        return value;
    }

    public static String[] getColumnValueList(List<DocumentSnapshot> documents, String filterColumnName, String filterType, String returnColumnName) {
        String[] columnList = null;
        DocumentSnapshot ds;
        int size = countFiltered(documents,filterColumnName,filterType);
        if (size > 0) {
            columnList = new String[size];
            for (int i=0;i<documents.size();i++) {
                ds = documents.get(i);
                if (filterType.equals(ds.get(filterColumnName))) {
                    columnList[i] = ds.get(returnColumnName).toString();
                }
            }
        }
        return columnList;
    }

    public static String[] getIdList(List<DocumentSnapshot> documents, String filterColumnName, String filterValue) {
        String[] codeList = null;
        DocumentSnapshot ds;
        int size = countFiltered(documents,filterColumnName,filterValue);
        if (size > 0) {
            codeList = new String[size];
            for (int i=0;i<documents.size();i++) {
                ds = documents.get(i);
                if (filterValue.equals(ds.get(filterColumnName))) {
                    codeList[i] = ds.getId();
                }
            }
        }
        return codeList;
    }

    private static int countFiltered(List<DocumentSnapshot> documents, String columnName, String filterValue) {
        DocumentSnapshot ds;
        int count = 0;
        for (int i=0;i<documents.size();i++) {
            ds = documents.get(i);
            if (filterValue.equals((String)ds.get(columnName))) count++;
        }
        return count;
    }
}
