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
        public boolean generateEquals = false;
        public boolean generateHashCode = false;
        
        // 业务类相关设置
        public boolean generateLogger = true;
        public String loggerFieldName = "LOGGER";
        public String loggerType = "slf4j"; // slf4j, log4j, jul
        
        // 通用设置
        public boolean autoDetectClassType = true;
        public boolean useFieldComments = true;
        public boolean generateSerialVersionUID = false;
        
        // 代码风格设置
        public boolean useBuilderPattern = false;
        public boolean generateFluentSetters = false;
        public String toStringStyle = "json"; // json, simple, apache
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

    public boolean isGenerateEquals() {
        return myState.generateEquals;
    }

    public boolean isGenerateHashCode() {
        return myState.generateHashCode;
    }

    public boolean isGenerateLogger() {
        return myState.generateLogger;
    }

    public String getLoggerFieldName() {
        return myState.loggerFieldName;
    }

    public String getLoggerType() {
        return myState.loggerType;
    }

    public boolean isAutoDetectClassType() {
        return myState.autoDetectClassType;
    }

    public boolean isUseFieldComments() {
        return myState.useFieldComments;
    }

    public boolean isGenerateSerialVersionUID() {
        return myState.generateSerialVersionUID;
    }

    public boolean isUseBuilderPattern() {
        return myState.useBuilderPattern;
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

    public void setGenerateEquals(boolean generateEquals) {
        myState.generateEquals = generateEquals;
    }

    public void setGenerateHashCode(boolean generateHashCode) {
        myState.generateHashCode = generateHashCode;
    }

    public void setGenerateLogger(boolean generateLogger) {
        myState.generateLogger = generateLogger;
    }

    public void setLoggerFieldName(String loggerFieldName) {
        myState.loggerFieldName = loggerFieldName;
    }

    public void setLoggerType(String loggerType) {
        myState.loggerType = loggerType;
    }

    public void setAutoDetectClassType(boolean autoDetectClassType) {
        myState.autoDetectClassType = autoDetectClassType;
    }

    public void setUseFieldComments(boolean useFieldComments) {
        myState.useFieldComments = useFieldComments;
    }

    public void setGenerateSerialVersionUID(boolean generateSerialVersionUID) {
        myState.generateSerialVersionUID = generateSerialVersionUID;
    }

    public void setUseBuilderPattern(boolean useBuilderPattern) {
        myState.useBuilderPattern = useBuilderPattern;
    }

    public void setGenerateFluentSetters(boolean generateFluentSetters) {
        myState.generateFluentSetters = generateFluentSetters;
    }

    public void setToStringStyle(String toStringStyle) {
        myState.toStringStyle = toStringStyle;
    }
}
