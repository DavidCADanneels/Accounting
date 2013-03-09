package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.CounterParties;
import be.dafke.Accounting.Objects.Accounting.CounterParty;
import be.dafke.Accounting.Objects.Accounting.Statement;
import be.dafke.Accounting.Objects.Accounting.Statements;
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
public class StatementsSAXParser {
    // READ
    //
    public static void readStatements(Statements statements, CounterParties counterParties){
        try {
            File file = statements.getXmlFile();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Element rootElement = (Element) doc.getElementsByTagName("Statements").item(0);
            NodeList nodeList = rootElement.getElementsByTagName("Statement");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element)nodeList.item(i);
                String statementNr = Utils.getValue(element, "ID1");
                String sequenceNr = Utils.getValue(element, "ID2");
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
                Statement statement = new Statement();
                statement.setName(statementNr+"-"+sequenceNr);
                statement.setStatementNr(statementNr);
                statement.setSequenceNumber(sequenceNr);
                statement.setDate(date);
                statement.setDebit(debit);
                statement.setAmount(amount);
                if(counterpartyName!=null){
                    CounterParty counterParty = counterParties.getBusinessObject(counterpartyName);
                    statement.setCounterParty(counterParty);
                }
                statement.setTransactionCode(transactionCode);
                statement.setCommunication(communication);
                try {
                    statements.addBusinessObject(statement);
                } catch (EmptyNameException e) {
                    System.err.println("Statement name is empty.");
                } catch (DuplicateNameException e) {
                    System.err.println("Statement name already exist.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // WRITE
    //
    public static void writeStatements(Statements statements) {
        try {
            Writer writer = new FileWriter(statements.getXmlFile());

            writer.write(statements.getXmlHeader());

            writer.write("<Statements>\r\n");
            for(Statement statement : statements.getBusinessObjects()) {
                writer.write("  <Statement>\r\n");
                writer.write("    <ID1>"+ statement.getStatementNr()+"</ID1>\r\n");
                writer.write("    <ID2>"+ statement.getSequenceNr()+"</ID2>\r\n");
                writer.write("    <Date>"+Utils.toString(statement.getDate())+"</Date>\r\n");
                writer.write("    <Sign>"+(statement.isDebit()?"D":"C")+"</Sign>\r\n");
                writer.write("    <Amount>"+ statement.getAmount()+"</Amount>\r\n");
                if(statement.getCounterParty()!=null && !statement.getCounterParty().toString().equals("")){
                    writer.write("    <CounterParty>"+ statement.getCounterParty()+"</CounterParty>\r\n");
                }
                writer.write("    <TransactionCode>" + statement.getTransactionCode() + "</TransactionCode>\r\n");
                if(statement.getCommunication()!=null && !statement.getCommunication().equals("")){
                    writer.write("    <Communication>" + statement.getCommunication() + "</Communication>\r\n");
                }
                writer.write("  </Statement>\r\n");
            }
            writer.write("</Statements>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
