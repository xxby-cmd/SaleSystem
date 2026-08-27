package OppositeDriectionTwoPoiners相向双指针;

public class OppositeDriectionTwoPoiners {
    public static Boolean containsNumbersSumOfTarget(int[] numbers,long target){
        if(numbers==null){
            throw new NullPointerException("数组不能为空");
        }else if(numbers.length<2){
            return false;
        }
        int i=0;
        int j=numbers.length-1;
        long sum;
        while(i<j){
            sum= (long) numbers[i]+(long) numbers[j];
            if(sum==target){
                return true;
            }else if(sum>target){
                j--;
            } else {
                i++;
            }
        }
        return false;
    }
    public static Boolean containsNumbersMultiplyOfTarget(int[] numbers,int target){
        if(numbers==null){
            throw new NullPointerException("数组不能为空");
        }else if(numbers.length<2){
            return false;
        }else if(numbers[0]<0||numbers[numbers.length-1]<0){
            throw new IllegalArgumentException("数组中不能包含负数");
        }
        int i=0;
        int j=numbers.length-1;
        long multiply;
        while(i<j){

            multiply= (long) numbers[i] *(long) numbers[j];
            if(multiply==target){
                return true;
            }else if(multiply>target){
                j--;
            } else {
                i++;
            }
        }
        return false;
    }
}
