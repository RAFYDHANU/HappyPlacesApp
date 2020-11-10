package com.example.happyplacesapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplacesapp.R
import com.example.happyplacesapp.model.HappyPlaceModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps2.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mHappyPlacesModel: HappyPlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps2)

        mHappyPlacesModel = intent.getParcelableExtra(MainActivity.HAPPY_PLACE_DETAILS)

        mHappyPlacesModel.let {
           // if (it != null) {
                setSupportActionBar(toolbar_map)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.title = it?.title

                toolbar_map.setNavigationOnClickListener {
                    onBackPressed()
                }

                val supportMapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                supportMapFragment.getMapAsync(this)
            }
        }


    override fun onMapReady(map: GoogleMap?) {
        val position = LatLng(mHappyPlacesModel!!.latitude, mHappyPlacesModel!!.longitude)
        map!!.addMarker(MarkerOptions().position(position).title(mHappyPlacesModel!!.location))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 20f)
        map.animateCamera(newLatLngZoom)
    }
}
