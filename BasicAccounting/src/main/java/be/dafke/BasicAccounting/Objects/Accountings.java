package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.io.File;
import java.util.TreeMap;

public class Accountings extends BusinessCollection<Accounting> {

    private final File xmlFolder;
    private final File htmlFolder;

    @Override
    public String getChildType(){
        return "Accounting";
    }

    public Accountings(File xmlFolder, File htmlFolder){
        this.xmlFolder = xmlFolder;
        this.htmlFolder = htmlFolder;
        super.setName("");
    }

    public File getXmlFolder(){
        return xmlFolder;
    }

    public File getHtmlFolder(){
        return htmlFolder;
    }

    @Override
    public Accounting createNewChild(String name) {
        return new Accounting(name);
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
}