package com.example.swing.swing;

import com.example.swing.swing.entity.Task;
import com.example.swing.swing.service.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PlanTablePanel extends JPanel {

    private final String[] columnNames;
    private final TaskService taskService;
    private JPanel planPanel = new JPanel(new BorderLayout());
    private JTable planTable;
    private final Logger logger= LogManager.getRootLogger();

    public PlanTablePanel(TaskService taskService, String surname,String month) {
        this.taskService = taskService;
        this.columnNames = getColumnNames();

        planTable = new JTable();
        planTable.setModel(new DefaultTableModel(getTableData(surname,month), columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return String.class;
                } else {
                    return Boolean.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        planTable.getColumnModel().getColumn(0).setPreferredWidth(150);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (column == 0) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                }
                if (value == null) {
                    c.setBackground(Color.WHITE);
                }
                else if(value!=null)
                    c.setBackground(Color.LIGHT_GRAY);

                return c;
            }
        };
        planTable.setDefaultRenderer(Boolean.class, renderer);

        JScrollPane planScrollPane = new JScrollPane(planTable);
        planPanel.add(planScrollPane, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("План", planPanel);
        this.setLayout(new BorderLayout());
        this.add(tabbedPane, BorderLayout.CENTER);
    }

    private String[] getColumnNames() {
        String[] columnNames = new String[32];
        columnNames[0] = "Задача";
        for (int i = 1; i <= 31; i++) {
            columnNames[i] = String.valueOf(i);
        }
        return columnNames;
    }

    public void refreshTableData(String surname, String month) {
        DefaultTableModel model = (DefaultTableModel) planTable.getModel();
        model.setDataVector(getTableData(surname, month), columnNames);
    }

    private Object[][] getTableData(String surname, String month) {

        List<Task> tasks = taskService.getTasks(surname);

        Object[][] data = new Object[tasks.size()][32];
        boolean hasTasksForMonth = false;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            LocalDate startDate = task.getStartDate();
            LocalDate endDate = task.getEndDate();

            if (startDate.getMonth().toString().equalsIgnoreCase(month) || endDate.getMonth().toString().equalsIgnoreCase(month)) {
                hasTasksForMonth = true;
                data[i][0] = task.getDescription();
                System.out.println(data[i][0]);
                LocalDate date = startDate;
                while (date.isBefore(endDate) || date.equals(endDate)) {
                    int dayOfMonth = date.getDayOfMonth();
                    if (dayOfMonth >= 1 && dayOfMonth <= 31) {
                        data[i][dayOfMonth] = new String(" ");
                    }
                    date = date.plusDays(1);
                }
            }
        }
        if (!hasTasksForMonth) {
            logger.error("Task in this month not found");
        }
        return data;
    }



}
