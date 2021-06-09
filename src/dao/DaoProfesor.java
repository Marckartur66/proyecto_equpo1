/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojo.PojoProfesor;
import singleton.ConnectDB;
import singleton.TransactionDB;


public class DaoProfesor implements IDaoGeneral<PojoProfesor, String> {
    
    private PreparedStatement  preparedStatement;
    private ConnectDB connection ;
    private List <PojoProfesor> ls;
    private PojoProfesor professor;

    public DaoProfesor() throws SQLException {
        connection = ConnectDB.getInstance();        
    } 
    
   private final String[] QUERIES = {
        "INSERT INTO professors (pfs_idcard, pfs_name, pfs_lastname, pfs_career, crs_id) VALUES (?, ?, ?, ?, ?)",
        "DELETE FROM professors WHERE pfs_idcard = ?",
        "SELECT * FROM professors WHERE pfs_idcard = ?",
        "SELECT * FROM professors",
        "UPDATE professors SET pfs_career = ? WHERE (pfs_idcard = ?)"
    };

    @Override
    public boolean create(PojoProfesor professor) throws SQLException {
        boolean response;
        TransactionDB t;
        
        t = new TransactionDB<PojoProfesor>(professor) {
            @Override
            public boolean execute(Connection connection) {
                boolean response = false;
                
                try {
                    preparedStatement = connection.prepareStatement(QUERIES[0]); 
        
                    preparedStatement.setString(1, professor.getIdCard()); 
                    preparedStatement.setString(2, professor.getNombre()); 
                    preparedStatement.setString(3, professor.getApellido()); 
                    preparedStatement.setString(4, professor.getCarrera()); 
                    preparedStatement.setInt(5, professor.getCurso_id()); 

                    preparedStatement.executeUpdate(); 
                    
                    System.out.println("PojoCourse guardada");
                     
                     response = true;                     
                 } catch (SQLException ex) {
                     Logger.getLogger(DaoProfesor.class.getName()).log(Level.SEVERE, null, ex);
                 }                 
                 return response;
             }
         };         
         response = connection.execute(t);
         return response;               
    }

    
    @Override
    public PojoProfesor readSingle(String id) throws SQLException {
         boolean response;
        TransactionDB t;    
        
        t = new TransactionDB<PojoProfesor>(){
           @Override
           public boolean execute(Connection connection) {
               boolean response = false;
               
               try {
                    preparedStatement = connection.prepareStatement(QUERIES[2]); 
  
                    preparedStatement.setString(1, id);        
                    ResultSet rs = preparedStatement.executeQuery(); 
                    professor = new PojoProfesor(); 

                    while (rs.next()) { 
                        professor.setIdCard(rs.getString("pfs_idcard"));             
                        professor.setNombre(rs.getString("pfs_name")); 
                        professor.setApellido(rs.getString("pfs_lastname")); 
                        professor.setCarrera(rs.getString("pfs_career")); 
                        professor.setCurso_id(rs.getInt("crs_id"));
                    } 
                    
                    response = true;
               } catch (SQLException ex) {
                   Logger.getLogger(DaoCurso.class.getName()).log(Level.SEVERE, null, ex);
               }
               
               return response;
           }  
        };
        connection.execute(t);  
        return professor;
    }

    @Override
    public List<PojoProfesor> readAll() throws SQLException {
        TransactionDB t;
        ls = new ArrayList<>();
                
        t = new TransactionDB<PojoProfesor>() {
            @Override
            public boolean execute(Connection connection) {
                boolean response = false;
                try {
                    PojoProfesor course;
                    
                    preparedStatement = connection.prepareStatement(QUERIES[3]); 
                    ResultSet rs = preparedStatement.executeQuery();
                    PojoProfesor professor; 

                    while (rs.next()){
                        professor = new PojoProfesor();
                        professor.setIdCard(rs.getString("pfs_idcard"));             
                        professor.setNombre(rs.getString("pfs_name")); 
                        professor.setApellido(rs.getString("pfs_lastname")); 
                        professor.setCarrera(rs.getString("pfs_career")); 
                        professor.setCurso_id(rs.getInt("crs_id"));

                        ls.add(professor);
                    } 
        
                    response = true;
                    return response;
                } catch (SQLException ex) {
                    Logger.getLogger(DaoCurso.class.getName()).log(Level.SEVERE, null, ex);
                }
                return response;
            }            
        };
         connection.execute(t);        
         return ls;
    }

    @Override
    public boolean update(PojoProfesor professor, String id) throws SQLException {      
         TransactionDB t;
        boolean response;
        
        t = new TransactionDB<PojoProfesor>(professor){
            @Override
            public boolean execute(Connection connection) {
                boolean response = false;
                try {
                    preparedStatement = connection.prepareStatement(QUERIES[4]); 
        
                    preparedStatement.setString(1, professor.getCarrera()); 
                    preparedStatement.setString(2, id);

                    preparedStatement.executeUpdate();
                    
                    response = true;                    
                } catch (SQLException ex) {
                    Logger.getLogger(DaoCurso.class.getName()).log(Level.SEVERE, null, ex);
                }
                return response;
            }            
        };
        response = connection.execute(t);
        return response;
    }
    
}
