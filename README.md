# URL Shortener API
API for short URL creation

## Project structure
- [Url Shortener](src%2Fmain%2Fjava%2Fcom%2Fowlsdonttalk%2Furlshortener): this package has
  Model, Repository and Controllers for handling simple API for URL Shorten Entity
- [Resources](src%2Fmain%2Fresources): this group contains properties for running application, tests & migration
  to set up persistent storage
- [Tools](tools): this folder has Docker container with PostgresSQL, needed for tests

## Project setup and run
All commands should be run from the repository root.

### Get the code
Clone the repository and go to its root.

```
git clone https://github.com/owlsdonttalk/url-shortener
cd url-shortener
```

### Application commands
To build & run docker container with PSQL as a storage inside of it.

```
docker-compose up --build
```

### API Endpoints
After you got application up & running try one of the following:

#### 1. Create Short URL

- Endpoint: ```/api/url/create```
- Method: POST
- Content-Type: application/json
- Creates a shortened URL
- force flag is used to suppress url validation check (if needed)

***Request Body:***

```
{
  "originalUrl": "http://example.com",
  "force": "true"   
}
```
#### 2. Get URL Entity by Short URL

- Endpoint: ```/api/url/{shortUrl}```
- Method: GET
- Retrieves the URL entity by its shortened URL.

#### 3. Delete URL Entity
- Endpoint: ```/api/url/{shortUrl}```
- Method: DELETE
- Deletes the URL entity by its shortened URL.

#### 4. Update URL Entity
- Endpoint: ```/api/url/{shortUrl}```
- Method: PUT
- Content-Type: application/json
- Updates the URL entity with new data.
```
{
"originalUrl": "http://anotherexample.com"
}
```

#### 5. Patch URL Entity
- Endpoint: ```/api/url/{shortUrl}```
- Method: PATCH
- Content-Type: application/json
- Partially updates the URL entity
```
{ 
  "originalUrl": "http://anotherexample.com"
}
```

#### 6. Redirect to Original URL
- Endpoint: ```/{shortUrl}```
- Method: GET
- Redirects to the original URL based on the shortened URL.