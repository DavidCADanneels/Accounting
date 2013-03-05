package be.dafke.Accounting.Objects.Accounting;

import java.io.File;
import java.util.ArrayList;

/**
 * User: Dafke
 * Date: 4/03/13
 * Time: 16:23
 */
public abstract class BusinessCollection<T extends BusinessObject> extends BusinessObject{

    private File htmlFolder;
    private File xmlFolder;

    public abstract T getBusinessObject(String name);

    public abstract ArrayList<T> getBusinessObjects();

    public T get(String name){
        return getBusinessObject(name);
    }

    public ArrayList<T> values(){
        return getBusinessObjects();
    }

    public T get(int index){
        return getBusinessObjects().get(index);
    }

    public int size(){
        return getBusinessObjects().size();
    }

//    public abstract T addBusinessObject(String name);



    public void setHtmlFolder(File parentFolder){
        setHtmlFile(new File(parentFolder, getType() + ".html"));
        htmlFolder = new File(parentFolder, getType());
        for(BusinessObject businessObject: getBusinessObjects()){
            businessObject.setHtmlFile(new File(htmlFolder, businessObject.getName() + ".html"));
        }
    }

    protected void setXmlFolder(File parentFolder) {
        setXmlFile(new File(parentFolder, getType() + ".xml"));
        xmlFolder = new File(parentFolder, getType());
        for(BusinessObject businessObject: getBusinessObjects()){
            businessObject.setXmlFile(new File(xmlFolder, businessObject.getName() + ".xml"));
        }
    }

    protected void createXmlFolder(){
        if(xmlFolder.mkdirs()){
            System.out.println(xmlFolder + " has been created");
        }
    }

    protected void createHtmlFolder(){
        if(htmlFolder.mkdirs()){
            System.out.println(htmlFolder + " has been created");
        }
    }
}
