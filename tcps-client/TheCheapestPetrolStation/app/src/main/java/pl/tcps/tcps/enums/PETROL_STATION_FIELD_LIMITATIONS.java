package pl.tcps.tcps.enums;

public enum PETROL_STATION_FIELD_LIMITATIONS {

    STATION_NAME_MAX_LENGTH(100),
    STREET_NAME_MAX_LENGTH(70),
    APARTMENT_NUMBER_MAX_LENGTH(20),
    CITY_NAME_MAX_LENGTH(50),
    POSTAL_CODE_MAX_LENGTH(6),
    DESCRIPTION_MAX_LENGTH(255);

    private final int maxFieldLenght;

    PETROL_STATION_FIELD_LIMITATIONS(final int length){
        maxFieldLenght = length;
    }

    public int getValue() {
        return maxFieldLenght;
    }
}
