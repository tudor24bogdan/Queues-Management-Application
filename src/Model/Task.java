package Model;

public class Task {
    private final int ID;
    private final int arrivalTime;
    private int serviceTime;

    public Task(int id, int atime, int stime) {
        this.ID = id;
        this.arrivalTime = atime;
        this.serviceTime = stime;
    }

    public synchronized int getServiceTime() {
        return serviceTime;
    }

    public void decrementServiceTime() {
        serviceTime -= 1;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public String toString() {
        return "(" + ID + "," + arrivalTime + "," + serviceTime + ")";
    }

    public int getID() {
        return ID;
    }
}
