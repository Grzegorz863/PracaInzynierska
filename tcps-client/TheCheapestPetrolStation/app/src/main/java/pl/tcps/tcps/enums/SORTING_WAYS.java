package pl.tcps.tcps.enums;

public enum SORTING_WAYS {
    SORT_BY_DISTANCE(0), SORT_BY_PB_95_PRICE(1), SORT_BY_PB_98_PRICE(2), SORT_BY_ON_PRICE(3), SORT_BY_LPG_PRICE(4);

    private final int way;

    SORTING_WAYS(final int newWay){
        way = newWay;
    }

    public int getValue() {
        return way;
    }
}
