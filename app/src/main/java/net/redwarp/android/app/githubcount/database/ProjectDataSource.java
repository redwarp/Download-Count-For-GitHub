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

package net.redwarp.android.app.githubcount.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import net.redwarp.android.app.githubcount.data.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectDataSource {
    private SQLiteDatabase database;
    private ProjectSQLiteHelper dbHelper;
    private String[] allColumns = {ProjectSQLiteHelper.COLUMN_ID, ProjectSQLiteHelper.COLUMN_USER, ProjectSQLiteHelper.COLUMN_REPOSITORY};
    private DataSetObserver mDataSetObserver = null;

    public ProjectDataSource(Context context) {
        dbHelper = new ProjectSQLiteHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void saveProject(Project project) {
        ContentValues values = new ContentValues();
        values.put(ProjectSQLiteHelper.COLUMN_USER, project.user);
        values.put(ProjectSQLiteHelper.COLUMN_REPOSITORY, project.repository);

        if (project.id == -1) {
            project.id = database.insert(ProjectSQLiteHelper.TABLE_PROJECTS, null, values);
        } else {
            database.update(ProjectSQLiteHelper.TABLE_PROJECTS, values, ProjectSQLiteHelper.COLUMN_ID + " = " + project.id, null);
        }
        if(mDataSetObserver != null){
            mDataSetObserver.onChanged();
        }
    }

    public void deleteProject(Project project) {
        if (project.id == -1) {
            return;
        }
        if (database.delete(ProjectSQLiteHelper.TABLE_PROJECTS, ProjectSQLiteHelper.COLUMN_ID + " = " + project.id, null) > 0) {
            project.id = -1;

            if(mDataSetObserver != null){
                mDataSetObserver.onChanged();
            }
        }
    }

    public List<Project> getAllProjects() {
        Cursor cursor = getAllProjectsCursor();
        List<Project> projects = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Project project = cursorToProject(cursor);
            projects.add(project);
            cursor.moveToNext();
        }
        cursor.close();

        return projects;
    }

    public Cursor getAllProjectsCursor(){
        return database.query(ProjectSQLiteHelper.TABLE_PROJECTS, allColumns, null, null, null, null, null, null);
    }

    private Project cursorToProject(Cursor cursor) {
        return new Project(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObserver = observer;
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObserver = null;
    }
}
