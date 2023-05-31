package com.andre.gitlist.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "user_database")
@Parcelize
data class User(
    @SerializedName("name")
    var name: String? = null,

    @PrimaryKey
    @SerializedName("login")
    var login: String,

    @SerializedName("location")
    var location: String? = null,

    @SerializedName("public_repos")
    var repository: Int? = 0,

    @SerializedName("public_gists")
    var gist: Int? = 0,

    @SerializedName("company")
    var company: String? = null,

    @SerializedName("followers")
    var followers: Int? = 0,

    @SerializedName("following")
    var following: Int? = 0,

    @SerializedName("avatar_url")
    var avatar: String? = null
) : Parcelable

data class SearchUsers(
    @SerializedName("items")
    var item: List<User>
)

data class ActiveUser(
    @SerializedName("total_count")
    var total: Int
)

data class ActiveOrg(
    @SerializedName("total_count")
    var total: Int
)