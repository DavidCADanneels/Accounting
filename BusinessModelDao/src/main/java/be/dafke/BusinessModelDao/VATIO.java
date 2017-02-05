package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
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

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;
import static be.dafke.Utils.Utils.parseBigDecimal;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class VATIO {

    public static void readVATFields(VATFields vatFields, File accountingFolder) {
        File xmlFile = new File(accountingFolder, "VATFields.xml");
        Element rootElement = getRootElement(xmlFile, VATFIELDS);
        for (Element element : getChildren(rootElement, VATFIELD)) {

            String name = getValue(element, NAME);
            VATField vatField = new VATField(name);

//            String amountString = getValue(element, AMOUNT);
//            BigDecimal amount = parseBigDecimal(amountString);
//            vatField.setAmount(amount);

            try {
                vatFields.addBusinessObject(vatField);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readVATTransactions(VATTransactions vatTransactions, VATFields vatFields, Accounts accounts, File accountingFolder){
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

        for (Element element: getChildren(rootElement, VATTRANSACTION)) {
            VATTransaction vatTransaction = new VATTransaction();
            for (Element vatBookingsElement : getChildren(element, VATBOOKING)) {
                String vatFieldString = getValue(vatBookingsElement, VATFIELD);
                String amountString = getValue(vatBookingsElement, AMOUNT);

                BigDecimal amount = parseBigDecimal(amountString);
                VATField vatField = vatFields.getBusinessObject(vatFieldString);
                if(vatField==null)System.err.println("Field["+vatFieldString+"] not found");
                VATMovement vatMovement = new VATMovement(amount, true);
                VATBooking vatBooking = new VATBooking(vatField, vatMovement);
                
                vatTransaction.addBusinessObject(vatBooking);
            }
            vatTransactions.addBusinessObject(vatTransaction);
        }
    }

    public static void writeVATFields(VATFields vatFields, File accountingFolder){
        File file = new File(accountingFolder, VATFIELDS+XML);
        try{
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(VATFIELDS, 2));
            for(VATField vatField: vatFields.getBusinessObjects()) {
                writer.write(
                            "  <"+VATFIELD+">\n" +
                            "    <"+NAME+">"+vatField.getName()+"</"+NAME+">\n" +
                            "    <"+AMOUNT+">"+vatField.getSaldo()+"</"+AMOUNT+">\n" +
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
        File file = new File(accountingFolder, VATTRANSACTIONS+XML);
        try{
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(VATTRANSACTIONS, 2));
            writer.write(
                    "  <"+DEBIT_ACCOUNT+">"+vatTransactions.getDebitAccount()+"</"+DEBIT_ACCOUNT+">\n" +
                    "  <"+CREDIT_ACCOUNT+">"+vatTransactions.getCreditAccount()+"</"+CREDIT_ACCOUNT+">\n" +
                    "  <"+DEBIT_CN_ACCOUNT+">"+vatTransactions.getDebitCNAccount()+"</"+DEBIT_CN_ACCOUNT+">\n" +
                    "  <"+CREDIT_CN_ACCOUNT+">"+vatTransactions.getCreditCNAccount()+"</"+CREDIT_CN_ACCOUNT+">\n"
            );
            for(VATTransaction vatTransaction: vatTransactions.getBusinessObjects()) {
                writer.write(
                    "  <"+VATTRANSACTION+">\n" +
                    "    <"+ID+">"+vatTransaction.getId()+"</"+ID+">\n"
                );
                for(VATBooking vatBooking:vatTransaction.getBusinessObjects()){
                    VATMovement vatMovement = vatBooking.getVatMovement();
                    VATField vatField = vatBooking.getVatField();
                    writer.write(
                    "    <"+VATBOOKING+">\n" +
                    "      <"+VATFIELD+">"+vatField.getName()+"</"+VATFIELD+">\n" +
                    "      <"+AMOUNT+">"+vatMovement.getAmount()+"</"+AMOUNT+">\n" +
//                    "      <"+INCREASE+">"+vatMovement.isIncrease()+"</"+INCREASE+">\n" +
                    "    </"+VATBOOKING+">\n"
                    );
                }
                writer.write(
                        "  </"+VATTRANSACTION+">\n"
                );
            }
            writer.write("</"+VATTRANSACTIONS+">");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(VATTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}