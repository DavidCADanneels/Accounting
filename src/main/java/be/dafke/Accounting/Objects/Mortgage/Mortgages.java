package be.dafke.Accounting.Objects.Mortgage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 11:06
 */
public class Mortgages {
    private final HashMap<String, Mortgage> mortgageTables;
    private File mortgageLocationXML;
    private File mortgageLocationHTML;

    public Mortgages(){
        mortgageTables = new HashMap<String, Mortgage>();
    }

    public void addMortgageTable(String mortgageName, Mortgage table) {
        mortgageTables.put(mortgageName, table);
    }

    public boolean containsMortgageName(String mortgageName) {
        return mortgageTables.containsKey(mortgageName);
    }

    public Mortgage getMortgage(String mortgageName) {
        return mortgageTables.get(mortgageName);
    }

    public ArrayList<Mortgage> getMortgagesTables() {
        return new ArrayList<Mortgage>(mortgageTables.values());
    }

    public void removeMortgageTable(Mortgage selectedMortgage) {
        mortgageTables.remove(selectedMortgage.toString());
    }

    public void setMortgageLocationHtml(File location) {
        mortgageLocationHTML = location;
        mortgageLocationHTML.mkdir();
    }

    public void setMortgageLocationXml(File location) {
        mortgageLocationXML = location;
        mortgageLocationXML.mkdir();
    }

    public File getMortgageLocationHtml() {
        return mortgageLocationHTML;
    }

    public File getMortgageLocationXml() {
        return mortgageLocationXML;
    }
}