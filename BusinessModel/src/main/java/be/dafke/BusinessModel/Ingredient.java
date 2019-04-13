package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

public class Ingredient extends BusinessObject {
    private Unit unit;

    public Ingredient(String name, Unit unit) {
        setName(name);
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
