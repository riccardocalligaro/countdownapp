package com.riccardocalligaro.countdownapp.adapters

import com.chad.library.adapter.base.entity.MultiItemEntity

class MultipleItem : MultiItemEntity {

    companion object {
        val DEFAULT_NORMAL = 1
        val DEFAUL_IMAGE = 2
        val EXTENDED_NORMAL = 3
        val EXTENDED_IMAGE = 3
        // val IMG_SPAN_SIZE = 1
        //  val IMG_TEXT_SPAN_SIZE = 4
        //  val IMG_TEXT_SPAN_SIZE_MIN = 2
    }

    private var itemType: Int = 0
    var spanSize: Int = 0

    constructor(itemType: Int, spanSize: Int, content: String) {
        this.itemType = itemType
        this.spanSize = spanSize
        this.content = content
    }

    constructor(itemType: Int, spanSize: Int) {
        this.itemType = itemType
        this.spanSize = spanSize
    }

    var content: String? = null

    override fun getItemType(): Int {
        return itemType
    }
}