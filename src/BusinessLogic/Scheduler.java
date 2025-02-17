package BusinessLogic;

import Model.Task;
import Model.Server;

import java.util.LinkedList;

public class Scheduler {
    private final LinkedList<Server> servers;
    private static int maxNoServers;
    private static int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        Scheduler.maxNoServers = maxNoServers;
        Scheduler.maxTasksPerServer = maxTasksPerServer;
        this.servers = new LinkedList<>();
        for (int i = 0; i < maxNoServers; i++) {
            servers.add(new Server(i + 1));
            servers.get(i).start();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        switch (policy) {
            case SHORTEST_QUEUE:
                strategy = new ConcreteStrategyQueue();
                break;
            case SHORTEST_TIME:
                strategy = new ConcreteStrategyTime();
                break;
        }
    }

    public boolean dispatchTask(Task t) {
        return strategy.addTask(servers, t);
    }

    public static int getMaxTasksPerServer() {
        return maxTasksPerServer;
    }

    public LinkedList<Server> getServers() {
        return servers;
    }

    public static int getMaxNoServers() {
        return maxNoServers;
    }



}
