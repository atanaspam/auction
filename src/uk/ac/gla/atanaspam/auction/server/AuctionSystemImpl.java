package uk.ac.gla.atanaspam.auction.server;

import uk.ac.gla.atanaspam.auction.client.AuctionSystemClient;

import java.io.*;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

/**
 * @author atanaspam
 * @version 0.1
 * @created 02/11/2015
 */
public class AuctionSystemImpl extends java.rmi.server.UnicastRemoteObject implements AuctionSystem {

    private AuctionPool auctionPool;
    ClientPool clientPool;
    SimpleDateFormat sdf;

    public AuctionSystemImpl() throws RemoteException {
        auctionPool = new AuctionPool();
        clientPool = new ClientPool();
    }

    public AuctionSystemImpl(String auction, String client) throws RemoteException {
        sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss",Locale.ENGLISH);
        Scanner sc = null;
        ObjectInputStream oin;

        clientPool = new ClientPool();
        auctionPool = new AuctionPool();
        try {
            sc = new Scanner(new File(client));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (sc.hasNext()){
            String line = sc.nextLine();
            String[] split = line.split(",");
            for (String id : split){
                clientPool.register(new Integer(id));
            }
        }
        sc.close();

        try {
            oin = new ObjectInputStream(
                    new FileInputStream(auction));
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        /*
        try {
            Auction a = new Auction(1, sdf.parse("01 Nov 2015 07:00:00"), sdf.parse("07 Nov 2015 16:27:00"), 100, clientPool.getClient(2), clientPool.getClient(1), false);
            auctionPool.addAuction(a);
            a = new Auction(2, sdf.parse("01 Nov 2015 07:00:00"), sdf.parse("07 Nov 2015 16:27:00"), 50, null, clientPool.getClient(2), false);
            auctionPool.addAuction(a);
            a = new Auction(3, sdf.parse("01 Nov 2015 07:00:00"), sdf.parse("06 Nov 2015 16:30:00"), 50, null, clientPool.getClient(2), false);
            auctionPool.addAuction(a);
            a = new Auction(4, sdf.parse("01 Nov 2015 07:00:00"), sdf.parse("02 Nov 2015 12:00:00"), 10, clientPool.getClient(3), clientPool.getClient(2), true);
            auctionPool.addAuction(a);
            auctionPool.writeToFile("auctions.txt");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */
    }

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

    @Override
    public int createNewAuction(Date endTime, int startPrice, int clientId) throws RemoteException {
        Client c = clientPool.getClient(clientId);
        try {
            if (c == null) throw new NoSuchClientException(clientId);
            int id = auctionPool.addAuction(endTime, startPrice, c);
            return id;
        }
        catch (NoSuchClientException e){
            return -1;
        }
    }

    @Override
    public ArrayList<Integer> getAuctionList(int clientId) throws RemoteException {
        if (clientPool.isRegistered(clientId)) {
            ArrayList<Integer> ids = new ArrayList<>();
            for (Auction a : auctionPool.getAuctions()) {
                ids.add(a.getId());
            }
            return ids;
        }else{
            return null;
        }
    }

    @Override
    public int register(AuctionSystemClient client) throws RemoteException {
        Client newClient = clientPool.register(client);
        return newClient.getId();
    }

    @Override
    public boolean login(int id, AuctionSystemClient client) throws RemoteException {
        if (clientPool.isRegistered(id)) {
            clientPool.getClient(id).setClient(client);
            return true;
        }
        else
            return false;
    }

    @Override
    public String getAuctionInfo(int auctionId) throws RemoteException {
        Auction a = auctionPool.getAllAuctions(auctionId);
        if (a == null){
            return "Unable to find auction...";
        }else{
            return String.format("Auction: %d, Current price: %d, Owner: %d, Current winner: %d, End time: %s",
            a.getId(), a.getCurrentPrice(), a.getOwner().getId(), a.getWinner(), sdf.format(a.getEndtime()));
        }
    }
}
