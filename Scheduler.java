import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Scheduler {
    private List<Casa> caseMarcat;
    private int nrMaxCase;
    private int maxTimeService;     //nr maxim pe care o persoana il petrece la casa de marcat
    private int totalTimeService;

    public Scheduler(int maxCase, int maxService){
        caseMarcat= new ArrayList<Casa>(maxCase);
        nrMaxCase=maxCase;
        maxTimeService=maxService;
        int i=0;
        while(i<maxCase){
            Casa c = new Casa();
            caseMarcat.add(c);
            i++;

        }
    }

    public void dispatchClient(Client c)  {
        int minWaitingTime=Integer.MAX_VALUE;
        int casaWithMin=0;
        for(int i=0;i<nrMaxCase;i++) {
            if (caseMarcat.get(i).getWaitingPeriod().get() < minWaitingTime) {
                minWaitingTime = caseMarcat.get(i).getWaitingPeriod().get();
                casaWithMin = i;
            }
        }
        //dupa ce ies din for am nr minim de asteptare al casei unde adaug urmatorul client
        averageTime(minWaitingTime,c);
        if(caseMarcat.get(casaWithMin).getNrClienti()==0) {                //inseamna ca avem casa goala pe care o deschidem si adaug client la ea
            caseMarcat.get(casaWithMin).addClient(c);
            Thread t = new Thread(caseMarcat.get(casaWithMin));
            t.start();
        }else{
            caseMarcat.get(casaWithMin).addClient(c);
        }
   }

    public void averageTime(int timpMin,Client c){
        totalTimeService=totalTimeService+timpMin+c.gettService();
    }

    public int getTotalTimeService() {
        return totalTimeService;
    }

    public String toString(){
        String r="";
        int i=0;
        int j=0;
        while(i<nrMaxCase){
            j=i+1;
            if(this.getCaseMarcat().get(i).getNrClienti()==0)
                r=r+"Coada "+j+": closed"+"\r\n";
            else {
                r = r + "Coada " + j + ": " + this.getCaseMarcat().get(i).getcoadaClienti() + "\r\n";
            }i++;
        }
        return r;
   }
    public List<Casa> getCaseMarcat() {
        return caseMarcat;
    }
    }
