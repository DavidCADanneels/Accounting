package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.VATField;
import be.dafke.BusinessModel.VATFields;
import be.dafke.BusinessModel.VATTransactions;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.NAME;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;
import static be.dafke.Utils.Utils.parseBigDecimal;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class VATIO {
    public static final String VATFIELD = "VATField";
    public static final String VATFIELDS = "VATFields";
    public static final String VATTRANSACTIONS = "VATTransactions";
    public static final String VATTRANSACTION = "VATTransaction";
    public static final String DEBIT_ACCOUNT = "DebitAccount";
    public static final String CREDIT_ACCOUNT = "CreditAccount";
    public static final String DEBIT_CN_ACCOUNT = "DebitCNAccount";
    public static final String CREDIT_CN_ACCOUNT = "CreditCNAccount";
    public static final String AMOUNT = "amount";

    public static void readVATFields(VATFields vatFields, File accountingFolder) {
        File xmlFile = new File(accountingFolder, "VATFields.xml");
        Element rootElement = getRootElement(xmlFile, VATFIELDS);
        for (Element element : getChildren(rootElement, VATFIELD)) {

            String name = getValue(element, NAME);
            VATField vatField = new VATField(name);

            String amountString = getValue(element, AMOUNT);
            BigDecimal amount = parseBigDecimal(amountString);
            vatField.setAmount(amount);

            try {
                vatFields.addBusinessObject(vatField);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readVATTransaction(VATTransactions vatTransactions, Accounts accounts, File accountingFolder){
        File xmlFile = new File(accountingFolder, "VATTransactions.xml");
        Element rootElement = getRootElement(xmlFile, VATTRANSACTIONS);
        String debitAccountString = getValue(rootElement, DEBIT_ACCOUNT);
        String creditAccountString = getValue(rootElement, CREDIT_ACCOUNT);
        String debitCNAccountString = getValue(rootElement, DEBIT_CN_ACCOUNT);
        String creditCNAccountString = getValue(rootElement, CREDIT_CN_ACCOUNT);

        if(debitAccountString!=null) {
            vatTransactions.setDebitAccount(accounts.getBusinessObject(debitAccountString));
        }
        if(creditAccountString!=null) {
            vatTransactions.setCreditAccount(accounts.getBusinessObject(creditAccountString));
        }
        if(debitCNAccountString!=null) {
            vatTransactions.setDebitCNAccount(accounts.getBusinessObject(debitCNAccountString));
        }
        if(creditCNAccountString!=null) {
            vatTransactions.setCreditCNAccount(accounts.getBusinessObject(creditCNAccountString));
        }
    }

    public static void writeVATFields(VATFields vatFields, File accountingFolder){
        File file = new File(accountingFolder, VATFIELDS+".xml");
        try{
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(VATFIELDS, 2));
            for(VATField vatField: vatFields.getBusinessObjects()) {
                writer.write(
                            "  <"+VATFIELD+">\n" +
                            "    <"+NAME+">" + vatField.getName() + "</"+NAME+">\n" +
                            "    <"+AMOUNT+">" + vatField.getAmount() + "</"+AMOUNT+">\n" +
                            "  </"+VATFIELD+">\n"
                );
            }
            writer.write(   "</"+VATFIELDS+">");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(VATFields.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void writeVATTransactions(VATTransactions vatTransactions, File accountingFolder){
        File file = new File(accountingFolder, VATTRANSACTIONS+".xml");
        try{
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(VATTRANSACTIONS, 2));
            writer.write(
                    "  <"+DEBIT_ACCOUNT+">"+vatTransactions.getDebitAccount()+"</"+DEBIT_ACCOUNT+">\n" +
                    "  <"+CREDIT_ACCOUNT+">"+vatTransactions.getCreditAccount()+"</"+CREDIT_ACCOUNT+">\n" +
                    "  <"+DEBIT_CN_ACCOUNT+">"+vatTransactions.getDebitCNAccount()+"</"+DEBIT_CN_ACCOUNT+">\n" +
                    "  <"+CREDIT_CN_ACCOUNT+">"+vatTransactions.getCreditCNAccount()+"</"+CREDIT_CN_ACCOUNT+">\n"
            );
//            for(VATTransaction vatTransaction: vatTransactions.getBusinessObjects()) {
//                writer.write(
//                        "  <"+VATTRANSACTION+">" +
//                        "    <"+NAME+">" + vatTransaction.getName() + "</"+NAME+">\n"
//                        "  </"+VATTRANSACTION+">\n"
//                );
//            }
            writer.write("</"+VATTRANSACTIONS+">");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(VATTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
