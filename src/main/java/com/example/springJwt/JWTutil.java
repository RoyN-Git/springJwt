package com.example.springJwt;

package com.jb.targil_spring1.util;

import com.jb.targil_spring1.beans.UserDetails;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTutil {
    //type of encryption - סוג של אלגורתים להצפנה
    private String signatureAlgorithm = SignatureAlgorithm.HS256.getJcaName();
    //our private key - מפתח ההצפנה שקיים רק אצלנו
    private String encodedSecretKey = "this+is+my+key+and+it+must+be+at+least+256+bits+long";
    //create our private key - יצירה של מפתח ההצפנה לשימוש ביצירה של הטוקנים שלנו
    private Key decodedSecrectKey = new SecretKeySpec(Base64.getDecoder().decode(encodedSecretKey), this.signatureAlgorithm);

    //generate key
    //we need user email, password and role (תפקיד) for create the token
    //since the userDetail is an instace of class, we need to make it hashcode
    //the token need to get clamis which is the information in hashcode
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();        //create new hash map for claims
        claims.put("userPass", userDetails.getPassword());  //insert password
        claims.put("userType", userDetails.getUserType());  //insert user type (role)
        return createToken(claims, userDetails.getEmail()); //send the subject (email)
    }

    //we create the JWT token from the information that we got.
    private String createToken(Map<String, Object> claims, String email) {
        Instant now = Instant.now();//get current time
        return Jwts.builder()       //create JWT builder to assist us with JWT creation
                .setClaims(claims)  //set our data (clamis-user,password,role,etc...)
                .setSubject(email)  //set our subject, the first item that we will check
                .setIssuedAt(Date.from(now))  //create issued at , which is current time
                //experation date, which after the date, we need to create a new token
                .setExpiration(Date.from(now.plus(30, ChronoUnit.MINUTES)))
                .signWith(this.decodedSecrectKey) //sign the token with our secret key
                .compact();   //compact our token, that it will be smaller :)
    }

    private Claims extractAllClaims(String token) throws ExpiredJwtException {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(this.decodedSecrectKey)
                .build();
        return jwtParser.parseClaimsJws(token).getBody();
    }

    private String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Date extractExperationDate(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token) {
        try {
            extractAllClaims(token);
            return false;
        } catch (ExpiredJwtException err) {
            return true;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userEmail = extractEmail(token);
        return (userEmail.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }

    //tester
    public static void main(String[] args) {
        //create our instance of user that the token will be created for him.
        UserDetails admin = new UserDetails("admin@admin.com", "12345", "Admin");
        //use our new shiny JWTutil.
        JWTutil myUtil = new JWTutil();
        //generate a new token
        String myToken = myUtil.generateToken(admin);
        //print the token on screen to show to our mother.
        System.out.println("my new shiny token:\n" + myToken);
        //get our clamis :)
        System.out.println("our token body is:\n" + myUtil.extractAllClaims(myToken));
        System.out.println("and the winner is:\n" + myUtil.extractEmail(myToken));
    }

    @GetMapping("login/{userEmail}")
    public ResponseEntity<?> getLoginToken(@PathVariable String userEmail) {
        UserDetails admin = new UserDetails(userEmail, "12345", "Admin");
        //JWTutil myJWT = new JWTutil();
        return new ResponseEntity<>(myUtil.generateToken(admin), HttpStatus.ACCEPTED);
    }
    @GetMapping("lecturer/all")  //http://localhost:8080/lecturer/all
    public ResponseEntity<?> getAllLecturer(@RequestHeader (name="Authorization") String token) throws LoginException {
        //check if token is valid
        if (myUtil.validateToken(token,new UserDetails("admin@admin.com","12345","admin"))){
            return new ResponseEntity<>(school.getAllLecturer(), HttpStatus.OK);
        } else {
            throw new LoginException("Invalid user !");
        }
//enable CrossOrigion, allow to get request from web browser on another port (security issue)
        @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")




    }




}