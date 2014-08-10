package com.shashi.android.androidutility;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class XMLPullParserHandler{

	List<Student> students;
    Student Student;
    //private String text;
    List<Student> students1 ;
    URL url=null;	
  
    public XMLPullParserHandler() 
    {
        students = new ArrayList<Student>();
        students1 = new ArrayList<Student>();
    }
   
    public List<Student> getstudents() 
    {
        return students;
    }
   
    public List<Student> parse(InputStream is,List<Student> students2 ) { //List<Student> parse(InputStream is) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        //String text;  
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
  
            int eventType = parser.getEventType();
             
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
                String tagname = parser.getName();
                switch (eventType) 
                {
                case XmlPullParser.START_TAG:
                	if(tagname.equals("gesmes:Envelope")){
                		break;
                	}else if(tagname.equals("gesmes:Sender")){
                		break;
                	}
                	else if (tagname.equals("Cube")) {
                		Student = new Student();
                	}
                break;
                case XmlPullParser.TEXT:
                	
                    //text = parser.getText();
                    break;
  
                case XmlPullParser.END_TAG:
                	 if (tagname.equals("Cube")) {
                         // add Student object to list
                		 if(null == parser.getAttributeValue(null,"currency")){
                        	 break;
                         }
                         Student.setCurrency( parser.getAttributeValue(null,"currency"));
                         Student.setRate(parser.getAttributeValue(null,"rate"));	
                         students.add(Student);
                         students2.add(Student);
                     }
                   break;
                default:
                    break;	
	
                }
                eventType = parser.next();
            }
  
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return students;
    }
    
    public List<Student> fetchXML(final String urlString ){
  
    	Thread thread = new Thread(new Runnable(){	
         @Override
        public void run() {
                try {
                	url = new URL(urlString);
                	 //NetworkUtil network_state = new NetworkUtil();
					HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
			        conn.setReadTimeout(10000 /* milliseconds */);
			        conn.setConnectTimeout(15000 /* milliseconds */);
			        conn.setRequestMethod("GET");
			        conn.setDoInput(true);
			        conn.connect();
			        	 	
			        InputStream stream = conn.getInputStream();
			        //students=
			        parse(stream,students1);		        	 	
			        stream.close(); 
	               } catch (Exception e) {
	                       e.printStackTrace();
	            }
	      
         	}});
        thread.start();
    return students1;
    }
}    
