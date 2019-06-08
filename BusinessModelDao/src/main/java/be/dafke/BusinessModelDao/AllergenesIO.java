package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;

public class AllergenesIO {
    public static void readAllergenes(Accounting accounting){
        Allergenes allergenes = accounting.getAllergenes();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+ALLERGENES + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, ALLERGENES);
        for (Element element : getChildren(rootElement, ALLERGENE)) {

            String name = getValue(element, NAME);
            String id = getValue(element, ID);
            String description = getValue(element, DESCRIPTION);
            Allergene allergene = new Allergene(id, name, description);
            try {
                allergenes.addBusinessObject(allergene);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeAllergenes(Accounting accounting) {
        Allergenes allergenes = accounting.getAllergenes();
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + ALLERGENES + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(ALLERGENES, 2));
        for (Allergene allergene : allergenes.getBusinessObjects()) {
                writer.write(
                        "  <" + ALLERGENE + ">\n" +
                            "    <" + ID + ">" + allergene.getName() + "</" + ID + ">\n" +
                            "    <" + NAME + ">" + allergene.getShortName() + "</" + NAME + ">\n" +
                            "    <" + DESCRIPTION + ">" + allergene.getDescription() + "</" + DESCRIPTION + ">\n" +
                            "  </" + ALLERGENE + ">\n"
                );
            }
            writer.write("</" + ALLERGENES + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}