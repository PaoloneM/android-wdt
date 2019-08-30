package com.noesysmobile.wdtlibtest

import android.content.Context
import android.support.annotation.LayoutRes
import android.widget.ArrayAdapter
import com.noesysmobile.wdtlibrary.Wdt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_row.view.*


class WdtListAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val wdts: List<Wdt>): ArrayAdapter<Wdt>(context, layoutResource, wdts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View
        if (convertView == null) {
            rowView = inflater.inflate(layoutResource, parent, false)
        } else {
            rowView = convertView
        }
        rowView.timeout_count.text = wdts[position].getOccurredTimeouts().toString()
        rowView.firstLine.text = "ID: ${wdts[position].getId()}"
        rowView.secondLine.text = "Timeout: ${wdts[position].getTimeout()}"
        return rowView
    }
}