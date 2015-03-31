package se.kth.mikaele3.sheepcounter;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

import se.kth.mikaele3.sheepcounter.Model.Model;


public class SignInActivity extends ActionBarActivity {

    private boolean loginProcessActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        loginProcessActive = false;
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void login(View view) {
        if (!loginProcessActive) {
            loginProcessActive = true;
            EditText editText = (EditText) findViewById(R.id.suppliedUsername);
            String username = editText.getText().toString();

            // asynchronously check if the given user name exists in the model
            new checkUsernameTask().execute(username);
        }
    }

    private void launchListView(String username) {
        TextView textView = (TextView) findViewById(R.id.login_result);
        textView.setText(""); // reset login information
        Intent intent = new Intent(this, ListViewActivity.class);
        intent.putExtra("se.kth.mikaele3.sheepcounter.USERNAME", username);
        startActivity(intent);
    }

    private void failedLoginMessage(String message) {
        TextView textView = (TextView) findViewById(R.id.login_result);
        textView.setText(message);
    }

    private class checkUsernameTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... parameters) {
            // params comes from the execute() call: params[0] is the username to check
            String username = parameters[0];
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

            boolean result = false;
            String message;
            try {
                result = Model.getInstance().checkUsername(username);
                if (!result)
                    message = "Invalid login, please try again!";
                else {
                    message = username;
                }
            } catch (IOException e) {
                message = "Connection error: " + e.getMessage();
            } catch (JSONException e) {
                message = "JSON error: " + e.getMessage();
            }
            return Boolean.toString(result) + "£" + message;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            String[] resultArray = result.split("£");
            Log.d("DEBUG TAG","the boolean string is " + resultArray[0]);
            boolean validUsername = Boolean.valueOf(resultArray[0]);
            if (validUsername) {
                String username = resultArray[1];
                launchListView(username);
            } else {
                String message = resultArray[1];
                failedLoginMessage(message);
            }
            loginProcessActive = false;

        }
    }
}
