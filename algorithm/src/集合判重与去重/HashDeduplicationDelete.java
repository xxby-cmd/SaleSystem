package 集合判重与去重;

import java.util.Set;
import java.util.TreeSet;

public class HashDeduplicationDelete {
    public static String deleteDuplicate(String str){
        Set<Character> tS = new TreeSet<>();
        for(char c:str.toCharArray()) {
            tS.add(c);
        }
        StringBuilder sb = new StringBuilder();
        for(char c:tS) {
            sb.append(c);
        }

        return sb.toString();
    }
}
