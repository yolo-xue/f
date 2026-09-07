package com.example.wechattheme

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ThemeEngine {

    fun initResourceHook(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        try {
            // 方式一：资源层拦截 - 强制替换微信原生背景色资源为白色 (#FFFFFF)
            resparam.res.setReplacement(MainHook.WECHAT_PACKAGE, "color", "action_bar_color", Color.WHITE)
            resparam.res.setReplacement(MainHook.WECHAT_PACKAGE, "color", "normal_bg_color", Color.WHITE)
        } catch (e: Throwable) {
            XposedBridge.log("资源替换失败，可能该版本无此 Color 字段: ${e.message}")
        }
    }

    fun initHook(lpparam: XC_LoadPackage.LoadPackageParam) {
        // 方式二：View 动态创建拦截 - 修改布局与控件属性
        try {
            XposedHelpers.findAndHookMethod(
                "android.view.LayoutInflater",
                lpparam.classLoader,
                "inflate",
                Int::class.javaPrimitiveType,
                ViewGroup::class.java,
                Boolean::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val view = param.result as? View ?: return
                        
                        // 遍历 View 树，强制替换所有黑暗背景与深色调
                        applyWhiteThemeRecursively(view)
                    }
                }
            )
        } catch (e: Throwable) {
            XposedBridge.log("LayoutInflater Hook 失败: ${e.message}")
        }
    }

    private fun applyWhiteThemeRecursively(view: View) {
        // 强行修改顶层背景色
        view.setBackgroundColor(Color.WHITE)

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                applyWhiteThemeRecursively(view.getChildAt(i))
            }
        } else if (view is TextView) {
            // 调整文字颜色，防止“白底白字”
            view.setTextColor(Color.parseColor("#111111"))
        }
    }
}
