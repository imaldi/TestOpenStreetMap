package com.aim2u.testinggooglemapapi

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        private val LOCATION_REQUEST_CODE: Int = 12121
        private const val TAG = "Location Test"
    }

    //now we need to create variables that we will need
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest : LocationRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        btnGetLocation.setOnClickListener {
            requestPermission()
            getLasLocation()
        }
    }

    private fun getLasLocation(){
        if (checkPermission()){
            if (isLocationEnabled()){
                //now let's get the location
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result

                    if(location == null){
                        getNewLocation()
                    }else {
                        main_text.text = "Your Current Coordinates are : \nLat : " + location.latitude +" ; Long : " + location.longitude
                    }
                }
            } else {
                Toast.makeText(this, "Please Enable Your Location Service", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermission()
        }
    }

    private fun getNewLocation() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
           return
        }
        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequest,locationCallback,Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation = p0.lastLocation
            //Now set the new location
//            main_text.text = "Your Current Coordinates are : \nLat : " + lastLocation.latitude +" ; Long : " + lastLocation.longitude
        }
    }

    //First we need a function to check permission
    private fun checkPermission():Boolean{

        if(
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false
    }

    //Now we need to create a function that will allow us  to get user permission
    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }

    //Now we need a function that check if the location service of the device is enabled
    private fun isLocationEnabled():Boolean{
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //this is a build in function that check the permission result
//        we will use it just for debugging our code
        if (requestCode == LOCATION_REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug", "you have the permission")
            }
        }
    }

