package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
import be.dafke.BusinessModel.VATField;
import be.dafke.BusinessModel.VATFields;

import java.io.*;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ddanneels on 14/01/2017.
 */
public class VATWriter {
    public enum Period {QUARTER,MONTH}

    private static void writeDeclarant(Writer writer, Contact contact){
        try {
            writer.write(
                    "        <ns2:Declarant>\n" +
                    "            <VATNumber>"+contact.getVatNumber()+"</VATNumber>\n" +
                    "            <Name>"+contact.getName()+"</Name>\n" +
                    "            <Street>"+contact.getAddressLine1()+"</Street>\n" +
                    "            <PostCode>"+contact.getPostalCode()+"</PostCode>\n" +
                    "            <City>"+contact.getCity()+"</City>\n" +
                    "            <CountryCode>"+contact.getCountryCode()+"</CountryCode>\n" +
                    "            <EmailAddress>"+contact.getEmail()+"</EmailAddress>\n" +
                    "            <Phone>"+contact.getPhone()+"</Phone>\n" +
                    "        </ns2:Declarant>\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeVATFields(VATFields vatFields, File xmlFolder, String year, String nr, Contact contact, Period period){
        File xmlFile = new File(xmlFolder, "VAT-"+year+"-"+nr+".xml");
        try {
            Writer writer = new FileWriter(xmlFile);
            writer.write(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<ns2:VATConsignment xmlns=\"http://www.minfin.fgov.be/InputCommon\" xmlns:ns2=\"http://www.minfin.fgov.be/VATConsignment\" VATDeclarationsNbr=\"1\">\n" +
                    "    <ns2:VATDeclaration SequenceNumber=\"1\">\n"
            );
            writeDeclarant(writer,contact);
            writer.write(
                    "        <ns2:Period>\n"
            );
            if(period == Period.QUARTER) {
                writer.write(
                    "            <ns2:Quarter>" + nr + "</ns2:Quarter>\n"
                );
            } else if(period == Period.MONTH){
                writer.write(
                    "            <ns2:Month>"+nr+"</ns2:Month>\n"
                );
            }
            writer.write(
                    "            <ns2:Year>"+year+"</ns2:Year>\n" +
                    "        </ns2:Period>\n" +
                    "        <ns2:Data>\n");
            for(VATField vatField:vatFields.getAllFields()){
                BigDecimal amount = vatField.getAmount();
                if(amount!=null && amount.compareTo(BigDecimal.ZERO)!=0){
                    String name = vatField.getName();
                    writer.write(
                    "            <ns2:Amount GridNumber=\""+name+"\">"+amount+"</ns2:Amount>\n"
                    );
                }
            }
            writer.write(
                    "        </ns2:Data>\n" +
                    "        <ns2:ClientListingNihil>NO</ns2:ClientListingNihil>\n" +
                    "        <ns2:Ask Restitution=\"NO\" Payment=\"NO\"/>\n" +
                    "    </ns2:VATDeclaration>\n" +
                    "</ns2:VATConsignment>");
            writer.flush();
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VATFields.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VATFields.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void writeCustomerListing(File xmlFolder, String year, Contact declarant, Contacts contacts){
        File xmlFile = new File(xmlFolder, "Customers-"+year+".xml");
        try {
            Writer writer = new FileWriter(xmlFile);
            BigDecimal totalTurnover = BigDecimal.ZERO;
            BigDecimal totalVatTotal = BigDecimal.ZERO;
            int nrOfCustomers=0;
            for(Contact contact:contacts.getBusinessObjects()){
                if(contact.isCustomer()){
                    totalTurnover = totalTurnover.add(contact.getTurnOver());
                    totalVatTotal = totalVatTotal.add(contact.getVATTotal());
                    nrOfCustomers++;
                }
            }
            totalTurnover.setScale(2);
            totalVatTotal.setScale(2);
            writer.write(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<ns2:ClientListingConsignment xmlns=\"http://www.minfin.fgov.be/InputCommon\" xmlns:ns2=\"http://www.minfin.fgov.be/ClientListingConsignment\" ClientListingsNbr=\"1\">\n" +
                    "    <ns2:ClientListing SequenceNumber=\"1\" ClientsNbr=\""+nrOfCustomers+"\" TurnOverSum=\""+totalTurnover+"\" VATAmountSum=\""+totalVatTotal+"\">\n"

            );
            writeDeclarant(writer,declarant);
            writer.write(
                    "        <ns2:Period>"+year+"</ns2:Period>\n"
            );
            for(Contact contact:contacts.getBusinessObjects()){
                if(contact.isCustomer()){
                    BigDecimal turnOver = contact.getTurnOver();
                    BigDecimal vatTotal = contact.getVATTotal();
                    String vatNumber = contact.getVatNumber();
                    String countryCode = vatNumber.substring(0,2);
                    vatNumber = vatNumber.substring(2);
                    writer.write(
                    "        <ns2:Client SequenceNumber=\"1\">\n" +
                    "            <ns2:CompanyVATNumber issuedBy=\""+countryCode+"\">"+vatNumber+"</ns2:CompanyVATNumber>\n" +
                    "            <ns2:TurnOver>"+turnOver+"</ns2:TurnOver>\n" +
                    "            <ns2:VATAmount>"+vatTotal+"</ns2:VATAmount>\n" +
                    "        </ns2:Client>\n"
                    );
                }
            }
            writer.write(
                    "    </ns2:ClientListing>\n" +
                    "</ns2:ClientListingConsignment>\n"
            );
            writer.flush();
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VATFields.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VATFields.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
