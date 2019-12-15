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

class VATIO {

    static void readVATFields(Accounting accounting) {
        VATFields vatFields = accounting.vatFields
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$VATFIELDS$XML_EXTENSION")
        if (xmlFile.exists()) {
            Element rootElement = getRootElement(xmlFile, VATFIELDS)
            for (Element element : getChildren(rootElement, VATFIELD)) {

                String name = getValue(element, NAME)
                VATField vatField = new VATField(name)

                try {
                    vatFields.addBusinessObject(vatField)
                } catch (EmptyNameException | DuplicateNameException e) {
                    e.printStackTrace()
                }
            }
        }
    }

    static void readVATTransactions(Accounting accounting) {
        VATTransactions vatTransactions = accounting.vatTransactions
        Accounts accounts = accounting.accounts
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$VATTRANSACTIONS$XML_EXTENSION")
        if (xmlFile.exists()) {
            Element rootElement = getRootElement(xmlFile, VATTRANSACTIONS)
            String debitAccountString = getValue(rootElement, DEBIT_ACCOUNT)
            String creditAccountString = getValue(rootElement, CREDIT_ACCOUNT)
            String debitCNAccountString = getValue(rootElement, DEBIT_CN_ACCOUNT)
            String creditCNAccountString = getValue(rootElement, CREDIT_CN_ACCOUNT)

            if (debitAccountString != null) {
                vatTransactions.setDebitAccount(accounts.getBusinessObject(debitAccountString))
            }
            if (creditAccountString != null) {
                vatTransactions.setCreditAccount(accounts.getBusinessObject(creditAccountString))
            }
            if (debitCNAccountString != null) {
                vatTransactions.setDebitCNAccount(accounts.getBusinessObject(debitCNAccountString))
            }
            if (creditCNAccountString != null) {
                vatTransactions.setCreditCNAccount(accounts.getBusinessObject(creditCNAccountString))
            }
        }
    }

    static void writeVATFields(Accounting accounting) {
        VATFields vatFields = accounting.vatFields
        File file = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$VATFIELDS$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(file)
            writer.write getXmlHeader(VATFIELDS, 2)
            for (VATField vatField : vatFields.businessObjects) {
                writer.write """\
  <$VATFIELD>
    <$NAME>$vatField.name</$NAME>
    <$AMOUNT>$vatField.saldo</$AMOUNT>
  </$VATFIELD>
"""
            }
            writer.write """\
</$VATFIELDS>"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(VATFields.class.name).log(Level.SEVERE, null, ex)
        }
    }

    static void writeVATTransactions(Accounting accounting) {
        VATTransactions vatTransactions = accounting.vatTransactions
        File file = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$VATTRANSACTIONS$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(file)
            writer.write getXmlHeader(VATTRANSACTIONS, 2)
            writer.write """\
  <$DEBIT_ACCOUNT>$vatTransactions.debitAccount</$DEBIT_ACCOUNT>
  <$CREDIT_ACCOUNT>$vatTransactions.creditAccount</$CREDIT_ACCOUNT>
  <$DEBIT_CN_ACCOUNT>$vatTransactions.debitCNAccount</$DEBIT_CN_ACCOUNT>
  <$CREDIT_CN_ACCOUNT>$vatTransactions.creditCNAccount</$CREDIT_CN_ACCOUNT>"""
            writer.write """
</$VATTRANSACTIONS>"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(VATTransactions.class.name).log(Level.SEVERE, null, ex)
        }
    }
}