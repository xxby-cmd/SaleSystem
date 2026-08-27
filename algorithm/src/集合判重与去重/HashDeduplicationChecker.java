package 集合判重与去重;

import java.util.HashSet;
import java.util.Set;

public class HashDeduplicationChecker {

    private HashDeduplicationChecker() {}

    public static boolean containsDuplicate(int[] numbers) {
        if (numbers == null) {
            throw new IllegalArgumentException("numbers is null");
        }else if(numbers.length==0||numbers.length==1){
            return false;
        }


        Set<Integer> set = new HashSet<>();
        for (int i : numbers) {
            if (!set.add(i)) {
                return true;
            }
        }
        return false;
    }
}
