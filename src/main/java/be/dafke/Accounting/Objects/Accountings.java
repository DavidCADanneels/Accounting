package be.dafke.Accounting.Objects;

import be.dafke.Accounting.XML.AccountingContentHandler;
import be.dafke.Accounting.XML.AccountingsContentHandler;
import be.dafke.Accounting.XML.FoutHandler;
import be.dafke.Accounting.XML.JournalContentHandler;
import be.dafke.Accounting.XML.MortgageContentHandler;
import be.dafke.Mortgage.Mortgage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.xml.sax.XMLReader;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Accountings implements ApplicationEventPublisherAware {
	private final HashMap<String, Accounting> accountings = new HashMap<String, Accounting>();
	private Accounting currentAccounting = null;
	private ApplicationEventPublisher applicationEventPublisher;

	public Accounting getCurrentAccounting() {
		return currentAccounting;
	}

	public void addAccounting(Accounting accounting) {
		accountings.put(accounting.toString(), accounting);
	}

//	public boolean contains(String name) {
//		return accountings.containsKey(name);
//	}

	public Collection<Accounting> getAccountings() {
		return accountings.values();
	}

	public void setCurrentAccounting(Accounting accounting) {
		currentAccounting = accounting;
	}

	public boolean isActive() {
		return currentAccounting != null;
	}

	public void close() {
		for(Accounting accounting : accountings.values()) {
			accounting.close();
		}
//		if (currentAccounting != null) {
//			currentAccounting.close();
//		}
		toXML();
	}

	private File getFile() {
		String folderName = "Accounting";
		String fileName = "Accountings.xml";
		File home = // FileSystemView.getFileSystemView().getDefaultDirectory();
		new File(System.getProperty("user.home"));
		File folder = new File(home, folderName);
		if (folder.exists() && !folder.isDirectory()) {
			File renamed = FileSystemView.getFileSystemView().createFileObject(home, folderName + "_file");
			folder.renameTo(renamed);
			folder = new File(home, folderName);
		}
		if (!folder.isDirectory()) {
			folder.mkdir();
		}
		File file = FileSystemView.getFileSystemView().getChild(folder, fileName);
		if (!file.exists()) {
			file = FileSystemView.getFileSystemView().createFileObject(folder, fileName);
		}
		return file;
	}

	public void fromXML() {
		System.out.println("fromXML");
		File file = getFile();
		String pad = file.getAbsolutePath();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException io) {
			}
		} else {
			try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				factory.setValidating(true);
				SAXParser parser = factory.newSAXParser();
				XMLReader reader = parser.getXMLReader();
				reader.setContentHandler(new AccountingsContentHandler(file, this));
				reader.setErrorHandler(new FoutHandler());
				reader.parse(pad);
			} catch (IOException io) {
				// FileSystemView.getFileSystemView().createFileObject(home, "TESTFILE");
				// System.out.println(pad + " has been created");
			} catch (Exception e) {
				e.printStackTrace();
			}
