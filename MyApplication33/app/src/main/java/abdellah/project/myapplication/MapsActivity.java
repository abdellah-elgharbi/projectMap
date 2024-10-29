package abdellah.project.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import abdellah.project.myapplication.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final String API_URL = "http://192.168.1.103/bakcposition/getAll.php"; // Remplace avec ton URL
    private RequestQueue requestQueue;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtenir la référence du fragment Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialiser la RequestQueue pour Volley
        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Vérifier les permissions de localisation
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Demander les permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Charger et afficher les positions depuis l'API
            loadPositionsFromAPI();
            // Démarrer la mise à jour de la localisation
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Toast.makeText(getApplicationContext(), latitude + " " + longitude, Toast.LENGTH_SHORT).show();
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Votre Position"));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {
                buildAlertMessageNoGps();
            }
        });
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void loadPositionsFromAPI() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, API_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // S'assurer qu'il y a au moins une position
                            if (response.length() == 0) return;

                            // Récupérer la dernière position
                            JSONObject lastPosition = response.getJSONObject(response.length() - 1);
                            double lastLatitude = lastPosition.getDouble("latitude");
                            double lastLongitude = lastPosition.getDouble("longitude");
                            String lastImei = lastPosition.getString("imei");
                            String lastDate = lastPosition.getString("date");

                            // Parcourir toutes les positions et ajouter un marqueur pour chaque position
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject position = response.getJSONObject(i);
                                double latitude = position.getDouble("latitude");
                                double longitude = position.getDouble("longitude");
                                String imei = position.getString("imei");
                                String date = position.getString("date");

                                // Ajouter un marqueur normal pour chaque position
                                LatLng latLng = new LatLng(latitude, longitude);
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title("IMEI: " + imei)
                                        .snippet("Date: " + date));
                            }

                            // Ajouter un marqueur distinct pour la dernière position
                            LatLng lastLatLng = new LatLng(lastLatitude, lastLongitude);
                            mMap.addMarker(new MarkerOptions()
                                    .position(lastLatLng)
                                    .title("Dernière Position - IMEI: " + lastImei)
                                    .snippet("Date: " + lastDate)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))); // Changez la couleur ici

                            // Centrer la caméra sur la dernière position
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 10));
                            float zoomLevel = 15.0f;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, zoomLevel));

                        } catch (JSONException e) {
                            Log.e("MapsActivity", "Erreur JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MapsActivity", "Erreur Volley: " + error.getMessage());
                    }
                });

        // Ajouter la requête à la RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    // Gestion de la réponse de la demande de permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // La permission a été accordée, charger les positions et démarrer les mises à jour de localisation
                loadPositionsFromAPI();
                startLocationUpdates();
            } else {
                // La permission a été refusée
                Toast.makeText(this, "Permission de localisation refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
