package BusinessLogic;

import GUI.SimulationFrame;
import Model.Task;
import Model.Server;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

public class SimulationManager implements Runnable {
    public static int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int maxArrivalTime;
    public int minArrivalTime;
    public int numberOfServers;
    public int numberOfClients;
   // public int maxTasksServer;
    public SelectionPolicy selectionPolicy;

    private static double averageWaitingTime = 0.0;
    private static double averageServiceTime = 0.0;
    private double peakHour = 0.0;
    private int timePeakHour = 0;
    private final Scheduler scheduler;
    private final SimulationFrame frame;
    private final LinkedList<Task> generatedTasks = new LinkedList<>();
    private FileWriter myWriter;

    public SimulationManager(SimulationFrame frames) {
        frame = frames;
        numberOfClients = Integer.parseInt(frame.getClients());
        numberOfServers = Integer.parseInt(frame.getQueues());
        System.out.println(numberOfServers);
        timeLimit = Integer.parseInt(frame.getSimulationInterval());
        maxArrivalTime = Integer.parseInt(frame.getMaxArrivalTime());
        minArrivalTime = Integer.parseInt(frame.getMinArrivalTime());
        maxProcessingTime = Integer.parseInt(frame.getMaxServiceTime());
        minProcessingTime = Integer.parseInt(frame.getMinServiceTime());
        scheduler = new Scheduler(numberOfServers, 1000);

        selectionPolicy = frame.getSelectionPolicy();
        scheduler.changeStrategy(selectionPolicy);

        generateNRandomClientTasks();
        try {
            myWriter = new FileWriter("log.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ByArrivalTime implements Comparator<Task> {
        @Override
        public int compare(Task o1, Task o2) {
            return o1.getArrivalTime() - o2.getArrivalTime();
        }
    }

    private void generateNRandomClientTasks() {
        int index = 0;
        Random rand = new Random();

        while (index != numberOfClients) {
            int atime = rand.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int time = rand.nextInt(maxProcessingTime - minProcessingTime + 1) + minProcessingTime;
            generatedTasks.add(new Task(index + 1, atime, time));
            index++;
        }
        Comparator<Task> byATime = new ByArrivalTime();
        generatedTasks.sort(byATime);
        for (int i = 0; i < numberOfClients; i++) {
            System.out.println(generatedTasks.get(i));
        }
    }

    public static void addAverageWaitingTime(int number) {
        averageWaitingTime += number;
    }

    public static void addAverageServiceTime(int number) {
        averageServiceTime += number;
    }

    public void peakHour(LinkedList<Server> serverList, int time) {
        int waitingTime = 0;
        for (Server queue : serverList) {
            waitingTime += queue.getWaitingPeriod().intValue();
        }
        if (waitingTime / (double) numberOfServers - peakHour > 1e-3) {
            peakHour = waitingTime / (double) numberOfServers;
            timePeakHour = time;
        }
    }

    public void printResult(LinkedList<Server> serverList, int time) {
        try {
            myWriter.write("Time " + time + "\n");
            myWriter.write("Waiting clients: ");
            StringBuilder waitingQueue = new StringBuilder();
            for (Task remainedTasks : generatedTasks) {
                myWriter.write(remainedTasks + ";");
                waitingQueue.append(remainedTasks).append("; ");
            }
            frame.setWaitingQueue(waitingQueue.toString());
            StringBuilder queues = new StringBuilder();
            myWriter.write("\n");
            for (Server queue : serverList) {
                myWriter.write("Queue " + queue.getId() + ": ");
                queues.append("Queue ").append(queue.getId()).append(": ");
                Task[] clients = queue.getTasks();
                if (clients.length == 0) {
                    myWriter.write("closed");
                    queues.append("closed");
                }
                for (Task task : clients) {
                    myWriter.write(task + "; ");
                    queues.append(task).append("; ");
                }
                myWriter.write("\n");
                queues.append("\n");
            }
            frame.setQueueEvolution(queues.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyEndOfSimulation(LinkedList<Server> serverList) {
        if (!generatedTasks.isEmpty())
            return false;
        else
            for (Server queue : serverList) {
                if (queue.getTaskSize() != 0)
                    return false;
            }
        return true;
    }

    @Override
    public void run() {
        int currentTime = 0;
        LinkedList<Server> serverList = scheduler.getServers();
        while (!verifyEndOfSimulation(serverList)) {
            frame.setTime(currentTime + "");
            Task client = generatedTasks.peekFirst();
            while (client != null && client.getArrivalTime() <= currentTime) {
                if (scheduler.dispatchTask(client)) {
                    addAverageServiceTime(client.getServiceTime());
                    generatedTasks.removeFirst();
                } else
                    break;
                client = generatedTasks.peekFirst();
            }
            peakHour(serverList, currentTime);
            printResult(serverList, currentTime);
            currentTime++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Problem with main thread sleeping");
            }
        }
        printResult(serverList, currentTime);
        frame.setAverageWaitingTimeJLabel(Math.round(averageWaitingTime / numberOfClients * 100.0) / 100.0 + "");
        frame.setAverageServiceTimeJLabel(Math.round(averageServiceTime / numberOfClients * 100.0) / 100.0 + "");
        frame.setPeakHourLabel(timePeakHour + "");
        for (Server queue : serverList)
            queue.stop();
        try {
            myWriter.write("Average waiting time: " + Math.round(averageWaitingTime / numberOfClients * 100.0) / 100.0 + "\nAverage service time: " + Math.round(averageServiceTime / numberOfClients * 100.0) / 100.0 + "\nPeak Hour at the time: " + timePeakHour);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}