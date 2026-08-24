package LinearScan线性扫描;
/*题目名称
统计目标整数出现次数

题目描述
给定一个整数数组 nums和一个目标整数 target。请统计 target在数组中出现了多少次，并返回出现次数。不得修改原数组。

输入：整数数组nums和目标整数target
输出：一个非负整数，表示 target出现的次数。
数据范围
0 ≤ nums.length ≤ 100000
-1_000_000_000 ≤ nums[i] ≤ 1_000_000_000
-1_000_000_000 ≤ target ≤ 1_000_000_000
示例一
输入：
nums = [4, 7, 4, 2, 4]
target = 4

输出：
3
示例二
输入：
nums = [1, 2, 3]
target = 8

输出：
0
示例三
输入：
nums = []
target = 5

输出：
0
示例四
输入：
nums = [-2, 0, -2, 3]
target = -2

输出：
*/

public final class OccurrenceCounter {
    private OccurrenceCounter() {}

    public static int countOccurrences(int[] numbers, int target){
        int count=0;
        if(numbers ==null){
            throw new IllegalArgumentException("数组不能为空引用");
        }else if(numbers.length==0){
            return 0;
        }
        for(int i: numbers){
            if(i==target){
                count++;
            }
        }
        return count;
    }
}
