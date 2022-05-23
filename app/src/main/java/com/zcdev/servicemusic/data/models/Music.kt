package com.zcdev.servicemusic.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "music_table")
class Music {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var path: String? = null
    var title: String? = null
    var aAlbum: String? = null
    var aArtist: String? = null

    constructor() {

    }

    constructor(id: Int?, aPath: String?, aName: String?, aAlbum: String?, aArtist: String?) {
        this.id = id
        this.path = aPath
        this.title = aName
        this.aAlbum = aAlbum
        this.aArtist = aArtist
    }

    constructor(i: Int, path: String, title: String)


}