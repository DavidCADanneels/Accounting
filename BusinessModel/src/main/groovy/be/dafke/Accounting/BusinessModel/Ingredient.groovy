package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Accounting.ObjectModel.Exceptions.NotEmptyException

class Ingredient extends BusinessObject {
    private Unit unit
    private Allergenes allergenes = new Allergenes()

    Ingredient(String name, Unit unit) {
        setName(name)
        this.unit = unit
    }

    Unit getUnit() {
        unit
    }

    String getAllergenesString() {
        allergenes.getBusinessObjects().stream().map(BusinessObject.&getName).collect(Collectors.joining(","))
    }

    Allergenes getAllergenes() {
        allergenes
    }

    void setUnit(Unit unit) {
        this.unit = unit
    }

    void overwriteAllAllergenes(Set<Allergene> allergenes) {
        this.allergenes = new Allergenes()
        for (Allergene allergene:allergenes) {
            addAllergene(allergene)
        }
    }

    void addAllergene(Allergene allergene){
        try {
            allergenes.addBusinessObject(allergene)
        } catch (EmptyNameException | DuplicateNameException e) {
            e.printStackTrace()
        }
    }

    void removeAllergene(Allergene allergene){
        try {
            allergenes.removeBusinessObject(allergene)
        } catch (NotEmptyException e) {
            e.printStackTrace()
        }
    }
}
