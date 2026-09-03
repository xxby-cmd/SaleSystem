CREATE DATABASE IF NOT EXISTS ProductInformation;
        USE ProductInformation;
CREATE TABLE total (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    product_code VARCHAR(50) NOT NULL UNIQUE,

    product_name ENUM(
        'StandardCylinder',
        'UnStandardCylinder'
    ) NOT NULL,

    brand Enum('SMC','CKD','KOGANEI','FESTO','REXROTH','PARKER','NOORGREN','AirTAC','CHELIC','MINDMAN','JELPC','SNS','XCHEN','OTHER') NOT NULL,

    series Enum('SAU','SC','SI','SDA','TN','TCM','MA','MAL','PB','CA2','CQ2','MGPM','CDJ2B','DSN','ADN','DFM','OTHER') NOT NULL,

    unit ENUM(
        'COUNT',
        'METER',
        'PACKAGE',
        'KILOGRAM'
    ) NOT NULL,

    bore_diameter INT,

    stroke_length INT,

    suffix Enum('S','J','H','K','SIL','C','CJ','OTHER'),

    CONSTRAINT chk_product_code_not_blank
        CHECK (TRIM(product_code) <> ''),

    CONSTRAINT chk_product_code_uppercase
        CHECK (BINARY product_code = BINARY UPPER(product_code)),

    CONSTRAINT chk_bore_diameter_positive
        CHECK (bore_diameter > 0),

    CONSTRAINT chk_stroke_length_positive
        CHECK (stroke_length > 0)
);

insert into total (product_code, product_name, brand, series, unit, bore_diameter, stroke_length, suffix)
values
    ('SAU32X175', 'StandardCylinder', 'FESTO', 'SAU', 'COUNT', 32, 175,null ),
    ('SAU20X150', 'StandardCylinder', 'FESTO', 'SAU', 'COUNT', 20, 150 ,null),
    ('MA20X50S', 'StandardCylinder', 'AirTAC', 'MA', 'COUNT', 20, 50, 'S');

select * from total;
select * from total where product_code = 'MA20X50S';
