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
    private File xmlFolder;
    private File htmlFolder;

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

    public void setHtmlFolder(File htmlFolder) {
        this.htmlFolder = htmlFolder;
    }

    public void setXmlFolder(File xmlFolder) {
        this.xmlFolder = xmlFolder;
    }

    public File getHtmlFolder() {
        return htmlFolder;
    }

    public File getXmlFolder() {
        return xmlFolder;
    }
}