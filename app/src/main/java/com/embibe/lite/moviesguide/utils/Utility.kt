package com.embibe.lite.moviesguide.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun isInternetAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}


fun <T : ViewDataBinding> ViewGroup.inflateDataBindingLayout(layoutId: Int): T =
    DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, this, false)
