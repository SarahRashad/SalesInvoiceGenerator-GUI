import controller.Controller;
import model.InvoiceHeader;
import view.MainView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        try {
            //Singleton Controller
            Controller controller = Controller.getInstance();
            boolean errorInLoading= false;
            //In case an error happend in creating the files new files are created with random names
            Random rand = new Random();
            int filenamePart = rand.nextInt();
            String invoiceHeaderFilename = "InvoiceHeader"+filenamePart+".csv";
            String invoiceLineFilename = "InvoiceLine"+filenamePart+".csv";
            String fileChecks="";
            try{
                File fheader = new File("InvoiceHeader.csv");
                if(!fheader.exists() || fheader.isDirectory()) {
                    throw new FileNotFoundException("InvoiceHeader.csv File not Found");
                }
                File fline = new File("InvoiceLine.csv");
                if(!fline.exists() || fline.isDirectory()) {
                    throw new FileNotFoundException("InvoiceHeader.csv File not Found");
                }
                fileChecks=controller.loadInvoiceData("InvoiceHeader.csv", "InvoiceLine.csv");
                if(!fileChecks.trim().equals(""))
                    throw new Exception();
                invoiceHeaderFilename="InvoiceHeader.csv";
                invoiceLineFilename= "InvoiceLine.csv";


            }catch(FileNotFoundException e){
                System.out.println(e.getMessage());
                System.out.println("as an alternative solution the program will two empty files wih the names "+invoiceHeaderFilename+" and "+invoiceLineFilename);
                errorInLoading=true;
            }
            catch(Exception e){
                System.out.println("Wrong File Format \n" +fileChecks+
                        "\nas an alternative solution the program will create two empty files wih the names "+invoiceHeaderFilename+" and "+invoiceLineFilename);
                errorInLoading=true;

            }
            //Creating initial empty invoiceheader and invoiceline files

            if(errorInLoading) {
                File invoiceHeaderFile = new File(invoiceHeaderFilename);
                invoiceHeaderFile.createNewFile();
                invoiceHeaderFilename = invoiceHeaderFile.getAbsolutePath();
                System.out.println(invoiceHeaderFilename);

                File invoicelineFile = new File(invoiceLineFilename);
                invoicelineFile.createNewFile();
                invoiceLineFilename = invoicelineFile.getAbsolutePath();
                System.out.println(invoiceLineFilename);
            }

            controller.loadInvoiceData(invoiceHeaderFilename, invoiceLineFilename);
            MainView mainView = new MainView("Sales Invoice Generator");
            mainView.setVisible(true);

        }catch(Exception e){
            System.out.println("An error occurs while creating new files");
        }
    }

    /**
     * testmain function used to test input file process
     * @param args
     */
    public static void testmain(String[] args) {

        //Singleton Controller
        Controller controller = Controller.getInstance();
        String invoiceHeaderFilename = "InvoiceHeader.csv";
        String invoiceLineFilename = "InvoiceLine.csv";
        String fileChecks = "";
        try {
            File fheader = new File(invoiceHeaderFilename);
            if (!fheader.exists() || fheader.isDirectory()) {
                throw new FileNotFoundException("InvoiceHeader.csv File not Found");
            }
            File fline = new File(invoiceLineFilename);
            if (!fline.exists() || fline.isDirectory()) {
                throw new FileNotFoundException("InvoiceLine.csv File not Found");
            }
            fileChecks = controller.loadInvoiceData(invoiceHeaderFilename, invoiceLineFilename);
            if (!fileChecks.trim().equals(""))
                throw new Exception();

            controller.loadInvoiceData(invoiceHeaderFilename, invoiceLineFilename);
            ArrayList<InvoiceHeader> ihl = controller.getInvoiceHeaders();
            for (InvoiceHeader ih : ihl) {
                System.out.println(ih);
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Wrong File Format \n" + fileChecks);

        }
    }

}
