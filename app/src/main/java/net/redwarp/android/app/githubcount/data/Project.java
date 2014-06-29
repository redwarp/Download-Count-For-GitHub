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


public class Project {
    public long id = -1;
    public String user;

    public String repository;

    public Project(long id, String user, String repository) {
        this.id = id;
        this.user = user;
        this.repository = repository;
    }

    public Project(String user, String repository) {
        this(-1, user, repository);
    }
}
