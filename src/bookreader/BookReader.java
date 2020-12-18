/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookreader;

import APV.AxionPDFFile;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.tree.WebTreeModel;
import de.intarsys.pdf.pd.PDPageNode;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author thusitha
 */
public class BookReader extends javax.swing.JFrame {

    /**
     * Creates new form Reader
     */
    BookDisplay display;
    public static int pages;;
    
    public int WINDOW_SIZE; //0-normal, 1-maximized, 2-minimize, 3-expand
    private Dimension DefaultSize=new Dimension(850, 450);
    public boolean LISTDOWN = true;
    public static int currentPage=-1, previousPage;
    private String path;
    private AxionPDFFile file;
    private PageQueue queue;
    private ImageIcon icon;
    private int MIN_PAGES_LOAD=8;
    private int MIN_PAGES_TODEL=30;
    private final int SLEEP=1000;
    boolean SlideSlipOn = false;
    boolean updatePage = false;
    public int PAGE_WIDTH, PAGE_HEIGHT;
    public boolean SEPERATE=false;
    private int type=0;//page mode
    boolean EXPANDED=false;
    /*
    0=single continues
    1=facing continues
    2=single
    3=facing
    4=3d book mode
    
    */
    
    
    
    public BookReader(String path) {
        initComponents();
        
        //hide main menu at startup
        webPanel7.setVisible(false);
        
        //moving undecorae window using its component
        ComponentMover mover = new ComponentMover(BookReader.class, webPanel2);
        addMouseListener(new DragListener());
        
        //resize undecorate window using its component
        ComponentResizer cr = new ComponentResizer();
        cr.setSnapSize(new Dimension(15, 15));
        //setBackground(new Color(0,0,0,0));
        cr.registerComponent(this);
        
        //window start in middle of the screen
        setLocationRelativeTo(null);
        
        this.path=path;
        file =new AxionPDFFile(path);
        
        try {
            BufferedImage tmpImage = (BufferedImage) file.getPageImage(1);
            PAGE_WIDTH=tmpImage.getWidth();
            PAGE_HEIGHT=tmpImage.getHeight();
        } catch (Exception e) {
            PAGE_WIDTH=webPanel8.getWidth();
            PAGE_HEIGHT=webPanel8.getHeight();
        }
        setTitle(""+file.getTitle());
        pages = file.getLength();
        webTextField2.setText(String.valueOf(pages));
        System.out.println("size "+pages);
        queue  = new PageQueue();
        backLoader.setPriority(Thread.NORM_PRIORITY-1);
        backLoader.setName("backLoader");
        backLoader.start();
        
        try {
            webTree1.setModel(new WebTreeModel(new PDFInfo().getModel(file, path)));
        } catch (IOException ex) {
            Logger.getLogger(BookReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we){ 
                windowClose();
                
            }
        });
        
