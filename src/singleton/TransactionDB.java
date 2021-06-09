/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package singleton;

import java.sql.Connection;


public abstract class TransactionDB<T> {
    protected T pojo;

    public TransactionDB(){}
    
    public TransactionDB(T pojo) {
        this.pojo = pojo;
    }    
    
    public abstract boolean execute(Connection connection);
        
}
