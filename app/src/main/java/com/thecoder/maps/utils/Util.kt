package com.thecoder.maps.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.thecoder.maps.R
import com.thecoder.maps.databinding.DialogInfoBinding
import com.thecoder.maps.databinding.DialogMsgBinding
import com.thecoder.maps.models.MySavedItem

object Util {
    // util class with useful methods
    // we will be using


    fun shareMsg(context: Context,msg:String){
        val i2=Intent(Intent.ACTION_SEND)
        i2.putExtra(Intent.EXTRA_TEXT,msg)
        i2.type="text/text"
        context.startActivity(i2)
    }

    fun showDialog(activity: Activity,title:String,msg:String,action:Runnable?,yesTxt:String?=null,noTxt:String?=null){
        val b2=DialogMsgBinding.inflate(activity.layoutInflater)
        val dialog=Dialog(activity, R.style.my_dialog)
        dialog.setContentView(b2.root)

        b2.tvTitle.text=title
        b2.tvMsg.text=msg
        if(yesTxt!=null){
            b2.btnNo.text=noTxt
            b2.btnYes.text=yesTxt
        }
        b2.btnNo.setOnClickListener {
            dialog.dismiss()


        }
        b2.btnYes.setOnClickListener {
            dialog.dismiss()
            action?.run()

        }
        dialog.show()

    }


    // method to show location info of selected point on map
    fun showLocationInfo(activity: Activity,item:MySavedItem){
        val b2=DialogInfoBinding.inflate(activity.layoutInflater)
        val dialog=Dialog(activity, R.style.my_dialog)
        dialog.setContentView(b2.root)
        b2.tvAddress.text=item.address
        b2.tvLatLong.text="${item.lat} / ${item.longi}"


        b2.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        // yes button is a share button
        b2.btnYes.setOnClickListener {
            dialog.dismiss()
            val msg="Here is the location \n ${item.address} \n latitude / longitude \n ${item.lat} / ${item.longi} "
          shareMsg(activity,msg)
        }
        dialog.show()

    }

    fun hasPerm(context: Context,perm:String):Boolean{
        return ContextCompat.checkSelfPermission(context,perm)==PackageManager.PERMISSION_GRANTED
    }
}