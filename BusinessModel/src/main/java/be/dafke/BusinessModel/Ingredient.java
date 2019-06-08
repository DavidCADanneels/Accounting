package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Ingredient extends BusinessObject {
    private Unit unit;
    private Set<Allergene> allergenes = new HashSet<>();

    public Ingredient(String name, Unit unit) {
        setName(name);
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public String getAllergenesString() {
        return allergenes.stream().map(BusinessObject::getName).collect(Collectors.joining(","));
    }

    public Set<Allergene> getAllergenes() {
        return allergenes;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void addAllergene(Allergene allergene){
        allergenes.add(allergene);
    }
}
