# Assignment for Kyotu

## How to use?

By calling the @POST /temperatures endpoint with the URL to the file to be processed, the application creates a background job. The response header contains the task ID.

Task statuses can be found in @GET /tasks endpoint.

After processing the task, its result can be found at the @GET /tasks/{id}/statistics endpoint.

Additionally, there is a background job that clears completed tasks.


## How to run application locally?

You can build docker image and run it:

```
docker build -t kyotu .
docker run -m 512m --memory-reservation=256m -p 8080:8080 -it kyotu exec
```

Alternatively, you can run it using Gradle Wrapper:

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

