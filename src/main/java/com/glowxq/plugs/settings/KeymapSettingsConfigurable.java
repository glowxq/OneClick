package com.glowxq.plugs.settings;

import com.glowxq.plugs.utils.I18nUtils;
import com.glowxq.plugs.utils.KeymapApplier;
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
        KeymapSettings settings = KeymapSettings.getInstance();
        return mySettingsComponent != null && mySettingsComponent.isModified(settings);
    }

    @Override
    public void apply() throws ConfigurationException {
        KeymapSettings settings = KeymapSettings.getInstance();
        if (mySettingsComponent != null) {
            mySettingsComponent.saveSettings(settings);
            // 应用快捷键设置到实际的Action
            KeymapApplier.applyKeymapSettings(settings);
        }
    }

    @Override
    public void reset() {
        KeymapSettings settings = KeymapSettings.getInstance();
        if (mySettingsComponent != null) {
            mySettingsComponent.loadSettings(settings);
        }
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
