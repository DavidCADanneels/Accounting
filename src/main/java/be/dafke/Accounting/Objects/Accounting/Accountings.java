package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.WriteableBusinessCollection;

import java.io.File;
import java.util.TreeMap;

public class Accountings extends WriteableBusinessCollection<Accounting> {

    public Accountings(){
        File xmlFolder = new File(System.getProperty("Accountings_xml"));
        setXmlFile(new File(xmlFolder, "Accountings.xml"));
        setHtmlFile(new File(xmlFolder, "Accountings.html"));
        readCollection();
    }

    @Override
    public Accounting createNewChild(String name) {
        return new Accounting();
    }

    public void createDefaultValuesIfNull(){
        for(Accounting accounting:getBusinessObjects()){
            accounting.createXmlFolders();
            accounting.createHtmlFolders();
        }
    }

	public void setCurrentObject(String name) {
		currentObject = dataTables.get(NAME).get(name);
	}

    @Override
    public Accounting addBusinessObject(Accounting value) throws EmptyNameException, DuplicateNameException {
        TreeMap<String, Accounting> map = dataTables.get(NAME);
        if(value.getName()==null || value.getName().trim().equals("")){
            throw new EmptyNameException();
        } else if(map.containsKey(value.getName().trim())){
            throw new DuplicateNameException();
        }
        map.put(value.getName().trim(), value);
        return value;
    }

    @Override
    public void readCollection() {
        readCollection("Accounting");
    }
}