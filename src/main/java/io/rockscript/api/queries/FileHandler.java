package io.rockscript.api.queries;

import io.netty.handler.codec.http.HttpMethod;
import io.rockscript.netty.router.AsyncHttpRequest;
import io.rockscript.netty.router.AsyncHttpResponse;
import io.rockscript.netty.router.Context;
import io.rockscript.netty.router.RequestHandler;
import io.rockscript.util.Io;

import java.io.InputStream;
import java.net.URLConnection;
import java.util.Map;

import static io.rockscript.util.Maps.entry;
import static io.rockscript.util.Maps.hashMap;

public class FileHandler implements RequestHandler {

  private final static Map<String,String> contentTypesByExtension = hashMap(
    entry(".html",  "text/html"),
    entry(".js",    "text/javascript"),
    entry(".css",   "text/css"),
    entry(".map",   "application/json"),
    entry(".woff2", "font/woff2"),
    entry(".ico",   "image/x-icon")
  );

  @Override
  public void handle(AsyncHttpRequest request, AsyncHttpResponse response, Context context) {
    if (request.getFullHttpRequest().getMethod()==HttpMethod.GET) {
      String path = request.getFullHttpRequest().getUri();
      if ("/".equals(path)) {
        path = "/index.html";
      }

      InputStream stream = FileHandler.class.getClassLoader().getResourceAsStream("webfiles" + path);
      if (stream!=null) {
        // If you know how to get the next this done async, you're my hero!
        // Real hero's submit PRs for async streaming fixes :)
        byte[] bytes = Io.readBytesFromStream(stream);
        response.bodyBytes(bytes);
        response.headerContentType(getContentTypeFromPath(path));
        response.statusOk();
        response.send();
        return;
      }

      String fileContent = Io.getResourceAsString( "webfiles" + path);
      if (fileContent!=null) {
      }
    }
    response.statusNotFound();
    response.send();
  }

  private String getContentTypeFromPath(String path) {
    int lastDotIndex = path.lastIndexOf('.');
    if (lastDotIndex!=-1) {
      String extension = path.substring(lastDotIndex);
      String contentType = contentTypesByExtension.get(extension);
      if (contentType!=null) {
        return contentType;
      }
    }
    String contentType = URLConnection.guessContentTypeFromName(path);
    if (contentType!=null) {
      return contentType;
    }
    throw new RuntimeException("Could not determine content type for "+path);
  }
}
