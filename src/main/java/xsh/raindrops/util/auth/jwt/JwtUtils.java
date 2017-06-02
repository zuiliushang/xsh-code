package xsh.raindrops.util.auth.jwt;

import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;

/**
 * JWT`安全工具类
 * JWT是Auth0提出的通过对JSON进行加密签名来实现授权验证的方案
 * 1. Headers 
 * 包括类别(typ).加密算法(alg);
 *  {"alg": "HS256",   "typ": "JWT" } 
 * 2. Claims
 * 包括需要传递的用户信息； 
 * { "sub": "1234567890", "name": "John Doe","admin": true } 
 * 3. Signature 
 * 根据alg算法与私有秘钥进行加密得到的签名字串； // 
 * 这一段是最重要的敏感信息，只能在服务端解密； 
 * @author Raindrops on 2017年5月19日
 */
public class JwtUtils {
	
	public static final String SECRET = "leaderHpEdu2017";
	/**
	 * sign JWT
	 * 签发
	 */
    public static String signJWT(Integer userId,String secret) {
        //head头部分是默认的
        //Sign JWT (HS256)
        System.out.println(System.currentTimeMillis());
        final long iat = System.currentTimeMillis() / 1000L; // issued at claim
        final long exp = iat + 60L*60*24*30; // expires claim. In this case the token expires in 60 seconds
        final JWTSigner signer = new JWTSigner(secret);
        final HashMap<String, Object> claims = new HashMap<String, Object>();
//        claims.put("iss", issuer);//JWT issuer
        claims.put("exp", exp);// expire time
        claims.put("iat", iat);// sign time
        claims.put("userId", userId); // content
        final String jwt = signer.sign(claims);
        System.out.println(jwt);
        return jwt;
    }
    
    /**
     * Verify
     * 验证
     * @param jwt
     * @return
     */
    public static int verifyJWT(String jwt,String secret) {
        try {
            final JWTVerifier verifier = new JWTVerifier(secret);
            final Map<String, Object> claims = verifier.verify(jwt);
            int userId= (int) claims.get("userId");
            System.out.println(claims.toString());
            return userId;
        } catch (Exception e) {
            // Invalid Token
            System.out.println("过期或错误的");
            return 0;
        }
    }
}
