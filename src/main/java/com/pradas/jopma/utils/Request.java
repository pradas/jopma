package main.java.com.pradas.jopma.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private String url;
    private String type;
    private HashMap<String, String> headers;
    private HashMap<String, String> parameters;
    private Integer code;
    private String result;
    private ArrayList<String> admitedType;

    public Request(String url, String type, String headers, String parameters) throws MalformedURLException {
        super();

        startAdmitedTypes();

        setUrl(url);
        setType(type);
        setHeaders(headers);
        setParameters(parameters);
    }

    private void startAdmitedTypes() {
        admitedType = new ArrayList<>();
        admitedType.add("GET");
        admitedType.add("POST");
        admitedType.add("PUT");
        admitedType.add("DELETE");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(String type) throws MalformedURLException {
        if (admitedType.contains(type))
            this.type = type;
        else
            throw new MalformedURLException();
    }

    public void setHeaders(String headers) {
        this.headers = StringUtils.stringToHashMap(headers, "=", ";");
    }

    public void setParameters(String parameters) {
        this.parameters = StringUtils.stringToHashMap(parameters, "=", ";");
    }

    public Integer getCode() {
        return code;
    }

    public String getResult() {
        return result;
    }

    public String doRequest() {
        HttpURLConnection con = null;
        DataOutputStream out = null;
        BufferedReader in = null;

        try {
            //Add the URL
            URL url = new URL(this.url);

            con = (HttpURLConnection) url.openConnection();

            //Add the type
            con.setRequestMethod(type);

            //Add headers map
            for(Map.Entry<String, String> entry : headers.entrySet()){
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }

            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            if (!type.equals("GET")) {
                con.setDoOutput(true);
                out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(StringUtils.getParamsString(parameters));
                out.flush();
            }

            code = con.getResponseCode();
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            con.disconnect();
            result = content.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return getResult();
    }

}
