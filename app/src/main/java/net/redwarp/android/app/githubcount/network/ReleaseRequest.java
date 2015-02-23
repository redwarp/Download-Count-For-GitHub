package net.redwarp.android.app.githubcount.network;

import com.android.volley.Response;

import net.redwarp.android.app.githubcount.data.Release;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Benoit Vermont on 16/01/15.
 */
public class ReleaseRequest extends GithubArrayRequest<List<Release>> {

  public ReleaseRequest(String user, String repository,
                        Response.Listener<List<Release>> listener,
                        Response.ErrorListener errorListener) {
    super("https://api.github.com/repos/" + user + "/" + repository + "/releases", listener,
          errorListener);
  }

  @Override
  protected List<Release> parseJson(JSONArray jsonArray) {
    return null;
  }
}
