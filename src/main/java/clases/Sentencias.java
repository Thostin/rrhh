/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package clases;

import java.util.ArrayList;

/**
 *
 * @author thotstin
 */
public interface Sentencias {
    // To insert a neqw column
    public boolean insert();
    
    // To read all the data from the database
    public ArrayList read();
    
    // To update liked object from database
    public boolean update();
    
    // To delete linked object from database
    public boolean delete();
}
