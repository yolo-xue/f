package com.example.wechattheme

import de.robv.android.xposed.IXposedHookInitPackageResources
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage, IXposedHookInitPackageResources {

    companion object {
        const val WECHAT_PACKAGE = "com.tencent.mm"
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        // 过滤非微信进程
        if (lpparam.packageName != WECHAT_PACKAGE) return

        XposedBridge.log("微信 Hook 模块启动，目标进程: ${lpparam.processName}")

        // 初始化功能模块
        ThemeEngine.initHook(lpparam)
        FeatureHook.initHook(lpparam)
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        if (resparam.packageName != WECHAT_PACKAGE) return

        // 注入资源层美化（替换 Color 与 Drawable）
        ThemeEngine.initResourceHook(resparam)
    }
}
