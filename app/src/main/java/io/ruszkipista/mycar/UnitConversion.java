package io.ruszkipista.mycar;

import java.util.Arrays;
import java.util.List;

public class UnitConversion {
        private String fromUnitOfMeasure;
        private String toUnitOfMeasure;
        private double conversionFactor;

        public static final List<UnitConversion> unitConversions = Arrays.asList(
                new UnitConversion("lb",     "kg",    0.45359237),
                new UnitConversion("L",      "galImp",0.2199692482990878),
                new UnitConversion("L",      "ml",    1000.0),
                new UnitConversion("L100km", "mpgImp",282.48093627967),
                new UnitConversion("kg",     "g",     1000.0),
                new UnitConversion("km",     "m",     1000.0),
                new UnitConversion("m",      "cm",    100.0),
                new UnitConversion("m",      "mm",    1000.0),
                new UnitConversion("cm",     "mm",    10.0),
                new UnitConversion("mi",     "km",    1.609344),
                new UnitConversion("mi",     "ft",    5280),
                new UnitConversion("mi",     "yd",    1760),
                new UnitConversion("ft",     "m",     0.3048),
                new UnitConversion("ft",     "in",    12.0),
                new UnitConversion("yd",     "ft",    3.0),
                new UnitConversion("yd",     "m",     0.9144),
                new UnitConversion("in",     "mm",    25.4),
                new UnitConversion("€",      "£",     0.89050),
                new UnitConversion("€",      "Ft",    323.82),
                new UnitConversion("€",      "$",     1.134)
        );

        private UnitConversion(String fromUnitOfMeasure,
                               String toUnitOfMeasure,
                               double conversionFactor) {
            this.fromUnitOfMeasure = fromUnitOfMeasure;
            this.toUnitOfMeasure = toUnitOfMeasure;
            this.conversionFactor = conversionFactor;
        }

        public static double convertUnit(String fromUnitOfMeasure, double fromQuantity, String toUnitOfMeasure){
            double toQuantity = 0.0;

            if ( ! fromUnitOfMeasure.equals(toUnitOfMeasure) ) {
                for (UnitConversion uc : unitConversions) {
                    if (uc.fromUnitOfMeasure.equals(fromUnitOfMeasure) && uc.toUnitOfMeasure.equals(toUnitOfMeasure)) {
                        toQuantity = fromQuantity * uc.conversionFactor;
                        break;
                    } else if (uc.fromUnitOfMeasure.equals(toUnitOfMeasure) && uc.toUnitOfMeasure.equals(fromUnitOfMeasure)) {
                        toQuantity = fromQuantity / uc.conversionFactor;
                        break;
                    }
                }
            } else {
                toQuantity = fromQuantity;
            }
            return toQuantity;
        }

    public double fuelEconomy(String fuelUnitOfMeasure, double fuelQuantity,
                              String lengthUnitOfMeasure, double lengthQuantity,
                              String targetUnitOfMeasure){
        double targetQuantity = 0;
        double lengthQuantityConv = 0;
        double fuelQuantityConv = 0.0;

        switch (targetUnitOfMeasure) {
            case Unit.Lper100KM:
                fuelQuantityConv = convertUnit(fuelUnitOfMeasure,fuelQuantity,Unit.LITRE);
                lengthQuantityConv = convertUnit(lengthUnitOfMeasure,lengthQuantity,Unit.KILOMETRE);
                if (lengthQuantityConv != 0.0) targetQuantity = fuelQuantityConv / 100.0 * lengthQuantityConv;
                break;

            case Unit.MIperGallI:
                fuelQuantityConv = convertUnit(fuelUnitOfMeasure,fuelQuantity,Unit.GALLON_IMPERIAL);
                lengthQuantityConv = convertUnit(lengthUnitOfMeasure,lengthQuantity,Unit.MILE);
                if (fuelQuantityConv != 0.0) targetQuantity = lengthQuantityConv / fuelQuantityConv;
                break;
        }
        return targetQuantity;
    }
}
