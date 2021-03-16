package com.britishbroadcast.myservice.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(val albumArt: String, val songInfo: String, val songResource: Int): Parcelable