package com.liticia.paymybuddy.integration.service;

import com.liticia.paymybuddy.integration.config.DataBaseTestConfig;
import org.mockito.internal.matchers.Null;

import java.sql.Connection;

public class DataBasePrepareService {
    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    public void clearDataBaseEntries(){
        Connection connection = null;
        try{
            connection = dataBaseTestConfig.getConnection();

            //insert into bankAccount
            connection.prepareStatement("INSERT INTO `bankaccount` (`id`, `account_number`, `user_id`, `description`, `created_at`, `active`) VALUES (NULL, 'IU24BONE', '1', 'momo', '2023-03-07 16:45:43.000000', '0')").execute();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }
}
