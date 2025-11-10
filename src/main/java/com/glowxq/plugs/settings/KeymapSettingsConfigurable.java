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
        // 快捷键是固定的，不需要修改
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        // 快捷键是固定的，不需要保存
    }

    @Override
    public void reset() {
        // 快捷键是固定的，不需要重置
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
