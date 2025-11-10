package com.glowxq.plugs.settings;

import com.glowxq.plugs.utils.I18nUtils;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * OneClick插件设置UI组件
 * 支持中英双语切换
 */
public class OneClickSettingsComponent {

    private final JPanel myMainPanel;

    // 语言设置
    private final JComboBox<String> languageComboBox = new JComboBox<>(new String[]{"中文", "English"});
    
    // JavaBean相关设置
    private final JBCheckBox generateSeparatorComment = new JBCheckBox();
    private final JBCheckBox generateGetterSetter = new JBCheckBox();
    private final JBCheckBox generateToString = new JBCheckBox();
    
    // 代码风格设置（合并到JavaBean设置）
    private final JBCheckBox generateFluentSetters = new JBCheckBox();
    private final JComboBox<String> toStringStyle = new JComboBox<>(new String[]{"json", "simple", "apache"});

    // 内部类设置（合并到JavaBean设置）
    private final JBCheckBox processInnerClasses = new JBCheckBox();
    private final JBCheckBox generateInnerClassSeparator = new JBCheckBox();
    private final JSpinner maxInnerClassDepth = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));

    // DTO/VO/BO生成设置
    private final JBCheckBox useBeanUtilsForConversion = new JBCheckBox();
    private final JBTextField beanUtilsClass = new JBTextField();
    private final JBCheckBox generateSerialAnnotation = new JBCheckBox();

    // 枚举类parse方法设置
    private final JBTextField enumParseMethodName = new JBTextField();
    private final JBTextField enumCodeFieldName = new JBTextField();

    public OneClickSettingsComponent() {
        // 初始化文本
        updateTexts();

        // 设置默认值
        generateSeparatorComment.setSelected(true);
        generateGetterSetter.setSelected(true);
        generateToString.setSelected(true);
        toStringStyle.setSelectedItem("json");
        processInnerClasses.setSelected(true);

        // 枚举类parse方法设置默认值
        enumParseMethodName.setText("parse");
        enumCodeFieldName.setText("code");

        // 添加语言切换监听器
        languageComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 更新设置
                boolean useEnglish = languageComboBox.getSelectedIndex() == 1; // 1表示English
                OneClickSettings.getInstance().setUseEnglish(useEnglish);
                // 刷新语言
                I18nUtils.refreshLanguage();
                // 更新界面文本
                updateTexts();
            }
        });

        // 创建面板布局
        myMainPanel = createMainPanel();
    }

    /**
     * 更新界面文本（支持国际化）
     */
    private void updateTexts() {
        // 语言设置 - 下拉菜单不需要更新文本

        // JavaBean设置
        generateSeparatorComment.setText(I18nUtils.message("settings.javabean.separator.comment"));
        generateGetterSetter.setText(I18nUtils.message("settings.javabean.getter.setter"));
        generateToString.setText(I18nUtils.message("settings.javabean.tostring"));

        // 代码风格设置（合并到JavaBean设置）
        generateFluentSetters.setText(I18nUtils.message("settings.style.fluent.setters"));

        // 内部类设置（合并到JavaBean设置）
        processInnerClasses.setText(I18nUtils.message("settings.inner.class.process"));
        generateInnerClassSeparator.setText(I18nUtils.message("settings.inner.class.separator"));

        // DTO/VO/BO生成设置
        useBeanUtilsForConversion.setText(I18nUtils.message("settings.dto.use.beanutils"));
        beanUtilsClass.setToolTipText(I18nUtils.message("settings.dto.beanutils.class.tooltip"));
        generateSerialAnnotation.setText(I18nUtils.message("settings.dto.generate.serial.annotation"));
    }

    /**
     * 创建主面板
     */
    private JPanel createMainPanel() {
        return FormBuilder.createFormBuilder()
                .addComponent(createLanguagePanel())
                .addVerticalGap(10)
                .addComponent(createTitledPanel(I18nUtils.getJavaBeanSettingsTitle(), createJavaBeanPanel()))
                .addVerticalGap(10)
                .addComponent(createTitledPanel(I18nUtils.message("settings.enum.title"), createEnumPanel()))
                .addVerticalGap(10)
                .addComponent(createTitledPanel(I18nUtils.message("settings.dto.generation.title"), createDtoGenerationPanel()))
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    /**
     * 创建语言设置面板
     */
    private JPanel createLanguagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(JBUI.Borders.compound(
                JBUI.Borders.customLine(Color.GRAY, 1, 0, 0, 0),
                JBUI.Borders.empty(10)
        ));

        JBLabel titleLabel = new JBLabel(I18nUtils.message("settings.language"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contentPanel.add(languageComboBox);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
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
                .addSeparator()
                .addComponent(generateFluentSetters)
                .addLabeledComponent(new JBLabel("ToString style:"), toStringStyle)
                .addSeparator()
                .addComponent(processInnerClasses)
                .addComponent(generateInnerClassSeparator)
                .addLabeledComponent(new JBLabel(I18nUtils.message("settings.inner.class.depth")), maxInnerClassDepth)
                .getPanel();
    }

    /**
     * 创建枚举类设置面板
     */
    private JPanel createEnumPanel() {
        return FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel(I18nUtils.message("settings.enum.parse.method.name") + ":"), enumParseMethodName)
                .addLabeledComponent(new JBLabel(I18nUtils.message("settings.enum.code.field.name") + ":"), enumCodeFieldName)
                .getPanel();
    }

    private JPanel createDtoGenerationPanel() {
        return FormBuilder.createFormBuilder()
                .addComponent(useBeanUtilsForConversion)
                .addLabeledComponent(new JBLabel(I18nUtils.message("settings.dto.beanutils.class")), beanUtilsClass)
                .addComponent(generateSerialAnnotation)
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

    public void setGenerateFluentSetters(boolean selected) {
        generateFluentSetters.setSelected(selected);
    }

    public void setToStringStyle(String style) {
        toStringStyle.setSelectedItem(style);
    }

    public boolean isUseEnglish() {
        return languageComboBox.getSelectedIndex() == 1;
    }

    public void setUseEnglish(boolean selected) {
        languageComboBox.setSelectedIndex(selected ? 1 : 0);
    }

    // 内部类设置的getter和setter方法
    public boolean isProcessInnerClasses() {
        return processInnerClasses.isSelected();
    }

    public void setProcessInnerClasses(boolean selected) {
        processInnerClasses.setSelected(selected);
    }

    public boolean isGenerateInnerClassSeparator() {
        return generateInnerClassSeparator.isSelected();
    }

    public void setGenerateInnerClassSeparator(boolean selected) {
        generateInnerClassSeparator.setSelected(selected);
    }

    public int getMaxInnerClassDepth() {
        return (Integer) maxInnerClassDepth.getValue();
    }

    public void setMaxInnerClassDepth(int depth) {
        maxInnerClassDepth.setValue(depth);
    }

    // DTO/VO/BO生成设置的getter和setter方法
    public boolean isUseBeanUtilsForConversion() {
        return useBeanUtilsForConversion.isSelected();
    }

    public void setUseBeanUtilsForConversion(boolean selected) {
        useBeanUtilsForConversion.setSelected(selected);
    }

    public String getBeanUtilsClass() {
        return beanUtilsClass.getText();
    }

    public void setBeanUtilsClass(String className) {
        beanUtilsClass.setText(className);
    }

    public boolean isGenerateSerialAnnotation() {
        return generateSerialAnnotation.isSelected();
    }

    public void setGenerateSerialAnnotation(boolean selected) {
        generateSerialAnnotation.setSelected(selected);
    }

    // 枚举类parse方法设置的getter和setter方法
    public String getEnumParseMethodName() {
        return enumParseMethodName.getText();
    }

    public void setEnumParseMethodName(String methodName) {
        enumParseMethodName.setText(methodName);
    }

    public String getEnumCodeFieldName() {
        return enumCodeFieldName.getText();
    }

    public void setEnumCodeFieldName(String fieldName) {
        enumCodeFieldName.setText(fieldName);
    }
}
