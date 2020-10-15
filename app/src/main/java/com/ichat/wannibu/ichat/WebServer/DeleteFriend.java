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

public class DeleteFriend {
    public String deleteFriend(String email, String pword, String friendmail, String request){
        String response = "";
        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("http://106.54.72.253/"+request);//服务器地址，指向Servlet
            List<NameValuePair> params=new ArrayList<NameValuePair>();//将id和pw装入list
            params.add(new BasicNameValuePair("EMAIL",email));
            params.add(new BasicNameValuePair("PASSW",pword));
            params.add(new BasicNameValuePair("FRIENDEMAIL",friendmail));
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
