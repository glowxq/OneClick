package com.glowxq.plugs.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * OneClick插件设置
 */
@Service
@State(
    name = "OneClickSettings",
    storages = @Storage("oneclick-settings.xml")
)
public final class OneClickSettings implements PersistentStateComponent<OneClickSettings.State> {

    private State myState = new State();

    public static OneClickSettings getInstance() {
        return ApplicationManager.getApplication().getService(OneClickSettings.class);
    }

    @Override
    public @Nullable State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        myState = state;
    }

    /**
     * 设置状态类
     */
    public static class State {
        // JavaBean相关设置
        public boolean generateSeparatorComment = true;
        public boolean generateGetterSetter = true;
        public boolean generateToString = true;
        
        // 代码风格设置
        public boolean generateFluentSetters = false;
        public String toStringStyle = "json"; // json, simple, apache

        // 内部类设置
        public boolean processInnerClasses = true;
        public boolean generateInnerClassSeparator = true;
        public int maxInnerClassDepth = 3;

        // DTO/VO/BO生成设置
        public boolean useBeanUtilsForConversion = true; // 使用BeanUtils进行属性复制
        public String beanUtilsClass = "org.springframework.beans.BeanUtils"; // BeanUtils类的全限定名
        public boolean generateSerialAnnotation = false; // 生成@Serial注解（JDK 14+），默认false兼容JDK 8

        // 枚举类parse方法设置
        public String enumParseMethodName = "parse"; // 枚举parse方法名，默认为parse
        public String enumCodeFieldName = "code"; // 枚举code字段名，默认为code

        // 语言设置
        public boolean useEnglish = false; // 默认使用中文
    }

    // Getter方法
    public boolean isGenerateSeparatorComment() {
        return myState.generateSeparatorComment;
    }

    public boolean isGenerateGetterSetter() {
        return myState.generateGetterSetter;
    }

    public boolean isGenerateToString() {
        return myState.generateToString;
    }

    public boolean isGenerateFluentSetters() {
        return myState.generateFluentSetters;
    }

    public String getToStringStyle() {
        return myState.toStringStyle;
    }

    // Setter方法
    public void setGenerateSeparatorComment(boolean generateSeparatorComment) {
        myState.generateSeparatorComment = generateSeparatorComment;
    }

    public void setGenerateGetterSetter(boolean generateGetterSetter) {
        myState.generateGetterSetter = generateGetterSetter;
    }

    public void setGenerateToString(boolean generateToString) {
        myState.generateToString = generateToString;
    }

    public void setGenerateFluentSetters(boolean generateFluentSetters) {
        myState.generateFluentSetters = generateFluentSetters;
    }

    public void setToStringStyle(String toStringStyle) {
        myState.toStringStyle = toStringStyle;
    }

    public boolean isUseEnglish() {
        return myState.useEnglish;
    }

    public void setUseEnglish(boolean useEnglish) {
        myState.useEnglish = useEnglish;
    }

    // 内部类设置的getter和setter方法
    public boolean isProcessInnerClasses() {
        return myState.processInnerClasses;
    }

    public void setProcessInnerClasses(boolean processInnerClasses) {
        myState.processInnerClasses = processInnerClasses;
    }

    public boolean isGenerateInnerClassSeparator() {
        return myState.generateInnerClassSeparator;
    }

    public void setGenerateInnerClassSeparator(boolean generateInnerClassSeparator) {
        myState.generateInnerClassSeparator = generateInnerClassSeparator;
    }

    public int getMaxInnerClassDepth() {
        return myState.maxInnerClassDepth;
    }

    public void setMaxInnerClassDepth(int maxInnerClassDepth) {
        myState.maxInnerClassDepth = maxInnerClassDepth;
    }

    // DTO/VO/BO生成设置的getter和setter方法
    public boolean isUseBeanUtilsForConversion() {
        return myState.useBeanUtilsForConversion;
    }

    public void setUseBeanUtilsForConversion(boolean useBeanUtilsForConversion) {
        myState.useBeanUtilsForConversion = useBeanUtilsForConversion;
    }

    public String getBeanUtilsClass() {
        return myState.beanUtilsClass;
    }

    public void setBeanUtilsClass(String beanUtilsClass) {
        myState.beanUtilsClass = beanUtilsClass;
    }

    public boolean isGenerateSerialAnnotation() {
        return myState.generateSerialAnnotation;
    }

    public void setGenerateSerialAnnotation(boolean generateSerialAnnotation) {
        myState.generateSerialAnnotation = generateSerialAnnotation;
    }

    // 枚举类parse方法设置的getter和setter方法
    public String getEnumParseMethodName() {
        return myState.enumParseMethodName;
    }

    public void setEnumParseMethodName(String enumParseMethodName) {
        myState.enumParseMethodName = enumParseMethodName;
    }

    public String getEnumCodeFieldName() {
        return myState.enumCodeFieldName;
    }

    public void setEnumCodeFieldName(String enumCodeFieldName) {
        myState.enumCodeFieldName = enumCodeFieldName;
    }
}
