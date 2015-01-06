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

package net.redwarp.android.app.githubcount.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.redwarp.android.app.githubcount.R;
import net.redwarp.android.app.githubcount.data.Project;

import java.util.List;

public class ProjectsAdapter extends BaseAdapter {

  private final List<Project> mProjects;

  public ProjectsAdapter(List<Project> projects) {
    mProjects = projects;
  }

  @Override
  public int getCount() {
    return mProjects.size();
  }

  @Override
  public Object getItem(int position) {
    return mProjects.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.list_project, parent, false);
    }
    TextView user = (TextView) convertView.findViewById(R.id.user);
    TextView repository = (TextView) convertView.findViewById(R.id.repository);
    Project project = (Project) getItem(position);

    user.setText(project.user);
    repository.setText(project.repository);

    return convertView;
  }
}
