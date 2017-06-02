package xsh.raindrops.util.auth.jwt;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.expression.spel.ast.Operator;
import org.apache.commons.codec.binary.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.netty.util.internal.StringUtil;

/**
 * JJWT
 * @author Raindrops on 2017年5月19日
 */
public class JJwtUtils {
	/**
	 * jwt相关设置
	 */
	public static final String JWT_ID = "jwt";
	public static final String JWT_SECRET = "7786df7fc3a34e26a61c034d5ec8245d";
	public static final int JWT_TTL = 60*60*1000;  //millisecond， TTL=time to live
	public static final int JWT_REFRESH_INTERVAL = 55*60*1000;  //millisecond
	public static final int JWT_REFRESH_TTL = 12*60*60*1000;  //millisecond
	
	public static final String JWT_CLAIM_ACCOUNT_ID="userId";
	public static final String JWT_CLAIM_NONCE="nonce";//随机字符串
	
	/**
	 * 由字符串生成加密key
	 * @return
	 */
	public static SecretKey generalKey(){
		String stringKey = JWT_SECRET;
		byte[] encodedKey = Base64.decodeBase64(stringKey);
	    SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
	    return key;
	}

	/**
	 * 创建jwt
	 * @param id
	 * @param subject
	 * @param ttlMillis
	 * @return
	 * @throws Exception
	 */
	public static String createJWT(String key,String subject, long ttlMillis) throws Exception {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		JwtBuilder builder = Jwts.builder()
			.setIssuedAt(now)
			.setSubject(subject)
		    .signWith(signatureAlgorithm, key);
		if (ttlMillis >= 0) {
		    long expMillis = nowMillis + ttlMillis;
		    Date exp = new Date(expMillis);
		    builder.setExpiration(exp);
		}
		return builder.compact();
	}
	
	/**
	 * 创建jwt
	 * @param id
	 * @param subject
	 * @param ttlMillis
	 * @return
	 * @throws Exception
	 */
	public static String createJWT(Operator userAccount,String key, long ttlMillis)  {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		//String nonce=StringUtil.generateRandomString(6);
		
		JwtBuilder builder = Jwts.builder()
			.setIssuedAt(now)
			//.setSubject(userAccount.getName())
			//.claim(JWT_CLAIM_ACCOUNT_ID, userAccount.getOpId())
			//.claim(JWT_CLAIM_NONCE, nonce)
		    .signWith(signatureAlgorithm, key);
		
		//不设置过期时间,使用随机字符串代替
//		if (ttlMillis > 0) {
//		    long expMillis = nowMillis + ttlMillis;
//		    Date exp = new Date(expMillis);
//		    builder.setExpiration(exp);
//		}
		return builder.compact();
	}
	
	/**
	 * 解密jwt
	 * @param jwt
	 * @return
	 * @throws Exception
	 */
	public static Claims parseJWT(String key,String jwt) throws Exception{
		Claims claims = Jwts.parser()         
		   .setSigningKey(key)
		   .parseClaimsJws(jwt).getBody();
		return claims;
	}
	
	/**
	 * 从jwt中获取账号id
	 * @param claims
	 * @return
	 * @throws Exception
	 */
	public static Long getUserId(String key,String jwt) throws Exception{
		Claims claims=parseJWT(key,jwt);
		return getUserId(claims);
	}
	
	/**
	 * 从jwt中获取账号id
	 * @param claims
	 * @return
	 * @throws Exception
	 */
	public static Long getUserId(Claims claims) throws Exception{
		if(claims==null){
			return null;
		}
		
		Object userIdObj= claims.get(JJwtUtils.JWT_CLAIM_ACCOUNT_ID);
		Long currentUserId=null;
		if(userIdObj==null){
			return null;
		}
		
		if(userIdObj instanceof Integer){
			 currentUserId=((Integer) userIdObj).longValue();
		 }else if(userIdObj instanceof Long){
			 currentUserId=((Long) userIdObj).longValue();
		 }else if(userIdObj instanceof String){
			 try {
				currentUserId=Long.valueOf((String)userIdObj);
			} catch (Exception e) {
			}
		 }else{
		 }
		return currentUserId;
	}
	
	
	 public static void main(String[] args) throws Exception {
	}
}
