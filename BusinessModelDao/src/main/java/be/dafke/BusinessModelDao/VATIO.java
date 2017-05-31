package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;
import static be.dafke.Utils.Utils.parseBigDecimal;
import static be.dafke.Utils.Utils.parseInt;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class VATIO {

    public static void readVATFields(VATFields vatFields, File accountingFolder) {
        File xmlFile = new File(accountingFolder, "VATFields.xml");
        if(xmlFile.exists()) {
            Element rootElement = getRootElement(xmlFile, VATFIELDS);
            for (Element element : getChildren(rootElement, VATFIELD)) {

                String name = getValue(element, NAME);
                VATField vatField = new VATField(name);

                try {
                    vatFields.addBusinessObject(vatField);
                } catch (EmptyNameException | DuplicateNameException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void readVATTransactions(VATTransactions vatTransactions, VATFields vatFields, Accounts accounts, File accountingFolder){
        File xmlFile = new File(accountingFolder, "VATTransactions.xml");
        if(xmlFile.exists()) {
            Element rootElement = getRootElement(xmlFile, VATTRANSACTIONS);
            String debitAccountString = getValue(rootElement, DEBIT_ACCOUNT);
            String creditAccountString = getValue(rootElement, CREDIT_ACCOUNT);
            String debitCNAccountString = getValue(rootElement, DEBIT_CN_ACCOUNT);
            String creditCNAccountString = getValue(rootElement, CREDIT_CN_ACCOUNT);

            if (debitAccountString != null) {
                vatTransactions.setDebitAccount(accounts.getBusinessObject(debitAccountString));
            }
            if (creditAccountString != null) {
                vatTransactions.setCreditAccount(accounts.getBusinessObject(creditAccountString));
            }
            if (debitCNAccountString != null) {
                vatTransactions.setDebitCNAccount(accounts.getBusinessObject(debitCNAccountString));
            }
            if (creditCNAccountString != null) {
                vatTransactions.setCreditCNAccount(accounts.getBusinessObject(creditCNAccountString));
            }

            for (Element element : getChildren(rootElement, VATTRANSACTION)) {
                String idString = getValue(element, ID);
                int id = parseInt(idString);
                String dateString = getValue(element, DATE);
                Calendar date = Utils.toCalendar(dateString);
                VATTransaction vatTransaction = new VATTransaction(id, date);
                String registeredString = getValue(element, REGISTERED);
                if("true".equals(registeredString)){
                    vatTransaction.markAsRegistered();
                }

                for (Element vatBookingsElement : getChildren(element, VATBOOKING)) {
                    String vatFieldString = getValue(vatBookingsElement, VATFIELD);
                    String amountString = getValue(vatBookingsElement, AMOUNT);

                    BigDecimal amount = parseBigDecimal(amountString);
                    VATField vatField = vatFields.getBusinessObject(vatFieldString);
                    if (vatField == null) System.err.println("Field[" + vatFieldString + "] not found");
                    VATMovement vatMovement = new VATMovement(amount);
                    VATBooking vatBooking = new VATBooking(vatField, vatMovement);

                    vatTransaction.addBusinessObject(vatBooking);
                }
                vatTransactions.addBusinessObject(vatTransaction);
            }

            String countString = getValue(rootElement, COUNT);
            int count = parseInt(countString);
            VATTransaction.setCount(count);
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
                    "    <"+ID+">"+vatTransaction.getId()+"</"+ID+">\n" +
                    "    <"+DATE+">"+Utils.toString(vatTransaction.getDate())+"</"+DATE+">\n" +
                    "    <"+REGISTERED+">"+vatTransaction.isRegistered()+"</"+REGISTERED+">\n"
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
            writer.write("  <"+COUNT+">"+VATTransaction.getCount()+"</"+ COUNT +">\n");
            writer.write("</"+VATTRANSACTIONS+">");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(VATTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
