/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookreader;

import java.awt.Image;

/**
 *
 * @author thusitha
 */
interface BookDisplay {
    
    public void Zoom(double ratio);
    public void nextPage();
    public void prevPage();
    public void jumpPage(int pageIndex);
    public void paintPage(int pageIndex, Image img);
    public void cleanPage(int pageIndex);
    public void fire();
}
