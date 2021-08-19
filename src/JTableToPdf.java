import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class JTableToPdf {

    private JFrame frame;
    private JTable table;
    private JButton button;
    private Document document;
    private PdfWriter writer;
    private JScrollPane scrollPane;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JTableToPdf().createAndShowGui();
            }
        });
    }

    public void openPdf() throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4.rotate(), 30, 30, 30, 30);
        writer = PdfWriter.getInstance(document, new FileOutputStream("Grafic.pdf"));
        document.open();
    }

    public void closePdf() {
        document.close();
    }

    public void addData(PdfPTable pdfTable) throws DocumentException {

        pdfTable.setHeaderRows(1);
        pdfTable.setTotalWidth(710f);
        pdfTable.setLockedWidth(true);
        float[] widths = new float[] { 90f,20f,20f,20f,20f,20f,20f,20f,20f,
                20f,20f,20f,20f,20f,20f,20f,20f,20f,20f,20f,20f,20f,20f,20f,20f,20f,20f,20f,20f,20f,20f,20f};
        pdfTable.setWidths(widths);
for(int i = 0; i <32; i++) {

    PdfPCell cell = new PdfPCell(new Paragraph(table.getColumnName(i)));

    cell.setBackgroundColor(new GrayColor(0.7f));
    pdfTable.addCell(cell);
}

       for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                pdfTable.addCell(table.getModel().getValueAt(i, j).toString());
            }
        }



        document.add(pdfTable);
    }

    public void createAndShowGui() {

        JTableExamples ex = new JTableExamples();
        frame = ex.f;
        table = ex.getTable();
        table.setBorder(BorderFactory.createLineBorder(Color.RED));


        ex.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
                    openPdf();
                    addData(pdfTable);
                    closePdf();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (DocumentException e1) {
                    e1.printStackTrace();
                }
            }
        });

//      frame.add(table.getTableHeader(), BorderLayout.NORTH);
       // frame.add(scrollPane, BorderLayout.CENTER);

        //frame.pack();
        //frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
