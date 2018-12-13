package io.ruszkipista.mycar;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;
import java.util.List;

public class Country {
    private String code;
    private String name;
    private String currency;

    public static final List<Country> countries = Arrays.asList(
            new Country("IE", "Republic Of Ireland", "€"),
            new Country("HU", "Hungary", "Ft"),
            new Country("NI", "Northern Ireland", "£"),
            new Country("UK", "United Kingdom", "£"),
            new Country("USA", "US America", "$")
    );

    private Country(String code, String name, String currency) {
        this.code = code;
        this.name = name;
        this.currency = currency;
    }

    public static String getNameById(String id) {
        String name = null;
        for (Country country : countries) {
            if (id.equals(country.code)) {
                name = country.name;
                break;
            }
        }
        return name;
    }

    public static int getIndexById(String id) {
        int index = -1;
        int count = 0;
        for (Country country : countries) {
            if (id.equals(country.code)) {
                index = count;
                break;
            }
            count++;
        }
        return index;
    }

    public static String getIdByIndex(int index) {
        String id = null;
        id = countries.get(index).code;
        return id;
    }

    public static String[] getCodeList() {
        String[] columnList = null;
        int size = countries.size();
        if (size > 0) {
            columnList = new String[size];
            for (int i = 0; i < size; i++) {
                columnList[i] = countries.get(i).code;
            }
        }
        return columnList;
    }

    public static String[] getNameList() {
        String[] columnList = null;
        int size = countries.size();
        if (size > 0) {
            columnList = new String[size];
            for (int i = 0; i < size; i++) {
                columnList[i] = countries.get(i).name;
            }
        }
        return columnList;
    }
}