//			Accounting tmpCurrent = currentAccounting;
			for(Accounting accounting : accountings.values()) {
//				currentAccounting = accounting;
				String name = accounting.toString();
				File subFolder = FileSystemView.getFileSystemView().getChild(file.getParentFile(), name);
				if (!subFolder.isDirectory()) {
					System.err.println(name + " not found or no directory");
				} else {
					// Accountings.openObject(subFile);
					File subFile = FileSystemView.getFileSystemView().getChild(subFolder, "Accounting.xml");
					if (!subFile.exists()) {
						System.err.println("no XML file found in " + name);
						return;
					} else {
						try {
							SAXParserFactory factory = SAXParserFactory.newInstance();
							factory.setValidating(true);
							SAXParser parser = factory.newSAXParser();
							XMLReader reader = parser.getXMLReader();
							reader.setContentHandler(new AccountingContentHandler(accounting));
							reader.setErrorHandler(new FoutHandler());
							reader.parse(subFile.getAbsolutePath());
						} catch (IOException io) {
							io.printStackTrace();
							FileSystemView.getFileSystemView().createFileObject(subFolder, "Accounting.xml");
							System.out.println(subFile.getAbsolutePath() + " has been created");
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					handleJournals(accounting);
					handleMortgages(accounting);
				}
			}
//			currentAccounting = tmpCurrent;
		}
	}

	private void handleJournals(Accounting accounting) {
		File journalFiles[] = FileSystemView.getFileSystemView().getFiles(accounting.getJournalLocationXml(), false);
		for(File journalFile : journalFiles) {
			String journalName = journalFile.getName().replaceAll(".xml", "");
			Journal journal = accounting.getJournals().get(journalName);
			accounting.setCurrentJournal(journal);
			try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				factory.setValidating(false);
				SAXParser parser = factory.newSAXParser();
				XMLReader reader = parser.getXMLReader();
				reader.setContentHandler(new JournalContentHandler(accounting, journal));
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

	private void handleMortgages(Accounting accounting) {
		File mortgagesFiles[] = FileSystemView.getFileSystemView().getFiles(accounting.getMortgageLocationXml(), false);
		for(File mortgagesFile : mortgagesFiles) {
			String mortgageName = mortgagesFile.getName().replaceAll(".xml", "");
			Mortgage mortgage = accounting.getMortgage(mortgageName);
			try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				factory.setValidating(false);
				SAXParser parser = factory.newSAXParser();
				XMLReader reader = parser.getXMLReader();
				reader.setContentHandler(new MortgageContentHandler(mortgage));
				reader.setErrorHandler(new FoutHandler());
				reader.parse(mortgagesFile.getAbsolutePath());
			} catch (IOException io) {
//				FileSystemView.getFileSystemView().createFileObject(subFolder, "Accounting.xml");
//				System.out.println(journalFile + " has been created");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void toXML() {
		if (currentAccounting != null) {
			try {
				Writer writer = new FileWriter(getFile());
				writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
						+ "<!DOCTYPE Accountings SYSTEM \"Accountings.dtd\">\r\n"
						+ "<?xml-stylesheet type=\"text/xsl\" href=\"Accountings.xsl\"?>\r\n" + "<Accountings>\r\n");
				for(Accounting acc : getAccountings()) {
					writer.write("  <Accounting name=\"" + acc.toString() + "\" xml=\"" + acc.getLocationXml()
							+ "\" html=\"" + acc.getLocationHtml() + "\"/>\r\n");
				}
				writer.write("  <CurrentAccounting name=\"" + currentAccounting.toString() + "\" xml=\""
						+ currentAccounting.getLocationXml() + "\" html=\"" + currentAccounting.getLocationHtml()
						+ "\"/>\r\n");
				writer.write("</Accountings>");
				writer.flush();
				writer.close();
			} catch (FileNotFoundException ex) {
				Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void open(String name) {
		if (currentAccounting != null) {
			currentAccounting.close();
		}
		currentAccounting = accountings.get(name);
	}

	public void openAccounting() {
		if (currentAccounting != null) {
			currentAccounting.close();
		}
		Object[] set = accountings.keySet().toArray();
		Object obj = JOptionPane.showInputDialog(null, "Chooser", "Select an accounting",
				JOptionPane.INFORMATION_MESSAGE, null, set, set[0]);
		String s = (String) obj;
		currentAccounting = accountings.get(s);
	}

	public Accounting newAccounting() {
		String name = JOptionPane.showInputDialog(null, "Enter a name");
		while (name == null || accountings.containsKey(name) || name.trim().isEmpty()) {
			name = JOptionPane.showInputDialog(null, "This name is empty or already exists. Enter another name");
		}
		currentAccounting = new Accounting(name);
		addAccounting(currentAccounting);
		return currentAccounting;
	}

	public Accounting getAccounting(String name) {
		return accountings.get(name);
	}

	public Accounting openInstance() {
		JFileChooser kiezer = new JFileChooser();
		kiezer.setDialogTitle(java.util.ResourceBundle.getBundle("Accounting").getString(
				"SELECTEER_BOEKHOUDING-BESTAND"));
		if (kiezer.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File bestand = kiezer.getSelectedFile();
			if (bestand != null) {
				currentAccounting = openObject(bestand);
			}
		}
		return currentAccounting;
	}

	public Accounting openObject(File bestand) {
		try {
			String location = bestand.getPath();
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(location));
			return (Accounting) in.readObject();
//					ObjectInputStream in_c = new ObjectInputStream(new FileInputStream(location + "_CounterParties"));
//					CounterParties.setInstance((CounterParties) in_c.readObject());
//					ObjectInputStream in_m = new ObjectInputStream(new FileInputStream(location + "_Movements"));
//					Movements.setAllMovements((ArrayList<Movement>) in_m.readObject());
		} catch (Exception e) {
			return null;
		}
	}

	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		System.out.println("set");
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public ApplicationEventPublisher getApplicationEventPublisher() {
		System.out.println("get");
		return applicationEventPublisher;
	}
}
