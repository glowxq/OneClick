package com.glowxq.plugs.icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * OneClick插件图标定义
 * 
 * @author glowxq
 */
public class OneClickIcons {
    
    /**
     * 插件主图标 - 16x16
     */
    public static final Icon PLUGIN_ICON_16 = IconLoader.getIcon("/icons/pluginIcon_16.png", OneClickIcons.class);

    /**
     * 插件主图标 - 32x32
     */
    public static final Icon PLUGIN_ICON_32 = IconLoader.getIcon("/icons/pluginIcon_32.png", OneClickIcons.class);

    /**
     * 插件主图标 - 40x40
     */
    public static final Icon PLUGIN_ICON_40 = IconLoader.getIcon("/icons/pluginIcon_40.png", OneClickIcons.class);

    /**
     * 插件主图标 - SVG
     */
    public static final Icon PLUGIN_ICON = IconLoader.getIcon("/icons/pluginIcon.svg", OneClickIcons.class);

    /**
     * 插件图标 - 深色主题
     */
    public static final Icon PLUGIN_ICON_DARK = IconLoader.getIcon("/icons/pluginIcon_dark.png", OneClickIcons.class);
    
    /**
     * 工具窗口图标
     */
    public static final Icon TOOL_WINDOW = PLUGIN_ICON;
    
    /**
     * Action图标
     */
    public static final Icon ACTION = PLUGIN_ICON;
}
