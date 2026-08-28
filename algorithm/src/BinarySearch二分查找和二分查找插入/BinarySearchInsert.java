package BinarySearch二分查找和二分查找插入;

public class BinarySearchInsert {
    public static int FindAndInsertIndex(int[] nums,int target){
        if(nums==null) {
            throw new IllegalArgumentException("数组为空");
        }else if(nums.length==0){
            return 0;
        }else if(nums.length==1){
            if(nums[0]>=target){
                return 0;
            }else{
                return 1;
            }
        }
        int low = 0;
        int high = nums.length-1;
        while(low<=high) {
            int mid = (low + high) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] > target) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }


        return low;
    }
}
