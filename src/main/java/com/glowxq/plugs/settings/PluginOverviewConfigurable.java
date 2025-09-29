package com.glowxq.plugs.settings;

import com.glowxq.plugs.ui.PluginOverviewPanel;
import com.glowxq.plugs.utils.I18nUtils;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * OneClick插件概览页面配置
 */
public class PluginOverviewConfigurable implements Configurable {

    private PluginOverviewPanel myOverviewPanel;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return I18nUtils.message("settings.overview.title");
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        myOverviewPanel = new PluginOverviewPanel();
        return myOverviewPanel.getPanel();
    }

    @Override
    public boolean isModified() {
        return false; // 概览页面不需要保存设置
    }

    @Override
    public void apply() throws ConfigurationException {
        // 概览页面不需要保存设置
    }

    @Override
    public void reset() {
        // 概览页面不需要重置设置
    }

    @Override
    public void disposeUIResources() {
        myOverviewPanel = null;
    }
}
