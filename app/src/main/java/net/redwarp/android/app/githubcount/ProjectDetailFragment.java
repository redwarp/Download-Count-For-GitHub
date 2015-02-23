/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright 2014 Redwarp
 */

package net.redwarp.android.app.githubcount;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.redwarp.android.app.githubcount.data.Release;
import net.redwarp.android.app.githubcount.data.adapters.ReleaseAdapter;
import net.redwarp.android.app.githubcount.network.GithubArrayRequest;
import net.redwarp.android.app.githubcount.network.ReleaseRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectDetailFragment extends Fragment {

  ExpandableListView mListView;
  TextView mErrorLabel;
  ProgressBar mProgressBar;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_project_detail, container, false);

    mListView = (ExpandableListView) view.findViewById(android.R.id.list);
    mErrorLabel = (TextView) view.findViewById(R.id.error);
    mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    mListView.setEmptyView(view.findViewById(android.R.id.empty));

    return view;
  }

  @Override
  public void onStart() {
    super.onStart();

    String user = getArguments().getString("user");
    String repository = getArguments().getString("repository");

    GithubArrayRequest<List<Release>>
        request =
        new ReleaseRequest(
            user, repository,
            new Response.Listener<List<Release>>() {
              @Override
              public void onResponse(List<Release> response) {
                mListView.setAdapter(new ReleaseAdapter(response));
                for (int i = 0; i < response.size(); i++) {
                  mListView.expandGroup(i);
                }
              }
            }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            mProgressBar.setVisibility(View.GONE);
            mErrorLabel.setVisibility(View.VISIBLE);
          }
        }) {
          @Override
          protected List<Release> parseJson(JSONArray jsonArray) {
            return parseReleases(jsonArray);
          }
        };
    request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                  DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    request.setTag(this);

    MyApplication.getRequestQueue().add(request);
  }

  @Override
  public void onStop() {
    super.onStop();

    MyApplication.getRequestQueue().cancelAll(this);
  }

  private List<Release> parseReleases(JSONArray jsonArray) {
    List<Release> releases = new ArrayList<>(jsonArray.length());
    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject object = jsonArray.optJSONObject(i);
      Release release = new Release(object);
      if (release.assets.size() > 0) {
        releases.add(release);
      }
    }
    Collections.sort(releases, new Comparator<Release>() {
      @Override
      public int compare(Release lhs, Release rhs) {
        if (lhs.createdDate != null && rhs.createdDate != null) {
          return rhs.createdDate.compareTo(lhs.createdDate);
        } else {
          return 0;
        }
      }
    });
    return releases;
  }

  public static ProjectDetailFragment newInstance(String user, String repository) {
    ProjectDetailFragment fragment = new ProjectDetailFragment();
    Bundle args = new Bundle();
    args.putString("user", user);
    args.putString("repository", repository);
    fragment.setArguments(args);

    return fragment;
  }
}
