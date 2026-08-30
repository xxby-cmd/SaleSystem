package 独立复写测试;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CustomerDirectory {
    private final Map<String,Customer> CustomerDiectory=new HashMap<>();

    public CustomerDirectory(){}
    public CustomerDirectory(Customer customer){
        if(customer==null){
            throw new IllegalArgumentException("客户数据未创建");
        }
        if(CustomerDiectory.containsKey(customer.getCustomerCode())){
            throw  new IllegalArgumentException("客户数据已存在");
        }
        CustomerDiectory.put(customer.getCustomerCode(),customer);
    }

    public void register(Customer customer){
        if(customer==null){
            throw new IllegalArgumentException("客户数据未创建");
        }
        if(CustomerDiectory.containsKey(customer.getCustomerCode())){
            throw  new IllegalArgumentException("客户数据已存在");
        }
        CustomerDiectory.put(customer.getCustomerCode(),customer);
    }


    public Customer findByCode(String code){
        if((code==null)){
            throw new IllegalArgumentException("编码不能为空");
        }
        code=code.trim().toUpperCase(Locale.ROOT);
        if(code.equalsIgnoreCase("")){
            throw new IllegalArgumentException("编码不能为纯空格或空字符串");
        }

        if(CustomerDiectory.containsKey(code)){
            return CustomerDiectory.get(code);
        }
        return null;
    }

    public int size(){
        return CustomerDiectory.size();
    }


}
