package be.dafke.BusinessActions;

import be.dafke.BusinessModel.Mortgages;

import java.util.EventListener;

/**
 * Created by ddanneels on 5/11/2016.
 */
public interface MortgagesListener extends EventListener{
    void setMortgages(Mortgages mortgages);
}
