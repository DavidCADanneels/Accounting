package be.dafke.ObjectModel;

/**
 * User: Dafke
 * Date: 11/03/13
 * Time: 15:07
 */
public interface BusinessCollectionProvider<T extends WriteableBusinessObject> {
    public WriteableBusinessCollection<T> getBusinessCollection();

    public void setBusinessCollection(WriteableBusinessCollection<T> businessCollection);
}
