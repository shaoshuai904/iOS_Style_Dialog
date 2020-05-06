package com.maple.msdialog

import android.app.Dialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.maple.msdialog.databinding.DialogActionSheetListBinding

/**
 * 页签List Dialog [ 标题 + 页签条目 + 取消按钮 ]
 *
 * @author : shaoshuai27
 * @date ：2020/5/6
 */
class ActionSheetListDialog(context: Context) : BaseDialog(context) {
    private val binding: DialogActionSheetListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_action_sheet_list, null, false)
    private var showTitle = false
    private val adapter by lazy { ActionSheetAdapter(mContext) }
    private var sheetItemList: MutableList<SheetItem>? = null

    companion object {
        const val ACTION_SHEET_ITEM_HEIGHT = 45
    }

    init {
        rootView = binding.root
        // set Dialog min width
        rootView?.minimumWidth = screenInfo().x
        binding.tvTitle.visibility = View.GONE
        binding.tvCancel.setOnClickListener { dialog.dismiss() }

        // create Dialog
        dialog = Dialog(context, R.style.ActionSheetDialogStyle)
        dialog.setContentView(binding.root)
        dialog.window?.apply {
            setGravity(Gravity.LEFT or Gravity.BOTTOM)
            attributes = attributes.apply {
                x = 0
                y = 0
            }
        }
    }

    fun setTitle(title: String?): ActionSheetListDialog {
        val color = ContextCompat.getColor(mContext, R.color.def_title_color)
        return setTitle(title, color, 16, false)
    }

    fun setTitle(title: String?, color: Int, size: Int, isBold: Boolean): ActionSheetListDialog {
        showTitle = true
        binding.tvTitle.apply {
            visibility = View.VISIBLE
            text = title
            setTextColor(color)
            textSize = size.toFloat()
            if (isBold) {
                setTypeface(typeface, Typeface.BOLD)
            }
        }
        adapter.showTitle(showTitle)
        return this
    }

    fun setCancelText(cancelText: String?): ActionSheetListDialog {
        val color = ContextCompat.getColor(mContext, R.color.def_title_color)
        return setCancelText(cancelText, color, 18, false)
    }

    fun setCancelText(cancelText: String?, color: Int, size: Int, isBold: Boolean): ActionSheetListDialog {
        binding.tvCancel.apply {
            text = cancelText
            setTextColor(color)
            textSize = size.toFloat()
            if (isBold) {
                setTypeface(typeface, Typeface.BOLD)
            }
        }
        return this
    }

//    fun addSheetItem(strItem: String?, listener: SheetItem.OnSheetItemClickListener?): ActionListDialog {
//        val color = ContextCompat.getColor(mContext, R.color.def_message_color)
//        return addSheetItem(strItem, color, listener)
//    }
//
//    fun addSheetItem(strItem: String?, color: Int, listener: SheetItem.OnSheetItemClickListener?): ActionListDialog {
//        return addSheetItem(SheetItem(strItem, color, listener))
//    }
//
//    fun addSheetItem(item: SheetItem): ActionListDialog {
//        if (sheetItemList == null) {
//            sheetItemList = ArrayList()
//        }
//        sheetItemList?.add(item)
//        return this
//    }

    fun addSheetItems(items: MutableList<SheetItem>, itemClickListener: SheetItem.OnSheetItemClickListener) {
        sheetItemList = items
        binding.lvData.adapter = adapter
        adapter.refresh(sheetItemList)
        binding.lvData.setOnItemClickListener { _, _, position, _ ->
            val item = adapter.getItem(position)
            itemClickListener.onClick(item)
            dialog.dismiss()
        }
    }

    /**
     * set items layout
     */
    private fun setSheetItems() {
        if (sheetItemList == null || sheetItemList!!.size <= 0) {
            return
        }
        val size = sheetItemList!!.size
        // 添加条目过多的时候控制高度
        val screenHeight = screenInfo().y
        if (size > screenHeight / dp2px(ACTION_SHEET_ITEM_HEIGHT * 2.toFloat())) {
            val params = binding.lvData.layoutParams as LinearLayout.LayoutParams
            params.height = screenHeight / 2
            binding.lvData.layoutParams = params
        }
    }

    fun show() {
        setSheetItems()
        dialog.show()
    }

}