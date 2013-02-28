package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Coda.CounterParties;
import be.dafke.Accounting.Objects.Coda.CounterParty;
import be.dafke.Accounting.Objects.Coda.Movement;
import be.dafke.Accounting.Objects.Coda.Movements;
import be.dafke.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 5:08
 */
public class MovementsSAXParser {
    public static void readMovements(Movements movements, CounterParties counterParties, File bankingFile){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(bankingFile.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Node movementsNode = doc.getElementsByTagName("Movements").item(0);
//            String xslLocation = movementsNode.getAttributes().getNamedItem("xsl").getNodeValue();
//            accounting.setLocationXSL(new File(xslLocation));

            String xmlLocation = doc.getElementsByTagName("location").item(0).getChildNodes().item(0).getNodeValue();
            movements.setFolder(xmlLocation);

            movementsFromXML(movements, counterParties, (Element) movementsNode);

        } catch (IOException io) {
            io.printStackTrace();
            FileSystemView.getFileSystemView().createFileObject("Banking.xml");
            System.out.println(bankingFile.getAbsolutePath() + " has been created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
}
