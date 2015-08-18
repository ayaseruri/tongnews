package org.x.tongnews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.x.tongnews.global.MApplication_;
import org.x.tongnews.object.PostsProvider;
import org.x.tongnews.object.SlidersProvider;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ayaseruri on 15/8/3.
 */
class DataHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "tongnews.db";
    private static DataHelper instance;
    private Map<String, Dao> daos = new HashMap<>();

    private DataHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, PostsProvider.class);
            TableUtils.createTable(connectionSource, SlidersProvider.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, PostsProvider.class, true);
            TableUtils.dropTable(connectionSource, SlidersProvider.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DataHelper getInstance(){
        if(null == instance){
            instance = new DataHelper(MApplication_.getInstance(), DB_NAME, null, MApplication_.getInstance().getVersionCode());
        }
        return instance;
    }

    public synchronized Dao getDao(Class clazz){
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            try {
                dao = super.getDao(clazz);
                daos.put(className, dao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

    public void clearTable(Class clazz){
        try {
            TableUtils.clearTable(this.getConnectionSource(), clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

