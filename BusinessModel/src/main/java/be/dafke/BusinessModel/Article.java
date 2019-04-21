package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.function.Predicate;

public class Article extends BusinessObject{
    private String HSCode = "";
    private String itemName = "";
    private Integer purchaseVatRate = 0;
    private Integer salesVatRate = 6;
    private Integer itemsPerUnit = 1;
    private BigDecimal purchasePrice = null;
    private BigDecimal salesPriceItemWithVat = null;
    private BigDecimal salesPriceUnitWithVat = null;
    private Contact supplier;
    private ArrayList<StockOrder> stockOrders = new ArrayList<>();
    private ArrayList<PromoOrder> promoOrders = new ArrayList<>();
    private ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<>();
    private ArrayList<SalesOrder> salesOrders = new ArrayList<>();

    private Integer initStock = 0;
    private Integer nrPromo = 0;
    private Integer nrAdded = 0;
    private Integer nrRemoved = 0;
    private Integer nrOrderedForSO = 0;
    private Integer nrOrderedByPO = 0;

    public Article(Article article, Contacts contacts){
        this(article.getName());
        HSCode = article.HSCode;
        itemName = article.itemName;
        purchaseVatRate = article.purchaseVatRate;
        salesVatRate = article.salesVatRate;
        itemsPerUnit = article.itemsPerUnit;
        purchasePrice = article.purchasePrice;
        salesPriceItemWithVat = article.salesPriceItemWithVat;
        salesPriceUnitWithVat = article.salesPriceUnitWithVat;
        String supplierName = article.supplier.getName();
        supplier = contacts.getBusinessObject(supplierName);
        nrRemoved = 0;
        nrAdded = 0;
        // TODO: copy open SO and PO ?
        nrOrderedByPO = article.getNrOrderedByPO();
        nrOrderedForSO = article.getNrOrderedForSO();
    }

    public Article(String name){
        setName(name);
    }

