package fm.last.android.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import fm.last.api.Artist;
import fm.last.api.Track;

/**
 * The DAO to access the table containing the recently played stations.
 * @author twa
 */
public class TrackDurationCacheDao extends AbstractDao<Track> {

	/**
	 * The table for the recent stations list.
	 */
	public static final String DB_TABLE_TRACKDURATIONS = "t_trackdurations";
	/**
	 * The {@link TrackDurationCacheDao} singleton instance.
	 */
	private static TrackDurationCacheDao instance = null;

	/**
	 * @return the {@link TrackDurationCacheDao} singleton instance.
	 */
	public static TrackDurationCacheDao getInstance() {
		if(instance != null) {
			return instance;
		} else {
			instance = new TrackDurationCacheDao();
			return instance;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see fm.last.android.db.AbstractDao#getTableName()
	 */
	@Override
	protected String getTableName() {
		return DB_TABLE_TRACKDURATIONS;
	}

	/*
	 * (non-Javadoc)
	 * @see fm.last.android.db.AbstractDao#buildObject(android.database.Cursor)
	 */
	@Override
	protected synchronized Track buildObject(Cursor c) {
		String artistname = c.getString(c.getColumnIndex("Artist"));
		String title = c.getString(c.getColumnIndex("Title"));
		String duration = c.getString(c.getColumnIndex("Duration"));
		Artist artist = new Artist(artistname, "", "", "", null, "", "", "", "");
		return new Track("", title, "", "", duration, "", "", "", artist, null, null, "", "");
	}

	/*
	 * (non-Javadoc)
	 * @see fm.last.android.db.AbstractDao#fillContent(android.content.ContentValues, java.lang.Object)
	 */
	@Override
	protected synchronized void fillContent(ContentValues content, Track data) {
		content.put("Artist", data.getArtist().getName());
		content.put("Title", data.getName());
		content.put("Duration", data.getDuration());
	}

	/**
	 * Read the last added station.
	 * @return the last station that has been added to the list.
	 */
	public synchronized long getDurationForTrack(String artist, String track) {
		List<Track> tracks = loadWithQualification("WHERE Artist='" + artist.replace("'", "''") + "' AND Title='" + track.replace("'", "''") + "'");
		if(tracks != null && tracks.size() > 0) {
			return Long.parseLong(tracks.get(0).getDuration());
		}
		return 0;
	}

}
