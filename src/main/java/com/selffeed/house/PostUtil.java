package com.selffeed.house;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Component
public class PostUtil {
    public static String houseZJJUrl = "http://zjj.sz.gov.cn/szfdcscjy/projectPublish/getHouseInfoListToPublicity";
    public String checkUrl(int preSellId, int ysProjectId, String fybId, String buildingBranch){
        try {
            // Define the URL you want to send the POST request to

            // Create a JSON payload as a string
            //{"buildingbranch":"","floor":"","fybId":"47820","housenb":"","status":0,"type":"",
            // "ysProjectId":29042,"preSellId":127569}
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

//            String jsonString = "{\"buildingbranch\":\"二单元\",\"floor\":\"\",\"fybId\":\"47820\",\"housenb\":\"\",\"status\":-1,\"type\":\"\",\"ysProjectId\":29042,\"preSellId\":127569}";
            String jsonString = "{\"buildingbranch\":\"二单元\",\"floor\":\"\",\"fybId\":\"47820\",\"housenb\":\"\",\"status\":-1,\"type\":\"\",\"ysProjectId\":29042,\"preSellId\":127569}";

            log.info("json arg:::");
//            log.info(jsonPayload);
            log.info(jsonString);
            byte[] utf8Bytes = jsonString.getBytes("UTF-8");
            jsonString = new String(utf8Bytes, "UTF-8");
            log.info("======");
            log.info(jsonString);


//            jsonString = new String(jsonString,"UTF-8");

            // Create a URL object
            URL obj = new URL(houseZJJUrl);

            // Open a connection to the URL
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Set the request method to POST
            con.setRequestMethod("POST");

            // Set the request headers
            con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            con.setRequestProperty("Cookie", "WSESSIONID-SZFDC-SCJY=q7BwXQzI9DU8nRj6UjLVbi2meT_Isk2sCP3NuWlxx6XqpZ9AP-sb!-1501610934; _trs_uv=lnljsna1_850_fltx; pgv_pvi=5195133952; szfdc-session-id=a1c5ca15-a276-4145-bb1d-94c363eace21; cookie_3.36_8080=85416332; AD_insert_cookie_89188=71376594");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");

            // Enable input and output streams for the connection
            con.setDoOutput(true);

            // Write the JSON payload to the output stream
            try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
//                out.writeBytes(jsonPayload);
//                out.writeBytes(jsonString);
                out.writeUTF(jsonString);
                out.flush();
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

                    // Print the JSON response
//                    System.out.println("Response JSON: " + response.toString());
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
