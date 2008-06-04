package fm.last;

import java.io.FileNotFoundException;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

public class Application extends android.app.Application
{
	static private Application instance;
	private String m_user;
	private String m_pass;
	private String m_sessionKey;
	private SharedPreferences m_preferences;
	private SQLiteDatabase m_db = null;
	
	public void onCreate()
	{
		m_preferences = getSharedPreferences( "Last.fm", MODE_PRIVATE );
		m_user = m_preferences.getString( "username", "" );
		m_pass = m_preferences.getString( "md5Password", "" );
		m_sessionKey = m_preferences.getString( "sessionKey", "" );
		
		instance = this;
	}

	public static Application instance()
	{
		return instance;
	}

	public SQLiteDatabase getDb() throws FileNotFoundException
	{
		if( m_db != null )
		{
			return m_db;
		}
		
		try
		{
			m_db = openDatabase( "lastFm", null );
		}catch( FileNotFoundException e )
		{
			m_db = createDatabase( "lastFm", 1, MODE_PRIVATE, null );
			createTables();
			return m_db;
		}
		return m_db;
	}
	
	private void createTables()
	{
		m_db.execSQL( "CREATE TABLE FriendsMap (contactId integer PRIMARY_KEY, username VARCHAR);" );
	}
	
	public String userName()
	{
		return m_user;
	}

	public String sessionKey()
	{
		return m_sessionKey;
	}
	
	public boolean setSessionKey( String sessionKey )
	{
		m_sessionKey = sessionKey;
		return
			m_preferences.edit().putString( "sessionKey", sessionKey )
						 		.commit();
	}

	public String password()
	{
		return m_pass;
	}
}