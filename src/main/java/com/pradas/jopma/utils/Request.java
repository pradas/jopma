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

    /**
     * Constructor to make a request object
     * @param url Request URL
     * @param type Request type
     * @param headers Request headers
     * @param parameters Request parameters
     * @throws MalformedURLException
     */
    public Request(String url, String type, String headers, String parameters) throws MalformedURLException {
        super();

        //Create the array type
        admitedType = new ArrayList<>();
        admitedType.add("GET");
        admitedType.add("POST");
        admitedType.add("PUT");
        admitedType.add("DELETE");

        this.url = url;
        setType(type);
        setHeaders(headers);
        setParameters(parameters);

        code = -1;
        result = "";
    }

    /**
     * Sets the type of the request.
     * @param type The type must be GET, POST, PUT or DELETE. Otherwise an exception is thrown
     * @throws MalformedURLException
     */
    private void setType(String type) throws MalformedURLException {
        if (admitedType.contains(type))
            this.type = type;
        else
            throw new MalformedURLException();
    }

    /**
     * Sets the headers of the request.
     * @param headers An string containing the headers of the request, each header must follow the sintax key=value;
     */
    private void setHeaders(String headers) {
        this.headers = StringUtils.stringToHashMap(headers, "=", ";");
    }

    /**
     * Sets the parameters of the request.
     * @param parameters An string containing the parameters of the request, each parameter must follow the sintax key=value;
     */
    private void setParameters(String parameters) {
        this.parameters = StringUtils.stringToHashMap(parameters, "=", ";");
    }

    /**
     * The code returned by the request.
     * @return Return 0 if the request has not been done or the request code response.
     */
    public Integer getCode() {
        return code;
    }

    /**
     * The result returned by the request.
     * @return Return void string if the request has not been done or the request response.
     */
    public String getResult() {
        return result;
    }

    /**
     * Do the request with the configured attributes.
     * @return Return the request response.
     */
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

            //If is not a GET request, we specify that we want to send data
            if (!type.equals("GET")) {
                con.setDoOutput(true);
                out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(StringUtils.hashMapToString(parameters, "=", "&"));
                out.flush();
            }

            //The request is done here.
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
