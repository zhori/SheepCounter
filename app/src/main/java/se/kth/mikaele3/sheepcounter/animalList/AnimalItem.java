package se.kth.mikaele3.sheepcounter.animalList;

/**
 * An AnimalItem represents one animal to be used by an AnimalArrayAdapter.
 *
 * Created by Mikael on 2015-03-23.
 */
public class AnimalItem {

    private final String name;
    private boolean checked;

    public AnimalItem(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
