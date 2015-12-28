package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.io.File;
import java.util.TreeMap;

public class Accountings extends BusinessCollection<Accounting> {

    public static final String ACCOUNTINGS = "Accountings";
    public static final String ACCOUNTING = "Accounting";
    private final File xmlFolder;
    private final File xslFolder;
    private final File htmlFolder;

    @Override
    public String getChildType(){
        return ACCOUNTING;
    }

    public Accountings(File xmlFolder, File xslFolder, File htmlFolder){
        this.xmlFolder = xmlFolder;
        this.xslFolder = xslFolder;
        this.htmlFolder = htmlFolder;
        setName(ACCOUNTINGS);
    }

    public File getXmlFolder(){
        return xmlFolder;
    }

    public File getHtmlFolder(){
        return htmlFolder;
    }

    public File getXslFolder() {
        return xslFolder;
    }

    @Override
    public Accounting createNewChild() {
        return new Accounting();
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