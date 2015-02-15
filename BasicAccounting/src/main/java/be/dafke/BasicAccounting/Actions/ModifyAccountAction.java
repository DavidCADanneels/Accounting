package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Account;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Created by ddanneel on 15/02/2015.
 */
public abstract class ModifyAccountAction implements Action{
    protected ArrayList<Account> accountList;

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public void putValue(String key, Object value) {
        accountList = (ArrayList<Account>)value;
    }

    @Override
    public void setEnabled(boolean b) {

    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }
}
