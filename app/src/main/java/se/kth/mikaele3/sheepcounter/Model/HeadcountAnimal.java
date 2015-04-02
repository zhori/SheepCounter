package se.kth.mikaele3.sheepcounter.Model;

import java.util.List;

/**
 * Represents an animal which is being counted in a headcount.
 *
 * Created by Mikael on 2015-03-31.
 */
public class HeadcountAnimal {

    private final String name;
    private final String animalID;
    private final List<String> countedBy;

    public HeadcountAnimal(String name, String animalID, List<String> countedBy) {
        this.name = name;
        this.animalID = animalID;
        this.countedBy = countedBy;
    }

    public String getName() {
        return name;
    }

    public List<String> getCountedBy() {
        return countedBy;
    }

    public String getAnimalID() {
        return animalID;
    }
}
