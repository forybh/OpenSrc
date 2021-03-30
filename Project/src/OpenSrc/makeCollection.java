package OpenSrc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.w3c.dom.Element;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class makeCollection {
    private File dir = new File("./SimpleIR/2주차 실습 html");
    private File files[] = dir.listFiles();

    public void fileList(String s) {
        File dir = new File(s);
        File files[] = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            int pos = fileName .lastIndexOf(".");
            String _fileName = fileName.substring(0, pos);
            System.out.println(_fileName);
        }

    }

    public void Collection(String s) throws ParserConfigurationException, IOException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        File dir = new File(s);
        File files[] = dir.listFiles();

        org.w3c.dom.Document d = docBuilder.newDocument();
        d.setXmlStandalone(true); //standalone="no" 를 없애준다.

        Element docs = d.createElement("docs");
        d.appendChild(docs);

        int id_num = 0;
        for(File i : files) {
            File input = new File(i.toString());
            Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
            //System.out.println(doc.title());
            //System.out.println(doc.body().text());
            Element id = d.createElement("doc");
            docs.appendChild(id);
            id.setAttribute("id", String.valueOf(id_num));
            Element title = d.createElement("title");
            title.appendChild(d.createTextNode(doc.title()));
            id.appendChild(title);
            Element body = d.createElement("body");
            body.appendChild(d.createTextNode(doc.body().text()));
            id.appendChild(body);
            id_num++;
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기

        DOMSource source = new DOMSource(d);
        StreamResult result = new StreamResult(new FileOutputStream(new File("./collection.xml")));

        transformer.transform(source, result);

    }


}
