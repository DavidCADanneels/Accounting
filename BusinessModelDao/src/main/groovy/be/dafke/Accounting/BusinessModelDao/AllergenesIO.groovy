package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.Allergene
import be.dafke.Accounting.BusinessModel.Allergenes
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import org.w3c.dom.Element

import java.util.logging.Level
import java.util.logging.Logger

import static be.dafke.Accounting.BusinessModelDao.XMLConstants.*
import static be.dafke.Accounting.BusinessModelDao.XMLReader.*
import static be.dafke.Accounting.BusinessModelDao.XMLWriter.getXmlHeader 

class AllergenesIO {
    static void readAllergenes(Accounting accounting){
        Allergenes allergenes = accounting.allergenes
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.name+"/"+ALLERGENES + XML_EXTENSION)
        Element rootElement = getRootElement(xmlFile, ALLERGENES)
        for (Element element : getChildren(rootElement, ALLERGENE)) {

            String name = getValue(element, NAME)
            String id = getValue(element, ID)
            String description = getValue(element, DESCRIPTION)
            Allergene allergene = new Allergene(id, name, description)
            try {
                allergenes.addBusinessObject(allergene)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static void writeAllergenes(Accounting accounting) {
        Allergenes allergenes = accounting.allergenes
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.name + "/" + ALLERGENES + XML_EXTENSION)
        try {
            Writer writer = new FileWriter(file)
            writer.write(getXmlHeader(ALLERGENES, 2))
            for (Allergene allergene : allergenes.businessObjects) {
                writer.write(
                        "  <" + ALLERGENE + ">\n" +
                                "    <" + ID + ">" + allergene.name + "</" + ID + ">\n" +
                                "    <" + NAME + ">" + allergene.shortName + "</" + NAME + ">\n" +
                                "    <" + DESCRIPTION + ">" + allergene.description + "</" + DESCRIPTION + ">\n" +
                                "  </" + ALLERGENE + ">\n"
                )
            }
            writer.write("</" + ALLERGENES + ">\n")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.name).log(Level.SEVERE, null, ex)
        }
    }
}