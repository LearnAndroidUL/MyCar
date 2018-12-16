package io.ruszkipista.mycar;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class ListGenerator {

    public static String getColumnValueById(List<DocumentSnapshot> documents, String returnColumnName, String id) {
        String value = null;
        DocumentSnapshot ds;
        if (id != null) {
            for (int i = 0; i < documents.size(); i++) {
                ds = documents.get(i);
                if (id.equals(ds.getId())) {
                    value = ds.get(returnColumnName).toString();
                    break;
                }
            }
        }
        return value;
    }

    public static String[] getColumnValueList(List<DocumentSnapshot> documents, String filterColumnName, String filterValue, String returnColumnName) {
        String[] columnList = null;
        DocumentSnapshot ds;
        int size = countFiltered(documents, filterColumnName, filterValue);
        if (size > 0) {
            columnList = new String[size];
            for (int i = 0; i < documents.size(); i++) {
                ds = documents.get(i);
                if (filterColumnName == null || filterValue.equals(ds.get(filterColumnName))) {
                    if (returnColumnName.equals(Constants.KEY_ID)) {
                        columnList[i] = ds.getId();
                    } else {
                        columnList[i] = ds.get(returnColumnName).toString();
                    }
                }
            }
        }
        return columnList;
    }

    private static int countFiltered(List<DocumentSnapshot> documents, String filterColumnName, String filterValue) {
        int count = 0;
        for (int i = 0; i < documents.size(); i++) {
            if (filterColumnName == null || filterValue.equals((String) documents.get(i).get(filterColumnName)))
                count++;
        }
        return count;
    }

    public static int getIndexByColumnValue(List<DocumentSnapshot> documents, String filterColumnName, String filterValue) {
        int index = -1;
        int count = 0;
        if (filterValue != null) {
            for (int i = 0; i < documents.size(); i++) {
                if (filterColumnName == null || filterValue.equals((String) documents.get(i).get(filterColumnName))) {
                    index = count;
                    break;
                }
                count++;
            }
        }
        return index;
    }

    public static String getColumnValueByIndex(List<DocumentSnapshot> documents, int index, String returnColumnName) {
        String value = null;
        if (returnColumnName.equals(Constants.KEY_ID)) {
            value = (String) documents.get(index).getId();
        } else {
            value = (String) documents.get(index).get(returnColumnName);
        }
        return value;
    }
}
