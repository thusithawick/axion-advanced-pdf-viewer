/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookreader;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author thusitha
 */
public class ContentNode extends DefaultMutableTreeNode{
    
    int pageID;
    
    public ContentNode(int id) {
        this.pageID=id;
    }

    public ContentNode(Object userObject, int id) {
        super(userObject);
        this.pageID=id;
    }

    public ContentNode(Object userObject, boolean allowsChildren, int id) {
        super(userObject, allowsChildren);
        this.pageID=id;
    }

    public int getPageID() {
        return pageID;
    }

    public void setPageID(int pageID) {
        this.pageID = pageID;
    }
    
    
    
}