        //hide title bar at expanded state
        rootPane.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (WINDOW_SIZE==3 || EXPANDED) {
                    if (e.getY()<=50) {
                        webPanel2.setVisible(true);
                    }else{
                        webPanel2.setVisible(false);
                    }
                }
            }
        });
        
        rootPane.requestFocusInWindow();
        
        
        CanvasContinuesSingle c = new CanvasContinuesSingle(this);
        c.setSize(webPanel8.getWidth(), webPanel8.getHeight()-50);
        c.webPanel1.setSize(webPanel8.getSize());
        webPanel8.add(c.webPanel1);
        c.webPanel1.setVisible(true);
        
            
        webComboBox2.addItem("Single Continues");
        webComboBox2.addItem("Facing Continues");
        webComboBox2.addItem("Single");
        webComboBox2.addItem("Facing");
        webComboBox2.addItem("Flip Facing");
        webComboBox2.addItem("Sketch Facing");
        
        display=c;
        display.jumpPage(0);
        
    }

    private BookReader() {

        initComponents();
        
        //hide main menu at startup
        webPanel7.setVisible(false);
        
        //moving undecorae window using its component
        ComponentMover mover = new ComponentMover(BookReader.class, webPanel2);
        addMouseListener(new DragListener());
        
        //resize undecorate window using its component
        ComponentResizer cr = new ComponentResizer();
        cr.setSnapSize(new Dimension(15, 15));
        //setBackground(new Color(0,0,0,0));
        cr.registerComponent(this);
        
        //window start in middle of the screen
        setLocationRelativeTo(null);
        
        
        setTitle("Axion PDF Reader");
        
        
        //hide title bar at expanded state
        rootPane.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (WINDOW_SIZE==3 || EXPANDED) {
                    if (e.getY()<=50) {
                        webPanel2.setVisible(true);
                    }else{
                        webPanel2.setVisible(false);
                    }
                }
            }
        });
        
        rootPane.requestFocusInWindow();
        
        
            
        webComboBox2.addItem("Single Continues");
        webComboBox2.addItem("Facing Continues");
        webComboBox2.addItem("Single");
        webComboBox2.addItem("Facing");
        webComboBox2.addItem("Flip Facing");
        webComboBox2.addItem("Sketch Facing");
        
    }
    
    /////////////////
    //Window resize//
    /////////////////
    
    public void windowMaximizeOrRestore(){
//            if(WINDOW_SIZE==0){//if normal then maximize both
//                WINDOW_SIZE=1;
//                setExtendedState(JFrame.MAXIMIZED_BOTH);
//                try {
//                    webLabel2.setIcon(new ImageIcon(getClass().getResource("/images/restore.png").toURI().getPath()));
//                    setSize(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height-30);
//                } catch (URISyntaxException ex) {
//                    Logger.getLogger(BookReader.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                setLocationRelativeTo(null);
//            }else{//else make window normal
//                WINDOW_SIZE=0;
//                setExtendedState(JFrame.NORMAL);
//                try {
//                    webLabel2.setIcon(new ImageIcon(getClass().getResource("/images/maximize.png").toURI().getPath()));
//                    setSize(DefaultSize);
//                } catch (URISyntaxException ex) {
//                    Logger.getLogger(BookReader.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                //setSize(DefaultSize);
//                setLocationRelativeTo(null);
//            }
            
    }
    
    public void windowExpand(){
        if (EXPANDED) {
            dispose();
            JFrame frame = this;
            frame.setExtendedState(JFrame.NORMAL);
            frame.setSize(DefaultSize);
            frame.setPreferredSize(DefaultSize);
            frame.setUndecorated(false);
            frame.setLocationRelativeTo(this);
            frame.setVisible(true);
            webPanel2.setVisible(true);
            EXPANDED=false;
        }else{
            dispose();
            JFrame frame = this;
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
            frame.setVisible(true);
            webPanel2.setVisible(false);
            EXPANDED=true;
        }
//            if(WINDOW_SIZE!=3){//if normal then maximize both
//                WINDOW_SIZE=3;
//                setExtendedState(JFrame.MAXIMIZED_BOTH);
//                setSize(Toolkit.getDefaultToolkit().getScreenSize());
//                setLocationRelativeTo(null);
//            }else{//else make window normal
//                WINDOW_SIZE=1;
//                //setSize(DefaultSize);
//                setExtendedState(JFrame.NORMAL);
//                try {
//                    webLabel2.setIcon(new ImageIcon(getClass().getResource("/images/maximize.png").toURI().getPath()));
//                    setSize(DefaultSize);
//                } catch (URISyntaxException ex) {
//                    Logger.getLogger(BookReader.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                setLocationRelativeTo(null);
//            }
        
    }
    
    /////////////////////
    //Thread backloader//
    /////////////////////
    
    
    boolean runnable=true;
    
    Thread backLoader = new Thread(new Runnable() {
        @Override
        public void run() {
            loop1:
            while(runnable){
                try {
                    int tmpPage=currentPage;
                    int il = 0;
                    int ir = 0;
                    if (!queue.exists(currentPage)) {
                        try {
                            queue.put(currentPage, file.getPageImage(currentPage));
                            display.paintPage(currentPage, queue.get(currentPage));
                        } catch (Exception e) {
                        }
                    }else{
                        try {
                            display.paintPage(currentPage, queue.get(currentPage));
                        } catch (Exception e) {
                        }
                    }
                    
                    if (currentPage != previousPage || queue.getMin() < currentPage-MIN_PAGES_LOAD || queue.getMax() > currentPage+MIN_PAGES_LOAD) {//if currentPage changed

                        for (il = tmpPage, ir = tmpPage; il > tmpPage-MIN_PAGES_LOAD || ir < tmpPage+MIN_PAGES_LOAD; il--, ir++) {
//                            System.out.println("loading");
                            if (updatePage) {
                                updatePage=false;
//                                System.out.println("loaded");
                                continue loop1;
                            }
                            if (!queue.exists(ir) && ir>=0 && ir<pages) {
                                try {
                                    queue.put(ir, file.getPageImage(ir));
                                    display.paintPage(ir, queue.get(ir));
                                } catch (Exception e) {
                                    
                                }
                            }else if(queue.exists(ir) && ir>=0 && ir<pages){
                                try {
                                    display.paintPage(ir, queue.get(ir));
                                } catch (Exception e) {
                                }
                            }
                            if (!queue.exists(il) && il>=0 && il<pages) {
                                try {
                                    queue.put(il, file.getPageImage(il));
                                    display.paintPage(il, queue.get(il));
                                } catch (Exception e) {
                                    
                                }
                            }else if(queue.exists(il) && il>=0 && il<pages){
                                try {
                                    display.paintPage(il, queue.get(il));
                                } catch (Exception e) {
                                }
                            }
//                            System.out.println("loaded");
                        }
                    }

                    if (queue.length()>MIN_PAGES_TODEL) {
//                        System.out.println("cleaning");
                        int length =queue.length();
                        for (int i = 0; i < length; i++) {
                            if (updatePage) {
                                updatePage=false;
//                                System.out.println("cleaned");
                                continue loop1;
                            }
                            try {
                                if (i<il || i>ir) {
                                    try {
                                        queue.remove(i);
                                        display.cleanPage(i);
                                        length =queue.length();
                                    } catch (Exception e) {
                                        length =queue.length();
                                    }
                                }
                            } catch (Exception e) {
                            }
                                
                        }
//                        System.out.println("cleaned");
                    }

                    try {
                        //Thread.sleep(SLEEP);
                    } catch (Exception e1) {
                    }

                } catch (OutOfMemoryError eE) {
                    new BookReader(path).setVisible(runnable);
                    try {
                        backLoader.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BookReader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        file.closeFile();
                    } catch (IOException ex) {
                        Logger.getLogger(BookReader.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BookReader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    queue.clean();
                    dispose();
                }
            }//while
//            System.out.println("backloader.end");
        }
    });
    
    
    public void updatePages() {
        updatePage=true;
    }
    
    
    public void jumpPage(int pageIndex){
        if (queue!=null) {
            
            //int tempPage=currentPage;
            
            if (pageIndex>=pages) {
                //tempPage=pages-1;
                currentPage=pages-1;
                display.jumpPage(currentPage);
            }else if(pageIndex<0){
                //tempPage=0;
                currentPage=0;
                display.jumpPage(currentPage);
            }else{
                currentPage=pageIndex;
                display.jumpPage(currentPage);
            }
            
            webTextField1.setText(String.valueOf(currentPage-1));
            System.out.println("queue.size:"+queue.length());
        }else{
            try {
                wait(1000);
            } catch (Exception e) {
            }
        }
    }
    
    public void openNewFile(String path){
        
        if (backLoader.getState().equals(Thread.State.RUNNABLE)) {
            try {
                backLoader.wait();
            } catch (InterruptedException ex) {
                
            }
        }
        this.path=path;
        
        if (file!=null) {
            try {
                file.closeFile();
            } catch (IOException ex) {
                
            } catch (InterruptedException ex) {
                
            }
        }
        file = new AxionPDFFile(path);
        
        try {
            BufferedImage tmpImage = (BufferedImage) file.getPageImage(1);
            PAGE_WIDTH=tmpImage.getWidth();
            PAGE_HEIGHT=tmpImage.getHeight();
        } catch (Exception e) {
            PAGE_WIDTH=webPanel8.getWidth();
            PAGE_HEIGHT=webPanel8.getHeight();
        }
        setTitle(""+file.getTitle());
        pages = file.getLength();
        System.out.println("size "+pages);
        if (queue!=null) {
            queue.clean();
        }
        queue  = new PageQueue();
        
        backLoader.setPriority(Thread.NORM_PRIORITY-1);
        backLoader.setName("backLoader");
        
        if (backLoader.getState().equals(Thread.State.BLOCKED) ||
                backLoader.getState().equals(Thread.State.WAITING)||
                backLoader.getState().equals(Thread.State.TIMED_WAITING)) {
            try {
                backLoader.notify();
            } catch (Exception ex) {
                
            }
        }
        
        
        try {
            webTree1.setModel(new WebTreeModel(new PDFInfo().getModel(file, path)));
        } catch (IOException ex) {
            Logger.getLogger(BookReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //hide title bar at expanded state
        rootPane.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (WINDOW_SIZE==3) {
                    if (e.getY()<=50) {
                        webPanel2.setVisible(true);
                    }else{
                        webPanel2.setVisible(false);
                    }
                }
            }
        });
        
        rootPane.requestFocusInWindow();
        
        
        CanvasContinuesSingle c = new CanvasContinuesSingle(this);
        c.setSize(webPanel8.getWidth(), webPanel8.getHeight()-50);
        c.webPanel1.setSize(webPanel8.getSize());
        webPanel8.add(c.webPanel1);
        c.webPanel1.setVisible(true);
        
        
        display=c;
        display.jumpPage(0);
        
    }

    public void windowClose() {
        
        runnable=false;
        queue.clean();
        try {
            file.closeFile();
        } catch (IOException ex) {
            
        } catch (InterruptedException ex) {
            
        }
        System.gc();
        System.exit(pages);
        
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
        webPanel2 = new com.alee.laf.panel.WebPanel();
        webPanel5 = new com.alee.laf.panel.WebPanel();
        webToolBar1 = new com.alee.laf.toolbar.WebToolBar();
        webButton3 = new com.alee.laf.button.WebButton();
        webToggleButton1 = new com.alee.laf.button.WebToggleButton();
        webComboBox2 = new com.alee.laf.combobox.WebComboBox();
        webComboBox1 = new com.alee.laf.combobox.WebComboBox();
        webButton4 = new com.alee.laf.button.WebButton();
        webTextField1 = new com.alee.laf.text.WebTextField();
        webTextField2 = new com.alee.laf.text.WebTextField();
        webButton2 = new com.alee.laf.button.WebButton();
        webButton1 = new com.alee.laf.button.WebButton();
        webToolBar2 = new com.alee.laf.toolbar.WebToolBar();
        webToggleButton2 = new com.alee.laf.button.WebToggleButton();
        webPanel3 = new com.alee.laf.panel.WebPanel();
        webPanel4 = new com.alee.laf.panel.WebPanel();
        webPanel6 = new com.alee.laf.panel.WebPanel();
        webPanel7 = new com.alee.laf.panel.WebPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        webTree1 = new com.alee.laf.tree.WebTree();
        webPanel8 = new com.alee.laf.panel.WebPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        webPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 102)));

        webPanel2.setBackground(new java.awt.Color(255, 255, 255));
        webPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        webPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                webPanel2MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                webPanel2MouseExited(evt);
            }
        });

        webPanel5.setBackground(new java.awt.Color(242, 242, 242));

        webToolBar1.setRollover(true);
        webToolBar1.setUndecorated(true);

        webButton3.setText("Open");
        webButton3.setFocusable(false);
        webButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        webButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        webButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webButton3ActionPerformed(evt);
            }
        });
        webToolBar1.add(webButton3);

        webToggleButton1.setText("Split");
        webToggleButton1.setFocusable(false);
        webToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        webToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        webToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webToggleButton1ActionPerformed(evt);
            }
        });
        webToolBar1.add(webToggleButton1);

        webComboBox2.setToolTipText("");
        webComboBox2.setMaximumSize(new java.awt.Dimension(125, 32767));
        webComboBox2.setMinimumSize(new java.awt.Dimension(125, 24));
        webComboBox2.setPreferredSize(new java.awt.Dimension(125, 24));
        webComboBox2.setRound(0);
        webComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webComboBox2ActionPerformed(evt);
            }
        });
        webToolBar1.add(webComboBox2);

        webComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "25%", "50%", "75%", "100%", "125%", "150%", "200%", "250%", "300%", "350%", "Fit Width", "Fit Height" }));
        webComboBox1.setSelectedIndex(3);
        webComboBox1.setToolTipText("");
        webComboBox1.setRound(0);
        webComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webComboBox1ActionPerformed(evt);
            }
        });
        webToolBar1.add(webComboBox1);

        webButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/expand.png"))); // NOI18N
        webButton4.setFocusable(false);
        webButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        webButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        webButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webButton4ActionPerformed(evt);
            }
        });
        webToolBar1.add(webButton4);

        webTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        webTextField1.setText("1");
        webTextField1.setMaximumSize(new java.awt.Dimension(40, 24));
        webTextField1.setMinimumSize(new java.awt.Dimension(40, 24));
        webTextField1.setPreferredSize(new java.awt.Dimension(40, 24));
        webTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webTextField1ActionPerformed(evt);
            }
        });
        webToolBar1.add(webTextField1);

        webTextField2.setEditable(false);
        webTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        webTextField2.setText("1");
        webTextField2.setMaximumSize(new java.awt.Dimension(40, 24));
        webTextField2.setMinimumSize(new java.awt.Dimension(40, 24));
        webTextField2.setPreferredSize(new java.awt.Dimension(40, 24));
        webTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webTextField2ActionPerformed(evt);
            }
        });
        webToolBar1.add(webTextField2);

        webButton2.setText("<");
        webButton2.setFocusable(false);
        webButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        webButton2.setMaximumSize(new java.awt.Dimension(50, 24));
        webButton2.setMinimumSize(new java.awt.Dimension(50, 24));
        webButton2.setPreferredSize(new java.awt.Dimension(50, 24));
        webButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        webButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webButton2ActionPerformed(evt);
            }
        });
        webToolBar1.add(webButton2);

        webButton1.setText(">");
        webButton1.setFocusable(false);
        webButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        webButton1.setMaximumSize(new java.awt.Dimension(50, 24));
        webButton1.setMinimumSize(new java.awt.Dimension(50, 24));
        webButton1.setPreferredSize(new java.awt.Dimension(50, 24));
        webButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        webButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webButton1ActionPerformed(evt);
            }
        });
        webToolBar1.add(webButton1);

        webPanel5.add(webToolBar1, java.awt.BorderLayout.CENTER);

        webToolBar2.setFloatable(false);
        webToolBar2.setToolbarStyle(com.alee.laf.toolbar.ToolbarStyle.attached);
        webToolBar2.setUndecorated(true);

        webToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/down.png"))); // NOI18N
        webToggleButton2.setText("Page Content");
        webToggleButton2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        webToggleButton2.setMaximumSize(new java.awt.Dimension(200, 24));
        webToggleButton2.setMinimumSize(new java.awt.Dimension(200, 24));
        webToggleButton2.setPreferredSize(new java.awt.Dimension(200, 24));
        webToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webToggleButton2ActionPerformed(evt);
            }
        });
        webToolBar2.add(webToggleButton2);

        javax.swing.GroupLayout webPanel2Layout = new javax.swing.GroupLayout(webPanel2);
        webPanel2.setLayout(webPanel2Layout);
        webPanel2Layout.setHorizontalGroup(
            webPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, webPanel2Layout.createSequentialGroup()
                .addComponent(webToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(webPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        webPanel2Layout.setVerticalGroup(
            webPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(webPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(webToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        webPanel3.setBackground(new java.awt.Color(51, 51, 51));
        webPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                webPanel3MouseClicked(evt);
            }
        });

        webPanel6.setBackground(new java.awt.Color(204, 255, 204));

        webPanel7.setBackground(new java.awt.Color(200, 200, 200));

        webTree1.setBackground(new java.awt.Color(204, 204, 204));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Page Content");
        webTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        webTree1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        webTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                webTree1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(webTree1);

        webPanel7.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        webPanel8.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout webPanel6Layout = new javax.swing.GroupLayout(webPanel6);
        webPanel6.setLayout(webPanel6Layout);
        webPanel6Layout.setHorizontalGroup(
            webPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(webPanel6Layout.createSequentialGroup()
                .addComponent(webPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(webPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        webPanel6Layout.setVerticalGroup(
            webPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(webPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
            .addComponent(webPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        webPanel4.add(webPanel6, java.awt.BorderLayout.CENTER);

        webPanel3.add(webPanel4, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout webPanel1Layout = new javax.swing.GroupLayout(webPanel1);
        webPanel1.setLayout(webPanel1Layout);
        webPanel1Layout.setHorizontalGroup(
            webPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(webPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(webPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        webPanel1Layout.setVerticalGroup(
            webPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(webPanel1Layout.createSequentialGroup()
                .addComponent(webPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(webPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(webPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(webPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleName("frame1");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void webPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_webPanel2MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount()==2) {
            windowMaximizeOrRestore();
        }
    }//GEN-LAST:event_webPanel2MouseClicked

    private void webPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_webPanel3MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount()==2) {
            windowMaximizeOrRestore();
        }
    }//GEN-LAST:event_webPanel3MouseClicked

    private void webPanel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_webPanel2MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_webPanel2MouseExited

    private void webComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webComboBox1ActionPerformed
        try {
            
            int tmpPage = currentPage;

            switch (webComboBox1.getSelectedIndex()) {

                case 0:
                    display.Zoom(-0.75);
                    break;
                case 1:
                    display.Zoom(-0.5);

                    break;
                case 2:
                    display.Zoom(-0.25);
                    break;
                case 3:
                    display.Zoom(0);
                    break;
                case 4:
                    display.Zoom(0.25);
                    break;
                case 5:
                    display.Zoom(0.5);
                    break;
                case 6:
                    display.Zoom(0.75);
                    break;
                case 7:
                    display.Zoom(1);
                    break;
                case 8:
                    display.Zoom(1.25);
                    break;
                case 9:
                    display.Zoom(1.5);
                    break;
                case 10://fit width
                    display.Zoom((webPanel8.getWidth()/PAGE_WIDTH)-0.5);
                    break;
                case 11://fit height
                    display.Zoom((webPanel8.getHeight()/PAGE_HEIGHT)-0.5);
                    break;
                default:
                display.Zoom(0);
            }
            webPanel8.revalidate();
            jumpPage(tmpPage);
            
        } catch (Exception e) {
        }
    }//GEN-LAST:event_webComboBox1ActionPerformed

    private void webTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webTextField1ActionPerformed
        // TODO add your handling code here:
        jumpPage(Integer.parseInt(webTextField1.getText()));
    }//GEN-LAST:event_webTextField1ActionPerformed

    private void webTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_webTree1ValueChanged
        // TODO add your handling code here:
        try {
            ContentNode node = (ContentNode) webTree1.getSelectedNode();
            jumpPage(node.getPageID()+1);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_webTree1ValueChanged

    private void webComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webComboBox2ActionPerformed
        // TODO add your handling code here:
        switch (webComboBox2.getSelectedIndex()) {
            case 0://single continues
                try {
                    display.fire();
                    CanvasContinuesSingle c = new CanvasContinuesSingle(this);
                    display=c;
                    webPanel8.removeAll();
                    webPanel8.add(c.webPanel1);
                    webPanel8.revalidate();
                } catch (Exception e) {
                }
                break;
                
            case 1://facing continues
                
                try {
                    display.fire();
                    CanvasContinuesFacing c1 = new CanvasContinuesFacing(this);
                    //c.setVisible(true);
                    display=c1;
                    webPanel8.removeAll();
                    webPanel8.add(c1.webPanel1);
                    webPanel8.revalidate();
                } catch (Exception e) {
                }
                    
                break;
                
            case 2://single
                
                try {
                    display.fire();
                    CanvasSingle c2 = new CanvasSingle(this);
                    //c.setVisible(true);
                    display=c2;
                    webPanel8.removeAll();
                    webPanel8.add(c2.webPanel1);
                    webPanel8.revalidate();
                } catch (Exception e) {
                }
                    
                break;
                
            case 3://facing
                
                try {
                    display.fire();
                    CanvasFacing c3 = new CanvasFacing(this);
                    //c.setVisible(true);
                    display=c3;
                    webPanel8.removeAll();
                    webPanel8.add(c3.webPanel1);
                    webPanel8.revalidate();
                } catch (Exception e) {
                }
                    
                break;
                
            case 4://flip facing
                
                try {
                    display.fire();
                    CanvasFlipFacing c4 = new CanvasFlipFacing(this);
                    //c.setVisible(true);
                    display=c4;
                    webPanel8.removeAll();
                    webPanel8.add(c4.webPanel1);
                    webPanel8.revalidate();
                } catch (Exception e) {
                }
                break;
                
            case 5://sketch facing
                
                try {
                    display.fire();
                    CanvasSketchFacing c5 = new CanvasSketchFacing(this);
                    //c.setVisible(true);
                    display=c5;
                    webPanel8.removeAll();
                    webPanel8.add(c5.webPanel1);
                    webPanel8.revalidate();
                } catch (Exception e) {
                }
                    
                break;
                
            default:
                
        }
    }//GEN-LAST:event_webComboBox2ActionPerformed
    
    private void webToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webToggleButton1ActionPerformed
        // TODO add your handling code here:
        if (SEPERATE) {
            SEPERATE=false;
        } else {
            SEPERATE=true;
            display.cleanPage(0);
        }
    }//GEN-LAST:event_webToggleButton1ActionPerformed

    private void webButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webButton3ActionPerformed
        // TODO add your handling code here:
        new OpenPDFFile(this).setVisible(true);
    }//GEN-LAST:event_webButton3ActionPerformed

    private void webButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webButton1ActionPerformed
        // TODO add your handling code here:
        jumpPage(++currentPage);
    }//GEN-LAST:event_webButton1ActionPerformed

    private void webButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webButton2ActionPerformed
        // TODO add your handling code here:
        jumpPage(--currentPage);
    }//GEN-LAST:event_webButton2ActionPerformed

    private void webButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webButton4ActionPerformed
        // TODO add your handling code here:
        windowExpand();
    }//GEN-LAST:event_webButton4ActionPerformed

    private void webToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webToggleButton2ActionPerformed
        // TODO add your handling code here:
        try {
            
            if (LISTDOWN) {
//                try {
//                    webToggleButton2.setIcon(new ImageIcon(getClass().getResource("/images/up.png").toURI().getPath()));
//                } catch (URISyntaxException ex) {
                
//                }
                webPanel7.setVisible(true);
                LISTDOWN=false;
            }else{
//                try {
//                    webToggleButton2.setIcon(new ImageIcon(getClass().getResource("/images/down.png").toURI().getPath()));
//                } catch (URISyntaxException ex) {
                
//                }
                webPanel7.setVisible(false);
                LISTDOWN=true;
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
        revalidate();
    }//GEN-LAST:event_webToggleButton2ActionPerformed

    private void webTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_webTextField2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        WebLookAndFeel.install();
        //</editor-fold>

        /* Create and display the form */
        
        if (args.length>0) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    //new BookReader("H:\\Library\\Books Library\\Encyclopedia\\The Cat Encyclopedia - The Definitive Visual Guide (2014).pdf").setVisible(true);
                    //new BookReader("E:\\Library\\Erotic\\Magazines\\FHM\\FHM Philippines - October 2015.pdf").setVisible(true);
                    new BookReader(args[0]).setVisible(true);
                }
            });
        }else{
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    //new BookReader("H:\\Library\\Books Library\\Encyclopedia\\The Cat Encyclopedia - The Definitive Visual Guide (2014).pdf").setVisible(true);
                    //new BookReader("E:\\Library\\Erotic\\Magazines\\FHM\\FHM Philippines - October 2015.pdf").setVisible(true);
                    new BookReader().setVisible(true);
                }
            });
        }
        
            
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private com.alee.laf.button.WebButton webButton1;
    private com.alee.laf.button.WebButton webButton2;
    private com.alee.laf.button.WebButton webButton3;
    private com.alee.laf.button.WebButton webButton4;
    private com.alee.laf.combobox.WebComboBox webComboBox1;
    private com.alee.laf.combobox.WebComboBox webComboBox2;
    private com.alee.laf.panel.WebPanel webPanel1;
    public com.alee.laf.panel.WebPanel webPanel2;
    private com.alee.laf.panel.WebPanel webPanel3;
    private com.alee.laf.panel.WebPanel webPanel4;
    private com.alee.laf.panel.WebPanel webPanel5;
    private com.alee.laf.panel.WebPanel webPanel6;
    private com.alee.laf.panel.WebPanel webPanel7;
    public com.alee.laf.panel.WebPanel webPanel8;
    public com.alee.laf.text.WebTextField webTextField1;
    public com.alee.laf.text.WebTextField webTextField2;
    private com.alee.laf.button.WebToggleButton webToggleButton1;
    private com.alee.laf.button.WebToggleButton webToggleButton2;
    private com.alee.laf.toolbar.WebToolBar webToolBar1;
    private com.alee.laf.toolbar.WebToolBar webToolBar2;
    private com.alee.laf.tree.WebTree webTree1;
    // End of variables declaration//GEN-END:variables

    private void printpagees(PDPageNode pn) {
        System.out.println("done");
    }
}
