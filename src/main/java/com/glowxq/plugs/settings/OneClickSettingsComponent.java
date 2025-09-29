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
    private final JBCheckBox useEnglish = new JBCheckBox();
    
    // JavaBean相关设置
    private final JBCheckBox generateSeparatorComment = new JBCheckBox();
    private final JBCheckBox generateGetterSetter = new JBCheckBox();
    private final JBCheckBox generateToString = new JBCheckBox();
    private final JBCheckBox generateEquals = new JBCheckBox();
    private final JBCheckBox generateHashCode = new JBCheckBox();
    
    // 业务类相关设置
    private final JBCheckBox generateLogger = new JBCheckBox();
    private final JBTextField loggerFieldName = new JBTextField();
    private final JComboBox<String> loggerType = new JComboBox<>(new String[]{"slf4j", "log4j", "jul"});

    // 通用设置
    private final JBCheckBox autoDetectClassType = new JBCheckBox();
    private final JBCheckBox useFieldComments = new JBCheckBox();
    private final JBCheckBox generateSerialVersionUID = new JBCheckBox();

    // 代码风格设置
    private final JBCheckBox useBuilderPattern = new JBCheckBox();
    private final JBCheckBox generateFluentSetters = new JBCheckBox();
    private final JComboBox<String> toStringStyle = new JComboBox<>(new String[]{"json", "simple", "apache"});

    // 内部类设置
    private final JBCheckBox processInnerClasses = new JBCheckBox();
    private final JBCheckBox generateInnerClassSeparator = new JBCheckBox();
    private final JSpinner maxInnerClassDepth = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));

    // JavaBean包规则设置
    private final JBCheckBox enablePackageDetection = new JBCheckBox();
    private final JBTextField javaBeanPackagePatterns = new JBTextField();
    private final JBTextField businessClassPackagePatterns = new JBTextField();

    // 字段排序设置
    private final JBCheckBox enableFieldSorting = new JBCheckBox();
    private final JComboBox<String> fieldSortType = new JComboBox<>(new String[]{"NAME", "LENGTH", "TYPE"});
    private final JBCheckBox sortAscending = new JBCheckBox();

    public OneClickSettingsComponent() {
        // 初始化文本
        updateTexts();

        // 设置默认值
        generateSeparatorComment.setSelected(true);
        generateGetterSetter.setSelected(true);
        generateToString.setSelected(true);
        generateLogger.setSelected(true);
        loggerFieldName.setText("LOGGER");
        loggerType.setSelectedItem("slf4j");
        autoDetectClassType.setSelected(true);
        toStringStyle.setSelectedItem("json");

        // 包规则设置默认值
        enablePackageDetection.setSelected(true);
        javaBeanPackagePatterns.setText("entity,model,bean,pojo,dto,vo,domain,data");
        businessClassPackagePatterns.setText("service,controller,manager,handler,component,config,util");

        // 字段排序设置默认值（仅对业务类生效）
        enableFieldSorting.setSelected(true); // 默认启用
        fieldSortType.setSelectedItem("NAME");
        sortAscending.setSelected(true);

        // 添加语言切换监听器
        useEnglish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 更新设置
                OneClickSettings.getInstance().setUseEnglish(useEnglish.isSelected());
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
        // 语言设置
        useEnglish.setText(I18nUtils.message("settings.language.english"));

        // JavaBean设置
        generateSeparatorComment.setText(I18nUtils.message("settings.javabean.separator.comment"));
        generateGetterSetter.setText(I18nUtils.message("settings.javabean.getter.setter"));
        generateToString.setText(I18nUtils.message("settings.javabean.tostring"));
        generateEquals.setText(I18nUtils.message("settings.javabean.equals"));
        generateHashCode.setText(I18nUtils.message("settings.javabean.hashcode"));

        // 业务类设置
        generateLogger.setText(I18nUtils.message("settings.business.logger"));

        // 通用设置
        autoDetectClassType.setText(I18nUtils.message("settings.general.auto.detect"));
        useFieldComments.setText(I18nUtils.message("settings.general.field.comments"));
        generateSerialVersionUID.setText(I18nUtils.message("settings.general.serial.version"));

        // 代码风格设置
        useBuilderPattern.setText(I18nUtils.message("settings.style.builder.pattern"));
        generateFluentSetters.setText(I18nUtils.message("settings.style.fluent.setters"));

        // 内部类设置
        processInnerClasses.setText(I18nUtils.message("settings.inner.class.process"));
        generateInnerClassSeparator.setText(I18nUtils.message("settings.inner.class.separator"));

        // 包规则设置
        enablePackageDetection.setText(I18nUtils.message("settings.package.detection.enable"));

        // 字段排序设置（仅对业务类生效）
        enableFieldSorting.setText(I18nUtils.message("settings.field.sorting.enable.business"));
        sortAscending.setText(I18nUtils.message("settings.field.sorting.ascending"));
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
                .addComponent(createTitledPanel(I18nUtils.getBusinessSettingsTitle(), createBusinessClassPanel()))
                .addVerticalGap(10)
                .addComponent(createTitledPanel(I18nUtils.getGeneralSettingsTitle(), createGeneralPanel()))
                .addVerticalGap(10)
                .addComponent(createTitledPanel(I18nUtils.getStyleSettingsTitle(), createCodeStylePanel()))
                .addVerticalGap(10)
                .addComponent(createTitledPanel(I18nUtils.message("settings.inner.class.title"), createInnerClassPanel()))
                .addVerticalGap(10)
                .addComponent(createTitledPanel(I18nUtils.message("settings.package.rules.title"), createPackageRulesPanel()))
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
        contentPanel.add(useEnglish);
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
                .addComponent(generateEquals)
                .addComponent(generateHashCode)
                .getPanel();
    }

    private JPanel createBusinessClassPanel() {
        return FormBuilder.createFormBuilder()
                .addComponent(generateLogger)
                .addLabeledComponent(new JBLabel("Logger field name:"), loggerFieldName)
                .addLabeledComponent(new JBLabel("Logger type:"), loggerType)
                .addSeparator()
                .addComponent(enableFieldSorting)
                .addLabeledComponent(new JBLabel(I18nUtils.message("settings.field.sorting.type")), fieldSortType)
                .addComponent(sortAscending)
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

    private JPanel createInnerClassPanel() {
        return FormBuilder.createFormBuilder()
                .addComponent(processInnerClasses)
                .addComponent(generateInnerClassSeparator)
                .addLabeledComponent(new JBLabel(I18nUtils.message("settings.inner.class.depth")), maxInnerClassDepth)
                .getPanel();
    }

    /**
     * 创建包规则设置面板
     */
    private JPanel createPackageRulesPanel() {
        return FormBuilder.createFormBuilder()
                .addComponent(enablePackageDetection)
                .addLabeledComponent(new JBLabel(I18nUtils.message("settings.package.javabean.patterns")), javaBeanPackagePatterns)
                .addLabeledComponent(new JBLabel(I18nUtils.message("settings.package.business.patterns")), businessClassPackagePatterns)
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

    public boolean isUseEnglish() {
        return useEnglish.isSelected();
    }

    public void setUseEnglish(boolean selected) {
        useEnglish.setSelected(selected);
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

    // 包规则设置的getter和setter方法
    public boolean isEnablePackageDetection() {
        return enablePackageDetection.isSelected();
    }

    public void setEnablePackageDetection(boolean selected) {
        enablePackageDetection.setSelected(selected);
    }

    public String getJavaBeanPackagePatterns() {
        return javaBeanPackagePatterns.getText();
    }

    public void setJavaBeanPackagePatterns(String patterns) {
        javaBeanPackagePatterns.setText(patterns);
    }

    public String getBusinessClassPackagePatterns() {
        return businessClassPackagePatterns.getText();
    }

    public void setBusinessClassPackagePatterns(String patterns) {
        businessClassPackagePatterns.setText(patterns);
    }

    // 字段排序设置的getter和setter方法
    public boolean isEnableFieldSorting() {
        return enableFieldSorting.isSelected();
    }

    public void setEnableFieldSorting(boolean selected) {
        enableFieldSorting.setSelected(selected);
    }

    public String getFieldSortType() {
        return (String) fieldSortType.getSelectedItem();
    }

    public void setFieldSortType(String sortType) {
        fieldSortType.setSelectedItem(sortType);
    }

    public boolean isSortAscending() {
        return sortAscending.isSelected();
    }

    public void setSortAscending(boolean selected) {
        sortAscending.setSelected(selected);
    }
}
