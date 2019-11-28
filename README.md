# Greetings AWS lambda API with Java 11

The starter project defines a simple `/greetings` REST resource that can accept `GET` requests using API gateway and Java 11 runtime.

The project folder also includes a `template.yaml` file. You can use this [SAM](https://github.com/awslabs/serverless-application-model) file to deploy the project to AWS Lambda and Amazon API Gateway or test in local with [SAM Local](https://github.com/awslabs/aws-sam-local).

Using [Maven](https://maven.apache.org/), you can create an AWS Lambda-compatible zip file simply by running the maven package command from the project folder. The package size is 35.25kB.

You can use [AWS SAM Local](https://github.com/awslabs/aws-sam-local) to run this project in local.

First, install SAM local:

```bash
$ brew tap aws/tap
$ brew install aws-sam-cli
```

Next, from the project root folder (where the `template.yaml` file is located) start the API with the SAM Local CLI:

```bash
$ sam local start-api
```

Using a new shell, you can send a test ping request to your API:

```bash
$ curl -s http://127.0.0.1:3000/greetings

{
    "greetings": "Hello Ciccio Palla, your age is: 23"
}
```

Or adding query parameters to customize the greeting message:

```bash
$ curl -s http://127.0.0.1:3000/greetings?firstName=Matteo&lastName=Gallo

{
    "greetings": "Hello Matteo Gallo, your age is: 10"
}
```

Skip the external REST call sending a parameter "age":

```bash
$ curl -s http://127.0.0.1:3000/greetings?firstName=Matteo&lastName=Gallo&age=1

{
    "greetings": "Hello Matteo Gallo, your age is: 1"
}
```

Alternatively if you want to just invoke the lambda function use the `event.json` file that simulates a request from the API gateway:

```bash
$ sam local invoke -e event.json
```

In this case you'll get directly the response from the lambda function.

```sample reponse
{
    "body": "{\"greetings\":\"Hello Matteo Gallo, your age is: 23\"}",
    "headers": {"Content-Type":"application/json"},
    "statusCode": 200
}
```

To invoke the lambda function avoiding running and shutting down the Docker container, using Docker and aws-cli instead of sam-cli.

First unpack lambda.jar fat jar:

```
mkdir -p target/lambda && (cd target/lambda; jar -xf ../lambda.jar)
```

Run docker from the root folder:
```
docker run --rm \
  -e DOCKER_LAMBDA_STAY_OPEN=1 \
  -e AWS_LAMBDA_FUNCTION_MEMORY_SIZE=128 \
  -p 9001:9001 \
  -v $PWD/target/lambda:/var/task:ro,delegated \
  lambci/lambda:java11 \
  com.mooveit.greetings.GreetingsLambdaHandler::handleRequest
```
  
Invoke lambda function with aws-cli:
```
aws lambda invoke --endpoint http://localhost:9001 --no-sign-request \
--function-name GreetingsLambdaFunction --payload '{"queryStringParameters": {"age":"44"} }' output.json \
--region us-east-1
``` 
 
Or call directly the REST endpoint:
```
curl -d '{"httpMethod": "GET", "queryStringParameters": {"firstName": "Matteo", "lastName": "Gallo", "age": "31" } }' \
-H "Content-Type: application/json" \
-X POST http://localhost:9001/2015-03-31/functions/GreetingsLambdaFunction/invocations
```

Execution stats when running with Docker + aws-cli (with no REST call - sending request param "age" - and without static init of HttpClient):

```Java 11 performance
Response time from Postman
cold: 535 ms
warm: 10 ms (pikes 16ms)

Lambda execution
Memory Size: 128 MB
Max Memory Used: 65 MB
Billed Duration: 100 ms

Cold-start
Init Duration: 476.18 ms
Duration: 88.16 ms

Avg warm duration (on 26 executions): 5.88 ms
```

The static initialization of HttpClient in GreetingsLambdaHandler helps the warm execution time when doing a REST call.