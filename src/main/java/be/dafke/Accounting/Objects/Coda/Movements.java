package be.dafke.Accounting.Objects.Coda;

import be.dafke.Accounting.GUI.CodaManagement.SearchOptions;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Movements implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Movement> movements = new ArrayList<Movement>();
    private File htmlFolder;
    private File xmlFolder;

    public void add(Movement value) {
		movements.add(value);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("CounterParties:");
		for (Movement movement : movements){
			builder.append("\r\n").append(movement.toString());
		}
		return builder.toString();
	}

	public Movement getMovement(int nr) {
		return movements.get(nr);
	}

	public ArrayList<Movement> getAllMovements() {
		return movements;
	}

	public ArrayList<Movement> getMovements(SearchOptions searchOptions) {
		ArrayList<Movement> result = new ArrayList<Movement>();
        CounterParty counterParty = searchOptions.getCounterParty();
        String transactionCode = searchOptions.getTransactionCode();
        String communication = searchOptions.getCommunication();
        boolean searchOnCounterParty = searchOptions.isSearchOnCounterParty();
        boolean searchOnTransactionCode = searchOptions.isSearchOnTransactionCode();
        boolean searchOnCommunication = searchOptions.isSearchOnCommunication();
		for(Movement movement : movements) {
			if ((!searchOnTransactionCode || transactionCode.equals(movement.getTransactionCode()))  &&
                    (!searchOnCommunication || communication.equals(movement.getCommunication())) &&
                    (!searchOnCounterParty || counterParty == movement.getCounterParty())) {
				result.add(movement);
			}
		}
		return result;
	}

	public int getSize() {
		return movements.size();
	}

    public void setXmlFolder(File xmlFolder) {
        this.xmlFolder = xmlFolder;
//        xmlFile = FileSystemView.getFileSystemView().getChild(this.xmlFolder, "Accounts.xml");
    }

    public File getXmlFolder(){
        return xmlFolder;
    }

    public void setHtmlFolder(File htmlFolder) {
        this.htmlFolder = htmlFolder;
//        htmlFile = FileSystemView.getFileSystemView().getChild(this.htmlFolder, "Accounts.html");
    }

    public File getHtmlFolder(){
        return htmlFolder;
    }

}
