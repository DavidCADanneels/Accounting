package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.NotEmptyException;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 11:06
 */
public class Mortgages extends BusinessCollection<Mortgage>{

    @Override
    public void removeBusinessObject(Mortgage value) throws NotEmptyException {
        if(value.getNrPayed()!=0 && !value.isPayedOff()){
            throw new NotEmptyException();
        }
        removeBusinessObject(NAME, value.getName());
    }

}