package way.application.core.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
public class ApplePublicKeyProvider {

    private static final String APPLE_KEYS_URL = "https://appleid.apple.com/auth/keys";

    public PublicKey getPublicKeyByKid(String kid) throws Exception {
        // 1. Apple 키셋 불러오기
        String jwkSetJson = new RestTemplate().getForObject(APPLE_KEYS_URL, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode keys = mapper.readTree(jwkSetJson).get("keys");

        // 2. kid 매칭된 키 찾기
        for (JsonNode key : keys) {
            if (key.get("kid").asText().equals(kid)) {
                String n = key.get("n").asText();
                String e = key.get("e").asText();

                byte[] modulus = Base64.getUrlDecoder().decode(n);
                byte[] exponent = Base64.getUrlDecoder().decode(e);

                BigInteger bigIntModulus = new BigInteger(1, modulus);
                BigInteger bigIntExponent = new BigInteger(1, exponent);

                RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntExponent);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return keyFactory.generatePublic(keySpec);
            }
        }

        throw new IllegalArgumentException("해당 kid에 대한 Apple 공개키를 찾을 수 없음: " + kid);
    }
}
