package com.example.cameracolorpickercompose.repo

import com.example.cameracolorpickercompose.models.ColorItem
import com.example.cameracolorpickercompose.utils.access
import com.example.cameracolorpickercompose.utils.runOnIo
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class ColorRepository(private val realm: Realm) {

    suspend fun addColor(hexCode: String) {
        runOnIo {
            realm.write {
                val colorItem = ColorItem().apply {
                    this.hexCode = hexCode
                }
                copyToRealm(colorItem)
            }
        }
    }

    fun getAllColors(): Flow<List<ColorItem>> {
        return realm.query<ColorItem>()
            .asFlow()
            .map { it.list }
    }

    suspend fun deleteColor(id: ObjectId) {
        runOnIo {
            realm.write {
                val item = query<ColorItem>("_id == $0", id).first().find()
                item?.access {
                    delete(this)
                }
            }
        }
    }
}