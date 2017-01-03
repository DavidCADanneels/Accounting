package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.Booking;
import be.dafke.ComponentModel.RefreshableTable;

import java.util.ArrayList;

/**
 * Created by ddanneels on 3/01/2017.
 */
public class JournalTable extends RefreshableTable<Booking> {
    public JournalTable(JournalDetailsDataModel journalDetailsDataModel) {
        super(journalDetailsDataModel);
    }

    public ArrayList<Booking> getSelectedObjects() {
        int[] rows = getSelectedRows();
        ArrayList<Booking> businessObjectArrayList = new ArrayList<>();
        for(int row : rows) {
            Booking businessObject = (Booking) model.getValueAt(row, 0);
            businessObjectArrayList.add(businessObject);
        }
        return businessObjectArrayList;
    }
}
