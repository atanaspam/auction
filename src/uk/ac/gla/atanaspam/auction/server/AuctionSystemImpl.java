package uk.ac.gla.atanaspam.auction.server;

import uk.ac.gla.atanaspam.auction.interfaces.AuctionSystem;
import uk.ac.gla.atanaspam.auction.interfaces.AuctionSystemClient;

import java.io.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/** This class stores the top-level implementation of the AuctionSystemServer
 * @author atanaspam
 * @created 02/11/2015
 */
public class AuctionSystemImpl extends java.rmi.server.UnicastRemoteObject implements AuctionSystem {

    private AuctionPool auctionPool;
    ClientPool clientPool;
    SimpleDateFormat sdf;

    public AuctionSystemImpl() throws RemoteException {
        auctionPool = new AuctionPool();
        clientPool = new ClientPool();
        sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss",Locale.ENGLISH);
    }

    /**
     * When a client wants to bid on an auction it invokes this method
     * @param auctionId The ID of the acution
     * @param bidAmount The new price
     * @param clientId The ID of the client that wants to bid
     * @return 0 if bid is successful -1 if auction has already ended, -2 if the price provided is lower
     * -3 if the auction doesnt exist -4 if the user is not registered
     * @throws RemoteException
     */
    @Override
    public int bid(int auctionId, int bidAmount, int clientId) throws RemoteException {
        int result = -4;
        if (clientPool.isRegistered(clientId)) {
            Client currentClient = clientPool.getClient(clientId);
            Auction target = auctionPool.getAuction(auctionId);
            if (target != null) {
                result = target.bid(bidAmount, currentClient);
            }else{
                result = -3;
            }
        }
        return result;
    }

    /**
     * This method is invoked when a new auction is to be added.
     * @param name The name of the auction.
     * @param endTime The endTime of the auction.
     * @param startPrice The starting price of the auction.
     * @param clientId The ID of the owner.
     * @return a positive Auction ID, -1 if the client is not authenticated, -2 if the startDate is already in the past.
     * @throws RemoteException
     */
    @Override
    public int createNewAuction(String name, Date endTime, int startPrice, int clientId) throws RemoteException {
        if (endTime.compareTo(new Date())<0)
            return -2;
        else {
            Client c = clientPool.getClient(clientId);
            if (c == null) return -1;
            int id = auctionPool.addAuction(name, endTime, startPrice, c);
            return id;
        }
    }

    /**
     * Returns the ID's of currently active auctions.
     * @param clientId The ID of the client requesting (To make sure the client has registered)
     * @return an arrayList of Auction ID's or null if the client is not authenticated
     * @throws RemoteException
     */
    @Override
    public ArrayList<Integer> getAuctionList(int clientId) throws RemoteException {
        if (clientPool.isRegistered(clientId)) {
            return auctionPool.getAuctionIDs();
        }else{
            return null;
        }
    }

    /**
     * This method is invoked each time a client is initialized and registers the client instance for callback.
     * @param client The client instance.
     * @return the ID that the server associates with this client.
     * @throws RemoteException
     */
    @Override
    public int register(AuctionSystemClient client) throws RemoteException {
        Client newClient = clientPool.register(client);
        return newClient.getId();
    }

    /**
     * This method allows a client that has disconnected for some reason to re-associate itself -
     * with an already registered client (Please note no checks are made if this really is the same client)
     * @param id The ID the client wants to be associated with.
     * @param client The client instance
     * @return whether the request has been approved
     * @throws RemoteException
     */
    @Override
    public boolean login(int id, AuctionSystemClient client) throws RemoteException {
        if (clientPool.isRegistered(id)) {
            clientPool.getClient(id).setClient(client);
            return true;
        }
        else
            return false;
    }

    /**
     * This method shows usefull information for a specific auction.
     * @param auctionId The auction we are interested in.
     * @return A String with the auction data or an error message.
     * @throws RemoteException
     */
    @Override
    public String getAuctionInfo(int auctionId) throws RemoteException {
        Auction a = auctionPool.getFromAllAuctions(auctionId);
        if (a == null){
            return "Unable to find auction...";
        }else{
            try {
                return String.format("Auction: %d, Name: %s, Current price: %d, Owner: %d, Current winner: %d, End time: %s",
                        a.getId(), a.getName(), a.getCurrentPrice(), a.getOwner().getId(), a.getWinner().getId(), sdf.format(a.getEndtime()));
            }catch (NullPointerException e){
                return String.format("Auction: %d,Name: %s, Current price: %d, Owner: %d, Current winner: %s, End time: %s",
                        a.getId(), a.getName(), a.getCurrentPrice(), a.getOwner().getId(), "no winner yet", sdf.format(a.getEndtime()));

            }
        }
    }

    /**
     * This method is used to write the currently active auctions to a file.
     * @return True of the write is successfull, False otherwise.
     */
    public boolean writeAuctionsToFile(){
        if(auctionPool.writeToFile("auctions.txt"))
            return true;
        else
            return false;
    }

    /**
     * This method is used to read stored auctions from a file.
     * @return True of the read is successfull, False otherwise.
     */
    public boolean readAuctionsFromFile(String filename) throws IOException{
        ObjectInputStream oin;
        try {
            oin = new ObjectInputStream(
                    new FileInputStream(filename));
            Object ob = oin.readObject();
            ArrayList<?> list = (ArrayList<?>) ob;
            for (Object o : list){
                if (o instanceof Auction){
                    Auction a = (Auction) o;
                    if(a.isFinished()){
                        auctionPool.addFinished(a);
                    }else {
                        auctionPool.addAuction(a);
                    }
                    System.out.println("Imported: "+ a);
                }
            }
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
