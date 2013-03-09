package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.CounterParties;
import be.dafke.Accounting.Objects.Accounting.CounterParty;
import be.dafke.Accounting.Objects.Accounting.Movement;
import be.dafke.Accounting.Objects.Accounting.Movements;
import be.dafke.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

            Element rootElement = (Element) doc.getElementsByTagName("Movements").item(0);
            NodeList nodeList = rootElement.getElementsByTagName("Movement");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element)nodeList.item(i);
                String statementNr = Utils.getValue(element, "Statement");
                String sequenceNr = Utils.getValue(element, "Sequence");
                String dateString = Utils.getValue(element, "Date");
                String debitString = Utils.getValue(element, "Sign");
                String amountString = Utils.getValue(element, "Amount");
                String counterpartyName = Utils.getValue(element, "CounterParty");
                String transactionCode = Utils.getValue(element, "TransactionCode");
                String communication = "";
                // communication can be an empty tag "<Communication></Communication>"
                NodeList communcationNodeList = element.getElementsByTagName("Communication");
                if(communcationNodeList.getLength()>0){
                    communication = communcationNodeList.item(0).getChildNodes().item(0).getNodeValue();
                }
                BigDecimal amount = new BigDecimal(amountString);
                Calendar date = Utils.toCalendar(dateString);
                boolean debit = ("D".equals(debitString));
                Movement movement = new Movement();
                movement.setName(statementNr+"-"+sequenceNr);
                movement.setStatementNr(statementNr);
                movement.setSequenceNumber(sequenceNr);
                movement.setDate(date);
                movement.setDebit(debit);
                movement.setAmount(amount);
                if(counterpartyName!=null){
                    CounterParty counterParty = counterParties.getBusinessObject(counterpartyName);
                    movement.setCounterParty(counterParty);
                }
                movement.setTransactionCode(transactionCode);
                movement.setCommunication(communication);
                try {
                    movements.addBusinessObject(movement);
                } catch (EmptyNameException e) {
                    System.err.println("Movement name is empty.");
                } catch (DuplicateNameException e) {
                    System.err.println("Movement name already exist.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // WRITE
    //
    public static void writeMovements(Movements movements) {
        try {
            Writer writer = new FileWriter(movements.getXmlFile());

            writer.write(movements.getXmlHeader());

            writer.write("<Movements>\r\n");
            for(Movement movement : movements.getBusinessObjects()) {
                writer.write("  <Movement>\r\n");
                writer.write("    <Statement>"+movement.getStatementNr()+"</Statement>\r\n");
                writer.write("    <Sequence>"+movement.getSequenceNr()+"</Sequence>\r\n");
                writer.write("    <Date>"+Utils.toString(movement.getDate())+"</Date>\r\n");
                writer.write("    <Sign>"+(movement.isDebit()?"D":"C")+"</Sign>\r\n");
                writer.write("    <Amount>"+movement.getAmount()+"</Amount>\r\n");
                if(movement.getCounterParty()!=null && !movement.getCounterParty().toString().equals("")){
                    writer.write("    <CounterParty>"+movement.getCounterParty()+"</CounterParty>\r\n");
                }
                writer.write("    <TransactionCode>" + movement.getTransactionCode() + "</TransactionCode>\r\n");
                if(movement.getCommunication()!=null && !movement.getCommunication().equals("")){
                    writer.write("    <Communication>" + movement.getCommunication() + "</Communication>\r\n");
                }
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
