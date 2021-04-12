package OpenSrc;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;

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
        else if(args[0].equals("-s")){
            searcher p = new searcher();
            String[] s = p.makeKey(args[3]);
            String[] title = p.getTitle();
            p.setPost(args[1]);
            ArrayList<Float> arr = new ArrayList<Float>();
            for(int i = 0; i < title.length; i++) {
                arr.add(p.CalcSim(s, i));
                System.out.println("Cos: " + i + " " + p.CalcSim(s, i));
            ArrayList<Float> arr = new ArrayList<Float>();
            for(int i = 0; i < title.length; i++) {
                arr.add(p.CalcSim(p.getIndex(args[1]), s, i));
            }
            p.topThree(arr);
        }

    }
}
