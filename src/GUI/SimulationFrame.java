package GUI;

import BusinessLogic.SelectionPolicy;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class SimulationFrame extends JFrame {

    private final JTextField NumberClientsTextField;
    private final JTextField NumberQueuesTField;
    private final JTextField SimulationIntervalTField;
    private final JTextField MinArrivalTField;
    private final JTextField MaxArrivalTField;
    private final JTextField MinServiceTField;
    private final JTextField MaxServiceTField;
    private final JButton btnNewButton;
    private final JButton btnTimeStrategy;
    private final JButton btnShortestQueueStrategy;
    private final JLabel TimeJLabel_1;
    private final JTextArea textArea_waitingClients;
    private final JTextArea textArea_queueEvolution;
    private final JLabel averageWaitingTimeLabel;
    private final JLabel averageServiceTimeLabel;
    private final JLabel PeakHourLabel;
    private SelectionPolicy selectionPolicy;

    public SimulationFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 700);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Top left panel for current time and waiting clients
        JPanel panelLeft = new JPanel();
        panelLeft.setBorder(new TitledBorder("Current Status"));
        panelLeft.setBounds(10, 10, 450, 250);
        contentPane.add(panelLeft);
        panelLeft.setLayout(null);

        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setBounds(10, 20, 80, 25);
        panelLeft.add(timeLabel);

        TimeJLabel_1 = new JLabel("");
        TimeJLabel_1.setBounds(100, 20, 80, 25);
        panelLeft.add(TimeJLabel_1);

        JLabel waitingClientsLabel = new JLabel("Waiting Clients:");
        waitingClientsLabel.setBounds(10, 60, 100, 25);
        panelLeft.add(waitingClientsLabel);

        textArea_waitingClients = new JTextArea();
        JScrollPane scrollPaneLeft = new JScrollPane(textArea_waitingClients);
        scrollPaneLeft.setBounds(10, 90, 430, 150);
        panelLeft.add(scrollPaneLeft);

        // Top right panel for queue evolution
        JPanel panelRight = new JPanel();
        panelRight.setBorder(new TitledBorder("Queue Evolution"));
        panelRight.setBounds(470, 10, 500, 250);
        contentPane.add(panelRight);
        panelRight.setLayout(null);

        textArea_queueEvolution = new JTextArea();
        JScrollPane scrollPaneRight = new JScrollPane(textArea_queueEvolution);
        scrollPaneRight.setBounds(10, 20, 480, 220);
        panelRight.add(scrollPaneRight);

        // Bottom panel for inputs and strategy selection
        JPanel panelBottom = new JPanel();
        panelBottom.setBorder(new TitledBorder("Inputs"));
        panelBottom.setBounds(10, 270, 960, 380);
        contentPane.add(panelBottom);
        panelBottom.setLayout(null);

        JLabel numberOfClientsLabel = new JLabel("Number of Clients:");
        numberOfClientsLabel.setBounds(10, 30, 150, 25);
        panelBottom.add(numberOfClientsLabel);

        NumberClientsTextField = new JTextField();
        NumberClientsTextField.setBounds(170, 30, 150, 25);
        panelBottom.add(NumberClientsTextField);
        NumberClientsTextField.setColumns(10);

        JLabel numberOfQueuesLabel = new JLabel("Number of Queues:");
        numberOfQueuesLabel.setBounds(10, 70, 150, 25);
        panelBottom.add(numberOfQueuesLabel);

        NumberQueuesTField = new JTextField();
        NumberQueuesTField.setBounds(170, 70, 150, 25);
        panelBottom.add(NumberQueuesTField);
        NumberQueuesTField.setColumns(10);

        JLabel simulationIntervalLabel = new JLabel("Simulation Interval:");
        simulationIntervalLabel.setBounds(10, 110, 150, 25);
        panelBottom.add(simulationIntervalLabel);

        SimulationIntervalTField = new JTextField();
        SimulationIntervalTField.setBounds(170, 110, 150, 25);
        panelBottom.add(SimulationIntervalTField);
        SimulationIntervalTField.setColumns(10);

        JLabel minArrivalTimeLabel = new JLabel("Min Arrival Time:");
        minArrivalTimeLabel.setBounds(10, 150, 150, 25);
        panelBottom.add(minArrivalTimeLabel);

        MinArrivalTField = new JTextField();
        MinArrivalTField.setBounds(170, 150, 150, 25);
        panelBottom.add(MinArrivalTField);
        MinArrivalTField.setColumns(10);

        JLabel maxArrivalTimeLabel = new JLabel("Max Arrival Time:");
        maxArrivalTimeLabel.setBounds(10, 190, 150, 25);
        panelBottom.add(maxArrivalTimeLabel);

        MaxArrivalTField = new JTextField();
        MaxArrivalTField.setBounds(170, 190, 150, 25);
        panelBottom.add(MaxArrivalTField);
        MaxArrivalTField.setColumns(10);

        JLabel minServiceTimeLabel = new JLabel("Min Service Time:");
        minServiceTimeLabel.setBounds(10, 230, 150, 25);
        panelBottom.add(minServiceTimeLabel);

        MinServiceTField = new JTextField();
        MinServiceTField.setBounds(170, 230, 150, 25);
        panelBottom.add(MinServiceTField);
        MinServiceTField.setColumns(10);

        JLabel maxServiceTimeLabel = new JLabel("Max Service Time:");
        maxServiceTimeLabel.setBounds(10, 270, 150, 25);
        panelBottom.add(maxServiceTimeLabel);

        MaxServiceTField = new JTextField();
        MaxServiceTField.setBounds(170, 270, 150, 25);
        panelBottom.add(MaxServiceTField);
        MaxServiceTField.setColumns(10);

        btnTimeStrategy = new JButton("Time Strategy");
        btnTimeStrategy.setBounds(340, 30, 150, 25);
        panelBottom.add(btnTimeStrategy);

        btnShortestQueueStrategy = new JButton("Shortest Queue Strategy");
        btnShortestQueueStrategy.setBounds(500, 30, 200, 25);
        panelBottom.add(btnShortestQueueStrategy);

        btnNewButton = new JButton("Start Simulation");
        btnNewButton.setBounds(710, 30, 150, 25);
        panelBottom.add(btnNewButton);

        JLabel avgWaitingTimeLabel = new JLabel("Average Waiting Time:");
        avgWaitingTimeLabel.setBounds(340, 70, 150, 25);
        panelBottom.add(avgWaitingTimeLabel);

        averageWaitingTimeLabel = new JLabel("--");
        averageWaitingTimeLabel.setBounds(500, 70, 100, 25);
        panelBottom.add(averageWaitingTimeLabel);

        JLabel avgServiceTimeLabel = new JLabel("Average Service Time:");
        avgServiceTimeLabel.setBounds(340, 110, 150, 25);
        panelBottom.add(avgServiceTimeLabel);

        averageServiceTimeLabel = new JLabel("--");
        averageServiceTimeLabel.setBounds(500, 110, 100, 25);
        panelBottom.add(averageServiceTimeLabel);

        JLabel peakHourLbl = new JLabel("Peak Hour:");
        peakHourLbl.setBounds(340, 150, 150, 25);
        panelBottom.add(peakHourLbl);

        PeakHourLabel = new JLabel("--");
        PeakHourLabel.setBounds(500, 150, 100, 25);
        panelBottom.add(PeakHourLabel);


        this.setVisible(true);
    }

    public String getClients() {
        return NumberClientsTextField.getText();
    }

    public String getQueues() {
        return NumberQueuesTField.getText();
    }

    public String getSimulationInterval() {
        return SimulationIntervalTField.getText();
    }

    public String getMinServiceTime() {
        return MinServiceTField.getText();
    }

    public String getMaxServiceTime() {
        return MaxServiceTField.getText();
    }

    public String getMinArrivalTime() {
        return MinArrivalTField.getText();
    }

    public String getMaxArrivalTime() {
        return MaxArrivalTField.getText();
    }

    public void StartListener(ActionListener start) {
        btnNewButton.addActionListener(start);
    }

    public void TimeStrategyListener(ActionListener timeStrategy) {
        btnTimeStrategy.addActionListener(timeStrategy);
    }

    public void ShortestQueueStrategyListener(ActionListener shortestQueueStrategy) {
        btnShortestQueueStrategy.addActionListener(shortestQueueStrategy);
    }

    public void setSelectionPolicy(SelectionPolicy policy) {
        this.selectionPolicy = policy;
    }

    public SelectionPolicy getSelectionPolicy() {
        return selectionPolicy;
    }

    public void showError(String errMessage) {
        JOptionPane.showMessageDialog(this, errMessage);
    }


    public void setTime(String time) {
        TimeJLabel_1.setText(time);
    }

    public void setWaitingQueue(String queue) {
        textArea_waitingClients.setText(queue);
    }

    public void setQueueEvolution(String queue) {
        textArea_queueEvolution.setText(queue);
    }

    public void setAverageWaitingTimeJLabel(String avg) {
        averageWaitingTimeLabel.setText(avg);
    }

    public void setAverageServiceTimeJLabel(String avg) {
        averageServiceTimeLabel.setText(avg);
    }

    public void setPeakHourLabel(String peak) {
        PeakHourLabel.setText(peak);
    }
}
