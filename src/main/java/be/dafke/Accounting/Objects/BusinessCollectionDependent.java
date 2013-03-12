package be.dafke.Accounting.Objects;

/**
 * User: Dafke
 * Date: 11/03/13
 * Time: 7:39
 */
public interface BusinessCollectionDependent<T extends BusinessObject> {

    public void setBusinessCollection(BusinessCollection<T> businessCollection);

}
