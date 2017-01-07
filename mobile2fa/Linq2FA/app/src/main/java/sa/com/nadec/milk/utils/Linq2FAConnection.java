package sa.com.nadec.milk.utils;


import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by snouto on 22/05/15.
 */
public class Linq2FAConnection {


    public static final String GET_HTTP_METHOD="GET";
    public static final String POST_HTTP_METHOD="POST";
    public static final String DEFAULT_PORT = "8080";
    //TODO:<Network> has been modified
    public static final String DEFAULT_BASEPATH="imapp";
    public static final String DEFAULT_RESTFUL_PATH="rest";
    private String hostName="127.0.0.1";
    private String port="8080";
    private String basePath="alfahres";
    private String restfulPath = "rest";
    private StringBuffer appendablePath;
    private List<Parameter> headers;
    private String methodType = "GET";
    private HttpURLConnection connection;
    private Object dataPayload;

    public Linq2FAConnection(String hostName, String port, String basePath, String restfulPath){

        this.setHostName(hostName);
        this.setPort(port);
        this.setBasePath(basePath);
        this.setRestfulPath(restfulPath);

    }

    public Linq2FAConnection(){}

    private String getCompleteUrl()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("http://").append(hostName).append(":")
                .append(port).append(basePath);

        return buffer.toString();
    }


    public Linq2FAConnection path(String path)
    {
        if(appendablePath == null)
        {
            //that means the string buffer is empty
            //so initialize it and append the complete Url to it
            appendablePath = new StringBuffer();
            appendablePath.append(getCompleteUrl());
        }
        if(path != null)
            appendablePath.append(path);

        return this;
    }

    public Linq2FAConnection setHeaders(Parameter[] headers)
    {
        if(this.headers == null)
            this.headers = new ArrayList<Parameter>();

        for(Parameter param : headers)
        {
            this.headers.add(param);
        }

        return this;
    }

    public Linq2FAConnection addHeader(Parameter header)
    {
        if(this.headers == null)
            this.headers = new ArrayList<Parameter>();

        this.headers.add(header);

        return this;

    }

    private void addHeaders()
    {
        if(this.headers != null && this.headers.size() > 0)
        {
            for(Parameter param : this.headers)
            {
                connection.setRequestProperty(param.getName(),param.getValue().toString());
            }
        }
    }

    public Linq2FAConnection method(String type)
    {
        this.setMethodType(type);
        return this;

    }

    public Linq2FAConnection setAuthorization(String userName , String password)
    {
        Parameter param = new Parameter("Authorization",setAuth(userName,password));

        this.addHeader(param);

        return this;
    }



    private Object setAuth(String userName, String password) {

       try
       {
           String combined = String.format("%s:%s", userName, password);

           String encoded = Base64.encodeToString(combined.getBytes("UTF-8"), Base64.DEFAULT);


           StringBuffer buff = new StringBuffer();
           buff.append("Basic").append(" ").append(encoded);

           return buff.toString();

       }catch (Exception s)
       {
           Log.e("Linq2FAConnection", s.getMessage());

           return "";

       }


    }

    public Linq2FAConnection setBody(Object data)
    {
        this.dataPayload = data;
        return this;
    }


    public HttpResponse call(Class<?> entity)
    {
        HttpResponse response = new HttpResponse();

        try
        {
            URL connectionUrl = new URL(appendablePath.toString());

            connection = (HttpURLConnection)connectionUrl.openConnection();

            connection.setRequestMethod(getMethodType());


            //Add the body payload

            if(this.dataPayload != null)
            {
                //Add the content-type flag
                this.addHeader(new Parameter("content-type","application/json;charset=UTF-8"));
                this.addHeaders();
                //convert the payload into json string
                Gson gson = new GsonBuilder().create();

                String jsonPayload = gson.toJson(this.dataPayload);

                OutputStream outputStream = connection.getOutputStream();

                byte[] binaryPayload = jsonPayload.getBytes("UTF-8");

                outputStream.write(binaryPayload);

            }else
            this.addHeaders();


            int responseCode = connection.getResponseCode();

            response.setResponseCode(String.valueOf(responseCode));

            if(responseCode == HttpURLConnection.HTTP_OK) {
                    //parse the response Stream
                    Object result = this.parseResponseStream(connection.getInputStream(),entity);

                    response.setPayload(result);

            }else
            {
                response.setPayload(connection.getResponseMessage());
            }

            connection.disconnect();

            this.clearInstance();

            return response;


        }
        catch (IOException s)
        {
            response.setResponseCode("401");
            response.setPayload("You are Not Authorized To access the system , Contact System Administrator");

            return response;
        }
        catch (Exception s)
        {
            s.printStackTrace();
            return null;
        }
    }




    public HttpResponse call(Type entity)
    {
        HttpResponse response = new HttpResponse();

        try
        {
            URL connectionUrl = new URL(appendablePath.toString());

            connection = (HttpURLConnection)connectionUrl.openConnection();

            connection.setRequestMethod(getMethodType());
            //Add the body payload

            if(this.dataPayload != null)
            {
                //Add the content-type flag
                this.addHeader(new Parameter("Content-Type","application/json"));
                //End of the body payload
                this.addHeaders();
                //convert the payload into json string
                Gson gson = new GsonBuilder().create();

                String jsonPayload = gson.toJson(this.dataPayload);

                OutputStream outputStream = connection.getOutputStream();

                byte[] binaryPayload = jsonPayload.getBytes("UTF-8");

                outputStream.write(binaryPayload);


            }else

             this.addHeaders();


            int responseCode = connection.getResponseCode();

            response.setResponseCode(String.valueOf(responseCode));

            if(responseCode == HttpURLConnection.HTTP_OK) {
                //parse the response Stream
                Object result = this.parseResponseStream(connection.getInputStream(),entity);

                response.setPayload(result);

            }else
            {
                response.setPayload(connection.getResponseMessage());
            }

            connection.disconnect();

            this.clearInstance();

            return response;


        }catch (Exception s)
        {
            s.printStackTrace();
            return null;
        }
    }

    private void clearInstance()
    {
        this.setMethodType(GET_HTTP_METHOD);
        this.headers = null;
        this.connection = null;
        this.appendablePath = new StringBuffer();
        this.setRestfulPath(DEFAULT_RESTFUL_PATH);
        this.setBasePath(DEFAULT_BASEPATH);
        this.setPort(DEFAULT_PORT);
    }

    private Object parseResponseStream(InputStream inputStream,Class<?> entity) {

        try
        {
            Gson gson = new GsonBuilder().create();

            InputStreamReader reader = new InputStreamReader(inputStream);

            BufferedReader bReader = new BufferedReader(reader);

            StringBuffer buff = new StringBuffer();

            String line = null;

            while((line=bReader.readLine()) != null)
            {
                buff.append(line);
            }

            String json = buff.toString();

            return gson.fromJson(json,entity);


        }catch (Exception s)
        {
            Log.e("Linq2FAConnection", s.getMessage());
            return null;
        }

    }


    private Object parseResponseStream(InputStream inputStream,Type entity) {

        try
        {
            Gson gson = new GsonBuilder().create();

            InputStreamReader reader = new InputStreamReader(inputStream);

            BufferedReader bReader = new BufferedReader(reader);

            StringBuffer buff = new StringBuffer();

            String line = null;

            while((line=bReader.readLine()) != null)
            {
                buff.append(line);
            }

            String json = buff.toString();

            return gson.fromJson(json,entity);


        }catch (Exception s)
        {
            Log.e("Linq2FAConnection", s.getMessage());
            return null;
        }

    }


    public String getHostName() {
        return hostName;
    }

    public Linq2FAConnection setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public String getPort() {
        return port;
    }

    public Linq2FAConnection setPort(String port) {
        this.port = port;
        return this;
    }

    public String getBasePath() {
        return basePath;
    }

    public Linq2FAConnection setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public String getRestfulPath() {
        return restfulPath;
    }

    public Linq2FAConnection setRestfulPath(String restfulPath) {
        this.restfulPath = restfulPath;
        return this;
    }

    public String getMethodType() {
        return methodType;
    }

    public Linq2FAConnection setMethodType(String methodType) {
        this.methodType = methodType;
        return this;
    }
}
