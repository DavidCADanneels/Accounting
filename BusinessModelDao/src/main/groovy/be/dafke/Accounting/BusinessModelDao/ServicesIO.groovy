package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import org.w3c.dom.Element

import java.util.logging.Level
import java.util.logging.Logger

import static be.dafke.Accounting.BusinessModelDao.XMLConstants.*
import static be.dafke.Accounting.BusinessModelDao.XMLReader.*
import static be.dafke.Accounting.BusinessModelDao.XMLWriter.getXmlHeader
import static be.dafke.Utils.Utils.parseBigDecimal

class ServicesIO {
    static void readServices(Accounting accounting){
        Services services = accounting.services
        Articles articles = accounting.articles
        Contacts contacts = accounting.contacts
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$SERVICES$XML_EXTENSION")
        Element rootElement = getRootElement(xmlFile, SERVICES)
        for (Element element : getChildren(rootElement, SERVICE)) {

            String name = getValue(element, NAME)
            Service service = new Service(name)

            String unitPriceString = getValue(element, UNIT_PRICE)
            if(unitPriceString){
                service.unitPrice = parseBigDecimal(unitPriceString)
            }

            String supplierName = getValue(element, SUPPLIER)
            if(supplierName) {
                Contact supplier = contacts.getBusinessObject(supplierName)
                if (supplier != null) {
                    service.setSupplier(supplier)
                }
            }
            try {
                services.addBusinessObject(service)
                articles.addBusinessObject(service)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static void writeServices(Accounting accounting) {
        Services services = accounting.services
        File file = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$SERVICES$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(file)
            writer.write getXmlHeader(SERVICES, 2)
            for (Service service : services.businessObjects) {
                writer.write """\
  <$SERVICE>
    <$NAME>$service.name</$NAME>
    <$UNIT_PRICE>$service.unitPrice</$UNIT_PRICE>
    <$SUPPLIER>$service.supplier</$SUPPLIER>
  </$SERVICE>
"""
            }
            writer.write """\
</$SERVICES>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
