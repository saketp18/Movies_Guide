package com.embibe.lite.moviesguide.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun <T : ViewDataBinding> ViewGroup.inflateDataBindingLayout(layoutId: Int): T =
    DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, this, false)
