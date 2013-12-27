package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.io.File;
import java.util.TreeMap;

public class Accountings extends BusinessCollection<Accounting> {

    private final File xmlFolder;
    private final File xslFolder;
    private final File dtdFolder;
    private final File htmlFile;
    private final File xmlFile;

    @Override
    public String getChildType(){
        return "Accounting";
    }

    public Accountings(File xmlFolder, File xslFolder, File dtdFolder){
//        File xmlFolder = new File(System.getProperty("Accountings_xml"));
        this.xmlFolder = xmlFolder;
        this.xmlFile = new File(xmlFolder, "Accountings.xml");
        this.htmlFile = new File(xmlFolder, "Accountings.html");
        this.xslFolder = xslFolder;
        this.dtdFolder = dtdFolder;
        super.setName("");
//        readCollection();
    }

    public File getXmlFolder(){
        return xmlFolder;
    }

    @Override
    public Accounting createNewChild(String name) {
        return new Accounting(name);
    }

    /*public void createDefaultValuesIfNull(){
        for(Accounting accounting:getBusinessObjects()){
            accounting.createXmlFolders();
            accounting.createHtmlFolders();
        }
    }*/

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

//    @Override
//    public void readCollection() {
//        readCollection("Accounting", true);
//    }
}