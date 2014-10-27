package main.java.pt.ist.p2p;


import java.io.IOException;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class ExampleSimple {

    final private Peer peer;

    public ExampleSimple(int peerId) throws Exception {
        peer = new PeerMaker(Number160.createHash(peerId)).setPorts(4000 + peerId).makeAndListen();
        FutureBootstrap fb = peer.bootstrap().setBroadcast().setPorts(4001).start();
        fb.awaitUninterruptibly();
        if (fb.getBootstrapTo() != null) {
            peer.discover().setPeerAddress(fb.getBootstrapTo().iterator().next()).start().awaitUninterruptibly();
        }
    }

    public static void main(String[] args) throws NumberFormatException, Exception {
        String[] argumentos = new String[3];
        argumentos[0] = "1";
        argumentos[1] = "test.me";
        argumentos[2] = "192.168.1.1";
        ExampleSimple dns = new ExampleSimple(Integer.parseInt(argumentos[0]));
        if (args.length == 3) {
            dns.store(argumentos[1], argumentos[2]);
        }
        if (args.length == 2) {
            System.out.println("Name:" + argumentos[1] + " IP:" + dns.get(argumentos[1]));
        }
    }

    private String get(String name) throws ClassNotFoundException, IOException {
        FutureDHT futureDHT = peer.get(Number160.createHash(name)).start();
        futureDHT.awaitUninterruptibly();
        if (futureDHT.isSuccess()) {
            return futureDHT.getData().getObject().toString();
        }
        return "not found";
    }

    private void store(String name, String ip) throws IOException {
        peer.put(Number160.createHash(name)).setData(new Data(ip)).start().awaitUninterruptibly();
    }
}
