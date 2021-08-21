package de.roamingthings.jokes.fn.authorizer;

import com.amazonaws.services.lambda.runtime.events.APIGatewayCustomAuthorizerEvent;
import com.amazonaws.services.lambda.runtime.events.IamPolicyResponseV1;
import com.nimbusds.jwt.SignedJWT;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static com.amazonaws.services.lambda.runtime.events.IamPolicyResponseV1.ALLOW;
import static com.amazonaws.services.lambda.runtime.events.IamPolicyResponseV1.DENY;
import static com.amazonaws.services.lambda.runtime.events.IamPolicyResponseV1.EXECUTE_API_INVOKE;
import static com.amazonaws.services.lambda.runtime.events.IamPolicyResponseV1.PolicyDocument;
import static com.amazonaws.services.lambda.runtime.events.IamPolicyResponseV1.Statement;
import static com.amazonaws.services.lambda.runtime.events.IamPolicyResponseV1.VERSION_2012_10_17;
import static com.amazonaws.services.lambda.runtime.events.IamPolicyResponseV1.builder;
import static io.micronaut.http.HttpHeaders.AUTHORIZATION;
import static java.util.Collections.singletonList;


@Introspected
public class AuthorizationHandler extends MicronautRequestHandler<APIGatewayCustomAuthorizerEvent, IamPolicyResponseV1> {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationHandler.class);

    private static final int TOKEN_START_INDEX = 7;
    private static final String JWT_CLAIM_NAME = "name";

    @Inject
    private String jwtPublicKey;

    @Override
    public IamPolicyResponseV1 execute(APIGatewayCustomAuthorizerEvent authorizerEvent) {
        if (authorizerEvent == null) {
            return createDenyResponse("*");
        }

        String bearerToken = getBearerToken(authorizerEvent);
        if (bearerToken == null) {
            return createDenyResponse(authorizerEvent.getMethodArn());
        }

        try {
            SignedJWT authTokenJwt = SignedJWT.parse(bearerToken);
            log.info("Skipping verification with public key: {}", jwtPublicKey);
            // authTokenJwt.verify(...);

            var claimsSet = authTokenJwt.getJWTClaimsSet();
            var subject = claimsSet.getSubject();
            var context = new HashMap<String, Object>();
            context.put("tokenName", claimsSet.getClaim(JWT_CLAIM_NAME));

            return createAllowResponse(subject, authorizerEvent.getMethodArn(), context);
        } catch (java.text.ParseException e) {
            log.info("Failed to parse JWT: ", e);
            return createDenyResponse(authorizerEvent.getMethodArn());
        }
    }

    private String getBearerToken(APIGatewayCustomAuthorizerEvent authorizerEvent) {
        var authorizationHeader = authorizerEvent.getHeaders().get(AUTHORIZATION);
        if (missingBearerToken(authorizationHeader)) {
            return null;
        }
        return authorizationHeader.substring(TOKEN_START_INDEX);
    }

    private boolean missingBearerToken(String authorizationHeader) {
        return authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") || authorizationHeader.length() < TOKEN_START_INDEX+1;
    }

    private IamPolicyResponseV1 createAllowResponse(String principalId, String methodArn, Map<String, Object> context) {
        return builder()
                .withPrincipalId(principalId)
                .withContext(context)
                .withPolicyDocument(
                        PolicyDocument.builder()
                                .withVersion(VERSION_2012_10_17)
                                .withStatement(
                                        singletonList(
                                                Statement.builder()
                                                        .withAction(EXECUTE_API_INVOKE)
                                                        .withEffect(ALLOW)
                                                        .withResource(singletonList(methodArn))
                                                        .build()
                                        )
                                )
                                .build()
                )
                .build();
    }

    private IamPolicyResponseV1 createDenyResponse(String methodArn) {
        return builder()
                .withPrincipalId("unknown")
                .withPolicyDocument(
                        PolicyDocument.builder()
                                .withVersion(VERSION_2012_10_17)
                                .withStatement(
                                        singletonList(
                                                Statement.builder()
                                                        .withAction(EXECUTE_API_INVOKE)
                                                        .withEffect(DENY)
                                                        .withResource(singletonList(methodArn))
                                                        .build()
                                        )
                                )
                                .build()
                )
                .build();
    }
}
