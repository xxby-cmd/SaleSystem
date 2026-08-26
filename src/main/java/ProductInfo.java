public class ProductInfo {
    public enum ProductName {
        StandardCylinder("标准气缸"),
        UnStandardCylinder("非标准气缸");
        private final String productName;
        ProductName(String productName) {
            this.productName = productName;
        }
        public String getProductName() {
            return productName;
        }
    }
    public enum Brand {
        SMC,//日本SMC
        CKD,//喜开理
        KOGANEI,//小金井
        FESTO,//费斯托
        REXROTH,//博世力士乐
        PARKER,//派克
        NOORGREN,//诺冠
        AirTAC,//亚德客
        CHELIC,//气立可
        MINDMAN,//金器
        JELPC,//佳尔灵
        SNS,//神驰气动
        XCHEN,//星辰气动
        OTHER,//其他品牌
    }
    public enum Series {
        SC,
        SI,
        SDA,
        TN,
        TCM,
        MA,
        MAL,
        PB,
        CA2,
        CQ2,
        MGPM,
        CDJ2B,
        DSN,
        ADN,
        DFM,
        OTHER,
    }
    public enum Unit {
        COUNT("只/个"),//只
        METER("米"),//米
        PACKAGE("包"),//包
        KILOGRAM("千克");//千克
        private final String unitCn;
        Unit(String unitCn) {
            this.unitCn = unitCn;
        }
        public String getUnitCn() {
            return unitCn;
        }

    }
}
