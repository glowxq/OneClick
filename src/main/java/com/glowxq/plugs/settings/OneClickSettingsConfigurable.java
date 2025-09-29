package com.glowxq.plugs.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * OneClick插件设置面板
 */
public class OneClickSettingsConfigurable implements Configurable {

    private OneClickSettingsComponent mySettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "OneClick Code Generator";
    }

    @Override
    public @Nullable JComponent createComponent() {
        mySettingsComponent = new OneClickSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        OneClickSettings settings = OneClickSettings.getInstance();
        return mySettingsComponent.isGenerateSeparatorComment() != settings.isGenerateSeparatorComment() ||
               mySettingsComponent.isGenerateGetterSetter() != settings.isGenerateGetterSetter() ||
               mySettingsComponent.isGenerateToString() != settings.isGenerateToString() ||
               mySettingsComponent.isGenerateEquals() != settings.isGenerateEquals() ||
               mySettingsComponent.isGenerateHashCode() != settings.isGenerateHashCode() ||
               mySettingsComponent.isGenerateLogger() != settings.isGenerateLogger() ||
               !mySettingsComponent.getLoggerFieldName().equals(settings.getLoggerFieldName()) ||
               !mySettingsComponent.getLoggerType().equals(settings.getLoggerType()) ||
               mySettingsComponent.isAutoDetectClassType() != settings.isAutoDetectClassType() ||
               mySettingsComponent.isUseFieldComments() != settings.isUseFieldComments() ||
               mySettingsComponent.isGenerateSerialVersionUID() != settings.isGenerateSerialVersionUID() ||
               mySettingsComponent.isUseBuilderPattern() != settings.isUseBuilderPattern() ||
               mySettingsComponent.isGenerateFluentSetters() != settings.isGenerateFluentSetters() ||
               !mySettingsComponent.getToStringStyle().equals(settings.getToStringStyle());
    }

    @Override
    public void apply() throws ConfigurationException {
        OneClickSettings settings = OneClickSettings.getInstance();
        settings.setGenerateSeparatorComment(mySettingsComponent.isGenerateSeparatorComment());
        settings.setGenerateGetterSetter(mySettingsComponent.isGenerateGetterSetter());
        settings.setGenerateToString(mySettingsComponent.isGenerateToString());
        settings.setGenerateEquals(mySettingsComponent.isGenerateEquals());
        settings.setGenerateHashCode(mySettingsComponent.isGenerateHashCode());
        settings.setGenerateLogger(mySettingsComponent.isGenerateLogger());
        settings.setLoggerFieldName(mySettingsComponent.getLoggerFieldName());
        settings.setLoggerType(mySettingsComponent.getLoggerType());
        settings.setAutoDetectClassType(mySettingsComponent.isAutoDetectClassType());
        settings.setUseFieldComments(mySettingsComponent.isUseFieldComments());
        settings.setGenerateSerialVersionUID(mySettingsComponent.isGenerateSerialVersionUID());
        settings.setUseBuilderPattern(mySettingsComponent.isUseBuilderPattern());
        settings.setGenerateFluentSetters(mySettingsComponent.isGenerateFluentSetters());
        settings.setToStringStyle(mySettingsComponent.getToStringStyle());
    }

    @Override
    public void reset() {
        OneClickSettings settings = OneClickSettings.getInstance();
        mySettingsComponent.setGenerateSeparatorComment(settings.isGenerateSeparatorComment());
        mySettingsComponent.setGenerateGetterSetter(settings.isGenerateGetterSetter());
        mySettingsComponent.setGenerateToString(settings.isGenerateToString());
        mySettingsComponent.setGenerateEquals(settings.isGenerateEquals());
        mySettingsComponent.setGenerateHashCode(settings.isGenerateHashCode());
        mySettingsComponent.setGenerateLogger(settings.isGenerateLogger());
        mySettingsComponent.setLoggerFieldName(settings.getLoggerFieldName());
        mySettingsComponent.setLoggerType(settings.getLoggerType());
        mySettingsComponent.setAutoDetectClassType(settings.isAutoDetectClassType());
        mySettingsComponent.setUseFieldComments(settings.isUseFieldComments());
        mySettingsComponent.setGenerateSerialVersionUID(settings.isGenerateSerialVersionUID());
        mySettingsComponent.setUseBuilderPattern(settings.isUseBuilderPattern());
        mySettingsComponent.setGenerateFluentSetters(settings.isGenerateFluentSetters());
        mySettingsComponent.setToStringStyle(settings.getToStringStyle());
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
