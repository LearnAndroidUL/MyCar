package io.ruszkipista.mycar;

import java.util.Arrays;
import java.util.List;

public class Unit {
    private String id;
    private String name;
    private String unitType;

    public static final String COL_ID = Constants.KEY_ID;
    public static final String COL_NAME = Constants.KEY_NAME;
    public static final String COL_UNIT_TYPE = Constants.KEY_UNIT_TYPE;

    public static final String VALUE_UNIT_TYPE_COUNT        = "CNT";
    public static final String VALUE_UNIT_TYPE_VOLUME       = "VOL";
    public static final String VALUE_UNIT_TYPE_MASS         = "MASS";
    public static final String VALUE_UNIT_TYPE_LENGTH       = "DIST";
    public static final String VALUE_UNIT_TYPE_AREA         = "AREA";
    public static final String VALUE_UNIT_TYPE_FUEL_ECONOMY = "FECO";
    public static final String VALUE_UNIT_TYPE_CURRENCY     = "CURR";
    public static final String VALUE_UNIT_TYPE_TIME         = "TIME";

    public static final String LITRE           = "L";
    public static final String GALLON_IMPERIAL = "galImp";
    public static final String KILOMETRE       = "km";
    public static final String MILE            = "mi";
    public static final String Lper100KM       = "L100km";
    public static final String MIperGallI      = "mpgImp";

        public static final List<Unit> list = Arrays.asList(
                new Unit(LITRE,        "litre",           VALUE_UNIT_TYPE_VOLUME),
                new Unit("ml",     "milli litre",     VALUE_UNIT_TYPE_VOLUME),
                new Unit(GALLON_IMPERIAL, "gallon imperial", VALUE_UNIT_TYPE_VOLUME),
                new Unit(Lper100KM,    "L/100km",         VALUE_UNIT_TYPE_FUEL_ECONOMY),
                new Unit(MIperGallI,   "miles/gallon Imp",VALUE_UNIT_TYPE_FUEL_ECONOMY),
                new Unit("kg",     "kilogramme",      VALUE_UNIT_TYPE_MASS),
                new Unit("g",      "gramme",          VALUE_UNIT_TYPE_MASS),
                new Unit("lb",     "pound",           VALUE_UNIT_TYPE_MASS),
                new Unit(KILOMETRE,    "kilometre",       VALUE_UNIT_TYPE_LENGTH),
                new Unit("m",      "metre",           VALUE_UNIT_TYPE_LENGTH),
                new Unit("cm",     "centimetre",      VALUE_UNIT_TYPE_LENGTH),
                new Unit("mm",     "millimetre",      VALUE_UNIT_TYPE_LENGTH),
                new Unit(MILE,         "mile",            VALUE_UNIT_TYPE_LENGTH),
                new Unit("ft",     "foot",            VALUE_UNIT_TYPE_LENGTH),
                new Unit("yd",     "yard",            VALUE_UNIT_TYPE_LENGTH),
                new Unit("in",     "inch",            VALUE_UNIT_TYPE_LENGTH),
                new Unit("m2",     "square metre",    VALUE_UNIT_TYPE_AREA),
                new Unit("ea",     "each",            VALUE_UNIT_TYPE_COUNT),
                new Unit("pc",     "piece",           VALUE_UNIT_TYPE_COUNT),
                new Unit("$",      "USA dollar",      VALUE_UNIT_TYPE_CURRENCY),
                new Unit("€",      "euro",            VALUE_UNIT_TYPE_CURRENCY),
                new Unit("£",      "pound",           VALUE_UNIT_TYPE_CURRENCY),
                new Unit("Ft",     "HU forint",       VALUE_UNIT_TYPE_CURRENCY),
                new Unit("year",   "year",            VALUE_UNIT_TYPE_TIME),
                new Unit("month",  "year",            VALUE_UNIT_TYPE_TIME),
                new Unit("day",    "day",             VALUE_UNIT_TYPE_TIME)
                );

        private Unit(String id, String name, String unitType) {
            this.id = id;
            this.name = name;
            this.unitType = unitType;
        }

    private static String getColumnValue(Unit unit, String returnColumnName) {
        String value = null;
        switch (returnColumnName) {
            case COL_ID:
                value = unit.id;
                break;
            case COL_NAME:
                value = unit.name;
                break;
            case COL_UNIT_TYPE:
                value = unit.unitType;
                break;
        }
        return value;
    }

    private static boolean compareColumnValue(Unit unit, String compareColumnName, String filterValue) {
        boolean match = false;
        if (filterValue==null) {
            match = true;
        } else {
            switch (compareColumnName) {
                case COL_ID:
                    if (filterValue.equals(unit.id)) match = true;
                    break;
                case COL_NAME:
                    if (filterValue.equals(unit.name)) match = true;
                    break;
                case COL_UNIT_TYPE:
                    if (filterValue.equals(unit.unitType)) match = true;
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
            if (filterValue.equals(getColumnValue(list.get(i),filterColumnName))) {
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
            if (filterValue.equals(getColumnValue(list.get(i),filterColumnName))) {
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
