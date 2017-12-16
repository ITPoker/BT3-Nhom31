package com.nhom31.tbdd.bt3_nhom31.BT1;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nhom31.tbdd.bt3_nhom31.R;

import java.util.ArrayList;

public class MainActivityBT1 extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    ArrayList<Marker> markers = new ArrayList<Marker>();
    static final int POLYGON_POINTS = 3;
    Polygon shape;
    Polyline line;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bt1);
        getSupportActionBar().hide();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Log.d("GOOGLEMAPS", (map == null)? "true" : "false");
        if(map != null) {

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    MainActivityBT1.this.setMarker("Local", latLng.latitude, latLng.longitude);
                }
            });
        }
    }

    private void setMarker(String locality, double latitude, double longitude) {
        if(markers.size() == POLYGON_POINTS){
            removeEverything(markers.size());
        }
        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .draggable(true)
                .position(new LatLng(latitude, longitude))
                .snippet("Marker");
        markers.add(map.addMarker(options));

        switch (markers.size()){
            case 2:
                drawLine();
                break;
            case 3:
                removeEverything(markers.size()-1);
                drawPolygon();
                break;
            default:
        }
    }

    private void removeEverything(int size) {
        switch (size){
            case 2:
                line.remove();
                line = null;
                break;
            case 3:
                for(Marker marker : markers){
                    marker.remove();
                }
                markers.clear();
                shape.remove();
                shape = null;
                break;
            default:
        }
    }

    private void drawLine() {
        PolylineOptions options = new PolylineOptions()
                .color(Color.BLUE)
                .width(3);
        for(int i=0; i<markers.size(); i++){
            options.add(markers.get(i).getPosition());
        }
        line = map.addPolyline(options);
    }

    private void drawPolygon() {
        PolygonOptions options = new PolygonOptions()
                .fillColor(0x330000FF)
                .strokeWidth(3)
                .strokeColor(Color.RED);
        for(int i=0; i<POLYGON_POINTS; i++){
            options.add(markers.get(i).getPosition());
        }
        shape = map.addPolygon(options);
    }
}