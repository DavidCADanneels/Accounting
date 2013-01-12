package be.dafke.Accounting.Objects;

import be.dafke.Mortgage.Mortgage;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author David Danneels
 */
public class Accounting implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private boolean savedXML;
//	private boolean savedHTML;
	private final Accounts rekeningen;
	private final Journals journals;
	private Journal currentJournal;
	private final Projects projects;
	private final JournalTypes journalTypes;
	private final HashMap<String, Mortgage> mortgageTables;
	private final String name;
	private File locationXML, accountLocationXML, journalLocationXML, balanceLocationXML;
	private File mortgageLocationXML, movementLocationXML, counterpartyLocationXML;
	private File locationHTML, accountLocationHTML, journalLocationHTML, balanceLocationHTML;
	private File mortgageLocationHTML, movementLocationHTML, counterpartyLocationHTML;
	private File locationXSL;

	public Accounting(String name) {
		this.name = name;
//		savedXML = true;
//		savedHTML = false;// TODO: true ?
		rekeningen = new Accounts();
		journals = new Journals();
		projects = new Projects();
		journalTypes = new JournalTypes();
		mortgageTables = new HashMap<String, Mortgage>();
		createXMLFolders();
//		createHTMLFolders();
	}

	private void createHTMLFolders() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			locationHTML = chooser.getSelectedFile();
		} else {
			File parent = FileSystemView.getFileSystemView().getHomeDirectory();
			locationHTML = createSubFolderIfNotExist(parent, name);
		}
		accountLocationHTML = createSubFolderIfNotExist(locationHTML, "Accounts");
		journalLocationHTML = createSubFolderIfNotExist(locationHTML, "Journals");
		balanceLocationHTML = createSubFolderIfNotExist(locationHTML, "Balances");
		mortgageLocationHTML = createSubFolderIfNotExist(locationHTML, "Mortgages");
		mortgageLocationHTML = createSubFolderIfNotExist(locationHTML, "Movements");
		counterpartyLocationHTML = createSubFolderIfNotExist(locationHTML, "CounterParties");
	}

	private void createXMLFolders() {
		File home = new File(System.getProperty("user.home"));
		File folder = createSubFolderIfNotExist(home, "Accounting");
		locationXML = createSubFolderIfNotExist(folder, name);
		accountLocationXML = createSubFolderIfNotExist(locationXML, "Accounts");
		journalLocationXML = createSubFolderIfNotExist(locationXML, "Journals");
		balanceLocationXML = createSubFolderIfNotExist(locationXML, "Balances");
		mortgageLocationXML = createSubFolderIfNotExist(locationXML, "Mortgages");
		mortgageLocationXML = createSubFolderIfNotExist(locationXML, "Movements");
		counterpartyLocationXML = createSubFolderIfNotExist(locationXML, "CounterParties");
		//
//		locationXSL = createSubFolderIfNotExist(folder, "xsl");
//		File defaultFolder = new File("xsl");
////		File folder = FileSystemView.getFileSystemView().get
//		File[] files = FileSystemView.getFileSystemView().getFiles(defaultFolder, true);
//		// File[] files = FileSystemView.getFileSystemView().getFiles(locationXSL,true);
//		for(File file : files) {
//			File newFile = FileSystemView.getFileSystemView().createFileObject(locationXSL, file.getName());
//			try {
//				newFile.createNewFile();
////				newFile.
//			} catch (IOException io) {
//
//			}
//			newFile.renameTo(file);
//			FileSystemView.getFileSystemView().createFileObject(file, file.getName());
//		}
	}

	private File createSubFolderIfNotExist(File folder, String folderName) {
		File subFolder = FileSystemView.getFileSystemView().getChild(folder, folderName);
		if (!subFolder.exists()) {
			File newFolder = FileSystemView.getFileSystemView().createFileObject(folder, folderName);
			newFolder.mkdir();
		}
		return subFolder;
	}

