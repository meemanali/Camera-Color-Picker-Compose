package com.example.cameracolorpickercompose.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ColorItem : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var hexCode: String = ""
}