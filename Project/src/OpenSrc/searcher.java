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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class searcher {
    private String post;

    public void setPost(String s) {
        post = s;
    }
    public String getPost() {
        return this.post;
    }
    public String[] makeKey(String s) {
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(s, true);
        String[] key = new String[kl.size()];
        for(int i = 0; i < kl.size(); i++) {
            Keyword kwrd = kl.get(i);
            key[i] = kwrd.getString();
        }
        return key;
    }
    public HashMap<String, ArrayList> getIndex(String s) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(s);
        //s = "index.post"
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object object =  objectInputStream.readObject();
        objectInputStream.close();
        return (HashMap)object;
    }
    public float InnerProduct(HashMap<String, ArrayList> hash, String[] keys, int id) {
        float calc = 0;
        for(String s : keys) {
            ArrayList temp = hash.get(s);
            if(temp.indexOf(id) % 2 == 0) {
                calc += (float)temp.get(temp.indexOf(id) + 1);
            }
        }
        return Math.round(calc * 100)/(float)100.0;
    }
    public float CalcSim(String[] keys, int id) throws IOException, ClassNotFoundException {
        HashMap<String, ArrayList> hash = getIndex(post);
        float calc = 0.0f;
        float size = 0.0f;
        float word = 0.0f;
        for(String s: keys) {
            ArrayList temp = hash.get(s);
            if(temp.indexOf(id) % 2 == 0) {
                size += ((float)temp.get(temp.indexOf(id) + 1) * (float)temp.get(temp.indexOf(id) + 1));
                word++;
            }
        }
        calc = InnerProduct(hash, keys, id);
//        System.out.println("calc = " + calc);
//        System.out.println("size = " + Math.sqrt(size));
//        System.out.println("word = " + word);
        float result = calc / ((float)Math.sqrt(size)*(float)Math.sqrt(word));
//        System.out.println("result = " + result);

        return (float)Math.round(result*100)/100;
    }
    public void topThree(ArrayList<Float> arr) throws IOException, SAXException, ParserConfigurationException {
        String[] title = getTitle();

        for(int i =0; i < 3; i++) {
            float max = Collections.max(arr);
            System.out.println(title[arr.indexOf(max)]);
            arr.set(arr.indexOf(max), -1.0f);
        }

    }
    public String[] getTitle() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = df.newDocumentBuilder();
        Document doc = db.parse("collection.xml");
        doc.getDocumentElement().normalize();

        NodeList nl = doc.getElementsByTagName("doc");
        String[] title = new String[nl.getLength()];
        for(int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                title[i] = getTagValue("title", element);
            }
        }
        return title;
    }
    private static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if(nValue == null)
            return null;
        return nValue.getNodeValue();
    }
}