//    companion object{
//        private val LOCATION_REQUEST_CODE: Int = 12121
////        private const val TAG = "Location Test"
//        private const val REQUEST_CHECK_SETTINGS: Int = 3232
//        private const val REQUEST_LOCATION = 1
//        private const val TAG = "MapsActivity"
//
//
//    }
//
//    private lateinit var lastLocation: Location
//    private lateinit var locationRequest : LocationRequest
//
//
//
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        setupLocationClient()
//
//
//    }
//
//    private fun setupLocationClient() {
//        fusedLocationClient =
//            LocationServices.getFusedLocationProviderClient(this)
//    }
//
//    private fun requestLocationPermissions() {
//        ActivityCompat.requestPermissions(this,
//            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//            REQUEST_LOCATION)
//    }
//
//    private fun getCurrentLocation() {
//// 1
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) !=
//            PackageManager.PERMISSION_GRANTED
//        ) {
//// 2
//            requestLocationPermissions()
//        } else {
//            fusedLocationClient.lastLocation.addOnCompleteListener {
//                val location = it.result
//                if (location != null) {
////                    Toast.makeText(
////                        this,
////                        "Lat : " + location.latitude + "Long : " + location.longitude,
////                        Toast.LENGTH_SHORT
////                    ).show()
////                    main_text.text = "Lat : " + location.latitude + "Long : " + location.longitude
//                    btnGetLocation.setOnClickListener{
//                        main_text.text = "Lat : " + location.latitude + "Long : " + location.longitude
//                    }
//                } else {
//// 8
//                    Log.e(TAG, "No location found")
//                }
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray) {
//        if (requestCode == REQUEST_LOCATION) {
//            if (grantResults.size == 1 && grantResults[0] ==
//                PackageManager.PERMISSION_GRANTED) {
//                getCurrentLocation()
//            } else {
//                Log.e(TAG, "Location permission denied")
//            }
//        }
//    }
//
//    private fun getNewLocation() {
//        locationRequest = LocationRequest()
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationRequest.interval = 0
//        locationRequest.fastestInterval = 0
//        locationRequest.numUpdates = 2
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        fusedLocationClient?.requestLocationUpdates(
//            locationRequest,locationCallback,Looper.myLooper()
//        )
//    }

    ////THIS IS SECOND VIDEO< FAILED
//    //now we need to create variables that we will need
//    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//    private lateinit var locationRequest : LocationRequest
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//
//        btnGetLocation.setOnClickListener {
//            requestPermission()
//            getLasLocation()
//        }
//    }
//
//    private fun getLasLocation(){
//        fusedLocationProviderClient.lastLocation.addOnFailureListener {
//                exception ->
//            if (exception is ResolvableApiException){
//                // Location settings are not satisfied, but this can be fixed
//                // by showing the user a dialog.
//                try {
//                    // Show the dialog by calling startResolutionForResult(),
//                    // and check the result in onActivityResult().
//                    exception.startResolutionForResult(this@MainActivity,
//                        REQUEST_CHECK_SETTINGS)
//                } catch (sendEx: IntentSender.SendIntentException) {
//                    // Ignore the error.
//                }
//            }
//        }
//        if (checkPermission()){
//            if (isLocationEnabled()){
//                //now let's get the location
//                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
//
//                    if(location == null){
//                        getNewLocation()
//                    }else {
//                        main_text.text = "Your Current Coordinates are : \nLat : " + location.latitude +" ; Long : "
//                        + location.longitude
//                    }
//                }
//                fusedLocationProviderClient.lastLocation.addOnFailureListener { exception ->
//                    if (exception is ResolvableApiException){
//                        // Location settings are not satisfied, but this can be fixed
//                        // by showing the user a dialog.
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            exception.startResolutionForResult(this@MainActivity,
//                                REQUEST_CHECK_SETTINGS)
//                        } catch (sendEx: IntentSender.SendIntentException) {
//                            // Ignore the error.
//                        }
//                    }
//                }
//
//            } else {
//
////                Toast.makeText(this, "Please Enable Your Location Service", Toast.LENGTH_SHORT).show()
//                fusedLocationProviderClient.lastLocation.addOnFailureListener {exception ->
//                    if (exception is ResolvableApiException){
//                        // Location settings are not satisfied, but this can be fixed
//                        // by showing the user a dialog.
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            exception.startResolutionForResult(this@MainActivity,
//                                REQUEST_CHECK_SETTINGS)
//                        } catch (sendEx: IntentSender.SendIntentException) {
//                            // Ignore the error.
//                        }
//                    }
//                }
//            }
//        } else {
//            requestPermission()
//        }
//    }
//
//    private fun getNewLocation() {
//        locationRequest = LocationRequest()
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationRequest.interval = 0
//        locationRequest.fastestInterval = 0
//        locationRequest.numUpdates = 2
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
////        if (ActivityCompat.checkSelfPermission(
////                this,
////                Manifest.permission.ACCESS_FINE_LOCATION
////            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
////                this,
////                Manifest.permission.ACCESS_COARSE_LOCATION
////            ) != PackageManager.PERMISSION_GRANTED
////        ) {
////
////            return
////        }
//        fusedLocationProviderClient!!.requestLocationUpdates(
//            locationRequest,locationCallback,Looper.myLooper()
//        )
//    }
//
//    private val locationCallback = object : LocationCallback(){
//        override fun onLocationResult(p0: LocationResult) {
//            var lastLocation = p0.lastLocation
//            //Now set the new location
//            main_text.text = "Your Current Coordinates are : \nLat : " + lastLocation.latitude +" ; Long : "
//            + lastLocation.longitude
//            Log.d("Latitude", "Latitude "+lastLocation.latitude)
//        }
//    }
//
//    //First we need a function to check permission
//    private fun checkPermission():Boolean{
//
//        if(
//            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
//            == PackageManager.PERMISSION_GRANTED ||
//            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
//            == PackageManager.PERMISSION_GRANTED
//                ){
//            return true
//        }
//
//        return false
//    }
//
//    //Now we need to create a function that will allow us  to get user permission
//    private fun requestPermission(){
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),
//            LOCATION_REQUEST_CODE
//        )
//    }
//
//    //Now we need a function that check if the location service of the device is enabled
//    private fun isLocationEnabled():Boolean{
//        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
//                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        //this is a build in function that check the permission result
////        we will use it just for debugging our code
//        if (requestCode == LOCATION_REQUEST_CODE){
//            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Log.d("Debug", "you have the permission")
//            }
//        }
//    }

//    This from another video
//    override fun onStart() {
//        super.onStart()
//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            getLastLocation();
//        } else {
//            askLocationPermission();
//        }
//    }
//
//    private fun askLocationPermission() {
//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
//                //show dialog
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
//            } else {
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
//            }
//        }
//    }
//
////    @SuppressLint("MissingPermission")
//    private fun getLastLocation() {
//        val locationTask = fusedLocationProviderClient.lastLocation
//
//        locationTask.addOnSuccessListener {
//            if(it != null){
////                Log.d(TAG, "onSuccess : "+it.toString())
////                Log.d(TAG, "onSuccess : "+it.latitude)
////                Log.d(TAG, "onSuccess : "+it.longitude)
//                main_text.text = it.latitude.toString()
//            } else {
//                Log.d(TAG, "onSuccess : Location is null")
//            }
//        }
//        locationTask.addOnFailureListener {
//            Log.e(TAG, "onFailure: " + it.localizedMessage)
//        }
//
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        if(requestCode == LOCATION_REQUEST_CODE){
//            if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                //permission granted
//                getLastLocation()
//            } else {
//                //Permission not grantd
//            }
//        }
//    }
}