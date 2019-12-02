INSERT INTO Shop (name, latitude, longitude, image) VALUES
('Grossery', 32.288742, -9.236141, 'Grossery.jpg'),
('Guitar store', 32.290511, -9.240839, 'Guitar.jpg'),
('Jeep vendor', 32.293727, -9.223454, 'Jeep.jpg'),
('Saad Store', 32.290253, -9.226773, 'Saad,jpg'),
('Marchane', 32.294676, -9.234354, 'Marchane.jpg'),
('TechoPlace', 32.290598, -9.236217, 'Techno.jpg');

INSERT INTO User (name, email, password) VALUES
('Toumani Sidibe', 'toumani49@gmail.com', SHA1('hello'));

INSERT INTO Connection VALUES
(SHA1('arbitrary auto-generated string'), 1, 32.288742, -9.236141);

UPDATE User_Shop
SET interaction = null
WHERE TIMEDIFF(NOW(), date) > '2:00:00'
AND user_id = 1;

SELECT s.id, s.name, s.latitude, s.longitude, s.image FROM
Shop s
EXCEPT
SELECT s.id, s.name, s.latitude, s.longitude, s.image FROM
Shop s
INNER JOIN User_Shop us
ON s.id = us.shop_id
WHERE user_id = 1
AND interaction = 0

SELECT s.id, s.name, s.latitude, s.longitude, s.image
FROM Shop s
INNER JOIN User_Shop us
ON s.id = us.shop_id
WHERE user_id = 1
AND interaction IS NULL