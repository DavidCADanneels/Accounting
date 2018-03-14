package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class Article extends BusinessObject{
    private String HSCode = "";
    private String itemName = "";
    private Integer purchaseVatRate = 0;
    private Integer salesVatRate = 6;
    private Integer itemsPerUnit = 1;
    private Integer minimumNumberForReduction = 10;
    private BigDecimal purchasePrice = null;
    private BigDecimal salesPriceSingleWithVat = null;
    private BigDecimal salesPricePromoWithVat = null;
    private Contact supplier, customer;

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

    public void setCustomer(Contact customer) {
        this.customer = customer;
    }

    public void setItemsPerUnit(Integer itemsPerUnit) {
        this.itemsPerUnit = itemsPerUnit;
    }

    public void setMinimumNumberForReduction(Integer minimumNumberForReduction) {
        this.minimumNumberForReduction = minimumNumberForReduction;
    }

    public void setSalesPriceSingleWithVat(BigDecimal salesPriceSingleWithVat) {
        this.salesPriceSingleWithVat = salesPriceSingleWithVat;
    }

    public void setSalesPricePromoWithVat(BigDecimal salesPricePromoWithVat) {
        this.salesPricePromoWithVat = salesPricePromoWithVat;
    }

    public Integer getItemsPerUnit() {
        return itemsPerUnit;
    }

    public Integer getMinimumNumberForReduction() {
        return minimumNumberForReduction;
    }

    public Contact getSupplier() {
        return supplier;
    }

    public Contact getCustomer() {
        return customer;
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

    public BigDecimal getSalesPriceSingleWithVat() {
        return salesPriceSingleWithVat;
    }

    public BigDecimal getSalesPricePromoWithVat() {
        return salesPricePromoWithVat;
    }

    public BigDecimal getSalesPriceSingleWithoutVat() {
        if(salesPriceSingleWithVat==null) return null;
        return salesPriceSingleWithVat.divide(getFactor());
    }

    public BigDecimal getSalesPricePromoWithoutVat() {
        if(salesPricePromoWithVat==null) return null;
        return salesPricePromoWithVat.divide(getFactor());
    }

    public Integer getPurchaseVatRate() {
        return purchaseVatRate;
    }

    public BigDecimal getPurchasePrice(int number){
        return multiply(purchasePrice, number);
    }

    private BigDecimal multiply(BigDecimal source, int number){
        return source.multiply(new BigDecimal(number));
    }

    private BigDecimal divide(BigDecimal source, int number){
        return source.divide(new BigDecimal(number));
    }

    // return 0.06
    private BigDecimal getPercentage(){
        return divide(new BigDecimal(purchaseVatRate),100);
    }

    // return 1.06
    private BigDecimal getFactor(){
        return BigDecimal.ONE.add(getPercentage());
    }

    public BigDecimal getPurchasePriceWithVat(){
        return purchasePrice.multiply(getFactor());
    }

    public BigDecimal getPurchasePriceWithVat(int number){
        return multiply(getPurchasePriceWithVat(), number);
    }

    public BigDecimal getProfit(BigDecimal salesprice){
        return salesprice.subtract(purchasePrice);
    }

    public BigDecimal getProfit(BigDecimal salesprice, int number){
        return multiply(getProfit(salesprice), number);
    }

    public BigDecimal getPurchaseVat(){
        return purchasePrice.multiply(getPercentage());
    }

    public BigDecimal getPurchaseVat(int number){
        return multiply(getPurchaseVat(), number);
    }

    public BigDecimal getSalesVatAmount(int number){
        BigDecimal salesPriceWithoutVat = getSalesPriceWithoutVat(number);
        return salesPriceWithoutVat.multiply(getPercentage());
    }

    public BigDecimal getSalesVatAmount(){
        BigDecimal salesPriceWithoutVat = getSalesPriceWithoutVat();
        return salesPriceWithoutVat.multiply(getPercentage());
    }

    public BigDecimal getSalesPriceWithoutVat() {
        BigDecimal salesPriceWithVat = getSalesPriceWithVat();
        return salesPriceWithVat.divide(getFactor());
    }

    public BigDecimal getSalesPriceWithoutVat(int number) {
        BigDecimal salesPriceWithVat = getSalesPriceWithVat(number);
        return salesPriceWithVat.divide(getFactor());
    }

    private BigDecimal getSalesPricePerUnitWithVat(int number){
        if (number >= minimumNumberForReduction){
            return salesPricePromoWithVat;
        }else {
            return salesPriceSingleWithVat;
        }
    }

    public BigDecimal getSalesPriceWithVat() {
        return salesPriceSingleWithVat;
    }

    public BigDecimal getSalesPriceWithVat(int number) {
        BigDecimal salesPricePerUnitWithVat = getSalesPricePerUnitWithVat(number);
        return multiply(salesPricePerUnitWithVat, number);
    }

    public static Predicate<Article> ofSupplier(Contact supplier) {
        return article -> article.getSupplier() == supplier;
    }

    public static Predicate<Article> forCustomer(Contact customer) {
        return article -> article.getCustomer() == customer;
    }
}
