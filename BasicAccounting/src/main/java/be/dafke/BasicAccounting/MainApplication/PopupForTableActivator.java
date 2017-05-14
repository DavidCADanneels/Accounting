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
    private static ArrayList<JPopupMenu> popupForTableActivators = new ArrayList<>();

    private final SelectableTable<BusinessObject> tabel;
    private final JPopupMenu popup;

    private PopupForTableActivator(JPopupMenu popup, SelectableTable tabel){
        this.popup = popup;
        this.tabel = tabel;
    }

    public static PopupForTableActivator getInstance(JPopupMenu popup, SelectableTable tabel){
        popupForTableActivators.add(popup);
        return new PopupForTableActivator(popup, tabel);
    }

    public void mouseClicked(MouseEvent me) {
        for(JPopupMenu activator:popupForTableActivators){
            activator.setVisible(false);
        }
        if (me.getButton() == 3) {
            Point cell = me.getPoint();
            int row = tabel.rowAtPoint(cell);

            ArrayList<Integer> selectedRows = new ArrayList<>();
            for(int i:tabel.getSelectedRows()){
                selectedRows.add(i);
            }
            if(!selectedRows.contains(row)){
                tabel.setRowSelectionInterval(row,row);
            }
            int col = tabel.columnAtPoint(cell);
            tabel.setColumnSelectionInterval(col, col);
            ArrayList<BusinessObject> selectedObjects = tabel.getSelectedObjects();
            if(!selectedObjects.isEmpty()) {
                popup.setLocation(me.getLocationOnScreen());
                popup.setVisible(true);
            }
        }
    }
}
