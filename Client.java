public class Client implements Comparable<Client>{
    private int ID;
    private int tArrival;
    private int tService;
    private int tFinish;

    public Client(int ID, int tArrival, int tService ){
        this.ID=ID;
        this.tArrival=tArrival;
        this.tService=tService;
    }

    public void determinatetFinish(int waitingPeriod){
        tFinish=tArrival+tService+waitingPeriod;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int gettArrival() {
        return tArrival;
    }

    public void settArrival(int tArrival) {
        this.tArrival = tArrival;
    }

    public int gettService() {
        return tService;
    }

    public void settService(int tService) {
        this.tService = tService;
    }

    public int gettFinish() {
        return tFinish;
    }

    @Override
    public String toString() {
        return "( "+ID +
                ", " + tArrival +
                ", " + tService+ " )";
      }

    @Override
    public int compareTo(Client cl) {
        return this.tArrival-cl.tArrival;
    }
}
