package com.aim2u.testinggooglemapapi

import android.Manifest
import android.app.PendingIntent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_open_street_map.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint


class OpenStreetMapActivity : AppCompatActivity() {

//    private lateinit var map: MapView

    companion object{
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val ctx = applicationContext
//        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_open_street_map)

        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)


        val mapController = map.controller
        mapController.setZoom(9.5)
        val startPoint = GeoPoint(48.8583, 2.2944)
        mapController.setCenter(startPoint)


        requestPermissionsIfNecessary(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
//        val prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
//        val prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        Configuration.getInstance().save(this, prefs);
        map.onPause()
    }

    override fun onStop() {
        PendingIntent.getBroadcast(applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT).cancel();
        super.onStop()
    }

    private fun requestPermissionsIfNecessary(permissions: Array<String>) {
        var permissionsToRequest = arrayListOf<String>()
        for (permission in permissions){
            if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED){
                //Pemission is not granted
                permissionsToRequest.add(permission)
            }
        }
        if(permissionsToRequest.size > 0){
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toArray(emptyArray<String>()),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var permissionToRequest = arrayListOf<String>()
        for (i in 0..grantResults.size-1){
            permissionToRequest.add(permissions[i])
        }
        if (permissionToRequest.size > 0){
            ActivityCompat.requestPermissions(
                this,
                permissionToRequest.toArray(arrayOf<String>()),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }
}