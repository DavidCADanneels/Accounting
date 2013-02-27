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
    private File locationHtml;
    private File locationXml;

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

    public void setLocationXml(File locationXml) {
        this.locationXml = locationXml;
        if(!this.locationXml.exists()){
            this.locationXml.mkdir();
        }
//        xmlFile = FileSystemView.getFileSystemView().getChild(this.locationXml, "Accounts.xml");
    }

    public File getLocationXml(){
        return locationXml;
    }

    public void setLocationHtml(File locationHtml) {
        this.locationHtml = locationHtml;
        if(!this.locationHtml.exists()){
            this.locationHtml.mkdir();
        }
//        htmlFile = FileSystemView.getFileSystemView().getChild(this.locationHtml, "Accounts.html");
    }

    public File getLocationHtml(){
        return locationHtml;
    }

}
