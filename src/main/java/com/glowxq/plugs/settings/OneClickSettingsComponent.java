package com.glowxq.plugs.settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

/**
 * OneClick插件设置UI组件
 */
public class OneClickSettingsComponent {

    private final JPanel myMainPanel;
    
    // JavaBean相关设置
    private final JBCheckBox generateSeparatorComment = new JBCheckBox("Generate separator comment");
    private final JBCheckBox generateGetterSetter = new JBCheckBox("Generate getter/setter methods");
    private final JBCheckBox generateToString = new JBCheckBox("Generate toString method");
    private final JBCheckBox generateEquals = new JBCheckBox("Generate equals method");
    private final JBCheckBox generateHashCode = new JBCheckBox("Generate hashCode method");
    
    // 业务类相关设置
    private final JBCheckBox generateLogger = new JBCheckBox("Generate logger field for business classes");
    private final JBTextField loggerFieldName = new JBTextField();
    private final JComboBox<String> loggerType = new JComboBox<>(new String[]{"slf4j", "log4j", "jul"});
    
    // 通用设置
    private final JBCheckBox autoDetectClassType = new JBCheckBox("Auto detect class type (JavaBean vs Business)");
    private final JBCheckBox useFieldComments = new JBCheckBox("Use field comments in generated methods");
    private final JBCheckBox generateSerialVersionUID = new JBCheckBox("Generate serialVersionUID for Serializable classes");
    
    // 代码风格设置
    private final JBCheckBox useBuilderPattern = new JBCheckBox("Generate Builder pattern");
    private final JBCheckBox generateFluentSetters = new JBCheckBox("Generate fluent setters (return this)");
    private final JComboBox<String> toStringStyle = new JComboBox<>(new String[]{"json", "simple", "apache"});

    public OneClickSettingsComponent() {
        // 设置默认值
        generateSeparatorComment.setSelected(true);
        generateGetterSetter.setSelected(true);
        generateToString.setSelected(true);
        generateLogger.setSelected(true);
        loggerFieldName.setText("LOGGER");
        loggerType.setSelectedItem("slf4j");
        autoDetectClassType.setSelected(true);
        toStringStyle.setSelectedItem("json");

        // 创建面板布局
        myMainPanel = FormBuilder.createFormBuilder()
                .addComponent(createTitledPanel("JavaBean Generation Settings", createJavaBeanPanel()))
                .addVerticalGap(10)
                .addComponent(createTitledPanel("Business Class Settings", createBusinessClassPanel()))
                .addVerticalGap(10)
                .addComponent(createTitledPanel("General Settings", createGeneralPanel()))
                .addVerticalGap(10)
                .addComponent(createTitledPanel("Code Style Settings", createCodeStylePanel()))
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    private JPanel createTitledPanel(String title, JPanel content) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(JBUI.Borders.compound(
                JBUI.Borders.customLine(Color.GRAY, 1, 0, 0, 0),
                JBUI.Borders.empty(10)
        ));
        
        JBLabel titleLabel = new JBLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createJavaBeanPanel() {
        return FormBuilder.createFormBuilder()
                .addComponent(generateSeparatorComment)
                .addComponent(generateGetterSetter)
                .addComponent(generateToString)
                .addComponent(generateEquals)
                .addComponent(generateHashCode)
                .getPanel();
    }

    private JPanel createBusinessClassPanel() {
        return FormBuilder.createFormBuilder()
                .addComponent(generateLogger)
                .addLabeledComponent(new JBLabel("Logger field name:"), loggerFieldName)
                .addLabeledComponent(new JBLabel("Logger type:"), loggerType)
                .getPanel();
    }

    private JPanel createGeneralPanel() {
        return FormBuilder.createFormBuilder()
                .addComponent(autoDetectClassType)
                .addComponent(useFieldComments)
                .addComponent(generateSerialVersionUID)
                .getPanel();
    }

    private JPanel createCodeStylePanel() {
        return FormBuilder.createFormBuilder()
                .addComponent(useBuilderPattern)
                .addComponent(generateFluentSetters)
                .addLabeledComponent(new JBLabel("ToString style:"), toStringStyle)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return generateSeparatorComment;
    }

    // Getter方法
    public boolean isGenerateSeparatorComment() {
        return generateSeparatorComment.isSelected();
    }

    public boolean isGenerateGetterSetter() {
        return generateGetterSetter.isSelected();
    }

    public boolean isGenerateToString() {
        return generateToString.isSelected();
    }

    public boolean isGenerateEquals() {
        return generateEquals.isSelected();
    }

    public boolean isGenerateHashCode() {
        return generateHashCode.isSelected();
    }

    public boolean isGenerateLogger() {
        return generateLogger.isSelected();
    }

    public String getLoggerFieldName() {
        return loggerFieldName.getText();
    }

    public String getLoggerType() {
        return (String) loggerType.getSelectedItem();
    }

    public boolean isAutoDetectClassType() {
        return autoDetectClassType.isSelected();
    }

    public boolean isUseFieldComments() {
        return useFieldComments.isSelected();
    }

    public boolean isGenerateSerialVersionUID() {
        return generateSerialVersionUID.isSelected();
    }

    public boolean isUseBuilderPattern() {
        return useBuilderPattern.isSelected();
    }

    public boolean isGenerateFluentSetters() {
        return generateFluentSetters.isSelected();
    }

    public String getToStringStyle() {
        return (String) toStringStyle.getSelectedItem();
    }

    // Setter方法
    public void setGenerateSeparatorComment(boolean selected) {
        generateSeparatorComment.setSelected(selected);
    }

    public void setGenerateGetterSetter(boolean selected) {
        generateGetterSetter.setSelected(selected);
    }

    public void setGenerateToString(boolean selected) {
        generateToString.setSelected(selected);
    }

    public void setGenerateEquals(boolean selected) {
        generateEquals.setSelected(selected);
    }

    public void setGenerateHashCode(boolean selected) {
        generateHashCode.setSelected(selected);
    }

    public void setGenerateLogger(boolean selected) {
        generateLogger.setSelected(selected);
    }

    public void setLoggerFieldName(String text) {
        loggerFieldName.setText(text);
    }

    public void setLoggerType(String type) {
        loggerType.setSelectedItem(type);
    }

    public void setAutoDetectClassType(boolean selected) {
        autoDetectClassType.setSelected(selected);
    }

    public void setUseFieldComments(boolean selected) {
        useFieldComments.setSelected(selected);
    }

    public void setGenerateSerialVersionUID(boolean selected) {
        generateSerialVersionUID.setSelected(selected);
    }

    public void setUseBuilderPattern(boolean selected) {
        useBuilderPattern.setSelected(selected);
    }

    public void setGenerateFluentSetters(boolean selected) {
        generateFluentSetters.setSelected(selected);
    }

    public void setToStringStyle(String style) {
        toStringStyle.setSelectedItem(style);
    }
}
