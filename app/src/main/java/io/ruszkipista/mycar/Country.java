package io.ruszkipista.mycar;

import java.util.Arrays;
import java.util.List;

public class Country {
    private String id;
    private String name;
    private String currency;

    public static final String COL_ID = Constants.KEY_ID;
    public static final String COL_NAME = Constants.KEY_NAME;
    public static final String COL_CURRENCY = Constants.KEY_CURRENCY;

    public static final List<Country> list = Arrays.asList(
            new Country("IE", "Republic Of Ireland", "€"),
            new Country("HU", "Hungary", "Ft"),
            new Country("NI", "Northern Ireland", "£"),
            new Country("UK", "United Kingdom", "£"),
            new Country("USA", "US America", "$")
    );

    private Country(String id, String name, String currency) {
        this.id = id;
        this.name = name;
        this.currency = currency;
    }

    private static String getColumnValue(Country country, String returnColumnName) {
        String value = null;
        switch (returnColumnName) {
            case COL_ID:
                value = country.id;
                break;
            case COL_NAME:
                value = country.name;
                break;
            case COL_CURRENCY:
                value = country.currency;
                break;
        }
        return value;
    }

    private static boolean compareColumnValue(Country country, String compareColumnName, String filterValue) {
        boolean match = false;
        if (filterValue==null) {
            match = true;
        } else {
            switch (compareColumnName) {
                case COL_ID:
                    if (filterValue.equals(country.id)) match = true;
                    break;
                case COL_NAME:
                    if (filterValue.equals(country.name)) match = true;
                    break;
                case COL_CURRENCY:
                    if (filterValue.equals(country.currency)) match = true;
                    break;
            }
        }
        return match;
    }

    private static int countFiltered(String filterColumnName, String filterValue) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (compareColumnValue(list.get(i), filterColumnName, filterValue)) count++;
        }
        return count;
    }

    public static String[] getColumnValueList(String filterColumnName, String filterValue, String returnColumnName) {
        String[] columnList = null;
        int count = 0;
        int size = list.size();
        if (size > 0) {
            columnList = new String[size];
            for (int i = 0; i < size; i++) {
                if (compareColumnValue(list.get(i), filterColumnName, filterValue)) {
                    columnList[count] = getColumnValue(list.get(i),returnColumnName);
                    count++;
                }
            }
        }
        return columnList;
    }

    public static String getColumnValueByFilterValue(String filterColumnName, String filterValue, String returnColumnName) {
        String value = null;
        for (int i = 0; i < list.size(); i++) {
            if (filterValue == null
                    || filterColumnName == null
                    || filterValue.equals(getColumnValue(list.get(i),filterColumnName))) {
                value = getColumnValue(list.get(i), returnColumnName);
                break;
            }
        }
        return value;
    }

    public static int getIndexByColumnValue(String filterColumnName, String filterValue) {
        int index = -1;
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (filterValue == null
                    || filterColumnName == null
                    || filterValue.equals(getColumnValue(list.get(i),filterColumnName))) {
                index = count;
                break;
            }
            count++;
        }
        return index;
    }

    public static String getColumnValueByIndex(int index, String returnColumnName) {
        String value = null;
        value = getColumnValue(list.get(index), returnColumnName);
        return value;
    }
}