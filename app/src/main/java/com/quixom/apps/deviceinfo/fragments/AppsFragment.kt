package com.quixom.apps.deviceinfo.fragments

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.quixom.apps.deviceinfo.R
import com.quixom.apps.deviceinfo.adapters.DeviceAdapter
import com.quixom.apps.deviceinfo.models.DeviceInfo
import com.quixom.apps.deviceinfo.utilities.KeyUtil

class AppsFragment : BaseFragment() {

    private var ivMenu: ImageView? = null
    private var ivBackArrow: ImageView? = null
    var tvTitle: TextView? = null
    var rvAppsList: RecyclerView? = null
    private var coordinatorLayout: CoordinatorLayout? = null
    private var appsTask : AppsTask? = null

    var mode: Int? = 0

    companion object {
        fun getInstance(mode: Int): AppsFragment {
            val appsFragment = AppsFragment()
            val bundle = Bundle()
            bundle.putInt(KeyUtil.KEY_MODE, mode)
            appsFragment.arguments = bundle
            return appsFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = inflater!!.inflate(R.layout.fragment_apps, container, false)

        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.UserAppsTheme)
        val localInflater = inflater.cloneInContext(contextThemeWrapper)
        val view = localInflater.inflate(R.layout.fragment_apps, container, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity!!.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.dark_parrot_green_blue)
            window . navigationBarColor = resources . getColor (R.color.dark_parrot_green_blue)
        }

        ivMenu = view.findViewById(R.id.iv_menu)
        ivBackArrow = view.findViewById(R.id.iv_back)
        tvTitle = view.findViewById(R.id.tv_title)
        rvAppsList = view.findViewById(R.id.rv_apps_list)
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()
        initToolbar()
        rvAppsList?.layoutManager = LinearLayoutManager(mActivity)
        rvAppsList?.hasFixedSize()
        val task = AppsTask(mode == 1)
        task.listener = {
            initAppsList(it)
        }
        task.execute(context)
        appsTask = task
    }

    override fun onDestroy() {
        super.onDestroy()
        appsTask?.listener = null
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && isAdded) {
            initToolbar()
        }
    }

    /**
     * Get data from bundle
     */
    private fun getBundleData() {
        val bundle = arguments
        if (bundle != null) {
            if (bundle.containsKey(KeyUtil.KEY_MODE)) {
                mode = bundle.getInt(KeyUtil.KEY_MODE)
            }
        }
    }

    private fun initToolbar() {
        ivMenu?.visibility = View.VISIBLE
        ivBackArrow?.visibility = View.GONE
        if (mode?.equals(KeyUtil.IS_USER_COME_FROM_USER_APPS)!!) {
            tvTitle?.text = mResources.getString(R.string.user_apps)
        } else {
            tvTitle?.text = mResources.getString(R.string.system_and_apps)
        }

        ivMenu?.setOnClickListener {
            mActivity.openDrawer()
        }
    }

    private fun initAppsList(apps : List<DeviceInfo>) {
        //creating our adapter
        val adapter = DeviceAdapter(apps, mActivity)

        if (mode == KeyUtil.IS_USER_COME_FROM_USER_APPS) {
            snackBarCustom(coordinatorLayout!!, apps.size.toString() + " " + mResources.getString(R.string.user_apps))
        } else {
            snackBarCustom(coordinatorLayout!!, apps.size.toString() + " " + mResources.getString(R.string.system_apps))
        }
        //now adding the adapter to RecyclerView
        rvAppsList?.adapter = adapter
    }

    /**
     * Display the Snackbar with the [Snackbar.LENGTH_SHORT] duration.
     *
     * @param message the message text.
     */

    private fun snackBarCustom(view: View, message: String) {
        val mSnackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val view: View? = mSnackBar.view
        val mainTextView = mSnackBar.view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mainTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        else
            mainTextView.gravity = Gravity.CENTER_HORIZONTAL
        mainTextView.gravity = Gravity.CENTER_HORIZONTAL
        mainTextView.setTextColor(Color.WHITE)
        view?.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.app_snackbar_color))
        mSnackBar.show()
    }

    class AppsTask(private val system: Boolean) : AsyncTask<Context, Void, List<DeviceInfo>>() {
        var listener: ((List<DeviceInfo>) -> Unit)? = null

        private fun  getAppsList(context : Context, system : Boolean) : List<DeviceInfo> {
            val list = ArrayList<DeviceInfo>();
            val flags = PackageManager.GET_META_DATA.or(PackageManager.GET_SHARED_LIBRARY_FILES);

            val pm : PackageManager = context.packageManager;
            val applications : List<ApplicationInfo> = pm.getInstalledApplications(flags);

            for (ai in applications) {
                val name: String = pm.getApplicationLabel(ai).toString();
                val isSystem: Boolean = ai.flags.and(ApplicationInfo.FLAG_SYSTEM) == 1;

                val di = DeviceInfo(name, ai);
                if (isSystem == system) {
                    list.add(di)
                }
            }
            return list;
        }

        override fun doInBackground(vararg params: Context?): List<DeviceInfo> {
            return getAppsList(params[0]!!, system);
        }

        override fun onPostExecute(result: List<DeviceInfo>?) {
            listener?.invoke(result!!)
        }

    }
}