    public void setHSCode(String HSCode) {
        this.HSCode = HSCode;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setSalesVatRate(Integer salesVatRate) {
        this.salesVatRate = salesVatRate;
    }

    public void setPurchaseVatRate(Integer purchaseVatRate) {
        this.purchaseVatRate = purchaseVatRate;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setSupplier(Contact supplier) {
        this.supplier = supplier;
    }

    public void setItemsPerUnit(Integer itemsPerUnit) {
        this.itemsPerUnit = itemsPerUnit;
    }


    public void setSalesPriceItemWithVat(BigDecimal salesPriceItemWithVat) {
        this.salesPriceItemWithVat = salesPriceItemWithVat;
    }

    public void setSalesPriceUnitWithVat(BigDecimal salesPriceUnitWithVat) {
        this.salesPriceUnitWithVat = salesPriceUnitWithVat;
    }

    public Integer getItemsPerUnit() {
        return itemsPerUnit;
    }

    public Contact getSupplier() {
        return supplier;
    }

    public BigDecimal getPurchasePrice(){
        return purchasePrice;
    }

    public String getHSCode() {
        return HSCode;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getSalesVatRate() {
        return salesVatRate;
    }

    public BigDecimal getSalesPriceItemWithVat() {
        return salesPriceItemWithVat;
    }

    public BigDecimal getSalesPriceUnitWithVat() {
        return salesPriceUnitWithVat;
    }

    public BigDecimal getSalesPriceItemWithoutVat() {
        if(salesPriceItemWithVat ==null) return null;
        return salesPriceItemWithVat.divide(getSalesFactor(),BigDecimal.ROUND_HALF_DOWN);
    }

    public BigDecimal getSalesPriceUnitWithoutVat() {
        if(salesPriceUnitWithVat ==null) return null;
        BigDecimal salesFactor = getSalesFactor();
        return salesPriceUnitWithVat.divide(salesFactor, BigDecimal.ROUND_HALF_DOWN);
    }

    public Integer getPurchaseVatRate() {
        return purchaseVatRate;
    }

    BigDecimal getPurchasePrice(int number){
        return purchasePrice.multiply(new BigDecimal(number));
    }

    // 6 -> 0.06
    private BigDecimal getPurchasePercentage(){
        return Utils.getPercentage(purchaseVatRate);
    }

    // 6 -> 0.06
    BigDecimal getSalesPercentage(){
        return Utils.getPercentage(salesVatRate);
    }

    // 6 -> 1.06
    private BigDecimal getPurchaseFactor(){
        return Utils.getFactor(purchaseVatRate);
    }

    // 6 -> 1.06
    BigDecimal getSalesFactor(){
        return Utils.getFactor(salesVatRate);
    }

    BigDecimal getPurchasePriceWithVat(){
        return purchasePrice.multiply(getPurchaseFactor());
    }

    BigDecimal getPurchasePriceWithVat(int number){
        return getPurchasePriceWithVat().multiply(new BigDecimal(number));
    }

    public BigDecimal getUnitProfit(BigDecimal salesprice){
        return salesprice.subtract(purchasePrice);
    }

    public BigDecimal getItemProfit(BigDecimal salesprice){
        return salesprice.subtract(purchasePrice.divide(new BigDecimal(itemsPerUnit), BigDecimal.ROUND_HALF_DOWN));
    }

    BigDecimal getPurchaseVat(){
        return purchasePrice.multiply(getPurchasePercentage());
    }

    BigDecimal getPurchaseVat(int number){
        return getPurchaseVat().multiply(new BigDecimal(number));
    }

    public static Predicate<Article> ofSupplier(Contact supplier) {
        return article -> article.getSupplier() == supplier;
    }

    public static Predicate<Article> withOrders(){
        return article -> article.getPurchaseOrders().size()>0 || article.getSalesOrders().size()>0;
    }

    public static Predicate<Article> inStock(){
        return article -> article.getNrInStock() > 0;
    }

    public ArrayList<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public ArrayList<SalesOrder> getSalesOrders() {
        return salesOrders;
    }

    public ArrayList<StockOrder> getStockOrders() {
        return stockOrders;
    }

    public ArrayList<PromoOrder> getPromoOrders() {
        return promoOrders;
    }

    public void addPurchaseOrder(PurchaseOrder purchaseOrder) {
        purchaseOrders.add(purchaseOrder);
    }

    public void addStockOrder(StockOrder stockOrder) {
        stockOrders.add(stockOrder);
    }

    public void addPromoOrder(PromoOrder promoOrder) {
        promoOrders.add(promoOrder);
    }

    public void setPoOrdered(int numberOfItems) {
        nrOrderedByPO += numberOfItems;
    }

    public void setPoCnOrdered(int numberOfItems) {
        nrOrderedByPO -= numberOfItems;
    }

    public void setPoDelivered(int numberOfItems) {
        nrOrderedByPO-=numberOfItems;
        nrAdded+=numberOfItems;
    }

    public void setPoCnDelivered(int numberOfItems) {
        nrOrderedByPO+=numberOfItems;
        nrAdded-=numberOfItems;
    }

    public void setStockOrderDelivered(int numberOfItems) {
        nrAdded+=numberOfItems;
        initStock+=numberOfItems;
    }

    public void setPromoOrderDelivered(int numberOfItems) {
        nrRemoved+=numberOfItems;
        // TODO: remove below line if 'PromoOrder extends Order' is implemented
//        nrOrderedForSO-=numberOfItems;
        nrPromo+=numberOfItems;
    }

    public void addSalesOrder(SalesOrder salesOrder) {
        salesOrders.add(salesOrder);
    }

    public void setSoOrdered(int numberOfItems) {
        nrOrderedForSO += numberOfItems;
    }

    public void setSoCnOrdered(int numberOfItems) {
        nrOrderedForSO -= numberOfItems;
    }

    public void setSoDelivered(int numberOfItems) {
        nrOrderedForSO-=numberOfItems;
        nrRemoved+=numberOfItems;
    }

    public void setSoCnDelivered(int numberOfItems) {
        nrOrderedForSO+=numberOfItems;
        nrRemoved-=numberOfItems;
    }

    public Integer getNrInStock() {
        return nrAdded - nrRemoved;
    }

    public Integer getNrAdded() {
        return nrAdded;
    }

    public Integer getNrRemoved() {
        return nrRemoved;
    }

    public Integer getNrOrderedForSO() {
        return nrOrderedForSO;
    }

    public Integer getNrOrderedByPO() {
        return nrOrderedByPO;
    }

    public Integer getInitStock() {
        return initStock;
    }

    public Integer getNrPromo() {
        return nrPromo;
    }
}
