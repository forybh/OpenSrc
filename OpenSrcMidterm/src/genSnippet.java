import org.jsoup.Jsoup;

import javax.swing.text.Document;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class genSnippet {
    public static void main(String[] args) throws FileNotFoundException {
            FileReader fr = new FileReader("input.txt");
            String[] s = {"라면 밀가루 달걀 밥 생선","라면 물 소금 반죽","첨부 봉지면 인기","초밥 라면 밥물 채소 소금","초밥 종류 활어"};

            if(args[0].equals('f')){
                File file = new File(args[1].toString());
//                Document doc = Jsoup.parse(file.toString(), "UTF-8", "http://example.com/");
            }
            if(args[2].equals('q')){
                String[] words = args[3].split(" ");
                ArrayList<Integer> c = new ArrayList<Integer>(5);
                for(int i = 0; i < 5; i++) {
                    c.add(0);
                }
                int index = 0;
                int count = 0;
                int max = 0;
                int t = 0;
                for(String w :s){
                    for(String temp:words){
                        if(w.contains(temp)){
                            count +=1;
                        }
                    }
                    c.add(count);
                    if(count > max) {
                        index = t;
                    }
                    t+=1;
                }
                System.out.println(s[index]);
            }



    }
}
