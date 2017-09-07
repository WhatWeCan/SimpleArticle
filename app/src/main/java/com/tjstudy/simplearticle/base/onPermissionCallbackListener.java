package com.tjstudy.simplearticle.base;

import java.util.List;

/**
 * 权限处理接口
 */
public interface onPermissionCallbackListener {
    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
