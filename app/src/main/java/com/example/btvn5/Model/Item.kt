package com.example.btvn5.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


    @Parcelize
    data  class  Item(
        var imageId: Int,
        var title: String,
        var price: Double,
        var category: Category
    ) : Parcelable
