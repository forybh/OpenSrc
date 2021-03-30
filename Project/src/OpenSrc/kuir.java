package OpenSrc;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;

import OpenSrc.*;

public class kuir {
    public static void main(String[] args) throws IOException, TransformerException, ParserConfigurationException, SAXException, ClassNotFoundException {
        if(args[0].equals("-c")) {
            makeCollection mc = new makeCollection();
            mc.Collection(args[1]);
        }

        else if(args[0].equals("-k")) {
            makeKeyword mk = new makeKeyword();
            String[] body = mk.getCollection(args[1]);
            String[] temp = mk.kkm(body);
            mk.makeIndex(temp);
        }

        else if(args[0].equals("-i")) {
            indexer i = new indexer();
            i.makeIndex();
            i.readIndex();

        }



    }
}
