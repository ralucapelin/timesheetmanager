// Packages to import
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Map;
import java.util.Set;


public class JTableExamples {
     private Integer getPontaj(Object str){
         HashMap<String, Integer> hm = new HashMap<>();
         hm.put("Z",12);
         hm.put("N",12);
         hm.put("7",8);
         hm.put("7S",8);
         hm.put("ZP",12);
         hm.put("7P",8);
         hm.put("C", 0);
         hm.put("/",0);
         return hm.get(str);
     }
    private static class CustomRenderer extends DefaultTableCellRenderer {

        private final TableModel model;

        public CustomRenderer(TableModel model) {
            this.model = model;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, col);

                c.setBackground(Color.cyan);

            return c;
        }
    }
    private String[][] dataPontaje = new String[15][34];
    private ArrayList<Integer> weekends(YearMonth yearMonth){
        int len = yearMonth.lengthOfMonth();
        int first = yearMonth.atDay(1).getDayOfWeek().getValue();
        ArrayList<Integer> weekend = new ArrayList<>();
        int i = 0;
        int dif = 6 - first + 1;

        while(dif < len && first !=7){
            weekend.add(dif);
            weekend.add(dif+1);
            dif+=7;
        }
        if(first == 7) {
            dif = 1;
            weekend.add(dif);
            i++;
            dif+=6;
            while(dif < len) {
                weekend.add(dif);
                weekend.add(dif+1);
                i+=2;
                dif+=7;
            }
        }
        return weekend;
    }
   YearMonth ym = YearMonth.now();

    public int sumRow(JTable mdl, int row) {

        int total = 0;
        // iterate over all columns
        for (int i = 1 ; i < mdl.getColumnCount() ; i++) {
            // null or not Integer will throw exception
            total += getPontaj((mdl.getValueAt(row, i)));
        }

        return total;
    }
    public int sumWeekend(JTable mdl, int row, YearMonth ym) {

        int total = 0;
        // iterate over all columns
        int size = weekends(ym).size();
        ArrayList<Integer> wk = weekends(ym);

        if (size%2==1) {

            if(wk.get(0)==1 && wk.get(1)==7){
                System.out.println("da");
                for(int i=0; i < size; i++){
                    if(i%2 == 0 && mdl.getValueAt(row,wk.get(i)) == "N"){
                        total+=5;
                    }
                    else if(i%2 == 1 && mdl.getValueAt(row,wk.get(i)-1) == "N") {
                        total += 7;
                    }else {
                        total+=getPontaj(mdl.getValueAt(row,wk.get(i)));
                    }

                }
            }
        }
        else {
            for(int i=0; i < size; i++){
                if(i%2 == 1 && mdl.getValueAt(row,wk.get(i)) == "N"){
                    total+=5;
                }
                else if(i%2 == 0 && mdl.getValueAt(row,wk.get(i)-1) == "N") {
                    total += 7;
                }
                else {
                    total+=getPontaj(mdl.getValueAt(row,wk.get(i)));
                }
            }
        }



        return total;
    }
    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 30; // Min width
            JTableHeader head= table.getTableHeader();

            width =Math.max(table.getColumnName(column).length()*10,width);

            System.out.println(width);
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
                width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        Object data = model.getValueAt(row, column);

        dataPontaje[row][column]=""+getPontaj(data);
    }
    public int isComplete(String data[][], int n,int k){
        for(int i = 0; i < n ; i++){
            if(data[i][k].equals("")){
               // System.out.println("aici "+ k +" "+ i);
                return 0;
            }

        }
        return 1;
    }

    // frame
    JFrame f;
    // Table
    JTable j;

    JButton button;
    JTable getTable(){
        return j;
    }
    // Constructor
    JTableExamples()
    {

        JTabbedPane tp = new JTabbedPane();
        // Frame initiallization
        f = new JFrame();

        // Frame Title
        f.setTitle("Grafic si Pontaje");

        // Data to be displayed in the JTable
        String[][] data = new String[15][32];

        for(int i = 0; i<15; i++){
            for(int j = 1 ;j < 32; j++){
                data[i][j]="";
            }
        }
        for(int i = 0; i<15; i++){
            for(int j = 1 ;j < 34; j++){
                dataPontaje[i][j]="";
            }
        }
        int rows = 15;
        for(int i = 0; i < 15; i++){
            data[i][0]="Asistenta nr." + i;
            dataPontaje[i][0]= "Asistenta nr." + i;
        }

        // Column Names
        String[] columnNames = new String[32];
        String[] columnNamesPontaje = new String[34];
        columnNames[0]="";
        columnNamesPontaje[0]="";
        for(int i = 1 ; i<32; i++){
            columnNames[i]=""+i;
            columnNamesPontaje[i]=""+i;
        }
        columnNamesPontaje[32] = "Total";
        columnNamesPontaje[33] = "Weekend";
        // Initializing the JTable
        j = new JTable(data, columnNames);

        //j.setBounds(60, 80, 200, 300);
        for(int i = 1; i < 32; i++){
            TableColumn zi = j.getColumnModel().getColumn(i);
            JComboBox comboBox = new JComboBox();
            comboBox.addItem("Z");
            comboBox.addItem("ZP");
            comboBox.addItem("N");
            comboBox.addItem("7");
            comboBox.addItem("7S");
            comboBox.addItem("7P");
            comboBox.addItem("C");
            comboBox.addItem("/");
            zi.setCellEditor(new DefaultCellEditor(comboBox));
        }

        JButton b=new JButton("Calculeaza pontaje");
        b.setBounds(500,400,160,30);

        f.add(b);
        JButton b2=new JButton("Genereaza Grafic");
        b2.setBounds(300,400,160,30);
        f.add(b2);

        button= new JButton("Genereaza PDF");

        //scrollPane = new JScrollPane(table);
        button.setBounds(700,400,160,30);
        f.add(button);


        JTable pontaje = new JTable(dataPontaje,columnNamesPontaje);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                for(int i = 0; i < rows; i++){
                    int ok = 1;
                    for(int k=1; k<j.getColumnCount(); k++){
                        if(j.getValueAt(i,k).equals("")){

                            ok=0;
                            break;
                        }
                    }
                    if(ok==1){
                        System.out.println("asistenta nr. " + i + " : " +sumRow(j,i) + ", " + sumWeekend(j,i,ym));
                        dataPontaje[i][32]=""+sumRow(j,i);
                        dataPontaje[i][33]=""+sumWeekend(j,i,ym);
                        pontaje.repaint();

                    }

                }

            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                HashMap<String, Integer> hm2 = new HashMap<>();
                hm2.put("Z",0);
                hm2.put("N",1);
                hm2.put("7",2);

                int[][] ore = new int[15][4];
                for(int i = 1; i < 32; i++){
                    ArrayList<String> ture = new ArrayList<>();
                    ture.add("N");
                    ture.add("N");
                    ture.add("Z");
                    ture.add("Z");
                    ture.add("7");

                    ArrayList<Integer> as = new ArrayList<>();
                    for(int j = 0; j<15; j++){
                        as.add(j);
                    }
                    while(isComplete(data,15,i)==0){
                        int idx = (int)(Math.random() * as.size());
                        int index = as.get(idx);
                        System.out.println(index + " " + as.size());
                        int tureIndex = (int)(Math.random()*ture.size());
                        String tura ="";
                        if(ture.size()>0)
                       tura= ture.get(tureIndex);
                        if(data[index][i].equals("")){

                            if(ture.size()>0){
                                int at  = hm2.get(tura);

                                if(ore[index][at]<5){
                                    data[index][i]=tura;
                                    ore[index][at]++;
                                    if(tura.equals("N") && i < 31){
                                        data[index][i+1]="/";
                                        dataPontaje[index][i+1]=""+getPontaj("/");
                                    }
                                    ture.remove(tureIndex);
                                    as.remove(idx);
                                    dataPontaje[index][i]=""+getPontaj(tura);
                                }
                            }
                            else {
                                data[index][i] = "/";
                                dataPontaje[index][i]=""+getPontaj("/");
                                as.remove(idx);
                            }

                        }
                    }
                }

                j.repaint();
                pontaje.repaint();
            }
        });

       /* j.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent event) {
                        int viewRow = j.getSelectedRow();
                        if (viewRow < 0) {
                            //Selection got filtered away.

                        } else {
                            int modelRow =
                                    j.convertRowIndexToModel(viewRow);
                          System.out.println( "Selected Row in view:  " + modelRow +
                                                    "Selected Row in model: %d.");
                        }
                    }
                }
        );*/
        for(int i:weekends(ym)){
            if(i>0)
            j.getColumnModel().getColumn(i).setCellRenderer(new CustomRenderer(j.getModel()));
        else break;
        }

        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(j);
        tp.addTab("Grafic", sp);
       // f.add(sp, BorderLayout.CENTER);

        JScrollPane sp2 = new JScrollPane(pontaje);
        tp.addTab("Pontaje", sp2);
        f.add(tp);
       // f.add(sp2, BorderLayout.SOUTH);
        f.validate();
      //  f.add(sp);
        // Frame Size
        f.setSize(1200, 600);
        // Frame Visible = true
        f.setVisible(true);
        //

        j.getModel().addTableModelListener(this::tableChanged);
        j.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        pontaje.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        resizeColumnWidth(j);
        resizeColumnWidth(pontaje);
    }

    // Driver  method
    public static void main(String[] args)
    {
        JTableExamples ex = new JTableExamples();

    }
}
