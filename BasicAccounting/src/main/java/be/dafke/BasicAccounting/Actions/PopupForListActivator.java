package be.dafke.BasicAccounting.Actions;

import be.dafke.ComponentModel.RefreshableTable;
import be.dafke.ObjectModel.BusinessObject;

import javax.swing.JList;
import javax.swing.JPopupMenu;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by ddanneel on 18/02/2015.
 */
public class PopupForListActivator extends MouseAdapter {
    private final JList<? extends BusinessObject> list;
    private final JPopupMenu popup;

    public PopupForListActivator(JPopupMenu popup, JList<? extends BusinessObject> list) {
        this.popup = popup;
        this.list = list;
    }

    public void mouseClicked(MouseEvent me) {
        Point location = me.getLocationOnScreen();
        if (me.getButton() == 3 && list.getSelectedIndex() != -1) {
            popup.show(null, location.x, location.y);
        } else {
            popup.setVisible(false);
        }
    }
}
