package net.redwarp.android.app.githubcount.network;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Benoit Vermont on 12/01/15.
 */
public abstract class GithubArrayRequest<T extends List> extends JsonRequest<T> {

  public GithubArrayRequest(String url, Response.Listener<T> listener,
                            Response.ErrorListener errorListener) {
    super(Method.GET, url, null, listener, errorListener);
  }

  @Override
  protected Response<T> parseNetworkResponse(NetworkResponse response) {
    try {
      String jsonString =
          new String(response.data, HttpHeaderParser.parseCharset(response.headers));
      JSONArray jsonArray = new JSONArray(jsonString);
      return Response.success(parseJson(jsonArray),
                              HttpHeaderParser.parseCacheHeaders(response));
    } catch (UnsupportedEncodingException e) {
      return Response.error(new ParseError(e));
    } catch (JSONException je) {
      return Response.error(new ParseError(je));
    }
  }

  protected abstract T parseJson(JSONArray jsonArray);
}
