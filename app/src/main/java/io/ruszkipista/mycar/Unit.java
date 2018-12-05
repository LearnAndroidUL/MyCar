package io.ruszkipista.mycar;

import java.util.Arrays;
import java.util.List;

public class Unit {
        private String unitOfMeasure;
        private String name;
        private String unitType;

    public static final String VALUE_UNIT_TYPE_COUNT        = "CNT";
    public static final String VALUE_UNIT_TYPE_VOLUME       = "VOL";
    public static final String VALUE_UNIT_TYPE_MASS         = "MASS";
    public static final String VALUE_UNIT_TYPE_LENGTH       = "DIST";
    public static final String VALUE_UNIT_TYPE_AREA         = "AREA";
    public static final String VALUE_UNIT_TYPE_FUEL_ECONOMY = "FECO";
    public static final String VALUE_UNIT_TYPE_CURRENCY     = "CURR";

    public static final String LITRE           = "L";
    public static final String GALLON_IMPERIAL = "galImp";
    public static final String KILOMETRE       = "km";
    public static final String MILE            = "mi";
    public static final String Lper100KM       = "L100km";
    public static final String MIperGallI      = "mpgImp";

        public static final List<Unit> units = Arrays.asList(
                new Unit(LITRE,      "litre",           VALUE_UNIT_TYPE_VOLUME),
                new Unit("ml",     "milli litre",     VALUE_UNIT_TYPE_VOLUME),
                new Unit(GALLON_IMPERIAL, "gallon imperial", VALUE_UNIT_TYPE_VOLUME),
                new Unit(Lper100KM, "L/100km",         VALUE_UNIT_TYPE_FUEL_ECONOMY),
                new Unit(MIperGallI, "miles/gallon Imp",VALUE_UNIT_TYPE_FUEL_ECONOMY),
                new Unit("kg",     "kilogramme",      VALUE_UNIT_TYPE_MASS),
                new Unit("g",      "gramme",          VALUE_UNIT_TYPE_MASS),
                new Unit("lb",     "pound",           VALUE_UNIT_TYPE_MASS),
                new Unit(KILOMETRE,     "kilometre",       VALUE_UNIT_TYPE_LENGTH),
                new Unit("m",      "metre",           VALUE_UNIT_TYPE_LENGTH),
                new Unit("cm",     "centimetre",      VALUE_UNIT_TYPE_LENGTH),
                new Unit("mm",     "millimetre",      VALUE_UNIT_TYPE_LENGTH),
                new Unit(MILE,     "mile",            VALUE_UNIT_TYPE_LENGTH),
                new Unit("ft",     "foot",            VALUE_UNIT_TYPE_LENGTH),
                new Unit("yd",     "yard",            VALUE_UNIT_TYPE_LENGTH),
                new Unit("in",     "inch",            VALUE_UNIT_TYPE_LENGTH),
                new Unit("m2",     "square metre",    VALUE_UNIT_TYPE_AREA),
                new Unit("ea",     "each",            VALUE_UNIT_TYPE_COUNT),
                new Unit("pc",     "piece",           VALUE_UNIT_TYPE_COUNT),
                new Unit("$",      "USA dollar",      VALUE_UNIT_TYPE_CURRENCY),
                new Unit("€",      "euro",            VALUE_UNIT_TYPE_CURRENCY),
                new Unit("£",      "pound",           VALUE_UNIT_TYPE_CURRENCY),
                new Unit("Ft",     "HU forint",       VALUE_UNIT_TYPE_CURRENCY)
                );

        private Unit(String unitOfMeasure, String name, String unitType) {
            this.unitOfMeasure = unitOfMeasure;
            this.name = name;
            this.unitType = unitType;
        }

    public static String getNameById(String id){
        String name = null;
        for (Unit unit:units) {
            if (id.equals(unit.unitOfMeasure)) {
                name = unit.name;
                break;
            }
        }
        return name;
    }
}
