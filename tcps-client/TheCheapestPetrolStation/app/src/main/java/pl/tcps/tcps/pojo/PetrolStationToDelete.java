package pl.tcps.tcps.pojo;

import java.util.Random;

public class PetrolStationToDelete {

    private String name;
    private String description;

    private static String[] names={"Orlen", "BP", "Shell", "Luk Oil", "mova","noname", "carrefour", "tarns oil"};
    private static String[] descriptions={"Oki doki", "Dużo dziwek", "Drogo w chuj", "bfjkdhdj", "njknfsa", "qwerty", "hugshjufd", "bhfdshjb"};

    public PetrolStationToDelete(){
        Random random = new Random();
        name = names[random.nextInt(names.length)];
        description = descriptions[random.nextInt(descriptions.length)];
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
