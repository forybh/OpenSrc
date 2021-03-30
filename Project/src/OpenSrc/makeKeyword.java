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
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class makeKeyword {
    private File dir = new File("./SimpleIR/2주차 실습 html");
    private File files[] = dir.listFiles();

    public String[] kkm(String[] s) {
        KeywordExtractor ke = new KeywordExtractor();
        String[] kkmStr = new String[s.length];
        for(int i = 0; i < kkmStr.length; i++) {
            kkmStr[i] = "";
        }
        for(int i = 0; i < s.length; i++) {
            KeywordList kl = ke.extractKeyword(s[i], true);
            for(int j =0; j < kl.size(); j++) {
                Keyword k = kl.get(j);
                String temp = k.getString() + ':' + k.getCnt() + '#';
                //System.out.println(temp);
                kkmStr[i] += temp;
            }
        }
        return kkmStr;
    }



    public String[] getCollection(String s) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = df.newDocumentBuilder();
        Document doc = db.parse(s);
        doc.getDocumentElement().normalize();

        NodeList nl = doc.getElementsByTagName("doc");
        String[] body = new String[nl.getLength()];
        for(int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                //System.out.println(getTagValue("body", element));
                body[i] = getTagValue("body", element);
            }
        }
        return body;
    }
    private static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if(nValue == null)
            return null;
        return nValue.getNodeValue();
    }

    public void makeIndex(String[] sList) throws ParserConfigurationException, TransformerException, FileNotFoundException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document document = docBuilder.newDocument();

        Element docs = document.createElement("docs");
        document.appendChild(docs);
        int id_num = 0;

        for(File i : files) {
            String fileName = i.getName();
            int pos = fileName .lastIndexOf(".");
            String _fileName = fileName.substring(0, pos);
            Element id = document.createElement("doc");
            docs.appendChild(id);
            id.setAttribute("id", String.valueOf(id_num));
            Element title = document.createElement("title");
            title.appendChild(document.createTextNode(_fileName));
            id.appendChild(title);
            Element body = document.createElement("body");
            body.appendChild(document.createTextNode(sList[id_num]));
            id.appendChild(body);
            id_num++;
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new FileOutputStream(new File("./index.xml")));

        transformer.transform(source, result);

    }

}
