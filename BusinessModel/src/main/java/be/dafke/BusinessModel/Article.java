package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        return salesPriceSingleWithVat.divide(getSalesFactor(),BigDecimal.ROUND_HALF_DOWN);
    }

    public BigDecimal getSalesPricePromoWithoutVat() {
        if(salesPricePromoWithVat==null) return null;
        BigDecimal salesFactor = getSalesFactor();
        BigDecimal divide = salesPricePromoWithVat.divide(salesFactor, BigDecimal.ROUND_HALF_DOWN);
        return divide;
    }

    public Integer getPurchaseVatRate() {
        return purchaseVatRate;
    }

    public BigDecimal getPurchasePrice(int number){
        return purchasePrice.multiply(new BigDecimal(number));
    }

    // return 0.06
    private BigDecimal getPurchasePercentage(){
        return new BigDecimal(purchaseVatRate).divide(new BigDecimal(100));
    }

    // return 0.06
    private BigDecimal getSalesPercentage(){
        return new BigDecimal(salesVatRate).divide(new BigDecimal(100));
    }

    // return 1.06
    private BigDecimal getPurchaseFactor(){
        return BigDecimal.ONE.add(getPurchasePercentage());
    }

    // return 1.06
    private BigDecimal getSalesFactor(){
        return BigDecimal.ONE.add(getSalesPercentage());
    }

    public BigDecimal getPurchasePriceWithVat(){
        return purchasePrice.multiply(getPurchaseFactor());
    }

    public BigDecimal getPurchasePriceWithVat(int number){
        return getPurchasePriceWithVat().multiply(new BigDecimal(number));
    }

    public BigDecimal getUnitProfit(BigDecimal salesprice){
        return salesprice.subtract(purchasePrice);
    }

    public BigDecimal getItemProfit(BigDecimal salesprice){
        return salesprice.subtract(purchasePrice.divide(new BigDecimal(itemsPerUnit), BigDecimal.ROUND_HALF_DOWN));
    }

    public BigDecimal getPurchaseVat(){
        return purchasePrice.multiply(getPurchasePercentage());
    }

    public BigDecimal getPurchaseVat(int number){
        return getPurchaseVat().multiply(new BigDecimal(number));
    }

    public BigDecimal getSalesVatAmount(int number){
        BigDecimal salesPriceWithoutVat = getSalesPriceWithoutVat(number);
        return salesPriceWithoutVat.multiply(getSalesPercentage());
    }

    public BigDecimal getSalesVatAmount(){
        BigDecimal salesPriceWithoutVat = getSalesPriceWithoutVat();
        return salesPriceWithoutVat.multiply(getSalesPercentage());
    }

    public BigDecimal getSalesPriceWithoutVat() {
        BigDecimal salesPriceWithVat = getSalesPriceWithVat();
        return salesPriceWithVat.divide(getSalesFactor(),BigDecimal.ROUND_HALF_DOWN);
    }

    public BigDecimal getSalesPriceWithoutVat(int number) {
        BigDecimal salesPriceWithVat = getSalesPriceWithVat(number);
        return salesPriceWithVat.divide(getSalesFactor(),BigDecimal.ROUND_HALF_DOWN);
    }

    public BigDecimal getSalesPriceWithVat() {
        return salesPriceSingleWithVat;
    }

    public BigDecimal getSalesPriceWithVat(int number) {
        if(salesPriceSingleWithVat==null||salesPricePromoWithVat==null) return BigDecimal.ZERO;
        if (number < minimumNumberForReduction){
            return salesPriceSingleWithVat.multiply(new BigDecimal(number));
        }else {
            if(itemsPerUnit == 1) {
                return salesPricePromoWithVat.multiply(new BigDecimal(number));
            } else{
                int nrOfUnits = number/itemsPerUnit;
                int remainingItems = number%itemsPerUnit;
                BigDecimal priceForUnits = salesPricePromoWithVat.multiply(new BigDecimal(nrOfUnits));
                BigDecimal priceForItems = salesPriceSingleWithVat.multiply(new BigDecimal(remainingItems));
                return priceForUnits.add(priceForItems);
            }
        }
    }

    public static Predicate<Article> ofSupplier(Contact supplier) {
        return article -> article.getSupplier() == supplier;
    }

    public static Predicate<Article> forCustomer(Contact customer) {
        return article -> article.getCustomer() == customer;
    }
}
