/*
* Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package com.wso2.org;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.Header;





public class GetCommentsCount {

    private static String Tokenkey = "";



    public static void main(String[] args) throws  ParseException {
        try {
            getCommentsCount();

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }
    //Done with pagination using split
    public static Map<String, String> splitLinkHeader(String header){
        String[] parts = header.split(",");
        Map <String, String> map = new HashMap<String, String>();
        for(int i = 0; i < parts.length; i++){
            String[] sections = parts[i].split(";");
            String PaginationUrl = sections[0].replaceFirst("<(.*)>", "$1");
            String urlPagChange =  PaginationUrl.trim();
            String name = sections[1].substring(6, sections[1].length() - 1);
            map.put(name, urlPagChange);
        }

        return map;
    }


    public static void getCommentsCount() throws ParseException, SQLException {
        String baseURL = "https://api.github.com";
        String url = "https://api.github.com/orgs/wso2/repos";

        try {

            CloseableHttpClient httpClient1 = HttpClientBuilder.create().build();
            HttpGet request1 = new HttpGet(url);
            request1.addHeader("content-type", "application/json");
            request1.addHeader("Authorization", "Bearer " + Tokenkey);
            HttpResponse resultNext = httpClient1.execute(request1);

            String repo_json = EntityUtils.toString(resultNext.getEntity(), "UTF-8");


            JsonElement findRepo = new JsonParser().parse(repo_json);
            JsonArray GetRepo = findRepo.getAsJsonArray();

            boolean containsNext = true;

            while(containsNext){

                if (resultNext.containsHeader("Link")){

                    Header[] linkHeader = resultNext.getHeaders("Link");
                    Map<String, String> linkMap = splitLinkHeader(linkHeader[0].getValue());

                    System.out.println(linkMap.get("next"));

                    try{
                        HttpGet requestForNext = new HttpGet(linkMap.get("next"));
                        requestForNext.addHeader("content-type", "application/json");
                        requestForNext.addHeader("Authorization", "Bearer " + Tokenkey);
                        HttpResponse httpResponse = resultNext = httpClient1.execute(requestForNext);

                        String repo_json_next = EntityUtils.toString(resultNext.getEntity(), "UTF-8");
                        JsonElement jelementNext = new JsonParser().parse(repo_json_next);
                        JsonArray jarrNext = jelementNext.getAsJsonArray();
                        GetRepo.addAll(jarrNext);
                    }
                    catch(Exception e){
                        containsNext = false;
                    }




                } else {
                    containsNext = false;
                }

            }


            for (int i = 0; i <GetRepo.size(); i++) {
                JsonObject jo1 = (JsonObject) GetRepo.get(i);

                String RepoName = jo1.get("name").toString();
                RepoName = RepoName.substring(1, RepoName.length()-1);
                System.out.println(RepoName);



                CloseableHttpClient httpClient2 = HttpClientBuilder.create().build();
                HttpGet request2 = new HttpGet(baseURL + "/repos/wso2/"+RepoName+"/issues/comments");
                request2.addHeader("Authorization", "Bearer " + Tokenkey);
                HttpResponse result2 = httpClient2.execute(request2);
                String issues_json = EntityUtils.toString(result2.getEntity(), "UTF-8");



                JsonElement CountIssues = new JsonParser().parse(issues_json);
                JsonArray getIssues = CountIssues.getAsJsonArray();

//                int NoOfIssuesComment = getIssues.size();
//                System.out.println("No of issues: " +NoOfIssuesComment);
//                System.out.println();



                for (int y = 0; y < getIssues.size(); y++) {
                    JsonObject jo2 = (JsonObject) getIssues.get(y);


                    String commentsUrl = jo2.getAsJsonObject("user").get("login").toString();
                    commentsUrl = commentsUrl.substring(1, commentsUrl.length() - 1);
                    System.out.println("Comments by: " + commentsUrl);

                    String Url = jo2.get("url").toString();
                    Url = Url.substring(1, Url.length() - 1);
                    System.out.println("Comments by: " + Url);




                }


            }


        }

        catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }



    }



}

