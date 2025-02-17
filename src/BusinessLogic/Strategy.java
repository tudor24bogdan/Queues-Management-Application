package BusinessLogic;

import Model.Task;
import Model.Server;

import java.util.LinkedList;

public interface Strategy {
    boolean addTask(LinkedList<Server> servers, Task t);
}
