package com.glowxq.plugs.ui;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

/**
 * OneClick插件概览面板
 * 显示插件功能介绍和快捷键说明
 */
public class PluginOverviewPanel {

    private final JPanel myMainPanel;

    public PluginOverviewPanel() {
        myMainPanel = createMainPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(JBUI.Borders.empty(10));

        // 创建标题
        JBLabel titleLabel = new JBLabel("<html><b>OneClick - Smart Code Generator</b></html>");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        panel.add(titleLabel, BorderLayout.NORTH);

        // 创建内容面板
        JPanel contentPanel = createContentPanel();
        JBScrollPane scrollPane = new JBScrollPane(contentPanel);
        scrollPane.setBorder(JBUI.Borders.empty());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createContentPanel() {
        FormBuilder builder = FormBuilder.createFormBuilder();

        // 核心功能介绍
        builder.addComponent(createSectionLabel("核心功能 / Core Features"));
        builder.addComponent(createFeatureDescription());
        builder.addVerticalGap(15);

        // 快捷键说明
        builder.addComponent(createSectionLabel("快捷键 / Shortcut"));
        builder.addComponent(createShortcutDescription());
        builder.addVerticalGap(15);

        // 使用场景
        builder.addComponent(createSectionLabel("使用场景 / Usage Scenarios"));
        builder.addComponent(createUsageScenarios());

        return builder.getPanel();
    }

    private JBLabel createSectionLabel(String text) {
        JBLabel label = new JBLabel("<html><b>" + text + "</b></html>");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 13f));
        return label;
    }

    private JPanel createFeatureDescription() {
        String osModifier = SystemInfo.isMac ? "Cmd" : "Ctrl";
        String description = "<html><div style='width: 600px; line-height: 1.6;'>" +
            "使用 <b>" + osModifier + "+Shift+D</b> 快捷键，智能识别场景并自动执行相应操作：<br><br>" +
            "• <b>选中变量名</b>：循环切换命名风格（小驼峰→大驼峰→下划线小写→下划线大写）<br>" +
            "• <b>选中字符串</b>：自动生成常量字段<br>" +
            "• <b>选中类名</b>：生成 DTO/VO/BO 类<br>" +
            "• <b>JavaBean类</b>：自动生成 getter/setter/toString 方法（JSON格式）<br>" +
            "• <b>枚举类</b>：自动生成 parse 方法，根据 code 字段解析枚举值<br><br>" +
            "所有功能都通过同一个快捷键完成，简单高效。</div></html>";

        JPanel panel = new JPanel(new BorderLayout());
        JBLabel label = new JBLabel(description);
        label.setBorder(JBUI.Borders.empty(5, 10));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createShortcutDescription() {
        String osModifier = SystemInfo.isMac ? "Cmd" : "Ctrl";
        String shortcut = osModifier + "+Shift+D";
        
        String description = "<html><div style='width: 600px; line-height: 1.6;'>" +
            "<b style='font-size: 16px; color: #0066CC;'>" + shortcut + "</b><br><br>" +
            "在Java类或枚举类中按此快捷键即可自动生成相应代码。</div></html>";

        JPanel panel = new JPanel(new BorderLayout());
        JBLabel label = new JBLabel(description);
        label.setBorder(JBUI.Borders.empty(5, 10));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createUsageScenarios() {
        String osModifier = SystemInfo.isMac ? "Cmd" : "Ctrl";
        String[] scenarios = {
            "<b>场景1：变量名命名风格转换</b><br>" +
            "选中变量名（如 userName）按 " + osModifier + "+Shift+D，循环切换：userName → UserName → user_name → USER_NAME",
            
            "<b>场景2：生成常量字段</b><br>" +
            "选中字符串字面量（如 \"USER_NOT_FOUND\"）按 " + osModifier + "+Shift+D，自动生成常量字段",
            
            "<b>场景3：生成DTO/VO/BO类</b><br>" +
            "选中类名（如 User）按 " + osModifier + "+Shift+D，选择类型后自动生成对应的数据传输对象类",
            
            "<b>场景4：生成JavaBean方法</b><br>" +
            "在包含私有字段的Java类中按 " + osModifier + "+Shift+D，自动生成 getter/setter/toString 方法（toString为JSON格式）",
            
            "<b>场景5：生成枚举类parse方法</b><br>" +
            "在包含 code 字段的枚举类中按 " + osModifier + "+Shift+D，自动生成 parse 方法用于根据 code 值解析枚举"
        };

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (String scenario : scenarios) {
            JBLabel label = new JBLabel("<html><div style='width: 600px; line-height: 1.8; margin-bottom: 8px;'>" + scenario + "</div></html>");
            label.setBorder(JBUI.Borders.empty(3, 10));
            panel.add(label);
        }

        return panel;
    }





}
