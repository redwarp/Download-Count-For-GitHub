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

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import net.redwarp.android.app.githubcount.data.Release;
import net.redwarp.android.app.githubcount.data.adapters.ReleaseAdapter;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailFragment extends Fragment {

    TextView mTitleLabel;
    ExpandableListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        mTitleLabel = (TextView) view.findViewById(R.id.title);
        mListView = (ExpandableListView) view.findViewById(R.id.listView);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        String user = getArguments().getString("user");
        String repository = getArguments().getString("repository");

        JsonArrayRequest request = new JsonArrayRequest("https://api.github.com/repos/" + user + "/" + repository + "/releases", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                mTitleLabel.setText("Class = " + jsonArray.getClass());
                parseReleases(jsonArray);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Request error", Toast.LENGTH_SHORT).show();
                mTitleLabel.setText("Request failed");
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(this);

        MyApplication.getRequestQueue().add(request);
    }

    @Override
    public void onStop() {
        super.onStop();

        MyApplication.getRequestQueue().cancelAll(this);
    }

    private void parseReleases(JSONArray jsonArray) {
        List<Release> releases = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.optJSONObject(i);
            Release release = new Release(object);
            releases.add(release);
        }

        mListView.setAdapter(new ReleaseAdapter(releases));
        for (int i = 0; i < releases.size(); i++) {
            mListView.expandGroup(i);
        }
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
