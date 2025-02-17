package BusinessLogic;

import Model.Task;
import Model.Server;

import java.util.LinkedList;

public class ConcreteStrategyTime implements Strategy {

    public Server checkMinOrBigger(LinkedList<Server> servers, int minWaitingPeriod) {
        Server suitableServer = null;
        int minAlternateWaitingPeriod = Integer.MAX_VALUE;

        for (Server server : servers) {
            int serverWaitingPeriod = server.getWaitingPeriod().intValue();
            long serverTaskSize = server.getTaskSize();

            if (serverWaitingPeriod == minWaitingPeriod && serverTaskSize < Scheduler.getMaxTasksPerServer()) {
                return server;
            }

            if (serverTaskSize < Scheduler.getMaxTasksPerServer() && serverWaitingPeriod < minAlternateWaitingPeriod) {
                minAlternateWaitingPeriod = serverWaitingPeriod;
                suitableServer = server;
            }
        }

        return suitableServer;
    }

    @Override
    public boolean addTask(LinkedList<Server> servers, Task task) {
        int numberOfServers = Scheduler.getMaxNoServers();
        if (numberOfServers == 0) {
            return false;
        }

        Server chosenServer = servers.get(0);
        int minWaitingPeriod = chosenServer.getWaitingPeriod().intValue();

        for (Server server : servers) {
            int serverWaitingPeriod = server.getWaitingPeriod().intValue();
            if (serverWaitingPeriod < minWaitingPeriod) {
                minWaitingPeriod = serverWaitingPeriod;
                chosenServer = server;
            }
        }

        if (chosenServer.getTaskSize() < Scheduler.getMaxTasksPerServer()) {
            chosenServer.addTask(task);
            SimulationManager.addAverageWaitingTime(chosenServer.getWaitingPeriod().intValue());
        } else {
            Server alternateServer = checkMinOrBigger(servers, minWaitingPeriod);
            if (alternateServer == null) {
                return false;
            }
            alternateServer.addTask(task);
            SimulationManager.addAverageWaitingTime(alternateServer.getWaitingPeriod().intValue());
        }
        return true;
    }
}
