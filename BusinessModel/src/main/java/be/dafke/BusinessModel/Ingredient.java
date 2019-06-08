package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import java.util.Set;
import java.util.stream.Collectors;

public class Ingredient extends BusinessObject {
    private Unit unit;
    private Allergenes allergenes = new Allergenes();

    public Ingredient(String name, Unit unit) {
        setName(name);
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public String getAllergenesString() {
        return allergenes.getBusinessObjects().stream().map(BusinessObject::getName).collect(Collectors.joining(","));
    }

    public Allergenes getAllergenes() {
        return allergenes;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void overwriteAllAllergenes(Set<Allergene> allergenes) {
        this.allergenes = new Allergenes();
        for (Allergene allergene:allergenes) {
            addAllergene(allergene);
        }
    }

    public void addAllergene(Allergene allergene){
        try {
            allergenes.addBusinessObject(allergene);
        } catch (EmptyNameException | DuplicateNameException e) {
            e.printStackTrace();
        }
    }

    public void removeAllergene(Allergene allergene){
        try {
            allergenes.removeBusinessObject(allergene);
        } catch (NotEmptyException e) {
            e.printStackTrace();
        }
    }
}
