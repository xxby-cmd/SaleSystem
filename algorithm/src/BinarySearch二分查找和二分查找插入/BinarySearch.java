package BinarySearch二分查找和二分查找插入;

public class BinarySearch {
    public static int FindNormalIndex(int[] nums,int target){
        if(nums==null) {
            throw new IllegalArgumentException("数组为空");
        }else if(nums.length==0){
            return -1;
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
        return -1;
    }
    public static int FindLeftIndex(int[] nums,int target){
        if(nums==null) {
            throw new IllegalArgumentException("数组为空");
        }else if(nums.length==0){
            return -1;
        }
        int low = 0;
        int high = nums.length-1;
        int leftIndex = -1;
        while(low<=high) {
            int mid = (low + high) / 2;
            if (nums[mid] == target) {
                    leftIndex=mid;
                    high=mid-1;
            } else if (nums[mid] > target) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return leftIndex;
    }



}