//	public static Accounting fromXML(){
//		
//	}

	public void addMortgageTable(String mortgageName, Mortgage table) {
		mortgageTables.put(mortgageName, table);
	}

	public boolean containsMortgageName(String mortgageName) {
		return mortgageTables.containsKey(mortgageName);
	}

	public Mortgage getMortgage(String mortgageName) {
		return mortgageTables.get(mortgageName);
	}

	public ArrayList<Mortgage> getMortgagesTables() {
		return new ArrayList<Mortgage>(mortgageTables.values());
	}

	public void removeMortgageTable(Mortgage selectedMortgage) {
		mortgageTables.remove(selectedMortgage.toString());
	}

//	private void setSavedXML(boolean saved) {
//		savedXML = saved;
//	}

//	public boolean isSavedXML() {
//		return savedXML;
//	}

//	private void setSavedHTML(boolean saved) {
//		savedHTML = saved;
//	}

//	public boolean isSaved() {
//		return savedHTML;
//	}

	@Override
	public String toString() {
		return name;
	}

//	public Journal addNewJournal() {
//		// TODO: implement a gui to create journals (using journalType) see NewAccountGUI
//		String name = JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("Accounting").getString(
//				"GEEF_NAAM_DAGBOEK"));
//		while (name == null || name.equals(""))
//			name = JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("Accounting").getString(
//					"GEEF_NAAM_DAGBOEK"));
//		String abbr = JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("Accounting").getString(
//				"GEEF_AFKORTING_DAGBOEK"));
//		while (abbr == null || abbr.equals(""))
//			abbr = JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("Accounting").getString(
//					"GEEF_AFKORTING_DAGBOEK"));
//		Journal journal = new Journal(name, abbr);
//		journal.setAccounting(this);
//		journals.put(journal.toString(),journal);
//		return journal;
//	}

	public Journals getJournals() {
		return journals;
	}

	public Projects getProjects() {
		return projects;
	}

	public Journal getCurrentJournal() {
		return currentJournal;
	}

	public void setCurrentJournal(Journal journal) {
		currentJournal = journal;
	}

	public Accounts getAccounts() {
		return rekeningen;
	}

//	public static Accounting newInstance(boolean transfer) {
//		// closeInstance();
//		if (transfer) {
//			// OUDE REKENINGEN OVERNEMEN
//			// later implementeren
//			JOptionPane.showMessageDialog(
//					null,
//					java.util.ResourceBundle.getBundle("Accounting").getString(
//							"NOG_NIET_GEIMPLEMENTEERD"));
//			accounting = new Accounting("Transferred");
//		} else {
//			// VOLLEDIG NIEUW
//			String name = JOptionPane.showInputDialog(null, "Enter a name");
//			while (Accountings.contains(name)) {
//				name = JOptionPane.showInputDialog(null, "Name already used, enter another name");
//			}
//			accounting = new Accounting(name);
//		}
//		return accounting;
//	}

	public void close() {
		save();
	}

	public void saveToXML() {
//		if (!isSaved()) {
//			int result = JOptionPane.showConfirmDialog(null,
//					java.util.ResourceBundle.getBundle("Accounting").getString("SAVE_XML?"));
//			if (result == JOptionPane.OK_OPTION) {
		System.out.println("Accounting.saveToXML");
		getAccounts().saveAllXML();
		getJournals().saveAllXML();
//			setSavedXML(true);
//		}
	}

	private void saveToHTML() {
//		int result = JOptionPane.showConfirmDialog(null,
//				java.util.ResourceBundle.getBundle("Accounting").getString("SAVE_XML?"));
//		if (result == JOptionPane.OK_OPTION) {
		System.out.println("Accounting.saveToHTML");
		getAccounts().saveAllHTML();
		getJournals().saveAllHTML();
//			setSavedHTML(true);
//		}

	}

	public void save() {
		// null check and saved check should be together
		// but isSaved is only changed after transactions
		// not if only new accounts have been created
		// TODO: setSaved(false) after creation/deleting/modifying accounts
//		if (location.equals("")) {
//			saveAs();
//		} else {
		if (locationHTML == null) {
			createHTMLFolders();
		}
		saveToXML();
		saveToHTML();
		serialiseer();
//		}
	}

