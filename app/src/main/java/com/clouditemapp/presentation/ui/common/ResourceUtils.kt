package com.clouditemapp.presentation.ui.common

import android.content.Context
import com.clouditemapp.R

object ResourceUtils {
    /**
     * 获取物品图片的资源ID，如果不存在则返回分类占位图或默认占位图
     */
    fun getItemImageRes(context: Context, resName: String, category: String): Any {
        val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
        return if (resId != 0) {
            resId
        } else {
            getPlaceholderForCategory(category)
        }
    }

    /**
     * 根据分类获取占位图
     */
    fun getPlaceholderForCategory(category: String): Int {
        return when (category) {
            "动物世界" -> R.drawable.ic_placeholder_default // 后续可细化
            "美味水果" -> R.drawable.ic_placeholder_default
            "新鲜蔬菜" -> R.drawable.ic_placeholder_default
            "交通工具" -> R.drawable.ic_placeholder_default
            "日常用品" -> R.drawable.ic_placeholder_default
            "自然现象" -> R.drawable.ic_placeholder_default
            "食物与饮料" -> R.drawable.ic_placeholder_default
            "身体部位" -> R.drawable.ic_placeholder_default
            else -> R.drawable.ic_placeholder_default
        }
    }
}
