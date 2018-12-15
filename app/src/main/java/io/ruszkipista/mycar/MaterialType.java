package io.ruszkipista.mycar;

import java.util.Arrays;
import java.util.List;

public class MaterialType {
    private String id;
    private String name;
    private String unitType;
    private String expenseType;

    public static final String COL_ID = Constants.KEY_ID;
    public static final String COL_NAME = Constants.KEY_NAME;
    public static final String COL_UNIT_TYPE = Constants.KEY_UNIT_TYPE;
    public static final String COL_EXPENSE_TYPE = Constants.KEY_EXPENSE_TYPE;

    public static final String MAT_TYPE_FUEL = "FUEL";
    public static final String MAT_TYPE_PART = "PART";
    public static final String MAT_TYPE_CONSUMABLE = "CONSUMABLE";
    public static final String MAT_TYPE_SERVICE = "SERVICE";
    public static final String MAT_TYPE_INSURANCE = "INSURANCE";
    public static final String MAT_TYPE_TAX = "TAX";

    public static final String EXP_TYPE_OP = "OPEX";
    public static final String EXP_TYPE_CAP = "CAPEX";

    public static final List<MaterialType> list = Arrays.asList(
            new MaterialType(MAT_TYPE_FUEL, "fuel", Unit.VALUE_UNIT_TYPE_VOLUME, EXP_TYPE_OP),
            new MaterialType(MAT_TYPE_PART, "part", Unit.VALUE_UNIT_TYPE_COUNT, EXP_TYPE_CAP),
            new MaterialType(MAT_TYPE_CONSUMABLE, "consumable", Unit.VALUE_UNIT_TYPE_COUNT, EXP_TYPE_OP),
            new MaterialType(MAT_TYPE_SERVICE, "service", Unit.VALUE_UNIT_TYPE_COUNT, EXP_TYPE_OP),
            new MaterialType(MAT_TYPE_INSURANCE, "insurance", Unit.VALUE_UNIT_TYPE_COUNT, EXP_TYPE_OP),
            new MaterialType(MAT_TYPE_TAX, "tax", Unit.VALUE_UNIT_TYPE_COUNT, EXP_TYPE_OP)
    );

    private MaterialType(String id, String name, String unitType, String expenseType) {
        this.id = id;
        this.name = name;
        this.unitType = unitType;
        this.expenseType = expenseType;
    }

    private static String getColumnValue(MaterialType materialType, String returnColumnName) {
        String value = null;
        switch (returnColumnName) {
            case COL_ID:
                value = materialType.id;
                break;
            case COL_NAME:
                value = materialType.name;
                break;
            case COL_UNIT_TYPE:
                value = materialType.unitType;
                break;
            case COL_EXPENSE_TYPE:
                value = materialType.expenseType;
                break;
        }
        return value;
    }

    private static boolean compareColumnValue(MaterialType materialType, String compareColumnName, String filterValue) {
        boolean match = false;
        if (filterValue == null) {
            match = true;
        } else {
            switch (compareColumnName) {
                case COL_ID:
                    if (filterValue.equals(materialType.id)) match = true;
                    break;
                case COL_NAME:
                    if (filterValue.equals(materialType.name)) match = true;
                    break;
                case COL_UNIT_TYPE:
                    if (filterValue.equals(materialType.unitType)) match = true;
                    break;
                case COL_EXPENSE_TYPE:
                    if (filterValue.equals(materialType.expenseType)) match = true;
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
                    columnList[count] = getColumnValue(list.get(i), returnColumnName);
                    count++;
                }
            }
        }
        return columnList;
    }

    public static String getColumnValueByFilterValue(String filterColumnName, String filterValue, String returnColumnName) {
        String value = null;
        for (int i = 0; i < list.size(); i++) {
            if (filterValue.equals(getColumnValue(list.get(i), filterColumnName))) {
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
            if (filterValue.equals(getColumnValue(list.get(i), filterColumnName))) {
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


