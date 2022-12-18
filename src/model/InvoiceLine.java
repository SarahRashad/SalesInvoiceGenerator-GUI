package model;

public class InvoiceLine {
    private String itemName;//Item Name
    private double itemPrice;//Item Price
    private int count;//count of the Item

    public InvoiceLine(){
        this.setItemName("default_item");
        this.setItemPrice(1.0);
        this.setCount(1);
    }

    /**
     * Non-default constructor
     * @param itemName
     * @param itemPrice
     * @param count
     */
    public InvoiceLine(String itemName,double itemPrice,int count){
        this.setItemName(itemName);
        this.setItemPrice(itemPrice);
        this.setCount(count);
    }

    /**
     * itemName getter function
     * @return itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * itemName setter function
     * @param itemName
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * itemPrice getter function
     * @return itemPrice
     */
    public double getItemPrice() {
        return itemPrice;
    }

    /**
     * itemPrice setter function
     * @param itemPrice
     */
    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    /**
     * Item count getter function
     * @return count
     */
    public int getCount() {
        return count;
    }

    /**
     * item count setter function
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * this function calculates Item total price
     * @return double value
     */
    public double getTotalItemPrice(){
        return this.getItemPrice()*this.getCount();
    }

    /**
     * @return String with invoice line information
     */
    @Override
    public String toString() {
        return
                "item name:" + itemName +
                        ", itemPrice:" + itemPrice +
                        ", count:" + count ;
    }
}
