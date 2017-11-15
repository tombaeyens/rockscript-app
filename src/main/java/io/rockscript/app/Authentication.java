package io.rockscript.app;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import io.rockscript.netty.router.ForbiddenException;
import io.rockscript.netty.router.Interceptor;
import io.rockscript.netty.router.InterceptorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class Authentication implements Interceptor {

  static Logger log = LoggerFactory.getLogger(Authentication.class);

  static ThreadLocal<String> uids = new ThreadLocal<>();
  static FirebaseAuth firebaseAuth = null;

  public static String getUidRequired() {
    String uid = uids.get();
    if (uid==null) {
      throw new ForbiddenException("Authorization header is required and must contain token id for this request");
    }
    return uid;
  }

  @Override
  public void intercept(InterceptorContext interceptorContext) {
    String idToken = interceptorContext.getRequest().getHeader("Authorization");
    if (idToken!=null) {
      String tokenUid = decodeToken(idToken);
      if (tokenUid==null) {
        throw new ForbiddenException("Invalid JWT");
      } else {
        uids.set(tokenUid);
        try {
          interceptorContext.next();
        } finally {
          uids.remove();
        }
      }
    } else {
      interceptorContext.next();
    }
  }

  private String decodeToken(String idToken) {
    try {
      FirebaseToken decodedToken = getFirebaseAuth().verifyIdTokenAsync(idToken).get();
      if (!decodedToken.getIssuer().equals("https://securetoken.google.com/rockscript-app")) {
        return null;
      }
      return decodedToken.getUid();
    } catch (Exception e) {
      log.debug("Id token authentication failed: "+e.getMessage(), e);
      return null;
    }
  }

  public static FirebaseAuth getFirebaseAuth() {
    if (firebaseAuth==null) {
      try {
        InputStream serviceAccount = Authentication.class.getResourceAsStream("/rockscript-app-firebase.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .setDatabaseUrl("https://rockscript-app.firebaseio.com/")
          .build();
        FirebaseApp.initializeApp(options);
        firebaseAuth = FirebaseAuth.getInstance();
      } catch (IOException e) {
        log.debug("Couldn't initialize authentication: "+e.getMessage(), e);
        throw new RuntimeException("Couldn't initialize authentication");
      }
    }
    return firebaseAuth;
  }
}
