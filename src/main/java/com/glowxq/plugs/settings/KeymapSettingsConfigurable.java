package com.glowxq.plugs.settings;

import com.glowxq.plugs.utils.I18nUtils;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 快捷键设置面板配置
 * 
 * @author glowxq
 */
public class KeymapSettingsConfigurable implements Configurable {

    private KeymapSettingsComponent mySettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return I18nUtils.message("settings.keymap.title");
    }

    @Override
    public @Nullable JComponent createComponent() {
        mySettingsComponent = new KeymapSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        if (mySettingsComponent == null) {
            return false;
        }
        
        KeymapSettings settings = KeymapSettings.getInstance();
        String currentShortcut = mySettingsComponent.getShortcut();
        String savedShortcut = settings.getShortcutForCurrentOS("smartOneClick");
        
        if (savedShortcut == null) {
            String osModifier = com.intellij.openapi.util.SystemInfo.isMac ? "Cmd" : "Ctrl";
            savedShortcut = osModifier + "+Shift+D";
        }
        
        return !currentShortcut.equals(savedShortcut);
    }

    @Override
    public void apply() throws ConfigurationException {
        if (mySettingsComponent == null) {
            return;
        }
        
        // 验证快捷键格式
        if (!mySettingsComponent.isShortcutValid()) {
            throw new ConfigurationException("快捷键格式不正确，请检查输入");
        }
        
        // 保存快捷键设置
        KeymapSettings settings = KeymapSettings.getInstance();
        String shortcut = mySettingsComponent.getShortcut();
        settings.setShortcutForCurrentOS("smartOneClick", shortcut);
        
        // 应用快捷键到实际的Action（这里需要实现KeymapApplier）
        // KeymapApplier.applyKeymapSettings(settings);
    }

    @Override
    public void reset() {
        if (mySettingsComponent != null) {
            mySettingsComponent.loadDefaultShortcut();
        }
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
