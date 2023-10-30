package com.selffeed.house;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Component
public class PostUtil {
    public static String houseZJJUrl = "http://zjj.sz.gov.cn/szfdcscjy/projectPublish/getHouseInfoListToPublicity";
    public String checkUrl(int preSellId, int ysProjectId, String fybId, String buildingBranch){
        try {
            String jsonPayload = "{\"buildingbranch\": \"" +buildingBranch +
                    "\", " +
                    "\"floor\": \"\"," +
                    "\"fybId\": \"" +fybId +
                    "\"," +
                    "\"housenb\": \"\"," +
                    "\"status\": -1," +
                    "\"type\": \"\"," +
                    "\"ysProjectId\": " + ysProjectId +
                    "," +
                    "\"preSellId\": " + preSellId +
                    "" +
//                    "\"key2\": \"value2\"" +
                    "}";
            URL obj = new URL(houseZJJUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");
            con.setRequestProperty("Accept", "application/json");

            // Enable input and output streams for the connection
            con.setDoOutput(true);

            // Write the JSON payload to the output stream
            try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {

                byte[] input = jsonPayload.getBytes("UTF-8");
                out.write(input, 0, input.length);
            }

            // Get the HTTP response code
            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read the JSON response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        log.debug(inputLine);
                        response.append(inputLine);
                    }

                    log.info(response.toString());
                    Gson gson = new Gson();
                    HouseServerOB responseObj = gson.fromJson(response.toString(), HouseServerOB.class);
                    System.out.println("Response Object: " + responseObj.toString());
                    responseObj.checkSellStatus();
                }
            } else {
                System.out.println("POST request failed");
            }

            if(1==1) return "xx";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "xx";
    }
    public static void main(String[] args) {
        log.info("exec post util.");
        new PostUtil().checkUrl(127569,29042,"47820","二单元");
    }
}
