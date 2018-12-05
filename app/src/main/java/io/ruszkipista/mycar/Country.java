package io.ruszkipista.mycar;

import java.util.Arrays;
import java.util.List;

public class Country {
        private String code;
        private String name;
        private String currency;

        public static final List<Country> countries = Arrays.asList(
                new Country("IE",  "Republic Of Ireland","€"),
                new Country("HU",  "Hungary",         "Ft"),
                new Country("NI",  "Northern Ireland","£"),
                new Country("UK",  "United Kingdom",  "£"),
                new Country("USA", "US America",      "$")
        );

        private Country(String code, String name, String currency) {
            this.code = code;
            this.name = name;
            this.currency = currency;
        }

    public static String getNameById(String id){
        String name = null;
        for (Country country:countries) {
            if (id.equals(country.code)) {
                name = country.name;
                break;
            }
        }
        return name;
    }
}