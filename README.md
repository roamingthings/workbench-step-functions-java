# Workbench for AWS Step Functions

This project demonstrates how to implement a serverless application using Java and the [Micronaut](https://micronaut.io)
Framework.

The following AWS technologies are used:
* [API Gateway](https://aws.amazon.com/api-gateway/) to provide calls into the application
* [Lambda](https://aws.amazon.com/lambda/) for 
* [Step Functions](https://aws.amazon.com/step-functions/) for building the business workflow
* [Serverless Application Model (SAM)](https://aws.amazon.com/serverless/sam/) for deployment 

The application uses a workflow to retrieve a joke from [JokeAPI](https://jokeapi.dev/) and store the result in a
[DynamoDB](https://aws.amazon.com/dynamodb/) table in anasynchronous way.

## Architecture

The application utilizes the following services:
* an API Gateway to handle incoming requests
* a Lambda function to create a new job and trigger the main workflow
* the main workflow implemented as a Step Functions statemachine responsible for tracking the job status and querying
the Joke API
* a Lambda function that actually talks to the Joke API to retrieve a new joke
* a Lambda function to query the status of a given job including the joke that has been retrieved
* a DynamoDB table persisting the state of a job and the retrieved joke

![Architecture](resources/workbench-step-functions.png)

Read more about the [three different implementation models](docs/implementations.md) that are demonstrated in this project.

## Prerequisites

To build and deploy the application the following tools will have to be installed:

* SAM CLI - [Install the SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html)
* AWS CLI - [Install the AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)
* Docker - [Install Docker community edition](https://hub.docker.com/search/?type=edition&offering=community)
* Java 11 - [Install Java 11](https://openjdk.java.net/install/) or use a manager like [SDKMAN!](https://sdkman.io/install) 

## Build the application

To build the wohle Application `sam` is used to build the Java artifacts and [CloudFormation](https://aws.amazon.com/cloudformation/)
template describing the application stack.

When building the application you can choose to skip the tests which reduces the build time.
```shell
sam build # Run tests during build
GRADLE_SKIP_TESTS=true sam build # Skip tests and only build the application      
```

Under the hood each function or application is build using [Gradle](https://gradle.org/). This allows you to build and
test each function or application individually by running

```shell
../gradlew clean build
```

### Disable tests during `sam build`

By default, Gradle will run all tests each time `sam build` is called. This is also true when using the AWS Toolbox in
IntelliJ or Visual Studio Code. This will slow down the development process or even prevent local execution due to
failing tests.

To prevent tests being run by `sam build` create a file `$HOME/.gradle/init.gradle.kts` and add the following lines:

```kotlin
allprojects {
    tasks.withType(Test::class) {
        onlyIf {
            System.getProperty("software.amazon.aws.lambdabuilders.scratch-dir") == null
                    || System.getenv("GRADLE_SAM_EXECUTE_TEST") != null
        }
    }
}
```

You can turn on testing by setting the environment variable `GRADLE_SAM_EXECUTE_TEST` to any value:

```shell
GRADLE_SAM_EXECUTE_TEST=true sam build
```

## Deploy the application

After building the application it can be deployed to your AWS account. Make sure that you have a working configuration
of your AWS credentials and a valid session.

```bash
sam deploy --guided
```

This command will deploy your application to AWS, with a series of prompts:

* **Stack Name**: The name of the stack to deploy to CloudFormation. This should be unique to your account and region, and a good starting point would be something matching your project name.
* **AWS Region**: The AWS region you want to deploy your app to.
* **Confirm changes before deploy**: If set to yes, any change sets will be shown to you before execution for manual review. If set to no, the AWS SAM CLI will automatically deploy application changes.
* **Allow SAM CLI IAM role creation**: Many AWS SAM templates, including this example, create AWS IAM roles required for the AWS Lambda function(s) included to access AWS services. By default, these are scoped down to minimum required permissions. To deploy an AWS CloudFormation stack which creates or modifies IAM roles, the `CAPABILITY_IAM` value for `capabilities` must be provided. If permission isn't provided through this prompt, to deploy this example you must explicitly pass `--capabilities CAPABILITY_IAM` to the `sam deploy` command.
* **<Resource> may not have authorization defined, Is this okay?**: Acknowledge that no authorization is required when calling the resource using the API Gateway.
* **Save arguments to samconfig.toml**: If set to yes, your choices will be saved to a configuration file inside the project, so that in the future you can just re-run `sam deploy` without parameters to deploy changes to your application.

You can find your API Gateway Endpoint URL in the output values displayed after deployment.

## Call the API

This application contains three APIs using different Micronaut implementations:
* Plain function handlers
* Two Micronaut applications for create and status endpoints
* One Micronaut application for all API endpoints

The base URL will be provided as output values after the application has been successfully deployed.

Since the application requires authorization a header containing a bearer token has to be provided:

```shell
curl -v -X POST https://ApiId>.execute-api.eu-central-1.amazonaws.com/Prod/jobs -H "Authorization: Bearer <TOKEN>"
```

A token can be generated using [jwt.io](https://jwt.io). The toke has to be a signed token.

You can also use the following sample token:
```shell
curl -v -X POST https://<ApiId>.execute-api.eu-central-1.amazonaws.com/Prod/jobs -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
```

Also, a public key for verifying an authentication token is deployed with the application it will not be used
to simplify the demo.

## Fetch, tail, and filter Lambda function logs

To simplify troubleshooting, SAM CLI has a command called `sam logs`. `sam logs` lets you fetch logs generated by your deployed Lambda function from the command line. In addition to printing the logs on the terminal, this command has several nifty features to help you quickly find the bug.

```bash
sam logs --stack-name workbench-step-functions-java -n <FunctionName> --tail
```

This example assumes that `workbench-step-functions-java` was used when deploying the application.

## Cleanup

To delete the application, use the AWS CLI:

```bash
aws cloudformation delete-stack --stack-name workbench-step-functions-java
```

This example assumes that `workbench-step-functions-java` was used when deploying the application.

## Resources

* [JokeAPI](https://jokeapi.dev/)
* [DynamoDB](https://aws.amazon.com/dynamodb/)
