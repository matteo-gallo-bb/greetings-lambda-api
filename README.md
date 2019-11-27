# Greetings AWS lambda API with Java 11

The starter project defines a simple `/greetings` REST resource that can accept `GET` requests using API gateway and Java 11 runtime.

The project folder also includes a `template.yaml` file. You can use this [SAM](https://github.com/awslabs/serverless-application-model) file to deploy the project to AWS Lambda and Amazon API Gateway or test in local with [SAM Local](https://github.com/awslabs/aws-sam-local).

Using [Maven](https://maven.apache.org/), you can create an AWS Lambda-compatible zip file simply by running the maven package command from the project folder.

You can use [AWS SAM Local](https://github.com/awslabs/aws-sam-local) to run this project in local.

First, install SAM local:

```bash
$ brew tap aws/tap
$ brew install aws-sam-cli
```

Next, from the project root folder - where the `template.yaml` file is located - start the API with the SAM Local CLI.

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

Alternatively if you want to just invoke the lambda function use the `event.json` file that simulates a request from the API gateway.

```bash
$ sam local invoke -e event.json
```

In this case you'll get directly the response from the lambda.

```sample reponse
{
    "body": "{\"greetings\":\"Hello Matteo Gallo, your age is: 23\"}",
    "headers": {"Content-Type":"application/json"},
    "statusCode": 200
}
```

It’s interesting to see that the new Java 11 runtime (running on Amazon Linux 2) seams faster (at least in local) than java 8.
Below the comparison between 2 lambdas (java 8 and 11) that are just returning a greeting message (with no REST call).

```lambda comparison between Java 8 and Java 11
java 8 -> Init Duration: 449.87 ms        Duration: 27.60 ms      Billed Duration: 100 ms Memory Size: 128 MB     Max Memory Used: 57 MB
Java 11 -> Init Duration: 293.23 ms       Duration: 13.33 ms     Billed Duration: 100 ms Memory Size: 128 MB     Max Memory Used: 63 MB
```