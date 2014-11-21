
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
import net.tomp2p.futures.FutureCreate;
import net.tomp2p.message.Message;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.builder.PutBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.replication.Replication;
import net.tomp2p.rpc.DirectDataRPC;
import net.tomp2p.storage.Data;
import main.java.pt.ist.gossip.core.Gossip;
import main.java.pt.ist.gossip.messages.*;

import static main.java.pt.ist.p2p.tomp2p.getGossip;



public class tomp2p {

    private static Peer peer1 = null;

    private static User u = new User();

    private static Random rnd = new Random();
    
    private static String USERNAMES = "username";

//    static List<Socket> peers = new ArrayList<Socket>();
    int id;
    
    private static Gossip gossip = new Gossip();
 //   private static Scanner keyboard1;

    public tomp2p() {


    }

    public static Peer PeerBuilder(String port) throws ClassNotFoundException, IOException {

        Bindings b = new Bindings();

        peer1 = new PeerMaker(new Number160(rnd)).setTcpPort(Integer.parseInt(port)).setUdpPort(Integer.parseInt(port)).setBindings(b).makeAndListen();

        InetAddress address = Inet4Address.getByName(getMyIp());

        PeerAddress peerAddress = new PeerAddress(new Number160(1), address, 10001, 10001);

        FutureDiscover future = peer1.discover().setPeerAddress(peerAddress).start();
        future.awaitUninterruptibly();

        FutureBootstrap future1 = peer1.bootstrap().setPeerAddress(peerAddress).start();
        future1.awaitUninterruptibly();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        return peer1;
    }

    public static String getMyIp() {

        String ip = null;

        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (java.net.UnknownHostException e) {
            System.out.println("error: ip not found");
            e.printStackTrace();
        }

        return ip;
    }

