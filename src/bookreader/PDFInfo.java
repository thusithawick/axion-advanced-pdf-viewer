package bookreader;

// PDFInfo.java

import APV.AxionPDFFile;
import com.sun.pdfview.OutlineNode;
import com.sun.pdfview.PDFFile;
import de.intarsys.pdf.pd.PDPageNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;


public class PDFInfo{
    
//   public static void main (String [] args) throws IOException{
//      /*if (args.length != 1)
//      {
//          System.err.println ("usage: java PDFInfo pdfspec");
//          return;
//      }*/
//      String s ="H:\\Library\\Books Library\\Encyclopedia\\The Cat Encyclopedia - The Definitive Visual Guide (2014).pdf";
//      RandomAccessFile raf = new RandomAccessFile (new File (s), "r");
//      FileChannel fc = raf.getChannel ();
//      ByteBuffer buf = fc.map (FileChannel.MapMode.READ_ONLY, 0, fc.size ());
//      PDFFile pdfFile = new PDFFile (buf);
//
//      System.out.println ("Major version = "+pdfFile.getMajorVersion ());
//      System.out.println ("Minor version = "+pdfFile.getMinorVersion ());
//      System.out.println ("Version string = "+pdfFile.getVersionString ()+"\n");
//
//      System.out.println ("Is printable = "+pdfFile.isPrintable ());
//      System.out.println ("Is saveable = "+pdfFile.isSaveable ()+"\n");
//
//      OutlineNode oln = pdfFile.getOutline ();
//      if (oln != null)
//      {
//          System.out.println ("Outline\n");
//          Enumeration e = oln.preorderEnumeration ();
//          while (e.hasMoreElements ())
//          {
//             DefaultMutableTreeNode node;
//             node = (DefaultMutableTreeNode) e.nextElement ();
//             System.out.println (node);
//          }
//      }
//   }
   
//   public DefaultMutableTreeNode getTree(String path, String name) throws FileNotFoundException, IOException{
//   
//      String s =path;
//      RandomAccessFile raf = new RandomAccessFile (new File (s), "r");
//      FileChannel fc = raf.getChannel ();
//      ByteBuffer buf = fc.map (FileChannel.MapMode.READ_ONLY, 0, fc.size ());
//      PDFFile pdfFile = new PDFFile (buf);
//
//
//      DefaultMutableTreeNode mainNode=null;
//      OutlineNode oln = pdfFile.getOutline ();
//      if (oln != null){
//          mainNode = new DefaultMutableTreeNode(name);
//          Enumeration e = oln.preorderEnumeration ();
//          cal(e, mainNode);
//      }
//      return mainNode;
//      
//   }
   private String gets(int l){
       String s = new String();
       for (int i = 0; i < (l-1)*4; i++) {
           s+="-";
       }
       return s;
   }
   
   int j,k;
    public DefaultMutableTreeNode getModel(AxionPDFFile file, String path) throws FileNotFoundException, IOException{
        
        String s =path;
        DefaultMutableTreeNode node0=null;
        RandomAccessFile raf = new RandomAccessFile (new File (s), "r");
        FileChannel fc = raf.getChannel ();
        ByteBuffer buf = fc.map (FileChannel.MapMode.READ_ONLY, 0, fc.size ());
        PDFFile pdfFile = new PDFFile (buf);
        
        OutlineNode oln = pdfFile.getOutline ();
        i=0;
        DefaultMutableTreeNode main = new DefaultMutableTreeNode("Contents");
        if (oln != null){

            System.out.println ("Outline\n");
            Enumeration e = oln.breadthFirstEnumeration();
            cc:
            while (e.hasMoreElements ()){
                node0 = (DefaultMutableTreeNode) e.nextElement ();
                System.out.println (node0);
                String[] nodePath=node0.toString().split(" ");
                int pageNumber=-1;
                    try {
                        pageNumber=Integer.parseInt(nodePath[nodePath.length-1]);
                    } catch (Exception e1) {
                        continue cc;
                    }
                if(pageNumber>=0){
                    ContentNode n = new ContentNode(node0.toString().replaceAll(String.valueOf(pageNumber), ""), pageNumber);
                    main.add(n);
                }else{
                    continue cc;
                }
                i++;
            }
        }
        k=i;
        System.out.println("count1:"+j);
        System.out.println("count2:"+k);
        return main;
    }
   
   int i =0;
   private void cal(PDPageNode node, int level){
       PDPageNode x1=node;
       inner:
       do{
//           System.out.println(gets(level)+x1.isPage());
//           System.out.println(gets(level)+x1.getNodeIndex());
//           System.out.println(gets(level)+x1.getCount());
//           System.out.println(gets(level)+x1);
           
           PDPageNode x2=x1.getFirstNode();
//           System.out.println(gets(level+1)+x2.isPage());
//           System.out.println(gets(level+1)+x2.getNodeIndex());
//           System.out.println(gets(level+1)+x2.getCount());
//           System.out.println(gets(level+1)+x2);
           if (!x2.isPage() && level<3) {
               cal(x2, level+1);
           }else{
//               System.out.println(gets(level+1)+x2.getFirstAnnotation());
               //System.out.println(x2.getGenericChildren());
           }
           if (x2.isValid() && x2.isPage() && level==2) {
               System.out.println(gets(level+1)+x2.getNodeIndex());
               i++;
           }
       }while((x1=x1.getNextNode())!=null);
       
   }
   
    public static void main(String[] args) {
        PDFInfo j = new PDFInfo();
        String path="E:\\Library\\Encyclopedia\\The Knowledge Encyclopedia - 2013.pdf";
       try {
           j.getModel(new AxionPDFFile(path), path);
       } catch (IOException ex) {
           Logger.getLogger(PDFInfo.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
}