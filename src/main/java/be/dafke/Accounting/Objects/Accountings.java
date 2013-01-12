package be.dafke.Accounting.Objects;

import be.dafke.Accounting.XML.AccountingsContentHandler;
import be.dafke.Accounting.XML.FoutHandler;
import org.xml.sax.XMLReader;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Accountings {
	// private static Accountings accountings =null;
	private final static HashMap<String, Accounting> accountings;
	private static Accounting currentAccounting = null;
	static {
		accountings = new HashMap<String, Accounting>();
	}

//	public static Accountings getInstance(){
//		
//	}
//	public Accountings() {
//		accountings = new HashMap<String, Accounting>();
//	}

//	public HashMap<String, Accounting> getAccountings() {
//		return accountings;
//	}

	public static Accounting getCurrentAccounting() {
		return currentAccounting;
	}

	public static void addAccounting(Accounting accounting) {
		accountings.put(accounting.toString(), accounting);
	}

	public static boolean contains(String name) {
		return accountings.containsKey(name);
	}

	public static Collection<Accounting> getAccountings() {
		return accountings.values();
	}

	public static void setCurrentAccounting(Accounting accounting) {
		currentAccounting = accounting;
	}

	public static boolean isActive() {
		return currentAccounting != null;
	}

	public static void close() {
		if (currentAccounting != null) {
			currentAccounting.close();
		}
		toXML();
	}

	private static File getFile() {
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

	public static void fromXML() {
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
				reader.setContentHandler(new AccountingsContentHandler(file));
				reader.setErrorHandler(new FoutHandler());
				reader.parse(pad);
			} catch (IOException io) {
				// FileSystemView.getFileSystemView().createFileObject(home, "TESTFILE");
				// System.out.println(pad + " has been created");
			} catch (Exception e) {
				e.printStackTrace();
			}
//		for(Accounting accounting : /*Accountings.*/getAccountings()) {
//			String name = accounting.toString();
//			File subFolder = FileSystemView.getFileSystemView().getChild(folder, name);
//			if (!subFolder.isDirectory()) {
//				System.err.println(name + " not found or no directory");
//			} else {
//				File subFile = FileSystemView.getFileSystemView().getChild(subFolder, "object");
//				Accountings.openObject(subFile);
//				File subFile = FileSystemView.getFileSystemView().getChild(subFolder, "Accounting.xml");
//				if (!subFile.exists()) {
//					System.err.println("no XML file found in " + name);
//				} else {
//					try {
//						SAXParserFactory factory = SAXParserFactory.newInstance();
//						factory.setValidating(true);
//						SAXParser parser = factory.newSAXParser();
//						XMLReader reader = parser.getXMLReader();
//						reader.setContentHandler(new AccountingContentHandler());
//						reader.setErrorHandler(new FoutHandler());
//						reader.parse(subFile.getAbsolutePath());
//					} catch (IOException io) {
//						FileSystemView.getFileSystemView().createFileObject(subFolder, "Accounting.xml");
//						System.out.println(subFile.getAbsolutePath() + " has been created");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}

//				for(Accounting acc : Accountings.getAccountings()) {
//					File accFolder = FileSystemView.getFileSystemView().getChild(subFolder, acc.toString());
//					File objectFile = FileSystemView.getFileSystemView().getChild(accFolder, "object");
//
//					// TODO: read all separate files: Accounts, Journals, ...
//					// Accounts accounts = acc.getAccounts();
//					//
//				}
//			}
//		}
		}
	}

	private static void toXML() {
		try {
			Writer writer = new FileWriter(getFile());
			writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
					+ "<!DOCTYPE Accountings SYSTEM \"Accountings.dtd\">\r\n"
					+ "<?xml-stylesheet type=\"text/xsl\" href=\"Accountings.xsl\"?>\r\n" + "<Accountings>\r\n");
			for(Accounting acc : getAccountings()) {
				writer.write("  <Accounting name=\"" + acc.toString() + "\"/>\r\n");
			}
			writer.write("  <CurrentAccounting name=\"" + currentAccounting.toString() + "\"/>\r\n");
			writer.write("</Accountings>");
			writer.flush();
			writer.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public static void open(String name) {
		if (currentAccounting != null) {
			currentAccounting.close();
		}
		currentAccounting = accountings.get(name);
	}

	public static void openAccounting() {
		if (currentAccounting != null) {
			currentAccounting.close();
		}
		Object[] set = accountings.keySet().toArray();
		Object obj = JOptionPane.showInputDialog(null, "Chooser", "Select an accounting",
				JOptionPane.INFORMATION_MESSAGE, null, set, set[0]);
		String s = (String) obj;
		currentAccounting = accountings.get(s);
	}

	public static void newAccounting() {
		String name = JOptionPane.showInputDialog(null, "Enter a name");
		while (name == null || accountings.containsKey(name) || name.trim().isEmpty()) {
			name = JOptionPane.showInputDialog(null, "This name is empty or already exists. Enter another name");
		}
		currentAccounting = new Accounting(name);
		addAccounting(currentAccounting);
	}

	public static Accounting getAccounting(String name) {
		return accountings.get(name);
	}

	public static void openInstance() {
		JFileChooser kiezer = new JFileChooser();
		kiezer.setDialogTitle(java.util.ResourceBundle.getBundle("Accounting").getString(
				"SELECTEER_BOEKHOUDING-BESTAND"));
		if (kiezer.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File bestand = kiezer.getSelectedFile();
			if (bestand != null) {
				currentAccounting = openObject(bestand);
			}
		}
	}

	public static Accounting openObject(File bestand) {
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
}
