package BusinessLogic;

import Model.Task;
import Model.Server;

import java.util.LinkedList;
//ssss
public class ConcreteStrategyQueue implements Strategy {

    @Override
    public boolean addTask(LinkedList<Server> servers, Task t) {
        int numberOfServers = Scheduler.getMaxNoServers();
        if (numberOfServers > 0) {
            Server chosen = servers.get(0);
            long minTaskSize = chosen.getTaskSize();//w

            for (Server currentServer : servers) {
                long currentTaskSize = currentServer.getTaskSize();
                if (currentTaskSize < minTaskSize) {
                    chosen = currentServer;
                    minTaskSize = currentTaskSize;
                }
            }

            if (minTaskSize < Scheduler.getMaxTasksPerServer()) {
                SimulationManager.addAverageWaitingTime(chosen.getWaitingPeriod().intValue());
                chosen.addTask(t);
                return true;
            }
        }
        return false;
    }
}
