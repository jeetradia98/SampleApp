package com.sampleapp.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sampleapp.SampleApplication
import com.sampleapp.R
import com.sampleapp.api.ApiInterface
import com.sampleapp.api.ApiManager
import com.sampleapp.model.base.CustomError
import com.sampleapp.utils.NetworkUtils
import com.sampleapp.utils.PrefUtils
import com.sampleapp.utils.common.ApiConstants
import com.sampleapp.utils.common.AppConstant
import com.sampleapp.utils.permission.PermissionUtil
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject
import java.net.SocketTimeoutException


abstract class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var apiInterface: ApiInterface

    protected var mInputMethodManager: InputMethodManager? = null
    private var mProgressDialog: ProgressDialog? = null
    private var mLastClickTime: Long = 0

    var apiManager: ApiManager = SampleApplication.getAppComponent().provideApiManager()
    var prefUtils: PrefUtils = SampleApplication.getAppComponent().providePrefUtil()
    var networkUtils: NetworkUtils = SampleApplication.getAppComponent().provideNetworkUtils()
    var permissionUtil: PermissionUtil =
        SampleApplication.getAppComponent().providePermissionUtil()


    open fun handleError(throwable: Throwable) {
        if (throwable is HttpException) {
            val httpException: HttpException = throwable as HttpException
            handleHttpError(httpException)
        } else if (throwable is SocketTimeoutException || throwable is ConnectException) {
            handleNetworkError()
        } else if (throwable is CustomError) {
            handleCustomError(throwable)
        } else {
            throwable.printStackTrace()
            showError(getString(R.string.api_failure))
        }
    }

    private fun handleNetworkError() {
        //ConnectException only occurs when
        //either internet was not connected before calling api
        //or internet was turned off in the middle of outgoing request
        showError(getString(R.string.internet_not_available))
    }

    private fun handleHttpError(exception: HttpException) {
        Log.e(
            "---",
            "handleHttpError called code :" + exception.code()
                .toString() + " message : " + exception.message()
        )
        //for HttpException, use code() method to get Http code.
        //you can switch over code and handle different errors
        val code: Int = exception.code()
        when (code) {
            ApiConstants.ResponseCode.NOT_FOUND -> showError(getString(R.string.resource_not_found))
            ApiConstants.ResponseCode.CONFLICT -> showError(getString(R.string.server_conflict))
            else -> showError(getString(R.string.unknown_error_occurred))
        }
    }

    private fun handleCustomError(exception: CustomError) {
        Log.d("---", "handleCustomError() called with: exception = [" + exception.message + "]")

        showError(exception.error)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SampleApplication.getAppComponent().inject(this)
        mProgressDialog = ProgressDialog(this)
        mProgressDialog!!.setMessage(getString(R.string.loading))
        mProgressDialog!!.setCancelable(false)
        Log.e("base app", "onCreate: baseapp activity")
        mInputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
    }

    fun showLoader() {
        if (!mProgressDialog!!.isShowing) {
            mProgressDialog!!.show()
        }
    }

    fun hideLoader() {
        if (mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }

    fun showAlert(msg: String?) {
        if (msg == null) return
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun showError(msg: String?) {
        if (msg == null) return
        Snackbar.make(findViewById(android.R.id.content), msg, BaseTransientBottomBar.LENGTH_SHORT)
            .show()
    }

    fun hideKeyBoard(view: View?) {
        if (view != null) {
            mInputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    fun showKeyBoard(view: View?) {
        if (view != null) {
            mInputMethodManager!!.showSoftInput(view, 0)
        }
    }

    /**
     * it will return true if consecutive click occurs within [AppConstant.Delays.MIN_TIME_BETWEEN_CLICKS]
     *
     * @return true indicating do not allow any click, false otherwise
     */
    val isClickDisabled: Boolean
        get() = if (SystemClock.elapsedRealtime() - mLastClickTime < AppConstant.Delays.MIN_TIME_BETWEEN_CLICKS) true else {
            mLastClickTime = SystemClock.elapsedRealtime()
            false
        }

    /**
     * to add fragment in container
     * tag will be same as class name of fragment
     *
     * @param containerId    id of fragment container
     * @param addToBackStack should be added to backstack?
     */
    fun addFragment(fragment: Fragment, containerId: Int, addToBackStack: Boolean) {
        val fragmentManager: FragmentManager = getSupportFragmentManager()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(containerId, fragment)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        }
        fragmentTransaction.commit()
    }

    /**
     * to replace fragment in container
     * tag will be same as class name of fragment
     *
     * @param containerId        id of fragment container
     * @param isAddedToBackStack should be added to backstack?
     */
    fun replaceFragment(fragment: Fragment, containerId: Int, isAddedToBackStack: Boolean) {
        val fragmentManager: FragmentManager = getSupportFragmentManager()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(containerId, fragment)
        if (isAddedToBackStack) {
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        }
        fragmentTransaction.commit()
    }
}