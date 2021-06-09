/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tstudentlate file, choose Tools | Tstudentlates
 * and open the tstudentlate in the editor.
 */
package main;

import java.sql.SQLException;
import Visual_interface.PrincipalView;


public class Main {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
                
        PrincipalView view = new PrincipalView();
        view.setVisible(true);        
            
    }
    
}
