package com.ichat.wannibu.ichat.WebServer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class Check
{
    public String check(String key, String value, String request) {
        String response="";
        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("http://106.54.72.253/"+request);//服务器地址，指向Servlet
            List<NameValuePair> params=new ArrayList<NameValuePair>();//将id和pw装入list
            params.add(new BasicNameValuePair(key,value));
            final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");//以UTF-8格式发送
            httpPost.setEntity(entity);
            HttpResponse httpResponse= httpclient.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode()==200)
            {
                HttpEntity entity1=httpResponse.getEntity();
                response = EntityUtils.toString(entity1, "utf-8");//以UTF-8格式解析
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public String check(String key, String value, String key2,String value2, String request) {
        String response="";
        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("http://106.54.72.253/"+request);//服务器地址，指向Servlet
            List<NameValuePair> params=new ArrayList<NameValuePair>();//将id和pw装入list
            params.add(new BasicNameValuePair(key,value));
            params.add(new BasicNameValuePair(key2,value2));
            final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");//以UTF-8格式发送
            httpPost.setEntity(entity);
            HttpResponse httpResponse= httpclient.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode()==200)
            {
                HttpEntity entity1=httpResponse.getEntity();
                response = EntityUtils.toString(entity1, "utf-8");//以UTF-8格式解析
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public String check(String key, String value, String key2,String value2, String key3,String value3, String request) {
        String response="";
        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("http://106.54.72.253/"+request);//服务器地址，指向Servlet
            List<NameValuePair> params=new ArrayList<NameValuePair>();//将id和pw装入list
            params.add(new BasicNameValuePair(key,value));
            params.add(new BasicNameValuePair(key2,value2));
            params.add(new BasicNameValuePair(key3,value3));
            final UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");//以UTF-8格式发送
            httpPost.setEntity(entity);
            HttpResponse httpResponse= httpclient.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode()==200)
            {
                HttpEntity entity1=httpResponse.getEntity();
                response = EntityUtils.toString(entity1, "utf-8");//以UTF-8格式解析
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
