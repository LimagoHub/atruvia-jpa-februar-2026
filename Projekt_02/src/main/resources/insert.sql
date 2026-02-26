--DROP TABLE IF EXISTS `order details` CASCADE;
--DROP TABLE IF EXISTS orders CASCADE;
--DROP TABLE IF EXISTS products CASCADE;
--DROP TABLE IF EXISTS categories CASCADE;
--DROP TABLE IF EXISTS suppliers CASCADE;
--DROP TABLE IF EXISTS shippers CASCADE;
--DROP TABLE IF EXISTS employees CASCADE;
--DROP TABLE IF EXISTS customers CASCADE;

--SET REFERENTIAL_INTEGRITY TRUE;
-- Hier folgen dann deine CREATE TABLE (falls Hibernate das nicht macht) und INSERT Befehle


-- 1. Kategorien
INSERT INTO categories (Categoryname, Description) VALUES
                                                       ('Beverages', 'Soft drinks, coffees, teas, beers, and ales'),
                                                       ('Condiments', 'Sweet and savory sauces, relishes, spreads, and seasonings'),
                                                       ('Confections', 'Desserts, candies, and sweet breads'),
                                                       ('Dairy Products', 'Cheeses'),
                                                       ('Grains/Cereals', 'Breads, crackers, pasta, and cereal'),
                                                       ('Meat/Poultry', 'Prepared meats'),
                                                       ('Produce', 'Dried fruit and bean curd'),
                                                       ('Seafood', 'Seaweed and fish');

-- 2. Versender (Shippers)
INSERT INTO shippers (ShipperID) VALUES (1), (2), (3);

-- 3. Lieferanten (Suppliers)
INSERT INTO suppliers (SupplierID, Companyname, Contactname, City, Country) VALUES
                                                                                (1, 'Exotic Liquids', 'Charlotte Cooper', 'London', 'UK'),
                                                                                (2, 'New Orleans Cajun Delights', 'Shelley Burke', 'New Orleans', 'USA'),
                                                                                (3, 'Grandma Kelly''s Homestead', 'Regina Murphy', 'Ann Arbor', 'USA');

-- 4. Kunden (Customers)
INSERT INTO customers (CustomerID, Companyname, Contactname, City, Country) VALUES
                                                                                ('ALFKI', 'Alfreds Futterkiste', 'Maria Anders', 'Berlin', 'Germany'),
                                                                                ('ANATR', 'Ana Trujillo Emparedados', 'Ana Trujillo', 'México D.F.', 'Mexico'),
                                                                                ('ANTON', 'Antonio Moreno Taquería', 'Antonio Moreno', 'México D.F.', 'Mexico'),
                                                                                ('BERGS', 'Berglunds snabbköp', 'Christina Berglund', 'Luleå', 'Sweden'),
                                                                                ('BLAUS', 'Blauer See Delikatessen', 'Hanna Moos', 'Mannheim', 'Germany');

-- 5. Mitarbeiter (Employees)
-- Hinweis: ReportsTo (Self-Reference) wird hier vereinfacht, Andrew (2) ist der Chef.
INSERT INTO employees (EmployeeID, Lastname, Firstname, Title, Salary, Notes) VALUES
                                                                                  (1, 'Davolio', 'Nancy', 'Sales Representative', 2954.55, 'Education includes a BA in psychology.'),
                                                                                  (2, 'Fuller', 'Andrew', 'Vice President, Sales', 5000.00, 'Andrew received his Ph.D. at Bowdoin College.'),
                                                                                  (3, 'Leverling', 'Janet', 'Sales Representative', 3119.15, 'Janet has a BS degree in chemistry.');

-- 6. Produkte
INSERT INTO products (Productname, Supplierid, Categoryid, Unitprice, Discontinued) VALUES
                                                                                        ('Chai', 1, 1, 18.00, 0),
                                                                                        ('Chang', 1, 1, 19.00, 0),
                                                                                        ('Aniseed Syrup', 1, 2, 10.00, 0),
                                                                                        ('Chef Anton''s Cajun Seasoning', 2, 2, 22.00, 0),
                                                                                        ('Chef Anton''s Gumbo Mix', 2, 2, 21.35, 1);

-- 7. Bestellungen (Orders)
INSERT INTO orders (OrderID, CustomerID, EmployeeID, ShipVia, Orderdate, Freight) VALUES
                                                                                      (10248, 'ALFKI', 1, 1, '2023-07-04 00:00:00', 32.38),
                                                                                      (10249, 'ANATR', 2, 2, '2023-07-05 00:00:00', 11.61),
                                                                                      (10250, 'ALFKI', 3, 2, '2023-07-08 00:00:00', 65.83);

-- 8. Bestelldetails (Order Details)
-- Composite Key: OrderID + ProductID
INSERT INTO orderdetails (orderid, ProductID, Unitprice, Quantity, Discount) VALUES
                                                                                    (10248, 3, 14.00, 12, 0),
                                                                                    (10248, 4, 9.80, 10, 0),
                                                                                    (10249, 5, 34.80, 5, 0),
                                                                                    (10250, 6, 18.60, 9, 0);