# Web Coding Challenge - Shopzz microservice

A microservice consumed by [Shopzz](https://github.com/Toumani/web-coding-challenge/).

## Abstract

This microservice exposes methods to interact with a database listing a set of fictive shops with their distance to user. Thus, a client can get a list of nearby shops by sending its position. The service also exposes registration and authentication services which give users personalized experience by letting them like or dislike shops. 

## Running the app

Running the app consists of executing a ```jar``` file. Since the app communicates with an external datasource, it also requires a valid connection to a database. A [MariaDB](https://mariadb.org/download/) database is used. By default the database address is ```localhost:3306```.

So prerequesites are:
* Java 8
* Mariadb 10.1.30 (or later)

**Setting up the database**

Setting up the database consists of creating a user, setting its password, creating a schema and giving access to the user on the scheme.

First we sign in MariaDB as root:

```jshelllanguage
suso mariadb
```

Then

```sql
CREATE USER `shopzz`@`%`;
SET PASSWORD FOR `shopzz`@`%` = PASSWORD('c6423kd');
CREATE DATABASE SHOPZZ;
GRANT ALL PRIVILEGES ON SHOPZZ.* TO `shopzz`@`%`;
```

Then execute ```database_structure.sql``` and ```database_datas.sql```. The database is now set up.

Once the database is set up, the service can be launched with:
 
 ```java -jar target/shopzz-app-0.0.1-SNAPSHOT.jar ```.
 
 It will be listening at port 9090. Make sure the port is not already in use by another process.
 
 ## Database structure
 
 The database structure is simple and made of 4 tables, ```User```, ```Shop```, ```Connection``` and ```User_Shop```.
 
* **User table**: 
 Infos related to a user which are name, email and password are stored in ```User``` table.
 
 * **Connection table**:
 Any time a user signs in, an instance of ```Connection``` is associated to his account. A connection is made of user's location and a hashcode standing for current session identifier.
 
 * **Shop table**:
 Records shop names and locations.
 
 * **User_Shop table**: 
 Records interactions between a user and a shop which are basically an addition to favorites, a dislike or neither nor.
 
 ## Services
 
 All exposed services are accessible by ```HTTP POST```. If it's obvious that registration and login requires this method you might be wondering why getting a list of shops is performed with ```POST```. Well, the answer is that client has to **send a hashcode** in each query to identify his session. This identifier could fit in cookies sent with the request but it makes sense to me to put it in the body since the service could evolve and require more informations. For instance we could imagine a that the server updates user's location at each request, which is not the case for the moment.
 
Services are:

* **/register**: Registers a new user. Fails if given email is already associated with an account.

Request body format:

```JSON
{
	"email": "tou@ma.ni",
	"password": "hello",
	"name": "Tex",
	"location": {
		"latitude": 32.288742,
		"longitude": -9.236141
	}
}
```

* **/sing-in**: Allows a registered user to sign into his account.

Request body format:
 
```JSON
{
	"email": "tou@ma.ni",
	"password": "hello",
	"location": {
		"latitude": 32.288742,
		"longitude": -9.236141
	}
}
```

* **/shops**: Returns an array of all shops sorted by distance to user. Recently disliked and favorite shop aren't included

Request body format:

```JSON
{
	"hashcode": "b8adf586687809a7d4d6eb61f62549209e218c75",
	"location": {
		"latitude": 0,
		"longitude": 0
	}
}
```

* **/favorite**: Returns an array of favorite shops sorted by distance to user.

Request body format:

```JSON
{
	"hashcode": "b8adf586687809a7d4d6eb61f62549209e218c75",
	"location": {
		"latitude": 0,
		"longitude": 0
	}
}
```

* **/like**: Adds a shop to favorite and returns the corresponding shop if exists, empty object if not.

Request body format:

```JSON
{
	"hashcode": "b8adf586687809a7d4d6eb61f62549209e218c75",
	"shop": {
		"id": 1,
		"name": "",
		"image": "",
		"location": {
			"latitude": 0.0,
			"longitude": 0.0
		}
	}
}
```

* **/dislike**: Dislike a shop so it won't be visible for the two next hours and return the correspondind shop if exists, empty object if not.

Request body format:

```JSON
{
	"hashcode": "b8adf586687809a7d4d6eb61f62549209e218c75",
	"shop": {
		"id": 2,
		"name": "",
		"image": "",
		"location": {
			"latitude": 0.0,
			"longitude": 0.0
		}
	}
}
```

* **/remove**: Removes a shop from favorite and return the corresponding shop if exists and is in favorites, empty object if not.

Request body format:

```JSON
{
	"hashcode": "b8adf586687809a7d4d6eb61f62549209e218c75",
	"shop": {
		"id": 1,
		"name": "",
		"image": "",
		"location": {
			"latitude": 0.0,
			"longitude": 0.0
		}
	}
}
```

_Note that for the five last services, some attributes are not being taken into account. A further release of the app might take them into account to make further investigations._

## Authors

* **Toumani** - *Initial work* - [Code Challenge MSA](https://github.com/Toumani/web-coding-challenge-msa/)

## License

This project is licensed under the MIT License 

