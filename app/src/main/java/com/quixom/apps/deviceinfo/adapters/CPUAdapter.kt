package com.quixom.apps.deviceinfo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quixom.apps.deviceinfo.MainActivity
import com.quixom.apps.deviceinfo.R
import com.quixom.apps.deviceinfo.models.FeaturesHW
/**
 * Created by akif on 9/21/17.
 */

class CPUAdapter(private var appslist: ArrayList<FeaturesHW>,
                 private var mActivity: MainActivity) : RecyclerView.Adapter<CPUAdapter.DeviceVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CPUAdapter.DeviceVH {
        val itemView = LayoutInflater.from(mActivity).inflate(R.layout.row_cpu_item, parent, false)
        return DeviceVH(itemView)
    }

    override fun onBindViewHolder(holder: CPUAdapter.DeviceVH, position: Int) {
        holder.bindData(appslist[position], position)
    }

    override fun getItemCount(): Int = appslist.size

    inner class DeviceVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(featureHW: FeaturesHW, position: Int) {

            val tvFeatureName: TextView = itemView.findViewById(R.id.tv_cpu_feature_name)
            val tvFeatureValue: TextView = itemView.findViewById(R.id.tv_cpu_feature_value)
            val viewSeparator: View = itemView.findViewById(R.id.separatorView)

            tvFeatureName.text = featureHW.featureLable
            tvFeatureValue.text = featureHW.featureValue

            if (featureHW.featureLable == mActivity.resources.getString(R.string.processor) && position > 0) {
                viewSeparator.visibility = View.VISIBLE
            } else {
                viewSeparator.visibility = View.GONE
            }
        }
    }
}