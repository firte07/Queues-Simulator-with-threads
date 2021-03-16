import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.sort;

public class SimulationManager implements Runnable {
    public int timerLimit;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int minServiceTime;
    public int maxServiceTime;
    public int nrCase;
    public int nrClienti;           
    public String rezultat;
    private Scheduler scheduler;
    private List<Client> clienti;
    File myObjCreate =null;
    FileWriter myObjWrite = null;
    private String numeIn;
    private String numeOut;
    private  float averageTime;

    public SimulationManager(String in, String out){
        numeIn=in;
        numeOut=out;
        clienti= Collections.synchronizedList(new ArrayList<Client>());
        read(numeIn);
        scheduler = new Scheduler(nrCase,maxServiceTime);
        generateRandomClients();
        sort(clienti);
        try {
            myObjCreate = new File(numeOut);
            myObjCreate.createNewFile();
            myObjWrite = new FileWriter(numeOut);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Nu pot creea la write");
        }
    }

    public void generateRandomClients(){
        Random r = new Random();
        int i=0;
        while(i<nrClienti){
            int tArrival=r.nextInt(Integer.MAX_VALUE)%(maxArrivalTime+1);
            if(tArrival<minArrivalTime) tArrival+=minArrivalTime;
            int tService=r.nextInt(Integer.MAX_VALUE)%(maxServiceTime+1);
            if(tService<minServiceTime) tService+=minServiceTime;
            Client c = new Client(i,tArrival,tService);
            clienti.add(c);
            i++;
        }
    }

    @Override
    public void run() {
        int conditieClienti=1;
        AtomicInteger currentTime =  new AtomicInteger(0);
        while (currentTime.get()<timerLimit) {
            if(conditieClienti==1){                                            //pt a nu primi eroare de indexoutofbound
            while((conditieClienti==1)&&(clienti.get(0).gettArrival()==currentTime.get())){             //daca timpul petrecut in magazin al primului client este egal cu timpul curent
                scheduler.dispatchClient(clienti.get(0));
                clienti.remove(0);
                if(clienti.isEmpty()==true) {
                    conditieClienti = 0;
                }
            }
            } rezultat="Time: "+currentTime+"\r\nWaiting clients: "+ clienti+"\r\n"+scheduler.toString()+"\r\n";
            scrie();
            System.out.println(rezultat); rezultat="";
            if(clienti.isEmpty() && scheduler.getCaseMarcat().get(1).getNrClienti()==0 && scheduler.getCaseMarcat().get(0).getNrClienti()==0)
                break;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTime.incrementAndGet();
        }
        calculeazaAverageTime();
        String rez=getAverageTime();
        try {
            myObjWrite.write(rez);
            myObjWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAverageTime() {
        return "Average waiting time: "+averageTime;
    }

    public void calculeazaAverageTime(){
        int totalTime = scheduler.getTotalTimeService();
        System.out.println("total time: " + totalTime);
        averageTime=(float)totalTime/nrClienti;
        System.out.println("average waiting time: " + averageTime);
    }

    public void scrie(){
        try {
            myObjWrite.write(rezultat);
            myObjWrite.write("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read(String numeIn){
        try {
            File myObj = new File(numeIn);
            Scanner myReader = new Scanner(myObj);
            nrClienti=myReader.nextInt();
            nrCase=myReader.nextInt();
            timerLimit=myReader.nextInt();
            String s= myReader.next();
            String[] ss=s.split("\\,");
            minArrivalTime=Integer.parseInt(ss[0]);
            maxArrivalTime=Integer.parseInt(ss[1]);
            String s1= myReader.next();
            String[] sss=s1.split("\\,");
            minServiceTime=Integer.parseInt(sss[0]);
            maxServiceTime=Integer.parseInt(sss[1]);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "SimulationManager{" +
                "timerLimit=" + timerLimit +
                ", minArrivalTime=" + minArrivalTime +
                ", maxArrivalTime=" + maxArrivalTime +
                ", minProTime=" + minServiceTime +
                ", maxProTime=" + maxServiceTime +
                ", nrCase=" + nrCase +
                ", nrClienti=" + nrClienti +
                '}';
    }

    public static void main(String[] args){
        SimulationManager sm = new SimulationManager(args[0],args[1]);
        Thread t = new Thread(sm);
        t.start();
    }
}
