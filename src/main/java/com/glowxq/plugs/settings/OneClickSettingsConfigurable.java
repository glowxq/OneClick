package com.glowxq.plugs.settings;

import com.glowxq.plugs.utils.I18nUtils;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * OneClick插件设置面板
 * 支持中英双语
 */
public class OneClickSettingsConfigurable implements Configurable {

    private OneClickSettingsComponent mySettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return I18nUtils.getSettingsTitle();
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
               mySettingsComponent.isGenerateFluentSetters() != settings.isGenerateFluentSetters() ||
               !mySettingsComponent.getToStringStyle().equals(settings.getToStringStyle()) ||
               mySettingsComponent.isUseEnglish() != settings.isUseEnglish() ||
               mySettingsComponent.isProcessInnerClasses() != settings.isProcessInnerClasses() ||
               mySettingsComponent.isGenerateInnerClassSeparator() != settings.isGenerateInnerClassSeparator() ||
               mySettingsComponent.getMaxInnerClassDepth() != settings.getMaxInnerClassDepth() ||
               // DTO/VO/BO生成设置
               mySettingsComponent.isUseBeanUtilsForConversion() != settings.isUseBeanUtilsForConversion() ||
               !mySettingsComponent.getBeanUtilsClass().equals(settings.getBeanUtilsClass()) ||
               mySettingsComponent.isGenerateSerialAnnotation() != settings.isGenerateSerialAnnotation() ||
               // 枚举类parse方法设置
               !mySettingsComponent.getEnumParseMethodName().equals(settings.getEnumParseMethodName()) ||
               !mySettingsComponent.getEnumCodeFieldName().equals(settings.getEnumCodeFieldName());
    }

    @Override
    public void apply() throws ConfigurationException {
        OneClickSettings settings = OneClickSettings.getInstance();
        settings.setGenerateSeparatorComment(mySettingsComponent.isGenerateSeparatorComment());
        settings.setGenerateGetterSetter(mySettingsComponent.isGenerateGetterSetter());
        settings.setGenerateToString(mySettingsComponent.isGenerateToString());
        settings.setGenerateFluentSetters(mySettingsComponent.isGenerateFluentSetters());
        settings.setToStringStyle(mySettingsComponent.getToStringStyle());
        settings.setUseEnglish(mySettingsComponent.isUseEnglish());
        settings.setProcessInnerClasses(mySettingsComponent.isProcessInnerClasses());
        settings.setGenerateInnerClassSeparator(mySettingsComponent.isGenerateInnerClassSeparator());
        settings.setMaxInnerClassDepth(mySettingsComponent.getMaxInnerClassDepth());

        // DTO/VO/BO生成设置
        settings.setUseBeanUtilsForConversion(mySettingsComponent.isUseBeanUtilsForConversion());
        settings.setBeanUtilsClass(mySettingsComponent.getBeanUtilsClass());
        settings.setGenerateSerialAnnotation(mySettingsComponent.isGenerateSerialAnnotation());

        // 枚举类parse方法设置
        settings.setEnumParseMethodName(mySettingsComponent.getEnumParseMethodName());
        settings.setEnumCodeFieldName(mySettingsComponent.getEnumCodeFieldName());
    }

    @Override
    public void reset() {
        OneClickSettings settings = OneClickSettings.getInstance();
        mySettingsComponent.setGenerateSeparatorComment(settings.isGenerateSeparatorComment());
        mySettingsComponent.setGenerateGetterSetter(settings.isGenerateGetterSetter());
        mySettingsComponent.setGenerateToString(settings.isGenerateToString());
        mySettingsComponent.setGenerateFluentSetters(settings.isGenerateFluentSetters());
        mySettingsComponent.setToStringStyle(settings.getToStringStyle());
        mySettingsComponent.setUseEnglish(settings.isUseEnglish());
        mySettingsComponent.setProcessInnerClasses(settings.isProcessInnerClasses());
        mySettingsComponent.setGenerateInnerClassSeparator(settings.isGenerateInnerClassSeparator());
        mySettingsComponent.setMaxInnerClassDepth(settings.getMaxInnerClassDepth());

        // DTO/VO/BO生成设置
        mySettingsComponent.setUseBeanUtilsForConversion(settings.isUseBeanUtilsForConversion());
        mySettingsComponent.setBeanUtilsClass(settings.getBeanUtilsClass());
        mySettingsComponent.setGenerateSerialAnnotation(settings.isGenerateSerialAnnotation());

        // 枚举类parse方法设置
        mySettingsComponent.setEnumParseMethodName(settings.getEnumParseMethodName());
        mySettingsComponent.setEnumCodeFieldName(settings.getEnumCodeFieldName());
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }
}
