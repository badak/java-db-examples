/* Use double semicolons as statement delimiter to get around procedure definitions using Spring JDBC */

DROP TABLE Nutrition IF EXISTS;;
DROP TABLE Product IF EXISTS;;
DROP TABLE StdNutrition IF EXISTS;;


CREATE TABLE StdNutrition(
    Code VARCHAR(10) PRIMARY KEY,
    Amount INT NOT NULL,
    Unit VARCHAR(10) NOT NULL,
    Unique(Amount, Unit)
);;

CREATE TABLE Product(
    Code VARCHAR(20) PRIMARY KEY,
    Name varchar(50) NOT NULL,
    EntryDate DATE NOT NULL,
    Amount numeric(6,2) NOT NULL,
    Unit varchar(10) NOT NULL,
    Quantity INT NOT NULL,
    StdNutritionCode VARCHAR(10) NOT NULL,
    FOREIGN KEY(StdNutritionCode) REFERENCES StdNutrition(Code)
);;

CREATE TABLE Nutrition(
    ProductCode VARCHAR(20),
    Name VARCHAR(50),
    Amount NUMERIC(6,2) NOT NULL,
    Unit VARCHAR(10) NOT NULL,
    PRIMARY KEY(ProductCode, Name),
    FOREIGN KEY(ProductCode) REFERENCES Product(Code)
);;


INSERT INTO StdNutrition(Code, Amount, Unit)
VALUES('100g', 100, 'g');;

INSERT INTO StdNutrition(Code, Amount, Unit)
VALUES('100ml', 100, 'ml');;


/* CB1 */
INSERT INTO Product(Code, Name, EntryDate, Amount, Unit, Quantity, StdNutritionCode)
VALUES('CB1', 'Chocolate Bar 1', '2013-12-10', 49.00, 'g', 1, '100g');;

INSERT INTO Nutrition(ProductCode, Name, Amount, Unit)
values('CB1', 'Energy', 2200, 'kJ');;

INSERT INTO Nutrition(ProductCode, Name, Amount, Unit)
values('CB1', 'Protein', 12.3, 'g');;

INSERT INTO Nutrition(ProductCode, Name, Amount, Unit)
values('CB1', 'Fat', 24.5, 'g');;

INSERT INTO Nutrition(ProductCode, Name, Amount, Unit)
values('CB1', 'Carbohydrate', 54, 'g');;


/* BB1 */
INSERT INTO Product(Code, Name, EntryDate, Amount, Unit, Quantity, StdNutritionCode)
VALUES('BB1', 'Breakfast Bar 1', '2014-02-01', 200.00, 'g', 6, '100g');;

INSERT INTO Nutrition(ProductCode, Name, Amount, Unit)
values('BB1', 'Energy', 1400, 'kJ');;

INSERT INTO Nutrition(ProductCode, Name, Amount, Unit)
values('BB1', 'Sodium', 210, 'mg');;

INSERT INTO Nutrition(ProductCode, Name, Amount, Unit)
values('BB1', 'Fat', 4.8, 'g');;


/* MLK1 */
INSERT INTO Product(Code, Name, EntryDate, Amount, Unit, Quantity, StdNutritionCode)
VALUES('MLK1', 'Milk 1', '2014-04-12', 2, 'l', 1, '100ml');;

INSERT INTO Nutrition(ProductCode, Name, Amount, Unit)
values('MLK1', 'Energy', 200, 'kJ');;

INSERT INTO Nutrition(ProductCode, Name, Amount, Unit)
values('MLK1', 'Protein', 3.6, 'g');;

INSERT INTO Nutrition(ProductCode, Name, Amount, Unit)
values('MLK1', 'Calcium', 161, 'mg');;

INSERT INTO Nutrition(ProductCode, Name, Amount, Unit)
values('MLK1', 'Fat', 2.3, 'g');;

INSERT INTO Nutrition(ProductCode, Name, Amount, Unit)
values('MLK1', 'Carbohydrate', 4.2, 'g');;



/* procedures & functons */

/* polymorphic functons */
DROP FUNCTION GetNutritionInfo IF EXISTS;;

CREATE FUNCTION GetNutritionInfo(productCode_ VARCHAR(20))
RETURNS TABLE(
    Name VARCHAR(50),
    Amount NUMERIC(6,2),
    Unit VARCHAR(10)
    )
READS SQL DATA
RETURN TABLE (
    SELECT  Name, Amount, Unit
    FROM    Nutrition
    WHERE   ProductCode = productCode_
    ORDER BY Name
);;


CREATE FUNCTION GetNutritionInfo(productCode_ VARCHAR(20), name_ VARCHAR(50))
RETURNS TABLE(
    Name VARCHAR(50),
    Amount NUMERIC(6,2),
    Unit VARCHAR(10)
    )
READS SQL DATA
RETURN TABLE (
    SELECT  Name, Amount, Unit
    FROM    Nutrition
    WHERE   ProductCode = productCode_ AND Name = name_
    ORDER BY Name
);;



/* procedure returning multiple result sets */
DROP PROCEDURE GetAllNutritionInfo IF EXISTS;;

CREATE PROCEDURE GetAllNutritionInfo()
READS SQL DATA
DYNAMIC RESULT SETS 1
BEGIN ATOMIC
    DECLARE result1 CURSOR FOR
    SELECT  prod.Code, prod.Name, prod.Amount, prod.Unit, prod.Quantity,
            prod.EntryDate, sm.Amount AS StdAmount, sm.Unit as StdUnit
    FROM    Product prod
    JOIN    StdNutrition sm ON (sm.Code = prod.StdNutritionCode)
    ORDER BY prod.Code
    ;
    
    DECLARE result2 CURSOR FOR
    SELECT  *
    FROM    Nutrition
    ORDER BY ProductCode, Name
    ;
    
    OPEN result1;
    OPEN result2;
END;;
