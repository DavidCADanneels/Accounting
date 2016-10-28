package be.dafke.ObjectModelDao;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ddanneels on 28/12/2015.
 */
public class XMLWriter {
    public static String getXmlHeader(BusinessObject object, int depth) {
        String className = object.getBusinessObjectType();
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n");

//        builder.append("<!DOCTYPE ").append(className).append(" SYSTEM \"");
//        for(int i=0 ; i<depth; i++){
//            builder.append("../");
//        }
//        builder.append("../xsd/").append(className).append(".xsd\">\r\n");

        builder.append("<"+className+" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n" +
                "xsi:noNamespaceSchemaLocation=\"");
        for(int i=0 ; i<depth; i++){
            builder.append("../");
        }
        builder.append("../xsd/").append(className).append(".xsd\">\r\n");
        return builder.toString();
    }

    public static void writeCollection(BusinessObject businessObject, File parentFolder, int depth){
        String businessObjectName = businessObject.getName();
        String businessObjectType = businessObject.getBusinessObjectType();
        parentFolder.mkdirs();
        File childFolder = null;
        try{
            childFolder = new File(parentFolder, businessObjectName);
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
        File xmlFile = new File(parentFolder, businessObjectName+".xml");
        try {
            Writer writer = new FileWriter(xmlFile);

            // Write the header with correct XSL-File, DTD-File and DTD-Root-Element
            writer.write(getXmlHeader(businessObject, depth));

            // get the object's properties
            Properties collectionProperties = businessObject.getOutputProperties();

            writer.write(writeNode(collectionProperties, 0));

            if(businessObject instanceof BusinessCollection){
                BusinessCollection businessCollection = (BusinessCollection)businessObject;
                writeChildren(writer, businessCollection, 0);

                if(businessCollection.getCurrentObject()!=null){
                    writer.write("  <CurrentObject>" + businessCollection.getCurrentObject().getName() + "</CurrentObject>\r\n");
                }
            }

            writer.write("</"+businessObjectType+">\r\n");

            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (IOException ex) {
            Logger.getLogger(BusinessCollection.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(businessObject instanceof BusinessCollection){
            BusinessCollection businessCollection = (BusinessCollection)businessObject;
            for(Object object : businessCollection.getBusinessObjects()) {
                if(object instanceof BusinessObject){
                    BusinessObject childObject = (BusinessObject) object;
//                    String type = childObject.getBusinessObjectType();
                    if(childObject.getName()!=null){
                        writeCollection(childObject, childFolder, depth + 1);
                    }
                }
            }
        }
    }

    private static String writeNode(Properties objectProperties, int depth){
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<Object, Object> entry : objectProperties.entrySet()){
            Object key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof ArrayList){
//                System.err.println(value);
                ArrayList list = (ArrayList)value;
                for (Object object : list) {
                    for(int i=0;i<depth;i++) builder.append("  ");
                    builder.append("  <" + list.get(0).getClass().getSimpleName() + ">\r\n");
                    BusinessObject businessObject = (BusinessObject) object;
                    builder.append(writeNode(businessObject.getOutputProperties(), depth + 1));
                    for(int i=0;i<depth;i++) builder.append("  ");
                    builder.append("  </" + list.get(0).getClass().getSimpleName() + ">\r\n");
                }
            } else{
                if(value!=null && !"".equals(value)){
                    for(int i=0;i<depth;i++) builder.append("  ");
                    builder.append("  <" + key + ">" + value + "</"+ key + ">\r\n");
                }
            }
        }
        return builder.toString();
    }

    private static void writeChildren(Writer writer, BusinessCollection collection, int depth) {
        try{
            // Iterate children and write their data
            for(Object object : collection.getBusinessObjects()) {
                BusinessObject businessObject = (BusinessObject) object;
                String objectName = businessObject.getName();
                // TODO: remove this to get more details in the parent file
                String objectType = businessObject.getBusinessObjectType();

                // Write the tag for the child e.g. <Accounting>
                for(int i=0;i<depth;i++) writer.write("  ");
                writer.write("  <"+objectType+">\r\n");

                // get the object's properties
                Properties objectProperties = businessObject.getOutputProperties();

                writer.write(writeNode(objectProperties, depth+1));

                // write the closing tag e.g. </Accounting>
                for(int i=0;i<depth;i++) writer.write("  ");
                writer.write("  </" + objectType + ">\r\n");
            }
        }catch (IOException io){
            io.printStackTrace();
        }
    }

}