    public static void comandLine() {

        Scanner keyboard = new Scanner(System.in);

        try {

            while (true) {


                //System.out.println(peer1.getPeerBean().getPeerMap().getAll());

                System.out.println("operation number:");
                System.out.println("1 - offer an item for sale");
                System.out.println("2 - search for an item to buy");
                System.out.println("3 - bid on an item");
                System.out.println("4 - accept a bid");
                System.out.println("5 - view item details");
                System.out.println("6 - purchase and bidding history");
                System.out.println("0 - exit app");

                int numero = keyboard.nextInt();
                switch (numero) {
                    case 1:
                        offerItem();
                        break;
                    case 2:
                        searchItem();
                        break;
                    case 4:
                        acceptBid();
                        break;
                    case 6:
                        history();
                        break;
                    case 0:
                        peer1.shutdown();
                        return;
                    default:
                        System.out.println("Invalid operation");
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static void offerItem() throws IOException, ClassNotFoundException {

        ItemSimple item = new ItemSimple();
        Scanner keyboard1 = new Scanner(System.in);
        

        System.out.println("Please, enter the name of the product:");
        String itemTitle = keyboard1.nextLine();
        
        System.out.println("Please, enter the description of the product:");
        String itemDescription = keyboard1.nextLine();
        
        item.setName(itemTitle.toLowerCase());
        item.setDescription(itemDescription);
        item.setDealer(u.getUsername());
        
        
        OfferItemServiceDHT.putDatItem(peer1,item);
        

    }

    public static void searchItem() throws IOException, ClassNotFoundException {

       
       List<ItemSimple> items = new ArrayList<ItemSimple>();
       System.out.println("String to search:");
       Scanner keyboard1 = new Scanner(System.in);
       String s = keyboard1.nextLine();
       ItemSimple iSimple = null;
       String[] choice = s.split("[ ]");
       List<String> myOperators = new ArrayList<String>();
       List<String> myOperands = new ArrayList<String>();

       
      
       for(String st : choice){
            if(st.toLowerCase().equals("and") || st.toLowerCase().equals("or") || st.toLowerCase().equals("not"))
                myOperators.add(st.toLowerCase());
            else
                myOperands.add(st);
            
          
        }
         
            
            try{
                
                items = SearchServiceDHT.booleanSearch(peer1, myOperators, myOperands);
             
            }catch(Exception e){
                 System.out.println(e.getMessage());
            }
            
             for(ItemSimple item : items)
                 System.out.println("Name: "+item.getName() + " Dealer: "+item.getDealer());
             
             SearchServiceDHT.clearMySearch();
       
       
       
               
    }

    public static void bidOnItem() throws IOException, ClassNotFoundException {
        System.out.println("not yet done");
    }

    public static void acceptBid() {

        System.out.println("not yet done");
    }

    public static void itemDetails() {
        System.out.println("not yet done");
    }

    public static void history() {
        System.out.println("not yet done");
    }


    public static boolean passVerifier(Peer peer1) throws ClassNotFoundException, IOException {

        User user;
        System.out.println("Do you have an account?");
        Scanner keyboard1 = new Scanner(System.in);
        String s = keyboard1.nextLine();

        if (s.equals("yes")) {
            
            System.out.println("user:");
            Scanner keyboard2 = new Scanner(System.in);
            String s2 = keyboard1.nextLine();
            String myHash = s2 + USERNAMES;
            FutureDHT futureDHT = peer1.get(Number160.createHash(myHash)).start();
            futureDHT.awaitUninterruptibly();
            
            if (futureDHT.isSuccess()) {
                user = (User) futureDHT.getData().getObject();
                if (user.getUsername().equals("admin")) {
                    System.out.println("you are logged in as admin");
                    getGossip().init(1, 1);
                    return true;
                } else {
                    System.out.println("******** WELCOME TO TOMP2P AUCTIONS ********");
                    System.out.println("you are logged in as " + user.getUsername());
                    getGossip().init(1, 0);
                    return true;
                }

            } else {
                System.out.println("User not found");
                register();
                return true;
            }
        } else register();

        return false;
    }


    public static void register() throws IOException {

        System.out.println("Register Menu");

        while (true) {

            System.out.println("Username:");

            Scanner keyboard1 = new Scanner(System.in);
            String s = keyboard1.nextLine();
            String userKey = s + USERNAMES;
            FutureDHT futureDHT = peer1.get(Number160.createHash(userKey)).start();
            futureDHT.awaitUninterruptibly();

            if (futureDHT.isSuccess()) {
                System.out.println("user already exists. Please choose another username");

            } else {
                u.setUsername(s);
                peer1.put(Number160.createHash(userKey)).setData(new Data(u)).start().awaitUninterruptibly();
                System.out.println("you are logged in as " + u.getUsername());
                break;
            }
        }

    }

    public static void registerAdmin() throws IOException {
        User adminUser = new User();
        String admin = "admin";
        adminUser.setUsername(admin);
        peer1.put(Number160.createHash(admin)).setData(new Data(adminUser)).start().awaitUninterruptibly();
    }

    
    public static Gossip getGossip() {
        return gossip;
    }

    public void sendMessages(MessageType mType) throws IOException {

        Random random = new Random();

        Message msg;
   //     msg = getGossip().getMessage(mType);

/*        FutureDHT futureDHT = peer1.send(Number160.createHash(random.nextInt())).setObject(msg).start();
        futureDHT.awaitUninterruptibly(); */

    }

    
    
  public static void main(String[] args) throws ClassNotFoundException, IOException {


      Peer p = tomp2p.PeerBuilder(args[0]);

      registerAdmin();
      passVerifier(p);

      //new sendThread(tom);
      tomp2p.comandLine();

    }
}



class sendThread extends Thread {
    tomp2p sv;

    public sendThread(tomp2p sv){
        this.sv = sv;
    }

    @Override
    public void run() {
        try{
            while (true){
                Thread.sleep(5);
                sv.sendMessages(MessageType.NODES_SUM);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}




