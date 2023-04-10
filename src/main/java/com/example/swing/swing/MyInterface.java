package com.example.swing.swing;

import com.example.swing.swing.entity.Employee;
import com.example.swing.swing.entity.Task;
import com.example.swing.swing.service.EmployeeService;
import com.example.swing.swing.service.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class MyInterface extends JFrame {
    private final String[] MONTHS = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
            "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    private JComboBox<String> monthComboBox=new JComboBox<>(MONTHS);
    private final Logger logger= LogManager.getRootLogger();
    private final TaskService taskService;
    private String[] columnNames= {"Выполнена", "Задачи", "Дата начала", "Дата окончания", "Дата выполнения"};
    private JTable table;
    private Object[][] data;
    private JPanel planPanel = new JPanel(new BorderLayout());
    private JPanel headerPanel = new JPanel();

    public MyInterface(EmployeeService employeeService,TaskService taskService) {
        this.taskService=taskService;
        List<String> employeeNames = employeeService.getEmployees().stream()
                .map(Employee::getSurname)
                .collect(Collectors.toList());

        JComboBox<String> employeeComboBox = new JComboBox<>(employeeNames.toArray(new String[0]));

        employeeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedEmployee = (String) employeeComboBox.getSelectedItem();
                List<Task> tasks = taskService.getTasks(selectedEmployee);
                data=updateTaskTableData(tasks);
                PlanTablePanel planTablePanel=new PlanTablePanel(taskService,selectedEmployee,MONTHS[1]);
                planTablePanel.refreshTableData(selectedEmployee,MONTHS[1]);
                planPanel.add(planTablePanel);
            }
        });

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel tasksPanel = new JPanel(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            Class<?>[] types = {Boolean.class, Object.class, Object.class, Object.class, Object.class};

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        table = new JTable(model);
        table.getColumnModel().getColumn(0).setCellRenderer(table.getDefaultRenderer(Boolean.class));
        table.getColumnModel().getColumn(0).setCellEditor(table.getDefaultEditor(Boolean.class));
        JScrollPane scrollPane = new JScrollPane(table);
        tasksPanel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Задачи", tasksPanel);

        headerPanel.add(new JLabel("Выберите месяц:"));
        headerPanel.add(monthComboBox);
        monthComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedEmployee = (String) employeeComboBox.getSelectedItem();
                List<Task> tasks = taskService.getTasks(selectedEmployee);
                data=updateTaskTableData(tasks);
                PlanTablePanel planTablePanel=new PlanTablePanel(taskService,selectedEmployee,(String)monthComboBox.getSelectedItem());
                planTablePanel.refreshTableData((String)monthComboBox.getSelectedItem(),MONTHS[1]);
                planPanel.add(planTablePanel);
            }
        });
        planPanel.add(headerPanel, BorderLayout.NORTH);
        tabbedPane.addTab("План", planPanel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel employeeLabel = new JLabel("Сотрудник:");
        JPanel employeePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        employeePanel.add(employeeLabel);
        employeePanel.add(employeeComboBox);
        mainPanel.add(employeePanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.SOUTH);

        JFrame frame = new JFrame("Планировщик");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }


    private Object[][] updateTaskTableData(List<Task> tasks) {
        Object[][] data = new Object[tasks.size()][5];
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            data[i][0] = task.isCompleted();
            data[i][1] = task.getDescription();
            data[i][2] = task.getStartDate();
            data[i][3] = task.getEndDate();
            data[i][4] = task.getCompletionDate();
        }
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setDataVector(data, columnNames);
        return data;
    }

    public static void main(String[] args) {
            SpringApplication.run(MyInterface.class, args);

    }
}
