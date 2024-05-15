package com.samsung.millioner.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentId

class ThemeListModel {
    @DocumentId
    var themeId: String? = null
    var title: String? = null
    var image: String? = null

    constructor()

    constructor(themeId: String, title: String, image: String) {
        this.themeId = themeId
        this.title = title
        this.image = image
    }
    override fun toString(): String {
        return "ThemeListModel(themeId=$themeId, title=$title, image=$image)"
    }
}
