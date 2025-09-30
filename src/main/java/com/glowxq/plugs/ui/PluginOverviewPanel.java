package com.glowxq.plugs.ui;

import com.glowxq.plugs.utils.I18nUtils;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        JBLabel titleLabel = new JBLabel("<html><h1>OneClick - Smart Code Generator 🚀</h1></html>");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
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

        // 插件介绍
        builder.addComponent(createSectionLabel("Core Features / 核心功能"));
        builder.addComponent(createFeatureList());
        builder.addVerticalGap(15);

        // 完整快捷键列表
        builder.addComponent(createSectionLabel("Keyboard Shortcuts / 快捷键列表"));
        builder.addComponent(createShortcutTable());
        builder.addVerticalGap(15);

        // 使用方法
        builder.addComponent(createSectionLabel("Quick Start / 快速开始"));
        builder.addComponent(createUsageInstructions());

        return builder.getPanel();
    }

    private JBLabel createSectionLabel(String text) {
        JBLabel label = new JBLabel("<html><h2>" + text + "</h2></html>");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        return label;
    }

    private JPanel createFeatureList() {
        String[] features = {
            "🎯 Smart One-Click Generate / 智能一键生成",
            "📦 JavaBean Methods / JavaBean方法生成",
            "🏗️ DTO/VO/BO Generator / 数据对象生成",
            "🛠️ Developer Tools / 开发工具集",
            "🗄️ Database Tools / 数据库工具",
            "📝 Code Templates / 代码模板",
            "🔄 Refactor Assistant / 重构助手",
            "🧹 Code Cleanup / 代码清理",
            "📊 Code Analysis / 代码分析"
        };

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (String feature : features) {
            JBLabel label = new JBLabel("<html><div style='width: 500px;'>• " + feature + "</div></html>");
            label.setBorder(JBUI.Borders.empty(3, 10));
            panel.add(label);
        }

        return panel;
    }



    private JPanel createShortcutTable() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = JBUI.insets(2, 5);
        gbc.anchor = GridBagConstraints.WEST;

        String osModifier = SystemInfo.isMac ? "Cmd" : "Ctrl";
        String[][] shortcuts = {
            {I18nUtils.message("overview.shortcuts.function"), I18nUtils.message("overview.shortcuts.key"), I18nUtils.message("overview.shortcuts.description")},
            {I18nUtils.message("overview.shortcuts.smart"), osModifier + "+Shift+D", I18nUtils.message("overview.shortcuts.smart.desc")},
            {I18nUtils.message("overview.shortcuts.devtools"), osModifier + "+Shift+U", I18nUtils.message("overview.shortcuts.devtools.desc")},
            {I18nUtils.message("overview.shortcuts.database"), osModifier + "+Shift+Y", I18nUtils.message("overview.shortcuts.database.desc")},
            {I18nUtils.message("overview.shortcuts.batch"), osModifier + "+Shift+B", I18nUtils.message("overview.shortcuts.batch.desc")},
            {I18nUtils.message("overview.shortcuts.template"), osModifier + "+Shift+T", I18nUtils.message("overview.shortcuts.template.desc")},
            {I18nUtils.message("overview.shortcuts.refactor"), osModifier + "+Shift+R", I18nUtils.message("overview.shortcuts.refactor.desc")},
            {I18nUtils.message("overview.shortcuts.comment"), osModifier + "+Shift+C", I18nUtils.message("overview.shortcuts.comment.desc")},
            {I18nUtils.message("overview.shortcuts.cleanup"), osModifier + "+Shift+L", I18nUtils.message("overview.shortcuts.cleanup.desc")},
            {I18nUtils.message("overview.shortcuts.analysis"), osModifier + "+Shift+A", I18nUtils.message("overview.shortcuts.analysis.desc")},
            {I18nUtils.message("overview.shortcuts.quickdoc"), osModifier + "+Shift+Q", I18nUtils.message("overview.shortcuts.quickdoc.desc")},
            {I18nUtils.message("overview.shortcuts.fold"), osModifier + "+Shift+F", I18nUtils.message("overview.shortcuts.fold.desc")}
        };

        for (int i = 0; i < shortcuts.length; i++) {
            for (int j = 0; j < shortcuts[i].length; j++) {
                gbc.gridx = j;
                gbc.gridy = i;
                
                JBLabel label = new JBLabel(shortcuts[i][j]);
                if (i == 0) { // 表头
                    label.setFont(label.getFont().deriveFont(Font.BOLD));
                }
                if (j == 1 && i > 0) { // 快捷键列
                    label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                    label.setForeground(new Color(0, 100, 200));
                }
                
                panel.add(label, gbc);
            }
        }

        return panel;
    }

    private JPanel createUsageInstructions() {
        String[] instructions = {
            "1. Right-click in Java class → \"OneClick\" menu / 在Java类中右键 → \"OneClick\"菜单",
            "2. Use keyboard shortcuts for quick access / 使用快捷键快速访问功能",
            "3. Settings: File → Settings → Tools → OneClick / 设置面板配置"
        };

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (String instruction : instructions) {
            JBLabel label = new JBLabel("<html><div style='width: 600px;'>" + instruction + "</div></html>");
            label.setBorder(JBUI.Borders.empty(2, 10));
            panel.add(label);
        }

        return panel;
    }


}
