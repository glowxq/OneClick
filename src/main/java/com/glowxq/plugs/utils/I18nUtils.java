package com.glowxq.plugs.utils;

import com.glowxq.plugs.settings.OneClickSettings;
import com.intellij.DynamicBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 国际化工具类
 * 支持中英双语切换
 * 
 * @author glowxq
 */
public class I18nUtils extends DynamicBundle {
    
    private static final String BUNDLE_NAME = "messages.OneClickBundle";
    private static I18nUtils INSTANCE = new I18nUtils();
    
    private I18nUtils() {
        super(BUNDLE_NAME);
    }
    
    /**
     * 获取国际化消息
     */
    public static String message(@NotNull @PropertyKey(resourceBundle = BUNDLE_NAME) String key, Object... params) {
        try {
            // 根据当前设置获取对应的ResourceBundle
            Locale currentLocale = getCurrentLocale();
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
            String message = bundle.getString(key);

            // 如果有参数，进行格式化
            if (params.length > 0) {
                return String.format(message, params);
            }
            return message;
        } catch (Exception e) {
            // 如果出错，回退到默认方式
            return INSTANCE.getMessage(key, params);
        }
    }
    
    /**
     * 根据设置获取当前语言环境
     */
    public static Locale getCurrentLocale() {
        OneClickSettings settings = OneClickSettings.getInstance();
        if (settings.isUseEnglish()) {
            return Locale.ENGLISH;
        } else {
            return Locale.CHINESE;
        }
    }

    /**
     * 是否使用英语
     */
    public static boolean isUseEnglish() {
        OneClickSettings settings = OneClickSettings.getInstance();
        return settings.isUseEnglish();
    }
    
    /**
     * 获取本地化的ResourceBundle
     */
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle(BUNDLE_NAME, getCurrentLocale());
    }
    
    /**
     * 刷新语言设置
     */
    public static void refreshLanguage() {
        // 清除ResourceBundle缓存，强制重新加载
        ResourceBundle.clearCache();
        // 设置新的语言环境
        Locale.setDefault(getCurrentLocale());
    }
    
    // 常用消息的便捷方法
    
    public static String getPluginName() {
        return message("plugin.name");
    }
    
    public static String getPluginDescription() {
        return message("plugin.description");
    }
    
    public static String getGenerateActionText() {
        return message("action.generate.text");
    }
    
    public static String getGenerateActionDescription() {
        return message("action.generate.description");
    }
    
    public static String getFoldActionText() {
        return message("action.fold.text");
    }
    
    public static String getFoldActionDescription() {
        return message("action.fold.description");
    }
    
    public static String getSettingsTitle() {
        return message("settings.title");
    }
    
    public static String getJavaBeanSettingsTitle() {
        return message("settings.javabean.title");
    }
    
    public static String getBusinessSettingsTitle() {
        return message("settings.business.title");
    }
    
    public static String getGeneralSettingsTitle() {
        return message("settings.general.title");
    }
    
    public static String getStyleSettingsTitle() {
        return message("settings.style.title");
    }
    
    public static String getSeparatorComment() {
        return message("separator.comment");
    }
    
    public static String getGenerationSuccessMessage() {
        return message("message.generation.success");
    }
    
    public static String getGenerationFailedMessage() {
        return message("message.generation.failed");
    }
}
