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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AddSimpleFragment extends Fragment {

  EditText userEditText;
  EditText repositoryEditText;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_simple_add, container, false);

    userEditText = (EditText) view.findViewById(R.id.userEditText);
    repositoryEditText = (EditText) view.findViewById(R.id.repositoryEditText);

    return view;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    menu.clear();

    inflater.inflate(R.menu.add_simple, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_add) {
      onOkClicked();

      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void onOkClicked() {
    Intent intent = new Intent();
    intent.putExtra("user", userEditText.getText().toString());
    intent.putExtra("repository", repositoryEditText.getText().toString());
    getActivity().setResult(Activity.RESULT_OK, intent);
    getActivity().finish();
  }
}
