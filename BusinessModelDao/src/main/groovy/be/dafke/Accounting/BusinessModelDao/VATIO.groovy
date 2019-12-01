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
        VATFields vatFields = accounting.getVatFields()
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + VATFIELDS + XML_EXTENSION)
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
        VATTransactions vatTransactions = accounting.getVatTransactions()
        Accounts accounts = accounting.getAccounts()
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + VATTRANSACTIONS + XML_EXTENSION)
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
        VATFields vatFields = accounting.getVatFields()
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + VATFIELDS + XML_EXTENSION)
        try {
            Writer writer = new FileWriter(file)
            writer.write(getXmlHeader(VATFIELDS, 2))
            for (VATField vatField : vatFields.getBusinessObjects()) {
                writer.write(
                        "  <" + VATFIELD + ">\n" +
                                "    <" + NAME + ">" + vatField.getName() + "</" + NAME + ">\n" +
                                "    <" + AMOUNT + ">" + vatField.getSaldo() + "</" + AMOUNT + ">\n" +
                                "  </" + VATFIELD + ">\n"
                )
            }
            writer.write("</" + VATFIELDS + ">")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(VATFields.class.getName()).log(Level.SEVERE, null, ex)
        }
    }

    static void writeVATTransactions(Accounting accounting) {
        VATTransactions vatTransactions = accounting.getVatTransactions()
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + VATTRANSACTIONS + XML_EXTENSION)
        try {
            Writer writer = new FileWriter(file)
            writer.write(getXmlHeader(VATTRANSACTIONS, 2))
            writer.write(
                    "  <" + DEBIT_ACCOUNT + ">" + vatTransactions.getDebitAccount() + "</" + DEBIT_ACCOUNT + ">\n" +
                            "  <" + CREDIT_ACCOUNT + ">" + vatTransactions.getCreditAccount() + "</" + CREDIT_ACCOUNT + ">\n" +
                            "  <" + DEBIT_CN_ACCOUNT + ">" + vatTransactions.getDebitCNAccount() + "</" + DEBIT_CN_ACCOUNT + ">\n" +
                            "  <" + CREDIT_CN_ACCOUNT + ">" + vatTransactions.getCreditCNAccount() + "</" + CREDIT_CN_ACCOUNT + ">\n"
            )
            writer.write("</" + VATTRANSACTIONS + ">")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(VATTransactions.class.getName()).log(Level.SEVERE, null, ex)
        }
    }
}