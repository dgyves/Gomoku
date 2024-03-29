package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameActivity extends AppCompatActivity {

    private Board boardObject = null;
    private GridViewAdapter gvAdapter = null;

    // 0: Player1, 1: Player2
    static int currentPlayer = 0;

    ContactsFragment cf;
    String emailAddress;
    Uri currentPhotoUri;
    String currentPhotoPath;
    File file;
    GridView gv;
    public static Bitmap game_result;
    public static String name1;
    public static String name2;
    public static Boolean play = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.currentPlayer = 0;

        // Bundle extra "Players" has value 0 for Human vs AI game, or 1 for Human vs Human
        Bundle b = getIntent().getExtras();
        int numPlayers = b.getInt("Players");

        boardObject = new Board(numPlayers);

        // Define gridview & parameters
        gv = findViewById(R.id.gridView);
        gv.setVerticalScrollBarEnabled(false);
        gv.setHorizontalScrollBarEnabled(false);
        gv.setVerticalSpacing(0);
        gv.setHorizontalSpacing(0);
        gv.setStretchMode(GridView.NO_STRETCH);

        gvAdapter = new GridViewAdapter(this, boardObject);
        gvAdapter.setPlayers(numPlayers);
        gv.setAdapter(gvAdapter);

        //If names changed from MainMenu settings, keep same name
        name1 = MainMenu.name1;
        name2 = MainMenu.name2;
    }

    /*
   Opens new Game Activity when user clicks on RESET GAME button
   Param: View view
   Returns: N/A
    */
    public void resetGame(View view) {
        this.recreate();
    }

    /*
    Opens new Game Activity when user clicks on Play Again button
    Param: View view
    Returns: N/A
     */
    public void playAgain(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        EndGameFragment end = (EndGameFragment) getSupportFragmentManager().findFragmentByTag("frag");
        if(end!=null){
            transaction.remove(end);
        }
        transaction.commit();
        this.recreate();
    }

    /*
    Opens new Settings fragment when user clicks on Share Results button, opens contacts list to browse
    Param: View view
    Returns: N/A
     */
    public void openHelp(View view) {
        HelpFragment hf = new HelpFragment();
        hf.setContainerActivity(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.gameActivityLayout, hf);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /*
    Opens new Settings fragment when user clicks on Share Results button, opens contacts list to browse
    Param: View view
    Returns: N/A
     */
    public void openSettings(View view) {
        SettingsFragment sf = new SettingsFragment();
        sf.setContainerActivity(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.gameActivityLayout, sf);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /*
    Changes player names depending on User input
    Param: View view
    Returns: N/A
     */
    public void changeNames(View view) {
        TextView tv1 = findViewById(R.id.player1TV);
        TextView tv2 = findViewById(R.id.player2TV);
        EditText e1 = findViewById(R.id.player1edit);
        EditText e2 = findViewById(R.id.player2edit);

        String name1 = e1.getText().toString();
        String name2 = e2.getText().toString();

        tv1.setText(name1);
        tv2.setText(name2);
        e1.setText(name1);
        e2.setText(name2);
    }

    /*
    Opens new Contacts fragment when user clicks on Share Results button, opens contacts list to browse
    Param: View view
    Returns: N/A
     */
    public void openContacts(View v) throws Exception {
        String path = createImageFile();
        cf = new ContactsFragment(path);
        cf.setContainerActivity(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.gameActivityLayout, cf);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /*
    Gets email by using content provider to read through contacts and their emails, if any.
    Param: View v
    Returns: String contactInfo
     */
    public String getContactInfo(View v) {
        String text = ((TextView)v).getText().toString();
        String id = text.substring(text.indexOf(" :: ") + 4);
        String name = text.substring(0, text.indexOf(" :: "));
        String contactInfo = "Name: " + name + "\n";

        Cursor emails = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null);
        while (emails.moveToNext()) {
            emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
            contactInfo += "Email: " + emailAddress + "\n";
        }
        emails.close();
        return contactInfo;
    }

    /*
    When name is clicked, opens an email intent and sends an email containing game_grid to that contact
    Param: View v
    Returns: N/A
     */
    public void onInfoClick(View v) throws Exception{
        String contactinfo = getContactInfo(v);
        if (!(contactinfo.contains("Email:"))){
            emailAddress = null;
        }
        System.out.println(file.getAbsolutePath());
        System.out.println(contactinfo);
        currentPhotoPath = cf.getPath();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("vnd.android.cursor.dir/email");
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {emailAddress});
        currentPhotoUri = FileProvider.getUriForFile(
                this,BuildConfig.APPLICATION_ID+".fileprovider", file);

        intent.putExtra(android.content.Intent.EXTRA_STREAM, currentPhotoUri)
                .putExtra(Intent.EXTRA_SUBJECT, "I played Gomoku, here are the results!")
                .putExtra(Intent.EXTRA_TEXT, "Sent from Gomoku app.");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(intent);
    }

    /*
    Creates an image file for the image and saves the file's path
    Param: N/A
    Returns: String file path
    */
    public String createImageFile(){
        View u = findViewById(R.id.linear_grid);
        u.setDrawingCacheEnabled(true);
        LinearLayout z = (LinearLayout) findViewById(R.id.linear_grid);
        int totalHeight = z.getChildAt(0).getHeight();
        int totalWidth = z.getChildAt(0).getWidth();
        u.layout(0, 0, totalWidth, totalHeight);
        u.buildDrawingCache(true);
        game_result = Bitmap.createBitmap(u.getDrawingCache());
        u.setDrawingCacheEnabled(false);

        //Save bitmap
        String fileName = new SimpleDateFormat("yyyyMMddhhmm'_grid.jpeg'").format(new Date());
        File root = Environment.getExternalStorageDirectory();
        try {
            if (root.canWrite()){
                file = new File(root, fileName);
                FileOutputStream out = new FileOutputStream(file);
                game_result.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            }
            return file.getAbsolutePath();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    When Menu button is clicked, starts MainMenu activity
    Param: View v
    Returns: N/A
     */
    public void startMenu(View view) {
        startActivity(new Intent(GameActivity.this, MainMenu.class));
    }

    public Bitmap getGame_result(){
        return game_result;
    }

    public String getName1(){
        return name1;
    }

    public String getName2(){
        return name2;
    }

    public void toggleSound(View view) {
        play = ((ToggleButton) view).isChecked();
    }

    public void resetSetings(View view) {
    }
}
