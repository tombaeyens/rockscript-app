package io.rockscript.api.queries;

import io.netty.handler.codec.http.HttpMethod;
import io.rockscript.netty.router.AsyncHttpRequest;
import io.rockscript.netty.router.AsyncHttpResponse;
import io.rockscript.netty.router.Context;
import io.rockscript.netty.router.RequestHandler;
import io.rockscript.util.Io;

import java.io.InputStream;

public class FileHandler implements RequestHandler {

  @Override
  public void handle(AsyncHttpRequest request, AsyncHttpResponse response, Context context) {
    if (request.getFullHttpRequest().getMethod()==HttpMethod.GET) {
      String path = request.getFullHttpRequest().getUri();
      if ("/".equals(path)) {
        path = "/index.html";
      }

      InputStream stream = FileHandler.class.getClassLoader().getResourceAsStream("webfiles" + path);
      if (stream!=null) {
        byte[] bytes = Io.readBytesFromStream(stream);
        response.bodyBytes(bytes);
//        if (path.startsWith("/img")) {
//        } else {
//          String fileContent = Io.toString(stream);
//          response.bodyString(fileContent);
//        }
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
}
