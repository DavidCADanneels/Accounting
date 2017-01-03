package be.dafke.BasicAccounting.MainApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by ddanneel on 18/02/2015.
 */
public class PopupForTableActivator extends MouseAdapter {
    private final JTable tabel;
    private final JPopupMenu popup;
//    private final ListSelectionModel selectionModel;

    public PopupForTableActivator(JPopupMenu popup, JTable tabel){//}, ListSelectionModel selectionModel) {
        this.popup = popup;
        this.tabel = tabel;
//        this.selectionModel = selectionModel;
    }

    public void mouseClicked(MouseEvent me) {
        if (me.getButton() == 3) {
            Point cell = me.getPoint();
            int col = tabel.columnAtPoint(cell);
            int row = tabel.rowAtPoint(cell);
            tabel.getSelectionModel().setSelectionInterval(row,row);
//            selectionModel.setSelectionInterval(row,row);
//            tabel.setSelectedRow(row);
//            tabel.setSelectedColumn(col);
            //
            Point location = me.getLocationOnScreen();
            popup.show(null, location.x, location.y);
        } else popup.setVisible(false);
    }
}
