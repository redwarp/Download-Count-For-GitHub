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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Redwarp on 29/06/2014.
 */
public class SingleFragmentActivity extends Activity {


    private static final String FRAGMENT_NAME = "fragment_name";
    private static final String FRAGMENT_ARGUMENTS = "fragment_arguments";

    public static Intent intentFor(Context context, Class<? extends Fragment> fragmentClass){
        final Intent intent = new Intent(context, SingleFragmentActivity.class);
        intent.putExtra(FRAGMENT_NAME, fragmentClass.getCanonicalName());

        return intent;
    }

    public static Intent intentFor(Context context, Class<? extends Fragment> fragmentClass, Bundle fragmentArgs){
        final Intent intent = intentFor(context, fragmentClass);
        intent.putExtra(FRAGMENT_ARGUMENTS, fragmentArgs);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            final Bundle arguments = getIntent().getBundleExtra(FRAGMENT_ARGUMENTS);
            final String fragmentClassName = getIntent().getStringExtra(FRAGMENT_NAME);

            Fragment fragment = Fragment.instantiate(this, fragmentClassName);
            if(arguments != null){
                fragment.setArguments(arguments);
            }

            getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
        }
    }
}
