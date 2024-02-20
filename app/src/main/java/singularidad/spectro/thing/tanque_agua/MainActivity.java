package singularidad.spectro.thing.tanque_agua;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();
    Runnable runnable;
    int delay = (5*1000);

    String url ="http://192.168.1.1/tankStatus";
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        TextView tvLevel = (TextView) findViewById( R.id.level );
                        TextView tvVolume = (TextView) findViewById( R.id.volume );
                        int intLevel = response.getInt( "level" );
                        int intVolume = response.getInt( "volume" );
                        tvLevel.setText( intLevel + " %" );
                        tvVolume.setText( intVolume + " Litros" );


                    } catch ( JSONException e ) {
                        throw new RuntimeException( e );
                    }
                }
                },new Response.ErrorListener() {


        @Override
        public void onErrorResponse(VolleyError error) {
            TextView tvLevel = (TextView) findViewById( R.id.level );
            TextView tvVolume = (TextView) findViewById( R.id.volume );
            tvLevel.setText(  "ERROR" );
            tvVolume.setText(  " SIN CONEXION" );
        }
    }
             );

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add( jsonObjectRequest );
    }

    @Override
    protected void onResume() {
    handler.postDelayed( runnable = new Runnable() {
        @Override
        public void run() {

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add( jsonObjectRequest );
            handler.postDelayed( runnable,delay );
        }
    },delay );
        super.onResume();
    }

    @Override
    protected void onPause() {
       handler.removeCallbacks( runnable );
        super.onPause();
    }
}