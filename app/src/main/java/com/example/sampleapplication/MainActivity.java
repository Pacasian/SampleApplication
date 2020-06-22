package com.example.sampleapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
public static String sd="hai";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent=new Intent(this, NotificationService.class);
        startService(intent);

    }

    /**
    public class CheckTables extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

        }

        @SuppressLint("SetTextI18n")
        @Override
        protected String doInBackground(String... params) {
            // @SuppressLint("WrongThread") String usernam = userTv.getText().toString();
            //@SuppressLint("WrongThread") String passwordd = passwordTv.getText().toString();

            try {
                con = new ConnectionClass().CONN();

                // Connect to database
                if (con == null) {
                    z = "Check Your Internet Access!";
                } else {
                    System.out.println("----------------------------------------");
                    String query = "select count(*) from mailTable ;";
                    //String query = "SELECT * FROM ipo ;";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    //ResultSetMetaData metaData=rs.getMetaData();
                    //int rowCount=metaData.getColumnCount();
                    rs.next();
                    //Moving the cursor to the last row
                     System.out.println("Table contains "+rs.getInt("count(mailID)")+" rows");
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage();
            }

            return z;


        }

    }
        */
}