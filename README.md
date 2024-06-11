# URL Shortener API
This is an API application that allows you to create short version on URLs and serves as a redirect proxy
for those URLs.  

## Project setup and run
All commands should be run from the repository root.

## Project requirements
- `Docker`

### Get the code
Clone the repository and go to its root.

```
git clone https://github.com/owlsdonttalk/url-shortener
cd url-shortener
```


### Application commands
To build & run docker container with PSQL as a storage inside of it.

In development mode: 
```
SPRING_PROFILES_ACTIVE=dev docker-compose up --build
```

In production mode: 
```
SPRING_PROFILES_ACTIVE=prod docker-compose up --build
```

To run tests:
```
./mvnw test
```

### API Endpoints
After you got application up & running try one of the following:

#### Actuator Endpoints

- Health: `` /actuator/health ``
- Other Actuator endpoints: `` /actuator ``

#### Shortening API

#### 1. Create Short URL

- **POST** ```/api/url/create```
- Content-Type: application/json
- Creates a shortened URL

***Request Body:***
```
{ "originalUrl": "http://example.com" }
```
#### 2. Get URL Entity by Short URL

- **GET** ```/api/url/{shortUrl}```
- Retrieves the URL entity by its shortened URL.

#### 3. Delete URL Entity
- **DELETE** ```/api/url/{shortUrl}```
- Deletes the URL entity by its shortened URL.

#### 4. Update URL Entity
- **PUT** ```/api/url/{shortUrl}```
- Content-Type: application/json
- Updates the URL entity with new data.
```
{ "originalUrl": "http://anotherexample.com" }
```

#### 5. Patch URL Entity
- **PATCH** ```/api/url/{shortUrl}```
- Content-Type: application/json
- Partially updates the URL entity
```
{  "originalUrl": "http://anotherexample.com" }
```

#### 6. Redirect to Original URL
- **GET** ```/{shortUrl}```
- Redirects to the original URL based on the shortened URL.


#### Statistics API

#### Get URL Click Count
- **GET** ``` /api/stats/{shortUrl} ```
- Retrieves the click count for the specified shortened URL.

#### Get Overall URL Count
- **GET** ``` /api/stats/overall ```
- Retrieves the total count of URLs.
 
#### Get URL Count by Status
- **GET** ``` /api/stats/status/{status} ```
- Retrieves the count of URLs by their status.