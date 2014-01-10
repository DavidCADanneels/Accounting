package be.dafke.BasicAccounting.Dao;

import java.io.File;

import be.dafke.FOP.Utils;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModelDao.ObjectModelSAXParser;

/**
 * User: Dafke
 * Date: 20/01/13
 * Time: 10:22
 */
public class AccountingsSAXParser {
    public static void toHtml(BusinessCollection<BusinessObject> accountings, File xmlFolder, File xslFolder, File htmlFolder){
        File xmlFile = new File(xmlFolder, "Accountings.xml");
        htmlFolder.mkdirs();
        File htmlFile = new File(htmlFolder, "Accountings.html");

        Utils.xmlToHtml(xmlFile, new File(xslFolder, "Accountings.xsl"), htmlFile,null);

        File accountingsXmlFolder = new File(xmlFolder, "Accountings");
        File accountingsHtmlFolder = new File(htmlFolder, "Accountings");

        ObjectModelSAXParser.toHtml(accountings, xmlFolder, xslFolder, htmlFolder);

        for(BusinessObject object1:accountings.getBusinessObjects()){
            String accountingName = object1.getName();
            File accountingXmlFolder = new File(accountingsXmlFolder, accountingName);
            File accountingHtmlFolder = new File(accountingsHtmlFolder, accountingName);
            accountingHtmlFolder.mkdirs();
            xmlFile = new File(accountingsXmlFolder, accountingName+".xml");
            htmlFile = new File(accountingsHtmlFolder, accountingName+".html");

            Utils.xmlToHtml(xmlFile, new File(xslFolder, "Accounting.xsl"), htmlFile, null);

            if(object1 instanceof BusinessCollection){
                BusinessCollection accounting = (BusinessCollection) object1;
                for(Object subObject : accounting.getBusinessObjects()){
                    BusinessObject middenObject = (BusinessObject) subObject;
                    xmlFile = new File(accountingXmlFolder, middenObject.getName()+".xml");
                    htmlFile = new File(accountingHtmlFolder, middenObject.getName()+".html");

                    Utils.xmlToHtml(xmlFile, new File(xslFolder, middenObject.getBusinessObjectType() + ".xsl"), htmlFile, null);

                    File collectionXmlFolder = new File(accountingXmlFolder, middenObject.getName());
                    File collectionHtmlFolder = new File(accountingHtmlFolder, middenObject.getName());
                    collectionHtmlFolder.mkdirs();
                    if(subObject instanceof BusinessCollection){
                        BusinessCollection collection2 = (BusinessCollection) subObject;
                        for(Object object : collection2.getBusinessObjects()){
                            BusinessObject onderObject = (BusinessObject) object;
                            xmlFile = new File(collectionXmlFolder, onderObject.getName()+".xml");
                            if(xmlFile.exists()){
                                htmlFile = new File(collectionHtmlFolder, onderObject.getName()+".html");

                                Utils.xmlToHtml(xmlFile, new File(xslFolder, onderObject.getBusinessObjectType()+".xsl"),htmlFile, null);
                            }
                        }
                    }
                }
            }
        }
    }
}
