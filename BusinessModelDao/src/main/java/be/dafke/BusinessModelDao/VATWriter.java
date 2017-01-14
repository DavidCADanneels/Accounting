package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.Contact;
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

    public static void writeVATFields(VATFields vatFields, File xmlFolder, String year, String nr, Contact contact, Period period){
        File xmlFile = new File(xmlFolder, year+"-"+nr+".xml");
        try {
            Writer writer = new FileWriter(xmlFile);
            writer.write(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<ns2:VATConsignment xmlns=\"http://www.minfin.fgov.be/InputCommon\" xmlns:ns2=\"http://www.minfin.fgov.be/VATConsignment\" VATDeclarationsNbr=\"1\">\n" +
                    "    <ns2:VATDeclaration SequenceNumber=\"1\">\n" +
                    "        <ns2:Declarant>\n" +
                    "            <VATNumber>"+contact.getVatNumber()+"</VATNumber>\n" +
                    "            <Name>"+contact.getName()+"</Name>\n" +
                    "            <Street>"+contact.getAddressLine1()+"</Street>\n" +
                    "            <PostCode>"+contact.getPostalCode()+"</PostCode>\n" +
                    "            <City>"+contact.getCity()+"</City>\n" +
                    "            <CountryCode>"+contact.getCountryCode()+"</CountryCode>\n" +
                    "            <EmailAddress>"+contact.getEmail()+"</EmailAddress>\n" +
                    "            <Phone>"+contact.getPhone()+"</Phone>\n" +
                    "        </ns2:Declarant>\n" +
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
}
