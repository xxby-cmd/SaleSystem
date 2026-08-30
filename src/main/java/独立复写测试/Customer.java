package 独立复写测试;

import java.util.Locale;

public class Customer {
    private String customerCode;
    private String customerName;
    //有参构造
    public Customer(String CustomerCode,String CustomerName){
        if((CustomerCode==null)||(CustomerName==null)){
            throw new IllegalArgumentException("编码或名称不能为空");
        }
        CustomerCode=CustomerCode.trim().toUpperCase(Locale.ROOT);
        CustomerName=CustomerName.trim();
        if(CustomerCode.equalsIgnoreCase("")||CustomerName.equalsIgnoreCase("")){
            throw new IllegalArgumentException("编码或名称不能为纯空格或空字符串");
        }
        this.customerCode =CustomerCode;
        this.customerName =CustomerName;
    }
    //获取编码
    public String getCustomerCode() {
        return customerCode;
    }
    //设置编码
    public void setCustomerCode(String customerCode) {
        if((customerCode ==null)){
            throw new IllegalArgumentException("编码或名称不能为空");
        }
        customerCode = customerCode.trim().toUpperCase(Locale.ROOT);
        if(customerCode.equalsIgnoreCase("")){
            throw new IllegalArgumentException("编码或名称不能为纯空格或空字符串");
        }
        this.customerCode = customerCode;
    }
    //获取名称
    public String getCustomerName() {
        return customerName;
    }
    //设置名称
    public void setCustomerName(String customerName) {
        if(customerName ==null){
            throw new IllegalArgumentException("编码或名称不能为空");
        }
        customerName = customerName.trim();
        if(customerName.equalsIgnoreCase("")){
            throw new IllegalArgumentException("编码或名称不能为纯空格或空字符串");
        }
        this.customerName = customerName;
    }
}
