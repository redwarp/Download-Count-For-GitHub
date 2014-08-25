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

package net.redwarp.android.app.githubcount.data;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Release {
    public String tagName;
    public String name;
    public List<Asset> assets;
    public DateTime publishedDate;
    public DateTime createdDate;

    public Release(JSONObject jsonObject) {
        tagName = jsonObject.optString("tag_name");
        name = jsonObject.optString("name");

        try {
            JSONArray assetArray = jsonObject.getJSONArray("assets");
            assets = new ArrayList<>(assetArray.length());
            for (int i = 0; i < assetArray.length(); i++) {
                assets.add(new Asset(assetArray.optJSONObject(i)));
            }
        } catch (JSONException e) {
            assets = new ArrayList<>(0);
        }

        DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
        String publishedAtString = jsonObject.optString("published_at");
        if (publishedAtString != null) {
            publishedDate = parser.parseDateTime(publishedAtString);
        }
        String createdAtString = jsonObject.optString("created_at");
        if (publishedAtString != null) {
            createdDate = parser.parseDateTime(createdAtString);
        }
    }
}
