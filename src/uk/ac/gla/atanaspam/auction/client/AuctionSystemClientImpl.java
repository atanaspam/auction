package uk.ac.gla.atanaspam.auction.client;


import uk.ac.gla.atanaspam.auction.server.AuctionSystem;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author atanaspam
 * @version 0.1
 * @created 02/11/2015
 */
public class AuctionSystemClientImpl extends java.rmi.server.UnicastRemoteObject implements AuctionSystemClient{

    private AuctionSystem server;

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    private int clientId;

    public AuctionSystemClientImpl() throws RemoteException{
        try {
            server = (AuctionSystem) Naming.lookup("rmi://130.209.245.90/AuctionSystemService");
            //server = (AuctionSystem) Naming.lookup("rmi://localhost/AuctionSystemService");
            clientId = server.register(this);
        }
        // Catch the exceptions that may occur – bad URL, Remote exception
        // Not bound exception or the arithmetic exception that may occur in
        // one of the methods creates an arithmetic error (e.g. divide by zero)
        catch (MalformedURLException murle) {
            System.out.println("MalformedURLException");
            System.out.println(murle);
        }
        catch (RemoteException re) {
            System.out.println("RemoteException");
            System.out.println(re);
        }
        catch (NotBoundException nbe) {
            System.out.println("NotBoundException");
            System.out.println(nbe);
        }
        catch (java.lang.ArithmeticException ae) {
            System.out.println("java.lang.ArithmeticException");
            System.out.println(ae);
        }

    }

    public AuctionSystem getServer() {
        return server;
    }

    public int getClientId() {
        return clientId;
    }

    @Override
    public boolean sendNotification(String s) throws RemoteException {
        System.out.println(s);
        return true;
    }

    @Override
    public boolean ping() throws RemoteException {
        return true;
    }

    public int bid(int auctionId, int price){
        int result = -4;
        try {
            result = server.bid(auctionId, price, clientId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int createNewAuction(Date endTime, int startPrice) throws RemoteException {
       int result = server.createNewAuction(endTime, startPrice, clientId);
        return result;
    }

    public ArrayList<Integer> getAuctions() throws RemoteException {
        return server.getAuctionList(clientId);
    }

    public String getAuctionInfo(int auctionId){
        String data = "Unable to reach server...";
        try {
            data = server.getAuctionInfo(auctionId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String parseCode(int code){
        switch (code){
            case -4:{
                return"Error during authentication.";
            }
            case -3:{
                return "Auction not found or finished.";
            }
            case -2:{
                return "Your price is lower than the auction's current price.";
            }
            case -1:{
                return "Auction has already finished.";
            }
            case 0:{
                return "Bid successfull.";
            }
            default:
                return "";
        }
    }

    public boolean login(int id){
        boolean success = false;
        try{
            success = server.login(id, this);
            if (success){
                this.clientId = id;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return success;
    }


    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        AuctionSystemClientImpl client = null;
        try {
            client = new AuctionSystemClientImpl();

        } catch (RemoteException e) {
            System.out.println("Unable to connect to server...");
        }
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            String[] command = scanner.nextLine().split(" ");
            switch (command[0]) {
                case "help": {
                    System.out.println("auctions                    -to see a list of currently active auctions");
                    System.out.println("id                          -to see your client id");
                    System.out.println("bid <auction ID> <amount>   -to vid for an auction");
                    System.out.println("add <endTime> <amount>      -to add a new auction (endTime format: DD-MMM-YYYY HH:MM:SS)");
                    System.out.println("info <auction ID>           -to view more info about a specific auction (includes finished auctions)");
                    break;
                }
                case "id": {
                    System.out.println("The current clientID is : " + client.clientId);
                    break;
                }
                case "bid": {
                    try {
                        int auctionID = Integer.parseInt(command[1]);
                        int amount = Integer.parseInt(command[2]);
                        int result = client.bid(auctionID, amount);
                        System.out.println(client.parseCode(result));
                    } catch (NumberFormatException e) {
                        System.out.println("Usage: bid <auction ID> <amount>");
                    }
                    break;
                }
                case "auctions": {
                    try {
                        System.out.println(client.getAuctions());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "add":{
                    try {
                        Date endDate = sdf.parse(command[1] + " " + command[2]);
                        int amount = Integer.parseInt(command[3]);
                        int result = client.createNewAuction(endDate, amount);
                        if (result == -1) {
                            System.out.println("A problem occured during identification, try logging in again...");
                        }else{
                            System.out.println("The id of your new auction is: " + result);
                        }
                    }catch(ParseException e){
                        System.out.println("Date must be of format: DD-MMM-YYYY HH:MM:SS. Example: 06 Nov 2015 18:00:00");
                    }catch(NumberFormatException e){
                        System.out.println("Usage: add <endTime> <amount>");
                    }catch(RemoteException e){
                        e.printStackTrace();
                    }
                    break;
                }
                case "info":{
                    try {
                        int auctionID = Integer.parseInt(command[1]);
                        System.out.println(client.getAuctionInfo(auctionID));

                    }catch(NumberFormatException e){
                        System.out.println("Usage: info <auction ID>");
                    }
                    break;
                }
                case "login":{
                    try {
                        int id = Integer.parseInt(command[1]);
                        boolean success = client.login(id);
                        if (success)
                            System.out.println("You are now logged in as "+ id);
                        else
                            System.out.println("Operation unsuccessful.");
                    }catch(Exception e){
                        System.out.println("Usage: info <auction ID>");
                    }
                    break;
                }
                default: {
                    System.out.println("Type help to see the available commands.");
                    break;
                }
            }

        }
    }


}
