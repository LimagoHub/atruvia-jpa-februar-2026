                                                                    (2, 'New Orleans Cajun Delights', 'Shelley Burke', 'New Orleans', 'USA'),
                                                                                (3, 'Grandma Kelly''s Homestead', 'Regina Murphy', 'Ann Arbor', 'USA');

-- 4. Kunden (Customers)
INSERT INTO customers (CustomerID, Companyname, Contactname, City, Country) VALUES
                                                                                ('ALFKI', 'Alfreds Futterkiste', 'Maria Anders', 'Berlin', 'Germany'),
                                                                                ('ANATR', 'Ana Trujillo Emparedados', 'Ana Trujillo', 'México D.F.', 'Mexico'),
                                                                                ('ANTON', 'Antonio Moreno Taquería', 'Antonio Moreno', 'México D.F.', 'Mexico'),
                                                                                ('BERGS', 'Berglunds snabbköp', 'Christina Berglund', 'Luleå', 'Sweden'),
                                                                                ('BLAUS', 'Blauer See Delikatessen', 'Hanna Moos', 'Mannheim', 'Germany');



-- 7. Bestellungen (Orders)
INSERT INTO orders (OrderID, CustomerID,  Orderdate, Freight) VALUES
                                                                                      (10248, 'ALFKI',  '2023-07-04 00:00:00', 32.38),
                                                                                      (10249, 'ANATR',  '2023-07-05 00:00:00', 11.61),
                                                                                      (10250, 'ALFKI',  '2023-07-08 00:00:00', 65.83);
