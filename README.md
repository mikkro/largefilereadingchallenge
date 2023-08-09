# Assignment for Kyotu

## How to use?

Set up path to file to be processed by calling the @POST /configuration.

Example request: 
```
{
    "filePath": "/Users/mikolaj/Desktop/example_file.csv"
}
```

Use the @GET /statistics/{city} endpoint to get statistics for a given city.
The first call creates a background job and the file is processed.
You can monitor background job with an endpoint @GET /tasks/{id}.
After the file is processed, the endpoint returns the result.
If the file has changed between requests, it is reprocessed.

## How to run application locally?

You can run it using Gradle Wrapper:

```
./gradlew bootRun
```

## How to run tests

To run tests you can use Gradle Wrapper:

```
./gradlew test
```

## How to get Swagger
Swagger can be found under following link
```
http://localhost:8080/swagger-ui/index.html
```

## How to get DB
To connect to DB use following link
```
http://localhost:8080/h2-console
```
Username: sa \
Password: password

