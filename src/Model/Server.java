package Model;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends Thread {
    private final LinkedBlockingQueue<Task> tasks;
    private final AtomicInteger waitingPeriod;
    private final int id;

    public Server(int id) {
        this.tasks = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger();
        this.id = id;
    }

    public synchronized void addTask(Task newTask) {
        if (tasks.offer(newTask)) {
            waitingPeriod.addAndGet(newTask.getServiceTime());
        } else {
            System.out.println("Full linked queue for server " + id);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Task currentTask = tasks.peek();
                if (currentTask != null) {
                    Thread.sleep(500);
                    currentTask.decrementServiceTime();
                    if (currentTask.getServiceTime() == 0) {
                        tasks.poll();
                    }
                    waitingPeriod.decrementAndGet();
                }
            } catch (InterruptedException e) {
                System.out.println("Problem with thread sleeping on server " + id);
                Thread.currentThread().interrupt();
            }
        }
    }

    public Task[] getTasks() {
        return tasks.toArray(new Task[0]);
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public synchronized long getTaskSize() {
        return tasks.size();
    }

    public long getId() {
        return id;
    }
}
