package com.thecoder.maps.ui

import android.Manifest
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mapbox.common.location.LocationError
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMapLongClickListener
import com.mapbox.maps.plugin.gestures.addOnMapLongClickListener
import com.mapbox.maps.plugin.locationcomponent.LocationConsumer
import com.mapbox.maps.plugin.locationcomponent.location
import com.thecoder.maps.R

import com.thecoder.maps.databinding.FragmentMapsBinding

import com.thecoder.maps.models.MySavedItem
import com.thecoder.maps.models.SavedViewModel
import com.thecoder.maps.utils.LocationTool
import com.thecoder.maps.utils.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FragmentMaps:Fragment() {

    lateinit var binding:FragmentMapsBinding
    lateinit var annotMan:PointAnnotationManager

    lateinit var myViewModel:SavedViewModel


    // hashmap to save and access location
    // based on coordinates
    val hashMap=HashMap<Double,MySavedItem>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding=FragmentMapsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        myViewModel= ViewModelProvider(this)[SavedViewModel::class.java]


        // registering observer for saving data states
        val livedata=myViewModel.getlocations()

        val observer=object :Observer<List<MySavedItem>>{
            override fun onChanged(value:List<MySavedItem>) {
                hashMap.clear()
                for(a in value){
                    hashMap[a.lat+a.longi] = a
                }




                // we  dont need it anymore
                livedata.removeObserver(this)

            }

        }
        livedata.observe(viewLifecycleOwner,observer)

        annotMan=  binding.mapView.annotations.createPointAnnotationManager()
        binding.mapView.location.enabled=true
        // waiting for map to load before adding listeners
        binding.mapView.mapboxMap.subscribeMapLoaded{

            // creating markers from saved locations
            // icon  to use
            val bitmap=BitmapFactory.decodeResource(requireContext().resources, R.drawable.loc_small)
            for(item in hashMap.values){
                // marker options, point object contains coordinates
                val options: PointAnnotationOptions = PointAnnotationOptions()
                    .withPoint(Point.fromLngLat(item.longi,item.lat))
                    .withIconImage(bitmap)
                annotMan.create(options)
            }

            binding.mapView.mapboxMap.addOnMapLongClickListener(object :OnMapLongClickListener{
                override fun onMapLongClick(point: Point): Boolean {
                    addmarkToMap(point)
                    return true
                }

            })


            // listening for clicks on added markers
            annotMan.addClickListener(object :OnPointAnnotationClickListener{
                override fun onAnnotationClick(annotation: PointAnnotation): Boolean {
                    // getting item from hasmap based on coordinates
                    val point=annotation.point
                    val item=hashMap[point.latitude()+point.longitude()]
                    if(item!=null){
                        Util.showLocationInfo(requireActivity(),item)
                    }
                    return true
                }

            })
        }




        // to let user go back to his location
        binding.btnLocate.setOnClickListener {
            if(Util.hasPerm(requireContext(),perms[0])){

                // checking if GPS is enabled
            if(LocationTool.isLocationEnabled(requireContext())){
                binding.mapView.location.getLocationProvider()?.registerLocationConsumer(locationConsumer)

            }else{
                Util.showDialog(requireActivity(),
                    getString(R.string.gps_disabled), getString(R.string.enable_gps_msg),{
                    val i2=LocationTool.getLocationEnableIntent()
                    gpsLaucher.launch(i2)
                }, getString(R.string.okay),getString(R.string.cancel))
            }

            }else{
                askLocationPerms()
            }
        }

        val args=arguments
        if(args!=null){
            // check if bundle contains item key sent from saved fragment
            if(args.containsKey("item")){
                val item= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    args.getSerializable("item",MySavedItem::class.java)!!
                } else {
                    args.getSerializable("item") as MySavedItem
                }

                val cameraPosition = CameraOptions.Builder()
                    .zoom(10.0)
                    .center(Point.fromLngLat(item.longi,item.lat))
                    .build()
                // set camera position
                binding.mapView.mapboxMap.setCamera(cameraPosition)

            }
        }else{
            // else  let  mapbox navigate to user's location
            if(Util.hasPerm(requireContext(),perms[0])) {
                if(LocationTool.isLocationEnabled(requireContext())){
                    binding.mapView.location.getLocationProvider()?.registerLocationConsumer(locationConsumer)
                }
            }
        }
    }

    // listener for mapbox location Component
    // might needed for doing something with user's location
    val locationConsumer = object: LocationConsumer {
        override fun onBearingUpdated(
            vararg bearing: Double,
            options: (ValueAnimator.() -> Unit)?
        ) {

        }

        override fun onError(error: LocationError) {

        }

        override fun onHorizontalAccuracyRadiusUpdated(
            vararg radius: Double,
            options: (ValueAnimator.() -> Unit)?
        ) {

        }

        override fun onLocationUpdated(
            vararg location: Point,
            options: (ValueAnimator.() -> Unit)?
        ) {


            if(location.isNotEmpty()){
                val point=location[0]

                // TODO: something with user's location
                // for now we will animate camera to user's location
                val cameraPosition = CameraOptions.Builder()
                    .zoom(10.0)
                    .center(point)
                    .build()
                // set camera position
                binding.mapView.mapboxMap.setCamera(cameraPosition)

                // unregistering it because we dont need it anymore
                binding.mapView.location.getLocationProvider()?.unRegisterLocationConsumer(this)


            }

        }

        override fun onPuckAccuracyRadiusAnimatorDefaultOptionsUpdated(options: ValueAnimator.() -> Unit) {

        }

        override fun onPuckBearingAnimatorDefaultOptionsUpdated(options: ValueAnimator.() -> Unit) {

        }

        override fun onPuckLocationAnimatorDefaultOptionsUpdated(options: ValueAnimator.() -> Unit) {

        }


    }




    // method to add marker on clicked map location
    //and fetch address from the coordinates and save them in the database

    fun addmarkToMap(point: Point){
        // icon  to use
        val bitmap=BitmapFactory.decodeResource(requireContext().resources, R.drawable.loc_small)

        // marker options, point object contains coordinates
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(bitmap)


        // creating a dialog for letting user wait until
        // marker with full details is added

        val dialog=Dialog(requireActivity(),R.style.my_dialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_saving)
        dialog.show()
        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                val city=LocationTool.getAddressFromLatLong(requireContext(),point.latitude(),point.longitude())
                withContext(Dispatchers.Main){
                    Toast.makeText(requireContext(),""+city,Toast.LENGTH_SHORT).show()


                    // once the address is returned , we will create marker and
                    // also save the entry in our database
                    if(dialog.isShowing){
                        dialog.dismiss()
                    }

                    val item=MySavedItem(city,point.latitude(),point.longitude())
                    myViewModel.addLocation(item)
                    hashMap[item.lat+item.longi]=item

                    annotMan.create(pointAnnotationOptions)
                }
            }
        }
    }


// ______________________________________


    // Permission stuff here
    val perms= arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
    fun askLocationPerms(){
        Util.showDialog(requireActivity(), getString(R.string.perm_required), getString(R.string.location_msg),{
            permsLauncher.launch(perms)
        })
    }
    val permsLauncher=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){

        if(Util.hasPerm(requireContext(),perms[0])){

            binding.btnLocate.performClick()
        }
        else if(!shouldShowRequestPermissionRationale(perms[0])){
        // handling relational condition

            Util.showDialog(requireActivity(),getString(R.string.perm_required),
                getString(R.string.denied_msg),{
                val i2=Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)

                    i2.data = Uri.fromParts("package",requireContext().packageName,null)
                    appSettingLauncher.launch(i2)
            }, getString(R.string.okay),getString(R.string.cancel))
        }

    }

    // request handler for app settings
    val appSettingLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(Util.hasPerm(requireContext(),perms[0])){
            binding.btnLocate.performClick()

        }
    }

    // launcher for enabling GPS
    val gpsLaucher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        if(LocationTool.isLocationEnabled(requireContext())){
            binding.btnLocate.performClick()
        }
    }


}