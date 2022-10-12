import * as path from "path";
import {HttpApi, HttpMethod} from "@aws-cdk/aws-apigatewayv2";
import {HttpLambdaIntegration} from "@aws-cdk/aws-apigatewayv2-integrations/lib/http/lambda";
import {Code, Runtime, Function} from "@aws-cdk/aws-lambda";
import * as cdk from '@aws-cdk/core';

export class InfraStack extends cdk.Stack {
    constructor(scope: cdk.Construct, id: string, props?: cdk.StackProps) {
        super(scope, id, props);

        const apiLambda = new Function(this, 'graalvm-lambda', {
            runtime: Runtime.PROVIDED_AL2,
            handler: 'com.momento.Handler',
            code: Code.fromAsset(path.join(__dirname, '../../target/function.zip')),
            memorySize: 1024
        });
        const api = new HttpApi(this, 'test-java-service', {
            apiName: 'test-java-service',
        });

        // Set up default root proxy integration
        api.addRoutes({
            path: '/hello',
            methods: [ HttpMethod.GET ],
            integration: new HttpLambdaIntegration('func-int', apiLambda),
        });

    }
}
