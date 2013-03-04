package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.CounterParties;
import be.dafke.Accounting.Objects.Accounting.CounterParty;
import be.dafke.Accounting.Objects.Accounting.Movement;
import be.dafke.Accounting.Objects.Accounting.Movements;
import be.dafke.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 5:08
 */
public class MovementsSAXParser {
    // READ
    //
    public static void readMovements(Movements movements, CounterParties counterParties){
        try {
            File file = movements.getXmlFile();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Node movementsNode = doc.getElementsByTagName("Movements").item(0);

            String xmlFile = doc.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
            String htmlFile = doc.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
            movements.setXmlFile(new File(xmlFile));
            movements.setHtmlFile(new File(htmlFile));

            movementsFromXML(movements, counterParties, (Element) movementsNode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    private static void movementsFromXML(Movements movements, CounterParties counterParties, Element movementsElement){
        NodeList movementsNode = movementsElement.getElementsByTagName("Movement");
        for (int i = 0; i < movementsNode.getLength(); i++) {
            Element element = (Element)movementsNode.item(i);
            String statementNr = element.getElementsByTagName("Statement").item(0).getChildNodes().item(0).getNodeValue();
            String sequenceNr = element.getElementsByTagName("Sequence").item(0).getChildNodes().item(0).getNodeValue();
            String dateString = element.getElementsByTagName("Date").item(0).getChildNodes().item(0).getNodeValue();
            String debitString = element.getElementsByTagName("Sign").item(0).getChildNodes().item(0).getNodeValue();
            String amountString = element.getElementsByTagName("Amount").item(0).getChildNodes().item(0).getNodeValue();
            String counterpartyName = element.getElementsByTagName("CounterParty").item(0).getChildNodes().item(0).getNodeValue();
            String transactionCode = element.getElementsByTagName("TransactionCode").item(0).getChildNodes().item(0).getNodeValue();
            String communication = "";
            // communication can be an empty tag "<Communication></Communication>"
            NodeList communcationNodeList = element.getElementsByTagName("Communication").item(0).getChildNodes();
            if(communcationNodeList.getLength()>0){
                communication = communcationNodeList.item(0).getNodeValue();
            }
            BigDecimal amount = new BigDecimal(amountString);
            Calendar date = Utils.toCalendar(dateString);
            boolean debit = ("D".equals(debitString));
            CounterParty counterParty = counterParties.getCounterPartyByName(counterpartyName);
            Movement movement = new Movement(statementNr, sequenceNr, date, debit, amount, counterParty, transactionCode, communication);
            movements.add(movement);
        }
    }

    // WRITE
    //
    public static void writeMovements(Movements movements) {
        try {
            Writer writer = new FileWriter(movements.getXmlFile());

            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n");
            writer.write("<!DOCTYPE Movements SYSTEM \"" + movements.getDtdFile().getCanonicalPath() + "\">\r\n");
            writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + movements.getXsl2XmlFile().getCanonicalPath() + "\"?>\r\n");

            writer.write("<Movements>\r\n");
            writer.write("  <xml>" + movements.getXmlFile() + "</xml>\r\n");
            writer.write("  <html>" + movements.getHtmlFile() + "</html>\r\n");
            for(Movement movement : movements.getAllMovements()) {
                writer.write("  <Movement>\r\n");
                writer.write("    <Statement>"+movement.getStatementNr()+"</Statement>\r\n");
                writer.write("    <Sequence>"+movement.getSequenceNr()+"</Sequence>\r\n");
                writer.write("    <Date>"+Utils.toString(movement.getDate())+"</Date>\r\n");
                writer.write("    <Sign>"+(movement.isDebit()?"D":"C")+"</Sign>\r\n");
                writer.write("    <Amount>"+movement.getAmount()+"</Amount>\r\n");
                writer.write("    <CounterParty>"+movement.getCounterParty()+"</CounterParty>\r\n");
                writer.write("    <TransactionCode>" + movement.getTransactionCode() + "</TransactionCode>\r\n");
                writer.write("    <Communication>"+movement.getCommunication()+"</Communication>\r\n");
                writer.write("  </Movement>\r\n");
            }
            writer.write("</Movements>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
