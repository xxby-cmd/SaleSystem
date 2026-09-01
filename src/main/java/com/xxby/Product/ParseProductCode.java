package com.xxby.Product;

import com.xxby.校验规则.ValidationUtil;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseProductCode {
    private final String productCode;//产品编码
    private final String series;
    private final String boreDiameter;//缸径
    private final String strokeLength;//行程
    private final String suffix;//后缀

    public ParseProductCode(String productCode){
        //校验产品编码格式
        ValidationUtil.validProductCode(productCode);
        //标准化
        productCode=productCode.trim().toUpperCase(Locale.ROOT);

        //解析产品编码
        Matcher matcher = ParseProductCode.parse(productCode);

        this.productCode=productCode;
        this.series=matcher.group(1);
        this.boreDiameter=matcher.group(2);
        this.strokeLength=matcher.group(4);
        if(matcher.group(5).isEmpty()){
            this.suffix=null;
        }else{
            this.suffix=matcher.group(5);
        }

    }

    private static Matcher parse(String productCode){
    Pattern pattern = Pattern.compile("([A-Z]{2,4})(-?\\d{1,3})(X)(-?\\d{1,4})([A-Z]*)");
    Matcher matcher = pattern.matcher(productCode);
    //解析产品编码
        if(matcher.matches()) {
        return matcher;
    }else{
        throw new IllegalArgumentException("产品编码格式错误:"+productCode);
    }
    }

    //获取产品编码
    public String getProductCode() {
        return productCode;
    }
    //获取产品编码的系列
    public String getSeries() {
        return series;
    }
    //获取产品编码的缸径
    public String getBoreDiameter() {
        return boreDiameter;
    }
    //获取产品编码的行程
    public String getStrokeLength() {
        return strokeLength;
    }
    //获取产品编码的后缀
    public String getSuffix() {
        return suffix;
    }
}
