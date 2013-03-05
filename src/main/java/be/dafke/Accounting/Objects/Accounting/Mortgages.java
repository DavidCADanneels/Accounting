package be.dafke.Accounting.Objects.Accounting;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 11:06
 */
public class Mortgages extends BusinessCollection<Mortgage>{

    private final HashMap<String, Mortgage> mortgages;

    public Mortgages(){
        mortgages = new HashMap<String, Mortgage>();
    }

    public void addMortgageTable(String mortgageName, Mortgage table) {
        mortgages.put(mortgageName, table);
    }

    public boolean containsMortgageName(String mortgageName) {
        return mortgages.containsKey(mortgageName);
    }

    @Override
    public Mortgage getBusinessObject(String mortgageName) {
        return mortgages.get(mortgageName);
    }

    @Override
    public ArrayList<Mortgage> getBusinessObjects() {
        return new ArrayList<Mortgage>(mortgages.values());
    }

    public void removeMortgageTable(Mortgage selectedMortgage) {
        mortgages.remove(selectedMortgage.toString());
    }
}