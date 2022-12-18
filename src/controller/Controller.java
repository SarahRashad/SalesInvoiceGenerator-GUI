package controller;


import model.InvoiceHeader;
import model.InvoiceLine;
import model.InvoicesData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class Controller is responsible for the flow data between the model(Data) and the view(UI)
 * Only one object is required to handle this task so a Singleton design is applied
 */
public class Controller {

    private static Controller controller = null;
    private InvoicesData invoicesData;
    private Controller(){
        invoicesData= new InvoicesData();
    }

    public static Controller getInstance(){
        if (controller==null){
            controller = new Controller();
        }
        return controller;
    }
    public String loadInvoiceData(String invoiceHeaderFilename, String invoiceLineFilename){
        return invoicesData.loadInvoiceData(invoiceHeaderFilename,invoiceLineFilename);
    }

    public void saveInvoiceData(){
        invoicesData.saveInvoiceData();
    }
    public void saveInvoiceData(String newHeaderFile, String newLineFile,boolean setCurrentSource){
        invoicesData.saveInvoiceData(newHeaderFile,newLineFile,setCurrentSource);
    }
    public ArrayList<InvoiceHeader> getInvoiceHeaders(){
        return invoicesData.getInvoiceHeaders();
    }

    public ArrayList<InvoiceLine> getInvoiceLines(){
        return invoicesData.getInvoiceLines();
    }

    public void createInvoiceHeader(InvoiceHeader invoiceHeader){
        invoicesData.createNewInvoice(invoiceHeader);
    }

    public InvoiceHeader getInvoice(int invoiceNumber){
        return invoicesData.getInvoice(invoiceNumber);
    }

    public void deleteInvoiceHeader(int invoiceNumber){
        invoicesData.deleteInvoice(invoiceNumber);
    }

    public void updateInvoiceHeader(InvoiceHeader invoiceHeader){
        invoicesData.updateInvoice(invoiceHeader);
    }

    public void saveInvoiceHeaders(String invoiceData,String invoiceHeaderFilename,String invoiceLineFilename){
        invoicesData.saveInvoiceHeaders(invoiceData,invoiceHeaderFilename,invoiceLineFilename);
    }

    public int getNextInvoiceNum(){
        return invoicesData.getNextInvoiceNum();
    }


    public static String validateNameFormat(String inputName){
        if(inputName.matches("[a-zA-Z].*$"))
            return "";
        return "*Please Make Sure to start \nthe Customer name or the item name \nwith a letter";
    }

    public static String validateDoubleFormat(String inputDouble){
        double isDouble;
        try {
            isDouble = Double.parseDouble(inputDouble);
        }
        catch(Exception e){
            return "Invalid Price Format ... \nplease enter a double value and then press enter";
        }
        if(isDouble<0){
            return "Invalid value: Cannot be of negative price";
        }

        return "";
    }
    public static String validateIntegerFormat(String inputInteger){
        Integer isInteger;
        try {
            isInteger = Integer.parseInt(inputInteger);
        }
        catch(Exception e){
            return "Invalid Format ... \nplease enter an Integer value and then press enter";
        }
        if(isInteger<0){
            return "Invalid value: Data field cannot be of negative value";
        }

        return "";
    }
    public static String validateDateFormat(String inputDate){
        if (inputDate.trim().equals(""))
            return "Empty Date Field \nuse MM/DD/YYYY or MM-DD-YYYY format";
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setLenient(false);
        try {
            Date javaDate = simpleDateFormat.parse(inputDate);

        }
        catch(ParseException e){
            simpleDateFormat= new SimpleDateFormat("dd-MM-yyyy");
            simpleDateFormat.setLenient(false);
            try {
                Date javaDate = simpleDateFormat.parse(inputDate);

            }
            catch(ParseException e2) {
                return "Please, use MM/DD/YYYY or MM-DD-YYYY format";
            }
        }
        return "";
    }

}
