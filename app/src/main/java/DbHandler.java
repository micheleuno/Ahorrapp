/**
 * Created by michele de conti on 21-05-2015.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
public class DbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    private static final String TABLE_USUARIOS = "usuarios";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PASSWORD = "password";

    public DbHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }



    @Override
        public void onCreate(SQLiteDatabase db) {
        String CREATE_USUARIOS_TABLE = "CREATE TABLE " +
                TABLE_USUARIOS + "("
                + COLUMN_ID + " TEXT PRIMARY KEY," + COLUMN_PASSWORD
                + " TEXT,"  + ")";
        db.execSQL(CREATE_USUARIOS_TABLE    );

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
            onCreate(db);

        }

    public void addUsuario(Usuario usuario) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, usuario.getID());
        values.put(COLUMN_PASSWORD, usuario.get_password());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USUARIOS, null, values);
        db.close();
    }

}
