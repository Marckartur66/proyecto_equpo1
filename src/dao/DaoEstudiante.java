/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tstudentlate file, choose Tools | Tstudentlates
 * and open the tstudentlate in the editor.
 */
package dao;

import singleton.ConnectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pojo.PojoEstudiante;
import dao.IDaoGeneral;
import java.util.logging.Level;
import java.util.logging.Logger;
import singleton.TransactionDB;

public class DaoEstudiante implements IDaoGeneral<PojoEstudiante, String>{
    
    private PreparedStatement  preparedStatement;
    private ConnectDB connection ;
    private List <PojoEstudiante> ls;
    private PojoEstudiante student;

    public DaoEstudiante() throws SQLException {
        connection = ConnectDB.getInstance();        
    }        
    
    private final String[] QUERIES = {
        "INSERT INTO students (std_enrollment, std_name, std_lastname, std_career, crs_id) VALUES (?, ?, ?, ?, ?)",
        "DELETE FROM students WHERE std_enrollment =  ?",
        "SELECT * FROM students WHERE std_enrollment = ?",
        "SELECT * FROM students",
        "UPDATE students SET std_career=? WHERE (std_enrollment = ?)"
    };

    @Override
    public boolean create(PojoEstudiante student) throws SQLException {
        boolean response;
        TransactionDB t;
        
        t = new TransactionDB<PojoEstudiante>(student) {
            @Override
            public boolean execute(Connection connection) {
                boolean response = false;
                
                try {
                    preparedStatement = connection.prepareStatement(QUERIES[0]); 
        
                    preparedStatement.setString(1, student.getInscripcion()); 
                    preparedStatement.setString(2, student.getNombre()); 
                    preparedStatement.setString(3, student.getApellido()); 
                    preparedStatement.setString(4, student.getCarrera()); 
                    preparedStatement.setInt(5, student.getCurso_id()); 

                    preparedStatement.executeUpdate(); 
                    
                    System.out.println("PojoCourse guardada");
                     
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

    public PojoEstudiante readSingle(String enrollment) throws SQLException {
        boolean response;
        TransactionDB t;    
        
        t = new TransactionDB<PojoEstudiante>(){
           @Override
           public boolean execute(Connection connection) {
               boolean response = false;
               
               try {
                    preparedStatement = connection.prepareStatement(QUERIES[2]); 
  
                    preparedStatement.setString(1, enrollment);        
                    ResultSet rs = preparedStatement.executeQuery(); 
                    student = new PojoEstudiante(); 

                    while (rs.next()) { 
                        student.setInscripcion(rs.getString("std_enrollment"));             
                        student.setNombre(rs.getString("std_name")); 
                        student.setApellido(rs.getString("std_lastname")); 
                        student.setCarrera(rs.getString("std_career")); 
                        student.setCurso_id(rs.getInt("crs_id"));
                    } 
                    
                    response = true;
               } catch (SQLException ex) {
                   Logger.getLogger(DaoCurso.class.getName()).log(Level.SEVERE, null, ex);
               }
               
               return response;
           }  
        };
        connection.execute(t);
        return student;
    }

    @Override
    public List<PojoEstudiante> readAll() throws SQLException {
        TransactionDB t;
        ls = new ArrayList<>();
                
        t = new TransactionDB<PojoEstudiante>() {
            @Override
            public boolean execute(Connection connection) {
                boolean response = false;
                try {
                    PojoEstudiante student;
                    
                    preparedStatement = connection.prepareStatement(QUERIES[3]); 
                    ResultSet rs = preparedStatement.executeQuery();
                   
                    while (rs.next()){
                        student = new PojoEstudiante();
                        student.setInscripcion(rs.getString("std_enrollment"));             
                        student.setNombre(rs.getString("std_name")); 
                        student.setApellido(rs.getString("std_lastname")); 
                        student.setCarrera(rs.getString("std_career")); 
                        student.setCurso_id(rs.getInt("crs_id"));

                        ls.add(student);
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
    public boolean update(PojoEstudiante student, String enrollment) throws SQLException {        
         TransactionDB t;
        boolean response;
        
        t = new TransactionDB<PojoEstudiante>(student){
            @Override
            public boolean execute(Connection connection) {
                boolean response = false;
                try {
                    preparedStatement = connection.prepareStatement(QUERIES[4]); 
        
                    System.out.println(student.getCarrera());

                    preparedStatement.setString(1, student.getCarrera()); 
                    preparedStatement.setString(2, enrollment);

                    preparedStatement.executeUpdate();

                    System.out.println("Actualizado");
                    
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
