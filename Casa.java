import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Casa implements Runnable {
    private BlockingQueue<Client> coadaClienti;
    private AtomicInteger waitingPeriod;
    private int nrClienti;
    private volatile boolean deschis = false;

    public Casa() {
        coadaClienti = new ArrayBlockingQueue<Client>(1000);
        waitingPeriod = new AtomicInteger(0);
        nrClienti = 0;
    }

    public void addClient(Client client){
        coadaClienti.add(client);
     //   System.out.println("S-a adaugat clientul: " + client);
        int procTime = client.gettService();
        while (procTime > 0) {
            waitingPeriod.incrementAndGet();
            procTime--;
        }
        deschide();
        nrClienti++;
    }

    public void removeClient(Client client){
        coadaClienti.remove(client);
        nrClienti--;
        if(coadaClienti.isEmpty()==true)
            deschis=false;
    }

    @Override
    public void run() {
        while (deschis) {
            Client actual = new Client(101,-1,-1);
            //if(coadaClienti.)
            Iterator iterator = coadaClienti.iterator();                           //pentru primul element, fara sa folosesc taken
            if(iterator.hasNext())
                actual=(Client)iterator.next();
       //     System.out.println("Timpul de servire: "+ actual.gettService()+"  "+actual);
            try {
                        Thread.sleep(1000);
                        waitingPeriod.getAndDecrement();

            }catch (InterruptedException e) {
                    e.printStackTrace();
            }
            actual.settService(actual.gettService()-1);
            if(actual.gettService()==0) {
                removeClient(actual);
            }

        }
    }

    public void inchide(){
        if(coadaClienti.isEmpty()==true)
            deschis=false;
    }
    public void deschide(){
            deschis=true;
    }


    public BlockingQueue<Client> getcoadaClienti() {
        return coadaClienti;
    }

    public void setCoadaClienti(BlockingQueue<Client> coadaClienti) {
        this.coadaClienti = coadaClienti;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public int getNrClienti() {
        return nrClienti;
    }
    /*  public Client[] getClienti(){
        Client[] totiClientii = new Client[];
        int i=0;
        while(i<nrClienti){
            totiClientii[i]=coadaClienti.
        }
            return totiClientii;
    }
*/
}
