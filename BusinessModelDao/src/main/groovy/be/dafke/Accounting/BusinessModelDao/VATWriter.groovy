package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Contacts
import be.dafke.Accounting.BusinessModel.VATField
import be.dafke.Accounting.BusinessModel.VATFields

import java.util.logging.Level
import java.util.logging.Logger

class VATWriter {
    enum Period {QUARTER,MONTH}

    static void writeDeclarant(Writer writer, Contact contact){
        try {
            writer.write(
                    "        <ns2:Declarant>\n" +
                            "            <VATNumber>"+contact.vatNumber+"</VATNumber>\n" +
                            "            <Name>"+contact.name+"</Name>\n" +
                            "            <Street>"+contact.streetAndNumber+"</Street>\n" +
                            "            <PostCode>"+contact.postalCode+"</PostCode>\n" +
                            "            <City>"+contact.city+"</City>\n" +
                            "            <CountryCode>"+contact.countryCode+"</CountryCode>\n" +
                            "            <EmailAddress>"+contact.email+"</EmailAddress>\n" +
                            "            <Phone>"+contact.phone+"</Phone>\n" +
                            "        </ns2:Declarant>\n"
            )
        } catch (IOException e) {
            e.printStackTrace()
        }
    }

    static void writeVATFields(VATFields vatFields, File xmlFile, String year, String nr, Contact contact, Period period){
        try {
            Writer writer = new FileWriter(xmlFile)
            writer.write(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                            "<ns2:VATConsignment xmlns=\"http://www.minfin.fgov.be/InputCommon\" xmlns:ns2=\"http://www.minfin.fgov.be/VATConsignment\" VATDeclarationsNbr=\"1\">\n" +
                            "    <ns2:VATDeclaration SequenceNumber=\"1\">\n"
            )
            writeDeclarant(writer,contact)
            writer.write(
                    "        <ns2:Period>\n"
            )
            if(period == Period.QUARTER) {
                writer.write(
                        "            <ns2:Quarter>" + nr + "</ns2:Quarter>\n"
                )
            } else if(period == Period.MONTH){
                writer.write(
                        "            <ns2:Month>"+nr+"</ns2:Month>\n"
                )
            }
            writer.write(
                    "            <ns2:Year>"+year+"</ns2:Year>\n" +
                            "        </ns2:Period>\n" +
                            "        <ns2:Data>\n")
            for(VATField vatField:getAllFields(vatFields)){
                BigDecimal amount = vatField.saldo
                if(amount!=null && amount.compareTo(BigDecimal.ZERO)!=0){
                    String name = vatField.name
                    writer.write(
                            "            <ns2:Amount GridNumber=\""+name+"\">"+amount+"</ns2:Amount>\n"
                    )
                }
            }
            writer.write(
                    "        </ns2:Data>\n" +
                            "        <ns2:ClientListingNihil>NO</ns2:ClientListingNihil>\n" +
                            "        <ns2:Ask Restitution=\"NO\" Payment=\"NO\"/>\n" +
                            "    </ns2:VATDeclaration>\n" +
                            "</ns2:VATConsignment>")
            writer.flush()
            writer.close()
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VATFields.class.name).log(Level.SEVERE, null, ex)
        } catch (IOException ex) {
            Logger.getLogger(VATFields.class.name).log(Level.SEVERE, null, ex)
        }
    }

    static ArrayList<VATField> getAllFields(VATFields fields){
        ArrayList<VATField> vatFields = fields.businessObjects
        VATField field71 = fields.get71()
        VATField field72 = fields.get72()
        if(field71.saldo.compareTo(BigDecimal.ZERO) > 0){
            vatFields.add(field71)
        }
        // normally only one if will be used as 71 = -72
        if(field72.saldo.compareTo(BigDecimal.ZERO) > 0){
            vatFields.add(field72)
        }
        vatFields
    }

    static void writeCustomerListing(File xmlFile, String year, Contact declarant, Contacts contacts){
        try {
            Writer writer = new FileWriter(xmlFile)
            BigDecimal totalTurnover = BigDecimal.ZERO
            BigDecimal totalVatTotal = BigDecimal.ZERO
            int nrOfCustomers=0
            StringBuffer buffer=new StringBuffer()
            for(Contact contact:contacts.businessObjects){
                BigDecimal turnOver = contact.turnOver
                String vatNumber = contact.vatNumber
                BigDecimal vatTotal = contact.VATTotal
                String countryCode = contact.countryCode
                if(contact.isCustomer() && vatNumber!=null && turnOver.compareTo(BigDecimal.ZERO)>0) {
                    totalTurnover = totalTurnover.add(contact.turnOver)
                    totalVatTotal = totalVatTotal.add(contact.VATTotal)
                    nrOfCustomers++
                    buffer.append(
                            "        <ns2:Client SequenceNumber=\"1\">\n" +
                                    "            <ns2:CompanyVATNumber issuedBy=\"" + countryCode + "\">" + vatNumber + "</ns2:CompanyVATNumber>\n" +
                                    "            <ns2:TurnOver>" + turnOver + "</ns2:TurnOver>\n" +
                                    "            <ns2:VATAmount>" + vatTotal + "</ns2:VATAmount>\n" +
                                    "        </ns2:Client>\n"
                    )
                }
            }
            totalTurnover.setScale(2)
            totalVatTotal.setScale(2)
            writer.write(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                            "<ns2:ClientListingConsignment xmlns=\"http://www.minfin.fgov.be/InputCommon\" xmlns:ns2=\"http://www.minfin.fgov.be/ClientListingConsignment\" ClientListingsNbr=\"1\">\n" +
                            "    <ns2:ClientListing SequenceNumber=\"1\" ClientsNbr=\""+nrOfCustomers+"\" TurnOverSum=\""+totalTurnover+"\" VATAmountSum=\""+totalVatTotal+"\">\n"

            )
            writeDeclarant(writer,declarant)
            writer.write(
                    "        <ns2:Period>"+year+"</ns2:Period>\n"
            )
            writer.write(buffer.toString())
            writer.write(
                    "    </ns2:ClientListing>\n" +
                            "</ns2:ClientListingConsignment>\n"
            )
            writer.flush()
            writer.close()
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VATFields.class.name).log(Level.SEVERE, null, ex)
        } catch (IOException ex) {
            Logger.getLogger(VATFields.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
