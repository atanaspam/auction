package uk.ac.gla.atanaspam.auction.server;

import uk.ac.gla.atanaspam.auction.interfaces.AuctionSystemClient;

import java.util.ArrayList;

/**
 * @author atanaspam
 * @version 0.1
 * @created 03/11/2015
 */
public class ClientPool {

    private ArrayList<Client> clients;
    private int nextId;

    public ClientPool() {
        this.clients = new ArrayList<>();
        nextId = 1;
    }

    public void register(int id){
        Client newClient = new Client(id);
        clients.add(newClient);
        nextId = newClient.getId()+1;
    }

    public Client register(AuctionSystemClient client){
        Client newClient = new Client(nextId++, client);
        clients.add(newClient);
        return newClient;
    }
    public Client getClient(int id){
        for (Client c : clients){
            if (c.getId() == id){
                return c;
            }
        }
        return null;
    }
    public boolean isRegistered( int clientId){
        for (Client c : clients){
            if (c.getId() == clientId){
                return true;
            }
        }
        return false;
    }
}
