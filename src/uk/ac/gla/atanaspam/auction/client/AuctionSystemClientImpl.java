package uk.ac.gla.atanaspam.auction.client;


import uk.ac.gla.atanaspam.auction.interfaces.AuctionSystemClient;
import uk.ac.gla.atanaspam.auction.interfaces.AuctionSystem;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class represents an Auction System client and implements all of its methods.
 * @author atanaspam
 * @created 02/11/2015
 */
public class AuctionSystemClientImpl extends java.rmi.server.UnicastRemoteObject implements AuctionSystemClient {

    private AuctionSystem server;
    private int clientId;

    public AuctionSystemClientImpl(String ip) throws RemoteException{
        try {
            server = (AuctionSystem) Naming.lookup("rmi://"+ip+"/AuctionSystemService");
            //server = (AuctionSystem) Naming.lookup("rmi://localhost/AuctionSystemService");
            clientId = server.register(this);
        }
        catch (MalformedURLException murle) {
            System.out.println("MalformedURLException");
            System.out.println(murle);
        } catch (RemoteException re) {
            System.out.println("RemoteException");
            System.out.println(re);
        } catch (NotBoundException nbe) {
            System.out.println("NotBoundException");
            System.out.println(nbe);
        } catch (java.lang.ArithmeticException ae) {
            System.out.println("java.lang.ArithmeticException");
            System.out.println(ae);
        }

    }

    public AuctionSystem getServer() {
        return server;
    }

    /**
     * This method is called by the Auction System Server in order to deliver a string message to the Client.
     * @param s The string to be delivered.
     * @return True if the message has been delivered.
     * @throws RemoteException
     */
    @Override
    public boolean sendNotification(String s) throws RemoteException {
        System.out.println(s);
        return true;
    }

    /**
     * This method is used by the Auction System Server to check if the client is still active and connected.
     * @return True if active
     * @throws RemoteException if not Active
     */
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

    public int createNewAuction(String name, Date endTime, int startPrice) throws RemoteException {
       int result = server.createNewAuction(name, endTime, startPrice, clientId);
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

    private String getHelp(){
        StringBuilder s = new StringBuilder();
        s.append("auctions                    -to see a list of currently active auctions\n");
        s.append("id                          -to see your client id\n");
        s.append("bid <auction ID> <amount>   -to bid for an auction\n");
        s.append("add <name> <endTime> <amount>      -to add a new auction (endTime format: DD-MMM-YYYY HH:MM:SS)\n");
        s.append("info <auction ID>           -to view more info about a specific auction (includes finished auctions)\n");
        s.append("exit                      -to exit the application\n");
        return s.toString();
    }


    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        AuctionSystemClientImpl client = null;
        if(( args.length == 0 )|| (args[0].equals(""))){
            System.out.println("Please provide the ip address of the server as an argument.");
            System.exit(1);
        }
        try {
            client = new AuctionSystemClientImpl(args[0]);

        } catch (RemoteException e) {
            System.out.println("Unable to connect to server...");
        }
        System.out.println("Available commands: auctions, id, bid <auction ID> <amount>, add <name> <endTime> <amount>, info <auction ID>, exit");
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            String[] command = scanner.nextLine().split(" ");
            if (command[0].equals("logout") || command[0].equals("exit")) {
                scanner.close();
                System.exit(0);
            }
            switch (command[0]) {
                case "help": {
                    System.out.println(client.getHelp());
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
                    } catch (Exception e) {
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
                        String name = command[1];
                        if (name.equals(""))
                                System.out.println("name cannot be empty.");
                        Date endDate = sdf.parse(command[2] + " " + command[3]);
                        int amount = Integer.parseInt(command[4]);
                        int result = client.createNewAuction(name, endDate, amount);
                        if (result == -1) {
                            System.out.println("A problem occurred during identification, try logging in again...");
                        }else if(result == -2){ System.out.println("The begin date cannot be in the past.");}
                        else{ System.out.println("The id of your new auction is: " + result); }
                    }catch(ParseException e){
                        System.out.println("Date must be of format: DD-MMM-YYYY HH:MM:SS. Example: 06-Nov-2015 18:00:00");
                    }catch(RemoteException e){
                        e.printStackTrace();
                    }catch(Exception e){
                        System.out.println("Usage: add <name> <endTime> <amount>");
                    }
                    break;
                }
                case "info":{
                    try {
                        int auctionID = Integer.parseInt(command[1]);
                        System.out.println(client.getAuctionInfo(auctionID));

                    }catch(Exception e){
                        e.printStackTrace();
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
