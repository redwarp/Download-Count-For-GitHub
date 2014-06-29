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

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import net.redwarp.android.app.githubcount.data.Project;
import net.redwarp.android.app.githubcount.data.adapters.ProjectsAdapter;
import net.redwarp.android.app.githubcount.database.ProjectDataSource;

import java.util.List;

public class MainFragment extends Fragment {
    ProjectDataSource mDataSource;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mDataSource = new ProjectDataSource(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDataSource.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Project project = (Project) mListView.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), ProjectDetailActivity.class);
                intent.putExtra("user", project.user);
                intent.putExtra("repository", project.repository);

                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivityForResult(new Intent(getActivity(), AddSimpleActivity.class), 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String user = data.getStringExtra("user");
                String repository = data.getStringExtra("repository");

                if (user != null && repository != null) {
                    Project project = new Project(user, repository);
                    mDataSource.open();
                    mDataSource.saveProject(project);
                    mDataSource.close();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        queryProjects();
    }

    private void queryProjects() {
        mDataSource.open();
        List<Project> projects = mDataSource.getAllProjects();
        mListView.setAdapter(new ProjectsAdapter(projects));
        mDataSource.close();
    }


}
