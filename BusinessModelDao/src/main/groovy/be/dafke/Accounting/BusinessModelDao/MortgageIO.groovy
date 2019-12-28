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
import static be.dafke.Utils.Utils.parseInt 

class MortgageIO {

    static void readMortgages(Accounting accounting) {
        Mortgages mortgages = accounting.mortgages
        Accounts accounts = accounting.accounts
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$MORTGAGES$XML_EXTENSION")
        File mortgagesFolder = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$MORTGAGES")
        Element rootElement = getRootElement(xmlFile, MORTGAGES)
        for (Element element : getChildren(rootElement, MORTGAGE)) {

            Mortgage mortgage = new Mortgage()
            mortgage.setName(getValue(element, NAME))
            String startCapitalString = getValue(element, TOTAL)
            String nrPayedString = getValue(element, NRPAYED)
            if(startCapitalString){
                mortgage.setStartCapital(parseBigDecimal(startCapitalString))
            }
            if(nrPayedString){
                mortgage.setAlreadyPayed(parseInt(nrPayedString))
            }
            String capitalAccount = getValue(element, CAPITAL_ACCOUNT)
            if(capitalAccount){
                mortgage.setCapitalAccount(accounts.getBusinessObject(capitalAccount))
            }
            String intrestAccount = getValue(element, INTREST_ACCOUNT)
            if(intrestAccount){
                mortgage.setIntrestAccount(accounts.getBusinessObject(intrestAccount))
            }

            try {
                mortgages.addBusinessObject(mortgage)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }

        for(Mortgage mortgage:mortgages.businessObjects){
            readMortgage(mortgage, mortgagesFolder)
        }
    }

    static void readMortgage(Mortgage mortgage, File mortgagesFolder) {
        String name = mortgage.name
        File xmlFile = new File(mortgagesFolder, "$name$XML_EXTENSION")
        Element rootElement = getRootElement(xmlFile, MORTGAGE)
        for (Element element : getChildren(rootElement, MORTGAGE_TRANSACTION)) {
            MortgageTransaction mortgageTransaction = new MortgageTransaction()
            mortgageTransaction.setMortgage(mortgage)
            mortgageTransaction.setNr(parseInt(getValue(element, NR)))
            mortgageTransaction.setMensuality(parseBigDecimal(getValue(element, MENSUALITY)))
            mortgageTransaction.setCapital(parseBigDecimal(getValue(element, CAPITAL)))
            mortgageTransaction.setIntrest(parseBigDecimal(getValue(element, INTREST)))
            mortgageTransaction.setRestCapital(parseBigDecimal(getValue(element, RESTCAPITAL)))

            mortgage.addBusinessObject(mortgageTransaction)
        }
    }


    static void writeMortgages(Accounting accounting){
        Mortgages mortgages = accounting.mortgages
        File mortgagesFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$MORTGAGES$XML_EXTENSION")
        File mortgagesFolder = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$MORTGAGES")
        try{
            Writer writer = new FileWriter(mortgagesFile)
            writer.write getXmlHeader(MORTGAGES, 2)
            // TODO: decide whether to store it here or in Mortgage.xml
            for(Mortgage mortgage: mortgages.businessObjects) writer.write """\
<$MORTGAGE>
  <$NAME>$mortgage.name</$NAME>
  <$CAPITAL_ACCOUNT>$mortgage.capitalAccount</$CAPITAL_ACCOUNT>
  <$INTREST_ACCOUNT>$mortgage.intrestAccount</$INTREST_ACCOUNT>
  <$NRPAYED>$mortgage.nrPayed</$NRPAYED>
  <$TOTAL>$mortgage.startCapital</$TOTAL>
</$MORTGAGE>
"""
            writer.write("""\
</$MORTGAGES>""")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Mortgages.class.name).log(Level.SEVERE, null, ex)
        }
        for(Mortgage mortgage:mortgages.businessObjects){
            writeMortgage(mortgage, mortgagesFolder)
        }
    }

    static void writeMortgage(Mortgage mortgage, File mortgagesFolder){
        mortgagesFolder.mkdirs()
        File mortgagesFile = new File(mortgagesFolder, "$mortgage.name$XML_EXTENSION")
        try{
            Writer writer = new FileWriter(mortgagesFile)
            writer.write getXmlHeader(MORTGAGE, 3)
            // TODO: decide whether to store it here or in Mortgages.xml
            writer.write """\
  <$NAME>$mortgage.name</$NAME>
  <$CAPITAL_ACCOUNT>$mortgage.capitalAccount</$CAPITAL_ACCOUNT>
  <$INTREST_ACCOUNT>$mortgage.intrestAccount</$INTREST_ACCOUNT>
  <$NRPAYED>$mortgage.nrPayed</$NRPAYED>
  <$TOTAL>$mortgage.startCapital</$TOTAL>
"""

            for(MortgageTransaction mortgageTransaction:mortgage.businessObjects) writer.write """\
  <$MORTGAGE_TRANSACTION>
    <$RESTCAPITAL>$mortgageTransaction.restCapital</$RESTCAPITAL>
    <$MENSUALITY>$mortgageTransaction.mensuality</$MENSUALITY>
    <$NR>$mortgageTransaction.nr</$NR>
    <$INTREST>$mortgageTransaction.intrest</$INTREST>
    <$CAPITAL>$mortgageTransaction.capital</$CAPITAL>
  </$MORTGAGE_TRANSACTION>
"""
            writer.write """\
</$MORTGAGE>"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Mortgage.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
