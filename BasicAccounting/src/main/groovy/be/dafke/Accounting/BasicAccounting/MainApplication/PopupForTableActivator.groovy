package be.dafke.Accounting.BasicAccounting.MainApplication

import be.dafke.Accounting.ObjectModel.BusinessObject
import be.dafke.ComponentModel.SelectableTable

import javax.swing.JPopupMenu
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class PopupForTableActivator extends MouseAdapter {
    private static ArrayList<JPopupMenu> popupForTableActivators = new ArrayList<>()

    private final SelectableTable<BusinessObject> tabel
    private final JPopupMenu popup

    private PopupForTableActivator(JPopupMenu popup, SelectableTable tabel){
        this.popup = popup
        this.tabel = tabel
    }

    static PopupForTableActivator getInstance(JPopupMenu popup, SelectableTable tabel){
        popupForTableActivators.add(popup)
        new PopupForTableActivator(popup, tabel)
    }

    static void closeAllPopups(){
        for(JPopupMenu activator:popupForTableActivators){
            activator.setVisible(false)
        }
    }

    void mouseClicked(MouseEvent me) {
        closeAllPopups()
        if (me.getButton() == 3) {
            Point cell = me.getPoint()
            int row = tabel.rowAtPoint(cell)

            ArrayList<Integer> selectedRows = new ArrayList<>()
            for(int i:tabel.getSelectedRows()){
                selectedRows.add(i)
            }
            if(!selectedRows.contains(row)){
                tabel.setRowSelectionInterval(row,row)
            }
            int col = tabel.columnAtPoint(cell)
            tabel.setColumnSelectionInterval(col, col)
            ArrayList<BusinessObject> selectedObjects = tabel.getSelectedObjects()
            if(!selectedObjects.isEmpty()) {
                popup.setLocation(me.getLocationOnScreen())
                popup.setVisible(true)
            }
        }
    }
}
