package com.xxby.Product;

public class ProductModel {
    private final String productCode;
    private ProductInfo.ProductName productName;
    private ProductInfo.Brand brand;
    private ProductInfo.Series series;
    private ProductInfo.Unit unit;
    private final int boreDiameter;//缸径
    private final int strokeLength;//行程
    private ProductInfo.Suffix suffix;//后缀
    // 构造方法
        public ProductModel(String productCode) {
        //用结构化解析初始化
        ParseProductCode pCP=new ParseProductCode(productCode);
        this.productCode = pCP.getProductCode();
        this.series = ProductInfo.Series.valueOf(pCP.getSeries());
        if(Integer.parseInt(pCP.getBoreDiameter())>0){
            this.boreDiameter = Integer.parseInt(pCP.getBoreDiameter());
        }else{
            throw new IllegalArgumentException("缸径必须大于0");
        }
        if(Integer.parseInt(pCP.getStrokeLength())>0){
            this.strokeLength = Integer.parseInt(pCP.getStrokeLength());
        }else{
            throw new IllegalArgumentException("行程必须大于0");
        }if(pCP.getSuffix()!=null) {
            this.suffix = ProductInfo.Suffix.valueOf(pCP.getSuffix());
        }
    }
    public ProductModel(String productCode,ProductInfo.ProductName productName,ProductInfo.Brand brand,ProductInfo.Unit unit) {
        //判断产品名称是否合法
        ParseProductCode pCP=new ParseProductCode(productCode);
        //用结构化解析初始化
        this.productCode = pCP.getProductCode();
        this.series = ProductInfo.Series.valueOf(pCP.getSeries());
        if(Integer.parseInt(pCP.getBoreDiameter())>0){
            this.boreDiameter = Integer.parseInt(pCP.getBoreDiameter());
        }else{
            throw new IllegalArgumentException("缸径必须大于0");
        }
        if(Integer.parseInt(pCP.getStrokeLength())>0){
            this.strokeLength = Integer.parseInt(pCP.getStrokeLength());
        }else{
            throw new IllegalArgumentException("行程必须大于0");
        }if(pCP.getSuffix()!=null) {
            this.suffix = ProductInfo.Suffix.valueOf(pCP.getSuffix());
        }
        this.productName = productName;
        this.brand = brand;
        this.unit = unit;
    }
    //获取产品编码
    public String getProductCode() {
        return productCode;
    }
    //获取产品名称
    public ProductInfo.ProductName getProductName() {
        return productName;
    }
    //设置产品名称
    public void setProductName(ProductInfo.ProductName productName) {
        this.productName = productName;
    }
    //获取品牌
    public ProductInfo.Brand getBrand() {
        return brand;
    }
    //设置品牌
    public void setBrand(ProductInfo.Brand brand) {
        this.brand = brand;
    }
    //获取系列
    public ProductInfo.Series getSeries() {
        return series;
    }
    //设置系列
    public void setSeries(ProductInfo.Series series) {
        this.series = series;
    }
    //获取单位
    public ProductInfo.Unit getUnit() {
        return unit;
    }
    //获取单位中文名称
    public String getUnitCn() {
        return unit.getUnitCn();
    }
    //设置单位
    public void setUnit(ProductInfo.Unit unit) {
        this.unit = unit;
    }
    //获取缸径
    public int getBoreDiameter() {
        return boreDiameter;
    }
    //获取行程
    public int getStrokeLength() {
        return strokeLength;
    }
    //获取后缀
    public ProductInfo.Suffix getSuffix() {
        return suffix;
    }

}
