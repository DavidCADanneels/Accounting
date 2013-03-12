package be.dafke.Accounting.Objects;

/**
 * User: Dafke
 * Date: 11/03/13
 * Time: 15:07
 */
public interface BusinessCollectionProvider<T extends BusinessObject> {
    public BusinessCollection<T> getBusinessCollection();

    public void setBusinessCollection(BusinessCollection<T> businessCollection);
}
