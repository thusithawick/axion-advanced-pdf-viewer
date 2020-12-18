/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookreader;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author thusitha
 */
public class CanvasContinuesFacing extends javax.swing.JFrame implements BookDisplay{
    
    public JLabel[] pageArray;
    BookReader bookReader;
    JPanel innerPanel;
    JPanel outerPanel;
    private int outerWidth,outerHeight;
    private int innerWidth, innerHeight;
    private int tempCurrentPage=0;
    double zoomRatio=0;
    private int mouseX=0, mouseY=0;
    private final int slidGap=10;
    /**
     * Creates new form CanvasContinues
     */
    public CanvasContinuesFacing(BookReader bookReader) {
        initComponents();
        innerWidth=bookReader.PAGE_WIDTH;
        innerHeight=bookReader.PAGE_HEIGHT;
        innerPanel=new JPanel();
        outerPanel=new JPanel();
        outerPanel.setBackground(Color.darkGray);
        innerPanel.setBackground(Color.darkGray);
        innerPanel.setSize((2*innerWidth)+slidGap, (innerHeight+slidGap)*bookReader.pages/2);
        innerPanel.setPreferredSize(new Dimension((2*innerWidth)+slidGap, (innerHeight+slidGap)*bookReader.pages/2));
        innerPanel.setLayout(new GridLayout(bookReader.pages/2, 2, slidGap, slidGap));
        
        
        
        if (bookReader.SEPERATE) {
        
            pageArray = new JLabel[bookReader.pages+2];
                pageArray[0] = new JLabel();
                pageArray[0].setBackground(Color.white);
                pageArray[0].setSize(innerWidth, innerHeight);
                pageArray[0].setOpaque(false);
                innerPanel.add(pageArray[0]);
                pageArray[0].setVisible(true);
            
            for (int i = 1; i < bookReader.pages-2; i++) {
                pageArray[i] = new JLabel();
                pageArray[i].setBackground(Color.white);
                pageArray[i].setSize(innerWidth, innerHeight);
                pageArray[i].setOpaque(true);
                innerPanel.add(pageArray[i]);
                pageArray[i].setVisible(true);
            }
                
                pageArray[pageArray.length-1] = new JLabel();
                pageArray[pageArray.length-1].setBackground(Color.white);
                pageArray[pageArray.length-1].setSize(innerWidth, innerHeight);
                pageArray[pageArray.length-1].setOpaque(false);
                innerPanel.add(pageArray[pageArray.length-1]);
                pageArray[pageArray.length-1].setVisible(true);
            
        }else{
        
            pageArray = new JLabel[bookReader.pages];
            for (int i = 0; i < bookReader.pages; i++) {
                pageArray[i] = new JLabel();
                pageArray[i].setBackground(Color.white);
                pageArray[i].setSize(innerWidth, innerHeight);
                pageArray[i].setOpaque(true);
                innerPanel.add(pageArray[i]);
                pageArray[i].setVisible(true);
            }
            
        }
        
        
        outerPanel.add(innerPanel);
        jScrollPane1.setViewportView(outerPanel);
        this.bookReader=bookReader;
        jScrollPane1.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                tempCurrentPage = bookReader.pages*jScrollPane1.getVerticalScrollBar().getValue()/jScrollPane1.getVerticalScrollBar().getMaximum();
                
                    if (bookReader.SEPERATE) {
                        if (bookReader.currentPage!=tempCurrentPage+2) {
                            System.out.println("jumped to:"+tempCurrentPage+2);
                            bookReader.webTextField1.setText(String.valueOf(tempCurrentPage+2));
                            bookReader.currentPage=tempCurrentPage+2;
                        }
                    }else{
                        if (bookReader.currentPage!=tempCurrentPage+1) {
                            System.out.println("jumped to:"+tempCurrentPage+1);
                            bookReader.webTextField1.setText(String.valueOf(tempCurrentPage+1));
                            bookReader.currentPage=tempCurrentPage+1;
                        }
                    }
            }
        });
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(35);
        
        outerPanel.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2) {
                    bookReader.windowMaximizeOrRestore();
                }else if (e.getButton()==2) {
                    bookReader.windowExpand();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                PressedMouse(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        
        outerPanel.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                dragMouse(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        
    }
    
    
    public void dragMouse(MouseEvent evt){
        int tempx=evt.getXOnScreen();
        int tempy=evt.getYOnScreen();
        jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getValue()+mouseY-tempy);
        jScrollPane1.getHorizontalScrollBar().setValue(jScrollPane1.getHorizontalScrollBar().getValue()+mouseX-tempx);
        mouseX=evt.getXOnScreen();
        mouseY=evt.getYOnScreen();
    }
    
    public void PressedMouse(MouseEvent evt){
        mouseX=evt.getXOnScreen();
        mouseY=evt.getYOnScreen();
        outerPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        webPanel1 = new com.alee.laf.panel.WebPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setBorder(null);
        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseEntered(evt);
            }
        });
        jScrollPane1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseDragged(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout webPanel1Layout = new javax.swing.GroupLayout(webPanel1);
        webPanel1.setLayout(webPanel1Layout);
        webPanel1Layout.setHorizontalGroup(
            webPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(webPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
        webPanel1Layout.setVerticalGroup(
            webPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(webPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(webPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(webPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jScrollPane1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseEntered
        // TODO add your handling code here:
        try {
            if (bookReader.WINDOW_SIZE==3) {
                bookReader.webPanel2.setVisible(false);
            }
        } catch (Exception e) {
        }
            
    }//GEN-LAST:event_jScrollPane1MouseEntered

    private void jScrollPane1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane1MouseDragged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CanvasContinuesFacing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CanvasContinuesFacing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CanvasContinuesFacing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CanvasContinuesFacing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CanvasContinuesFacing(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public com.alee.laf.panel.WebPanel webPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void Zoom(double ratio) {
        this.zoomRatio=ratio;
        innerPanel.setVisible(false);
        outerPanel.setVisible(false);
        innerPanel.setPreferredSize(new Dimension((int) (2*innerWidth*(1+zoomRatio)),(int)  ((slidGap+((innerHeight/2)*(1+zoomRatio))))*bookReader.pages));
        outerPanel.setPreferredSize(new Dimension((int) (webPanel1.getWidth()*(1+zoomRatio)),(int)  ((slidGap+((innerHeight/2)*(1+zoomRatio))))*bookReader.pages));
        
        innerPanel.removeAll();
        System.gc();
        //innerPanel.setLayout(new GridLayout(bookReader.pages, 1, 10, 10));
        
        
        if (bookReader.SEPERATE) {
        
            pageArray = new JLabel[bookReader.pages+2];
                pageArray[0] = new JLabel();
                pageArray[0].setBackground(Color.white);
                pageArray[0].setSize(innerWidth, innerHeight);
                pageArray[0].setOpaque(false);
                innerPanel.add(pageArray[0]);
                pageArray[0].setVisible(true);
            
            for (int i = 1; i < bookReader.pages-2; i++) {
                pageArray[i] = new JLabel();
                pageArray[i].setBackground(Color.white);
                pageArray[i].setSize(innerWidth, innerHeight);
                pageArray[i].setOpaque(true);
                innerPanel.add(pageArray[i]);
                pageArray[i].setVisible(true);
            }
                
                pageArray[pageArray.length-1] = new JLabel();
                pageArray[pageArray.length-1].setBackground(Color.white);
                pageArray[pageArray.length-1].setSize(innerWidth, innerHeight);
                pageArray[pageArray.length-1].setOpaque(false);
                innerPanel.add(pageArray[pageArray.length-1]);
                pageArray[pageArray.length-1].setVisible(true);
            
        }else{
        
            pageArray = new JLabel[bookReader.pages];
            for (int i = 0; i < bookReader.pages; i++) {
                pageArray[i] = new JLabel();
                pageArray[i].setBackground(Color.white);
                pageArray[i].setSize(innerWidth, innerHeight);
                pageArray[i].setOpaque(true);
                innerPanel.add(pageArray[i]);
                pageArray[i].setVisible(true);
            }
            
        }
        innerPanel.setVisible(true);
        outerPanel.setVisible(true);
        System.out.println("dfdf"+jScrollPane1.getHorizontalScrollBar().getMaximum());
        jScrollPane1.getHorizontalScrollBar().setValue(jScrollPane1.getHorizontalScrollBar().getMaximum()/2);
        jScrollPane1.revalidate();
        
    }

    @Override
    public void nextPage() {
        
        
        if (bookReader.SEPERATE) {
            
        }else{
        
        }
        
    }

    @Override
    public void prevPage() {
        
        
        if (bookReader.SEPERATE) {
            
        }else{
        
        }
        
    }

    @Override
    public void jumpPage(int pageIndex) {
        
        
        if (bookReader.SEPERATE) {
//            if (pageIndex/2!=0 || pageIndex/2!=pageArray.length-1) {
                int ss =jScrollPane1.getVerticalScrollBar().getMaximum();
                System.out.println("ss"+ss);
                jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getMaximum()/(bookReader.pages)*(pageIndex+1));
//            }
        }else{
            int ss =jScrollPane1.getVerticalScrollBar().getMaximum();
            System.out.println("ss"+ss);
            jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getMaximum()/(bookReader.pages)*(pageIndex));
        }
        
    }

    @Override
    public void paintPage(int pageIndex, Image img) {
        
        
        if (bookReader.SEPERATE) {
            if (pageIndex!=0 || pageIndex!=pageArray.length-1) {
                try {
                pageArray[pageIndex].setIcon(new ImageIcon(img.getScaledInstance((int) (innerWidth*(1+zoomRatio)),(int)  (innerHeight*(1+zoomRatio)), Image.SCALE_SMOOTH)));
                } catch (Exception e) {
                }
            }
        }else{
            try {
            pageArray[pageIndex+1].setIcon(new ImageIcon(img.getScaledInstance((int) (innerWidth*(1+zoomRatio)),(int)  (innerHeight*(1+zoomRatio)), Image.SCALE_SMOOTH)));
            } catch (Exception e) {
            }
        }
        
    }

    @Override
    public void cleanPage(int pageIndex) {
        
        
        if (bookReader.SEPERATE) {
            //if (pageIndex!=0 || pageIndex!=pageArray.length-1) {
                pageArray[pageIndex].setIcon(null);
            //}
        }else{
            pageArray[pageIndex+1].setIcon(null);
        }
        
    }

    @Override
    public void fire() {
        pageArray=null;
        System.gc();
        dispose();
    }
}
