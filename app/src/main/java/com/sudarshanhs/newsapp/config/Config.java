package com.sudarshanhs.newsapp.config;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;

/**
 * Created by Sudarshan on 27-Dec-16.
 */
public class Config {

    static  final String TAG="config";

    public static String getXmlFromUrlGet(String url)
    {
        String xml = null;
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            HttpEntity httpEntity = response.getEntity();
            xml = EntityUtils.toString(httpEntity);

        }
        catch (UnsupportedEncodingException e) {
           Log.d(TAG, "Exception  >" + Log.getStackTraceString(e));

        } catch (Exception  e) {
            Log.d(TAG, "Exception  >" + Log.getStackTraceString(e));
        }

        return xml;
    }

    public static org.w3c.dom.Document getDomElement(String xml){
        org.w3c.dom.Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return doc;
    }

    public static String getValue(Element item, String str)
    {
        NodeList n = item.getElementsByTagName(str);
        return Config.getElementValue(n.item(0));
    }

    public static final String getElementValue( Node elem )
    {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

}