//	public Accounting saveAs() {
//		JFileChooser kiezer = new JFileChooser();
//		kiezer.setDialogTitle(java.util.ResourceBundle.getBundle("Accounting").getString("WAAR_OPSLAAN"));
//		int result = kiezer.showSaveDialog(null);
//		while (result != JFileChooser.APPROVE_OPTION)
//			result = kiezer.showSaveDialog(null);
//		File bestand = kiezer.getSelectedFile();
//		location = bestand.getPath();
//		saveToXML();
//		saveToHTML();
	// serialiseer();
//		return this;
//	}

	public void serialiseer() {
		try {
			File object = FileSystemView.getFileSystemView().getChild(locationXML, "object");
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(object));
			out.writeObject(this);
			out.flush();
			out.close();
		} catch (Exception e) {
			System.out.println(java.util.ResourceBundle.getBundle("Accounting").getString(
					"FOUT_SERIALISEREN")
					+ "Accounting");
			e.printStackTrace();
		}
//		try {
//			File counterparties = FileSystemView.getFileSystemView().getChild(locationXML, "counterparties");
//			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(counterparties));
//			out.writeObject(CounterParties.getInstance());
//			out.flush();
//			out.close();
//		} catch (Exception e) {
//			System.out.println(java.util.ResourceBundle.getBundle("Accounting").getString(
//					"FOUT_SERIALISEREN")
//					+ "Counterparties");
//			e.printStackTrace();
//		}
//		try {
//			File movements = FileSystemView.getFileSystemView().getChild(locationXML, "movements");
//			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(movements));
//			out.writeObject(Movements.getAllMovements());
//			out.flush();
//			out.close();
//		} catch (Exception e) {
//			System.out.println(java.util.ResourceBundle.getBundle("Accounting").getString(
//					"FOUT_SERIALISEREN")
//					+ "Movements");
//			e.printStackTrace();
//		}
	}

	public JournalTypes getJournalTypes() {
		return journalTypes;
	}

	// XML Locations: Getters and setters
	// Default Location
	protected File getXMLLocation() {
		return locationXML;
	}

	protected File getXMLAccountLocation() {
		return accountLocationXML;
	}

	protected File getXMLJournalLocation() {
		return journalLocationXML;
	}

	protected File getXMLBalanceLocation() {
		return balanceLocationXML;
	}

	protected File getXMLMortgageLocation() {
		return mortgageLocationXML;
	}

	protected File getXMLMovementLocation() {
		return movementLocationXML;
	}

	protected File getXMLCounterpartyLocation() {
		return counterpartyLocationXML;
	}

	// HTML Locations: Getters and setters
	// Default Location
	public void setLocation(File location) {
		locationHTML = location;
		locationHTML.mkdir();
	}

	public File getLocation() {
		return locationHTML;
	}

	// AccountLocations
	public void setAccountLocation(File location) {
		accountLocationHTML = location;
		accountLocationHTML.mkdir();
	}

	public File getAccountLocation() {
		return accountLocationHTML;
	}

	// Journal Location
	public void setJournalLocation(File location) {
		journalLocationHTML = location;
		journalLocationHTML.mkdir();
	}

	public File getJournalLocation() {
		return journalLocationHTML;
	}

	// Balance Location
	public void setBalanceLocation(File location) {
		balanceLocationHTML = location;
		balanceLocationHTML.mkdir();
	}

	public File getBalanceLocation() {
		return balanceLocationHTML;
	}

	// Mortgage Location
	public void setMortgageLocation(File location) {
		mortgageLocationHTML = location;
		mortgageLocationHTML.mkdir();
	}

	public File getMortgageLocation() {
		return mortgageLocationHTML;
	}

	// Movement Location
	public void setMovementLocation(File location) {
		movementLocationHTML = location;
		movementLocationHTML.mkdir();
	}

	public File getMovementLocation() {
		return movementLocationHTML;
	}

	// Counterparty location
	public void setCounterpartyLocation(File location) {
		counterpartyLocationHTML = location;
		counterpartyLocationHTML.mkdir();
	}

	public File getCounterPartyLocation() {
		return counterpartyLocationHTML;
	}

	// XSL
	public File getLocationXSL() {
		return locationXSL;
	}

	public void setLocationXSL(File location) {
		locationXSL = location;
	}
//	public void setSaved(boolean save) {
//		setSavedHTML(save);
//		setSavedXML(save);
//	}
}
