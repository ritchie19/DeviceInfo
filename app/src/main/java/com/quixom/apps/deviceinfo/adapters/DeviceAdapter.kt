package com.quixom.apps.deviceinfo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.quixom.apps.deviceinfo.MainActivity
import com.quixom.apps.deviceinfo.R
import com.quixom.apps.deviceinfo.models.DeviceInfo
import com.quixom.apps.deviceinfo.utilities.Methods


/**
 * Created by akif on 9/21/17.
 */

class DeviceAdapter(internal var appslist: List<DeviceInfo>,
                    internal var mActivity: MainActivity) : RecyclerView.Adapter<DeviceAdapter.DeviceVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceAdapter.DeviceVH {
        val itemView = LayoutInflater.from(mActivity).inflate(R.layout.row_infomation, parent, false)
        return DeviceVH(itemView)
    }

    override fun onBindViewHolder(holder: DeviceAdapter.DeviceVH, position: Int) {
        holder.bindData(appslist[position])
    }

    override fun getItemCount(): Int {
        return appslist.size
    }

    inner class DeviceVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(deviceInfo: DeviceInfo) {

            val ivAppLogo: ImageView = itemView.findViewById(R.id.iv_app_icon)
            val appName: TextView? = itemView.findViewById(R.id.tv_app_name)
            val tvAppPackageName: TextView? = itemView.findViewById(R.id.tv_app_package_name)
            val publicSourceDir:TextView? = itemView.findViewById(R.id.tv_app_public_source_dir);
            val dataDir:TextView? = itemView.findViewById(R.id.tv_app_data_dir);

            appName?.text = deviceInfo.appLabel
            tvAppPackageName?.text = deviceInfo.ai.packageName
            Glide.with(ivAppLogo)
                    .load("package://" + deviceInfo.ai.packageName)
                    .into(ivAppLogo)

//            ivAppLogo?.setImageDrawable(deviceInfo.appLogo)
            publicSourceDir?.text = deviceInfo.ai.publicSourceDir
            dataDir?.text = deviceInfo.ai.dataDir

            itemView.setOnClickListener {
                Methods.avoidDoubleClicks(itemView)

                val launchIntent = mActivity.packageManager.getLaunchIntentForPackage(deviceInfo.ai.packageName)
                if (launchIntent != null) {
                    startActivity(mActivity, launchIntent, null)
                }
            }
        }
    }
}
