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
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import net.redwarp.android.app.githubcount.data.Asset;
import net.redwarp.android.app.githubcount.data.Release;
import net.redwarp.android.app.githubcount.R;

import java.util.List;

public class ReleaseAdapter extends BaseExpandableListAdapter {
    private final List<Release> mReleases;

    public ReleaseAdapter(List<Release> releases) {
        mReleases = releases;
    }

    @Override
    public int getGroupCount() {
        return mReleases.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mReleases.get(groupPosition).assets.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mReleases.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mReleases.get(groupPosition).assets.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition * 1000000L;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 1000000L + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_title, parent, false);
        }
        TextView titleView = (TextView) convertView.findViewById(R.id.title);
        titleView.setText(((Release) getGroup(groupPosition)).name);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        Asset asset = (Asset) getChild(groupPosition, childPosition);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(asset.name);
        TextView count = (TextView) convertView.findViewById(R.id.count);
        count.setText(String.valueOf(asset.downloadCount));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
