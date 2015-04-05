package se.kth.mikaele3.sheepcounter.animalList;

import java.io.Serializable;
import java.util.List;

/**
 * An AnimalItem represents one animal to be used by an AnimalArrayAdapter.
 *
 * Created by Mikael on 2015-03-23.
 */
public class AnimalItem implements Serializable{

    private final String name;
    private final String animalID;
    private final List<String> countedBy;
    private boolean checked;

    public AnimalItem(String name, String animalID, List<String> countedBy, boolean checked) {
        this.name = name;
        this.checked = checked;
        this.animalID = animalID;
        this.countedBy = countedBy;

    }

    public String getName() {
        return name;
    }

    public String getAnimalID() {return animalID;}

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isCountedBySomeoneElse(String username){

        if(countedBy.isEmpty())
            return false;

        if(countedBy.contains(username) && countedBy.size() == 1)
            return false;

        return true;
    }
}
