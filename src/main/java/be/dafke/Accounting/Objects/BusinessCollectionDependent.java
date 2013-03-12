package be.dafke.Accounting.Objects;

/**
 * User: Dafke
 * Date: 11/03/13
 * Time: 7:39
 */
public interface BusinessCollectionDependent<T extends WriteableBusinessObject> {

    public void setBusinessCollection(WriteableBusinessCollection<T> businessCollection);

}
