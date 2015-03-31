package se.kth.mikaele3.sheepcounter.Model;

import java.util.List;

/**
 * Created by Mikael on 2015-03-30.
 */
public class AnimalListMetaInfo {

    private String listIdentifier;
    private String farm;
    private String name;
    private String createdBy;
    private List<String> users;

    public AnimalListMetaInfo(String listIdentifier, String farm, String name, String createdBy, List<String> users) {
        this.listIdentifier = listIdentifier;
        this.farm = farm;
        this.name = name;
        this.createdBy = createdBy;
        this.users = users;
    }

    public String getListIdentifier() {
        return listIdentifier;
    }

    public String getFarm() {
        return farm;
    }

    public String getName() {
        return name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public List<String> getUsers() {
        return users;
    }
}
