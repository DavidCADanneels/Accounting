package be.dafke.BusinessModelDao;

import org.apache.fop.cli.InputHandler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Vector;

/**
 * User: david
 * Date: 27-12-13
 * Time: 20:55
 */
public class XMLtoHTMLWriter {

//    public static void toHtml(BusinessCollection businessCollection, File xmlFolder, File xslFolder, File htmlFolder){
//        String businessCollectionName = businessCollection.getName();
//        String businessCollectionType = businessCollection.getBusinessObjectType();
//        File xmlFile = new File(xmlFolder, businessCollectionName + ".xml");
//        File htmlFile = new File(htmlFolder, businessCollectionName + ".html");
//
//        xmlToHtml(xmlFile, new File(xslFolder, businessCollectionType + ".xsl"), htmlFile, null);
//
//        File xmlCollectionFolder = new File(xmlFolder, businessCollectionName);
//        File htmlCollectionFolder = new File(htmlFolder, businessCollectionName);
//        htmlCollectionFolder.mkdirs();
//
//        for(Object object:businessCollection.getBusinessObjects()){
//            BusinessObject businessObject = (BusinessObject)object;
//            String businessObjectName = businessObject.getName();
//
//            String businessObjectType = businessObject.getBusinessObjectType();
//
//            File objectXmlFile = new File(xmlCollectionFolder, businessObjectName+".xml");
//            File objectHtmlFile = new File(htmlCollectionFolder, businessObjectName+".html");
//
//            xmlToHtml(objectXmlFile, new File(xslFolder, businessObjectType+".xsl"), objectHtmlFile, null);
//
////            if(object instanceof BusinessCollection && !(businessObject.writeChildren())){
////                BusinessCollection subCollection = (BusinessCollection)object;
////                String subCollectionName = subCollection.getName();
////                if(subCollectionName!=null){
////                    toHtml(subCollection, xmlCollectionFolder, xslFolder, htmlCollectionFolder);
////                }
////            }
//        }
//    }

    public static void xmlToHtml(File xmlFile, File xslFile, File htmlFile, Vector params) {
        InputHandler inputHandler = new InputHandler(xmlFile, xslFile, params);
        try {
            if (!htmlFile.exists()) {
//                htmlFile.getParentFile().mkdirs();
                if(htmlFile.createNewFile()){
                    System.out.println(htmlFile + " has been created");
                }
            }
            OutputStream out = new BufferedOutputStream(new FileOutputStream(htmlFile));
            inputHandler.transformTo(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
