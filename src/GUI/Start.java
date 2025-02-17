package GUI;

import BusinessLogic.SelectionPolicy;
import BusinessLogic.SimulationManager;

import java.awt.event.ActionEvent;

public class Start {
    public static void main(String[] args) {
        SimulationFrame frame = new SimulationFrame();

        frame.StartListener(
                (ActionEvent e) -> {
                    SimulationManager gen = new SimulationManager(frame);
                    Thread t = new Thread(gen);
                    t.start();
                }
        );
        frame.TimeStrategyListener(
                (ActionEvent e) -> frame.setSelectionPolicy(SelectionPolicy.SHORTEST_TIME)
        );
        frame.ShortestQueueStrategyListener(
                (ActionEvent e) -> frame.setSelectionPolicy(SelectionPolicy.SHORTEST_QUEUE)
        );
    }
}
