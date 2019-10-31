package com.riccardocalligaro.countdownapp.adapters

import com.riccardocalligaro.countdownapp.db.entities.Countdown

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.riccardocalligaro.countdownapp.R


class QuickAdapter(layoutId: Int, countdowns: List<Countdown>?) :
    BaseQuickAdapter<Countdown, BaseViewHolder>(layoutId, countdowns) {

    override fun convert(viewHolder: BaseViewHolder?, countdown: Countdown?) {
        viewHolder?.let { holder ->
            countdown?.let {
                holder.setText(R.id.recycler_view_main_event_name, it.name)
            }
        }
    }
}