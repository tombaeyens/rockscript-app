package io.rockscript.app;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import io.rockscript.http.servlet.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

public class Authentication implements Filter {

  static Logger log = LoggerFactory.getLogger(Authentication.class);

  static ThreadLocal<String> uids = new ThreadLocal<>();
  FirebaseAuth firebaseAuth = null;

  /** To be used in the app to verify authenticated user */
  public static String getUidRequired() {
    String uid = uids.get();
    if (uid==null) {
      throw new ForbiddenException("Authorization header is required and must contain token id for this request");
    }
    return uid;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String idToken = httpRequest.getHeader("Authorization");
    if (idToken!=null) {
      String tokenUid = decodeToken(idToken);
      if (tokenUid==null) {
        throw new ForbiddenException("Invalid JWT");
      } else {
        uids.set(tokenUid);
        try {
          chain.doFilter(request, response);
        } finally {
          uids.remove();
        }
      }
    } else {
      chain.doFilter(request, response);
    }
  }

  private String decodeToken(String idToken) {
    try {
      FirebaseToken decodedToken = firebaseAuth.verifyIdTokenAsync(idToken).get();
      if (!decodedToken.getIssuer().equals("https://securetoken.google.com/rockscript-app")) {
        return null;
      }
      return decodedToken.getUid();
    } catch (Exception e) {
      log.debug("Id token authentication failed: "+e.getMessage(), e);
      return null;
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    initFirebaseAuth();
  }

  public FirebaseAuth initFirebaseAuth() {
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

  @Override
  public void destroy() {
  }
}
