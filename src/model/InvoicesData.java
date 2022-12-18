package model;

import java.util.ArrayList;
import java.util.Iterator;

public class InvoicesData {
    private ArrayList<InvoiceHeader> invoiceHeaders;
    private ArrayList<InvoiceLine> invoiceLines;

    private String invoiceHeaderFilename;
    private String invoiceLineFilename;

    public InvoicesData(){
        this.setInvoiceHeaders(new ArrayList<InvoiceHeader>());
        this.setInvoiceLines(new ArrayList<InvoiceLine>());
    }
    public InvoicesData(String invoiceHeaderFilename, String invoiceLineFilename){
        this.setInvoiceHeaderFilename(invoiceHeaderFilename);
        this.setInvoiceLineFilename(invoiceLineFilename);
        this.loadInvoiceData();

    }


    public ArrayList<InvoiceHeader> getInvoiceHeaders() {
         return invoiceHeaders;
    }

    public void setInvoiceHeaders(ArrayList<InvoiceHeader> invoiceHeaders) {
        this.invoiceHeaders = invoiceHeaders;
    }

    public ArrayList<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(ArrayList<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public String getInvoiceHeaderFilename() {
        return invoiceHeaderFilename;
    }

    public void setInvoiceHeaderFilename(String invoiceHeaderFilename) {
        this.invoiceHeaderFilename = invoiceHeaderFilename;
    }

    public String getInvoiceLineFilename() {
        return invoiceLineFilename;
    }

    public void setInvoiceLineFilename(String invoiceLineFilename) {
        this.invoiceLineFilename = invoiceLineFilename;
    }

    public void loadInvoiceData(){
        this.invoiceHeaders= FileOperations.readInvoiceHeaderFile(this.getInvoiceHeaderFilename());
        this.invoiceLines= FileOperations.readInvoiceLineFile(this.getInvoiceLineFilename(), this.getInvoiceHeaders());
    }

    public String loadInvoiceData(String newHeaderFile, String newLineFile){
        int numberOfColumns1= FileOperations.getNumberofFileColumns(newHeaderFile);
        int numberOfColumns2= FileOperations.getNumberofFileColumns(newLineFile);
        if((numberOfColumns1==3)&&(numberOfColumns2==4)){
            String checkHeader= FileOperations.checkInvoiceHeaderFileFormat(newHeaderFile);
            String checkLines= FileOperations.checkInvoiceLineFileFormat(newLineFile);
            if(checkHeader.equals("")&&checkLines.equals("")) {
                this.setInvoiceHeaderFilename(newHeaderFile);
                this.setInvoiceLineFilename(newLineFile);
                this.loadInvoiceData();
                return "";
            }else return checkHeader+"\n"+checkLines;
        }else if((numberOfColumns2==3)&&(numberOfColumns1==4)){
            String checkHeader= FileOperations.checkInvoiceHeaderFileFormat(newLineFile);
            String checkLines= FileOperations.checkInvoiceLineFileFormat(newHeaderFile);
            if(checkHeader.equals("")&&checkLines.equals("")) {
                this.setInvoiceHeaderFilename(newHeaderFile);
                this.setInvoiceLineFilename(newLineFile);
                this.loadInvoiceData();
                return "";
            }else return checkHeader+"\n"+checkLines;
        }else if(numberOfColumns2==-3){ //Don't save an invoice with no items
            this.setInvoiceHeaderFilename(newHeaderFile);
            this.setInvoiceLineFilename(newLineFile);
            return "";
        }

        return "The files number of columns are not as expected";
    }

    public void saveInvoiceData(){
        FileOperations.writeInvoiceDate(this.getInvoiceHeaderFilename(), this.getInvoiceLineFilename(), this.getInvoiceHeaders());
    }

    public void saveInvoiceHeaders(String invoiceData,String invoiceHeaderFilename, String invoiceLineFilename){
        FileOperations.readInvoiceHeaderFileFromStringtoFile(invoiceData,invoiceHeaderFilename,invoiceLineFilename,getInvoiceLineFilename());
        this.loadInvoiceData(invoiceHeaderFilename,invoiceLineFilename);
    }

    public void saveInvoiceData(String newHeaderFile, String newLineFile,boolean setCurrentSource){
        if(setCurrentSource){
            this.setInvoiceHeaderFilename(newHeaderFile);
            this.setInvoiceLineFilename(newLineFile);
            FileOperations.writeInvoiceDate(this.getInvoiceHeaderFilename(), this.getInvoiceLineFilename(), this.getInvoiceHeaders());
        }
        else
            FileOperations.writeInvoiceDate(newHeaderFile, newLineFile, this.getInvoiceHeaders());
    }

    public void createNewInvoice(InvoiceHeader invoiceHeader){
        this.invoiceHeaders.add(invoiceHeader);
        this.saveInvoiceData();
    }

    public InvoiceHeader getInvoice(int invoiceNumber){
        Iterator itr = this.getInvoiceHeaders().iterator();

        // Holds true till there is single element
        // remaining in the object
        while (itr.hasNext()) {
            InvoiceHeader ih= (InvoiceHeader) itr.next();
            if (ih.getInvoiceNum() == invoiceNumber) {
                return ih;
            }
        }
        return null;
    }
    public void deleteInvoice(int invoiceNumber){
        Iterator itr = this.getInvoiceHeaders().iterator();

        // Holds true till there is single element
        // remaining in the object
        while (itr.hasNext()) {
            InvoiceHeader ih= (InvoiceHeader) itr.next();
            if (ih.getInvoiceNum() == invoiceNumber) {
                itr.remove();
                break;
            }
        }
        this.saveInvoiceData();
    }

    public void updateInvoice(InvoiceHeader invoiceHeader){
        boolean added=false;
        for (int i=0 ; i< this.getInvoiceHeaders().size();i++) {
            if (this.getInvoiceHeaders().get(i).getInvoiceNum() == invoiceHeader.getInvoiceNum()) {
                this.getInvoiceHeaders().remove(i);
                this.getInvoiceHeaders().add(i,invoiceHeader);
                added=true;
                break;}
            }
        if(!added)
            this.getInvoiceHeaders().add(invoiceHeader);
        this.saveInvoiceData();
    }

    public int getNextInvoiceNum(){
        int max=-1;
        Iterator itr = this.getInvoiceHeaders().iterator();

        // Holds true till there is single element
        // remaining in the object
        while (itr.hasNext()) {
            InvoiceHeader ih= (InvoiceHeader) itr.next();
            if (ih.getInvoiceNum() > max) {
                max=ih.getInvoiceNum();
            }
        }
        return max+1;
    }




}
