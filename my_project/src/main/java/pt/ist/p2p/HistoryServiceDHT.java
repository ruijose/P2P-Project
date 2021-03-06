package main.java.pt.ist.p2p;


import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.*;
import java.io.*;

import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.message.Message;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;

public class HistoryServiceDHT {

	private static Number160 PURCHASEDITEMS = Number160.createHash("purchasedItems");
    private static Number160 BIDHISTORY = Number160.createHash("BIDSHISTORY");
    
    public static void getHistory(Peer myPeer, String userName) throws ClassNotFoundException, IOException{
		Number160 locationKey = Number160.createHash(userName);
		List<Bid> listItemsBidded = new ArrayList<Bid>();
		List<Item> listItemsPurchased = new ArrayList<Item>();
		
		FutureDHT futureGetBidded = myPeer.get(locationKey).setDomainKey(BIDHISTORY).setAll().start().awaitUninterruptibly();
		FutureDHT futureGetPurchased = myPeer.get(locationKey).setDomainKey(PURCHASEDITEMS).setAll().start().awaitUninterruptibly();
		
		Iterator<Data> iteratorItemsBidded = futureGetBidded.getDataMap().values().iterator();
		Iterator<Data> iteratorItemsPurchased = futureGetPurchased.getDataMap().values().iterator();

		Object o;
		
		int counterItem = 0;
		while(counterItem < futureGetBidded.getDataMap().size()){         
            o = iteratorItemsBidded.next().getObject();
            if(o instanceof Bid)
            	listItemsBidded.add((Bid) o);
            counterItem++;
        }
        
		counterItem = 0;
		while(counterItem < futureGetPurchased.getDataMap().size()){         
            o = iteratorItemsPurchased.next().getObject();
            if(o instanceof Item)
            	listItemsPurchased.add((Item) o);
            counterItem++;
        }
		
		System.out.println("***Bidded Items***");
        for(Bid bid : listItemsBidded){
        	System.out.println("Nome Item: "+bid.getItemName() +  " | " + "Value: "+bid.getBid());;	
        }
        System.out.println("");
        
        System.out.println("***Purchased Items***");
        for(Item item : listItemsPurchased){
        	System.out.println("Nome Item: "+item.getName() +  " | " + "Value: "+item.getSoldValue());;	
        }
        System.out.println("");
        
    
        
    }
    
    
    
    
    
}
