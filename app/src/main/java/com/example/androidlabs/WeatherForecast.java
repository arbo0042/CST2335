package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.androidlabs.ProfileActivity.ACTIVITY_NAME;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends AppCompatActivity {


    // Create new variables to point to the ImageView ID's

    private ImageView weatherIconImage;
    private TextView minimum;
    private TextView maximum;
    private TextView UVRating;
    private TextView currentTemp;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

       progressBar = (ProgressBar) findViewById(R.id.determinateBar);
       progressBar.setVisibility(View.VISIBLE);

       //Assign variable to the proper layout
        weatherIconImage = (ImageView)findViewById(R.id.imageView);
        minimum = (TextView)findViewById(R.id.minTemperature);
        maximum = (TextView)findViewById(R.id.maxTemperature);
        UVRating = (TextView)findViewById(R.id.uvRating);
        currentTemp = (TextView)findViewById(R.id.currentTemperature);

        ForecastQuery theQuery = new ForecastQuery();
        theQuery.execute();

    }

public class ForecastQuery extends AsyncTask<String, Integer, String>{

            String UV;
            String min;
            String maxTemp;
            String currentTemperature;
            Bitmap currentWeather;
            String iconName;


    @Override                       //Type 1
    protected String doInBackground(String... strings) {
        String ret = null;
        //destination
        String queryURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
        String queryUvURL = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";

        try {       // Connect to the server:
            URL url = new URL(queryURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inStream = urlConnection.getInputStream();

            //Set up the XML parser:
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(inStream, "UTF-8");

            int EVENT_TYPE;         //While not the end of
            //Iterate over the XML tags: the document:
            while ((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT) {
                switch (EVENT_TYPE) {
                    case START_TAG:         //This is a start tag < ... >
                        String tagName = xpp.getName(); // What kind of tag?
                        if (tagName.equals("temperature")) {
                            currentTemperature = xpp.getAttributeValue(null, "value"); //What is the String associated with message?
                            publishProgress(25);

                            min = xpp.getAttributeValue(null, "min"); //What is the String associated with message?
                            publishProgress(50);


                            maxTemp = xpp.getAttributeValue(null, "max"); //What is the String associated with message?
                            publishProgress(75);

                        }
                        if (tagName.equals("weather")) {

                            iconName = xpp.getAttributeValue(null, "icon");
                            publishProgress(75);
                        }


                        break;
                    case END_TAG:           //This is an end tag: </ ... >
                        break;
                    case TEXT:              //This is text between tags < ... > Hello world </ ... >
                        break;
                }
                xpp.next(); // move the pointer to next XML element

            }
            //Create a new connection for the UV URL STream
            URL myUvUrl = new URL(queryUvURL);
            HttpURLConnection myUVUrlConnection = (HttpURLConnection) myUvUrl.openConnection();
            InputStream myUVInStream = myUVUrlConnection.getInputStream();

            //Set up the JSON object parser:
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(myUVInStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            String result = sb.toString();

            JSONObject jObject = new JSONObject(result);
            UV = jObject.getString("value");

            // Check to see if the image exists.  If yes, then read from the file.
            if (fileExistance(iconName + "png")) {

                Log.i(ACTIVITY_NAME, "Image "+iconName+".png was found Locally");

                FileInputStream fis = null;
                try {
                    fis = openFileInput(iconName + "png");
                } catch (FileNotFoundException e) {
                Log.i(ACTIVITY_NAME, "Image was not found");
                    e.printStackTrace();
                }
                currentWeather = BitmapFactory.decodeStream(fis);

            }  // not found locally, so go to the URL
                else {

                Log.i(ACTIVITY_NAME, "Image "+iconName+".png was not found Locally, Image was Downloaded.");

                URL urlImage = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                HttpURLConnection connection = (HttpURLConnection) urlImage.openConnection();
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    currentWeather = BitmapFactory.decodeStream(connection.getInputStream());
                }
                // Now save to local storage
                FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                currentWeather.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                outputStream.flush();
                outputStream.close();
            }
                publishProgress(100);
        }
        catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
        catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
        catch(XmlPullParserException pe){ ret = "XML Pull exception. The XML is not properly formed" ;}
        catch(JSONException e)          { ret = "There is an Error with the JSON file";}
        //What is returned here will be passed as a parameter to onPostExecute:
        return ret;
    }

    @Override                   //Type 2
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //Update GUI stuff only:

      progressBar.setVisibility(View.VISIBLE);
      progressBar.setProgress(values[0]);
      }

    @Override                   //Type 3
    protected void onPostExecute(String sentFromDoInBackground) {
        super.onPostExecute(sentFromDoInBackground);

        progressBar.setVisibility(View.INVISIBLE);

        //update GUI Stuff:
        minimum.setText("Minimum Temperature: " + min + "°C");
        maximum.setText("Maximum Temperature: " +maxTemp + "°C");
        currentTemp.setText("Current Temperature: " + currentTemperature+ "°C");
        weatherIconImage.setImageBitmap(currentWeather);
        UVRating.setText("UV Index Today is: "  + UV);
    }


    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();   }

}
}
