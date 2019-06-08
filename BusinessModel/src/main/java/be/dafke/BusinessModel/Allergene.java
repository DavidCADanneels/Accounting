package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

public class Allergene extends BusinessObject {
    private String shortName;
    private String description;

    public Allergene(String id, String shortName, String description) {
        this.shortName = shortName;
        this.description = description;
        setName(id);
    }

    public String getShortName() {
        return shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
