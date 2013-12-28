package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Accountings extends BusinessCollection<Accounting> {

    private final File xmlFolder;
    private List<AccountingExtension> extensions;

    public void addExtension(AccountingExtension extension){
        extensions.add(extension);
    }

    @Override
    public String getChildType(){
        return "Accounting";
    }

    public Accountings(File xmlFolder){
        this.xmlFolder = xmlFolder;
        super.setName("");
        extensions = new ArrayList<AccountingExtension>();
    }

    public File getXmlFolder(){
        return xmlFolder;
    }

    @Override
    public Accounting createNewChild(String name) {
        Accounting accounting = new Accounting(name);
        for(AccountingExtension extension : extensions){
            extension.extendConstructor(accounting);
        }
        return accounting;
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

    public List<AccountingExtension> getExtensions() {
        return extensions;
    }
}