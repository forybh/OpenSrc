package OpenSrc;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import OpenSrc.makeKeyword;

public class indexer {
    public void makeIndex() throws IOException, ParserConfigurationException, SAXException {
        FileOutputStream fs = new FileOutputStream("./index.post");
        ObjectOutputStream os = new ObjectOutputStream(fs);

        HashMap hm = new HashMap<String, ArrayList>();
        hm = makeHash();

        os.writeObject(hm);
        os.close();

    }
    public void readIndex() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("index.post");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object object =  objectInputStream.readObject();
        objectInputStream.close();;
        System.out.println("읽어온 개체의 타입 : " + object.getClass());

        HashMap hashMap = (HashMap)object;
        Iterator<String > it = hashMap.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            ArrayList value = (ArrayList) hashMap.get(key);
            System.out.println(key + " -> "+ value);
        }

    }
    private static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if(nValue == null)
            return null;
        return nValue.getNodeValue();
    }
    public float getWeight(String str, int d) throws ParserConfigurationException, SAXException, IOException {
        int count = 0;
        makeKeyword mk = new makeKeyword();
        String[] body = mk.getCollection("./index.xml");
        int tf = 0;
        for(int i = 0; i < body.length; i++) {
            for(String s : body[i].split("#")) {
                if(s.split(":")[0].equals(str)) count++;
                if(i == d && s.split(":")[0].equals(str)) {
                    tf = Integer.parseInt(s.split(":")[1]);
                }
            }
        }
        int df = count;
        int N = body.length;
        if(df == 0) {
            return -1000;
        }
        float w = (float) (tf * (Math.log(N)-Math.log(df)));

        return (float)(Math.round(w*100)/100.0);
    }

    public HashMap<String, ArrayList> makeHash() throws IOException, SAXException, ParserConfigurationException {
        HashMap<String, ArrayList> hm = new HashMap<String, ArrayList>();
        makeKeyword mk = new makeKeyword();
        String[] body = mk.getCollection("./index.xml");
        for(int i = 0; i < body.length; i++) {
            for(String s : body[i].split("#")) {
                ArrayList arr = new ArrayList();
                if(!hm.containsKey(s.split(":")[0])){
                    arr.add(i);
                    arr.add(getWeight(s.split(":")[0], i));
                    hm.put(s.split(":")[0], arr);
                }
                else {
                    arr = hm.get(s.split(":")[0]);
                    arr.add(i);
                    arr.add(getWeight(s.split(":")[0], i));
                    hm.put(s.split(":")[0], arr);
                }
            }
        }

        return hm;
    }
}
