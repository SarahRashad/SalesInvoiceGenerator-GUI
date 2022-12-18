package model;

import java.util.Date;
import java.util.ArrayList;
//This is a POJO class
//It only carries data about invoice header
public class InvoiceHeader {

    private int invoiceNum;//an ID for the invoice
    private String invoiceDate;// The date on which the invoice is issued
    private String customerName; // the customer name
    private ArrayList<InvoiceLine> invoiceLines; // An arraylist of the items in the invoice

    /**
     * Non-default constructor for InvoiceHeader
     * The only constructor to the class
     * @param invoiceNum
     * @param invoiceDate
     * @param customerName
     * @param invoiceLines
     */
    public InvoiceHeader(int invoiceNum,String invoiceDate,String customerName, ArrayList<InvoiceLine> invoiceLines){
        this.setInvoiceNum(invoiceNum);
        this.setInvoiceDate(invoiceDate);
        this.setCustomerName(customerName);
        this.setInvoiceLines(invoiceLines);

    }

    //Setters and getters to the member fields in the class
    public int getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(int invoiceNum) {
        this.invoiceNum = invoiceNum;
    }
    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(ArrayList<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public void addInvoiceLine(InvoiceLine invoiceLine){
        this.invoiceLines.add(invoiceLine);
    }

    /**
     * This method Calculates the total invoice price
     * @return double value
     */
    public double getTotalInvoicePrice(){
        double total=0.0;
        for(InvoiceLine il: this.getInvoiceLines()){
            total+=il.getTotalItemPrice();
        }
        return total;
    }

    /**
     *
     * @return a String with the Invoice Header information
     */
    @Override
    public String toString() {
        String printout="";
        printout+="Invioice Number:"+this.getInvoiceNum()+"\n";
        printout+="{"+"\n";
        printout+="Invoice Date: "+this.getInvoiceDate()+", Customer Name: "+this.getCustomerName()+"\n";
        for(InvoiceLine il: this.getInvoiceLines()){
            printout+= il.toString()+"\n";
        }
        printout+="}"+"\n";
        return printout;
    }
}
