package be.dafke.Accounting.Objects;

/**
 * User: Dafke
 * Date: 11/03/13
 * Time: 7:39
 */
public interface BusinessTypeCollectionDependent<T extends BusinessType> {

    public void setBusinessTypeCollection(BusinessTypeCollection<T> businessTypeCollection);

}
