package com.ixitrip.chandan.ixigotrip.internetComm;

import android.os.AsyncTask;
import android.util.Log;

import com.ixitrip.chandan.ixigotrip.background.SearchList;
import com.ixitrip.chandan.ixigotrip.response.SearchResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by chandan on 4/8/2017.
 */

public class SearchAsyncTask extends AsyncTask<String,Integer,SearchResponse> {
    String responseStr;



    @Override
    protected SearchResponse doInBackground(String... params)
    {
        // TODO Auto-generated method stub

        responseStr = postDataGetStream(params);
        SearchResponse response = null;


        try
        {

            response = parse(responseStr);
        }
        catch(Exception e){
            e.printStackTrace();
        }


        return response;
    }

    @Override
    protected void onPostExecute(SearchResponse response)
    {

    }

    @Override
    protected void onProgressUpdate(Integer... progress)
    {
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    public String postDataGetStream(String... param)
    {
        String data = "";
        try {
            data= downloadUrl("http://build2.ixigo.com/action/content/zeus/autocomplete?searchFor=tpAutoComplete&neCategories=City&query=query");

        }catch (IOException e)
        {
            Log.d("exception here",""+e.toString());
        }
        return data;
    }


    public SearchResponse parse(String response) {
        ArrayList<SearchList> itemList = new ArrayList<>();
        String cityName="";
        String countryName="";
       String state="";
        String cityId="";
        String xid="";
        String id="";
        String address="";
        try {

            JSONObject reader = new JSONObject(response);
           Log.d("serchres",""+response);
            JSONArray search  = reader.getJSONArray("data");
            //Log.d("total order output","lll");
            // itemList.add(new AllocateList("id", "lastid", "orderDate", "deliveryDate", "price", "status"));

            for(int i=0; i< search.length(); i++) {
                JSONObject orderData = search.getJSONObject(i);

                cityName = orderData.getString("cityName");
                address = orderData.getString("address");
                countryName = orderData.getString("countryName");
                state = orderData.getString("state");
                xid=orderData.getString("xid");
                id=orderData.getString("id");
                cityId = orderData.getString("cityId");

                itemList.add(new SearchList(address,cityName,countryName, state,xid, id, cityId));

            }


        } catch (Exception e) {
            Log.d("hhhhhhh", "parse ");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new SearchResponse(itemList);
    }
}
