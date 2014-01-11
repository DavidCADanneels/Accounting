package be.dafke.ObjectModel;

/**
 * User: Dafke
 * Date: 11/03/13
 * Time: 10:11
 */
public abstract class BusinessTypeCollection<T extends BusinessType> extends BusinessCollection<T>{
    @Override
    public boolean separateFile(){
        return false;
    }

}
