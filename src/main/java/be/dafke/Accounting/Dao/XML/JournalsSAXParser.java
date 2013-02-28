package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Journals;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.XMLReader;

import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 5:02
 */
public class JournalsSAXParser {
    public static void readJournals(Accounting accounting, File accountingFile){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(accountingFile.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Node journalsNode = doc.getElementsByTagName("Journals").item(0);

            String xmlLocation = doc.getElementsByTagName("location").item(0).getChildNodes().item(0).getNodeValue();
            accounting.getJournals().setFolder(xmlLocation);

            journalsFromXML(accounting, (Element) journalsNode);

        } catch (IOException io) {
            io.printStackTrace();
            FileSystemView.getFileSystemView().createFileObject("Journals.xml");
            System.out.println(accountingFile.getAbsolutePath() + " has been created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void journalsFromXML(Accounting accounting, Element journalsElement) {
        NodeList journalsNode = journalsElement.getElementsByTagName("Journal");
        for (int i = 0; i < journalsNode.getLength(); i++) {
            Element element = (Element)journalsNode.item(i);
            String journal_name = element.getElementsByTagName("journal_name").item(0).getChildNodes().item(0).getNodeValue();
            String journal_short = element.getElementsByTagName("journal_short").item(0).getChildNodes().item(0).getNodeValue();
            String journal_type = element.getElementsByTagName("journal_type").item(0).getChildNodes().item(0).getNodeValue();
            System.out.println("Journal: "+journal_name+" | "+journal_short+" | "+journal_type);
            try{
                accounting.getJournals().addJournal(journal_name, journal_short, accounting.getJournalTypes().get(journal_type));
            } catch (DuplicateNameException e) {
                System.err.println("There is already an journal with the name \""+journal_name+"\" and/or abbreviation \""+journal_short+"\".");
            } catch (EmptyNameException e) {
                System.err.println("Journal name and abbreviation cannot be empty.");
            }
        }
        Journals journals = accounting.getJournals();
        File xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), journals.getFolder());
        File journalFiles[] = FileSystemView.getFileSystemView().getFiles(xmlFolder, false);
        for(File journalFile : journalFiles) {
            String journalName = journalFile.getName().replaceAll(".xml", "");
            Journal journal = accounting.getJournals().get(journalName);
            accounting.setCurrentJournal(journal);
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(false);
                SAXParser parser = factory.newSAXParser();
                XMLReader reader = parser.getXMLReader();
                reader.setContentHandler(new JournalContentHandler(accounting.getAccounts(), journal));
                reader.setErrorHandler(new FoutHandler());
                reader.parse(journalFile.getAbsolutePath());
            } catch (IOException io) {
//				FileSystemView.getFileSystemView().createFileObject(subFolder, "Accounting.xml");
//				System.out.println(journalFile + " has been created");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
