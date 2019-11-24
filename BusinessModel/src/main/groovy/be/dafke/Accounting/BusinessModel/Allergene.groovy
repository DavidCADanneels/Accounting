package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class Allergene extends BusinessObject {
    private String shortName
    private String description

    Allergene(String id, String shortName, String description) {
        this.shortName = shortName
        this.description = description
        setName(id)
    }

    String getShortName() {
        shortName
    }

    String getDescription() {
        description
    }

    void setShortName(String shortName) {
        this.shortName = shortName
    }

    @Override
    boolean isDeletable(){
        true
    }

    void setDescription(String description) {
        this.description = description
    }
}
