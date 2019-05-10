package com.example.btvn5.Model

import android.icu.util.ULocale
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    var categoryId: Int,
    var categoryName: String
) : Parcelable