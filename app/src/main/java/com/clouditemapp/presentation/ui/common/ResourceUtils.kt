package com.clouditemapp.presentation.ui.common

import android.content.Context
import com.clouditemapp.R
import java.io.File

object ResourceUtils {
    /**
     * 获取物品图片的资源，优先返回家长自定义路径，否则返回内置资源ID，都不存在则返回分类占位图
     */
    fun getItemImageRes(context: Context, resName: String, category: String, customPath: String? = null): Any {
        // 1. 优先加载家长自定义图片
        if (!customPath.isNullOrEmpty()) {
            val file = File(customPath)
            if (file.exists()) {
                return file
            }
        }

        // 2. 加载内置资源
        val resId = context.resources.getIdentifier(resName, "drawable", context.packageName)
        return if (resId != 0) {
            resId
        } else {
            // 3. 回退到分类占位图
            getPlaceholderForCategory(category)
        }
    }

    /**
     * 根据分类获取占位图
     */
    fun getPlaceholderForCategory(category: String): Int {
        return when (category) {
            "动物世界" -> R.drawable.ic_placeholder_default
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
