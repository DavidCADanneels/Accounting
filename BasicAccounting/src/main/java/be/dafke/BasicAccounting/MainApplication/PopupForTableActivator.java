package be.dafke.BasicAccounting.MainApplication;

import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.BusinessObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by ddanneel on 18/02/2015.
 */
public class PopupForTableActivator extends MouseAdapter {
    private final SelectableTable<BusinessObject> tabel;
    private final JPopupMenu popup;

    public PopupForTableActivator(JPopupMenu popup, SelectableTable tabel){
        this.popup = popup;
        this.tabel = tabel;
    }

    public void mouseClicked(MouseEvent me) {
        if (me.getButton() == 3) {
            Point cell = me.getPoint();
            int col = tabel.columnAtPoint(cell);
            int row = tabel.rowAtPoint(cell);

            ArrayList<Integer> selectedRows = new ArrayList<>();
            for(int i:tabel.getSelectedRows()){
                selectedRows.add(i);
            }
            if(selectedRows.contains(row)){
                // keep selection
            } else {
                tabel.setRowSelectionInterval(row,row);
            }
            //
            // TODO: close all other popups first (popups of any component)
            popup.setLocation(me.getLocationOnScreen());
            popup.setVisible(true);

        } else popup.setVisible(false);
    }
}
