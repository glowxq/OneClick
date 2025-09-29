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
        JBLabel titleLabel = new JBLabel("<html><h1>OneClick - 智能代码生成器 🚀</h1></html>");
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
        builder.addComponent(createSectionLabel("功能概览"));
        builder.addComponent(createFeatureList());
        builder.addVerticalGap(15);

        // 快捷键说明
        builder.addComponent(createSectionLabel("快捷键说明"));
        builder.addComponent(createShortcutTable());
        builder.addVerticalGap(15);

        // 使用方法
        builder.addComponent(createSectionLabel("使用方法"));
        builder.addComponent(createUsageInstructions());
        builder.addVerticalGap(15);

        // 代码模板
        builder.addComponent(createSectionLabel("代码模板库"));
        builder.addComponent(createTemplateList());

        return builder.getPanel();
    }

    private JBLabel createSectionLabel(String text) {
        JBLabel label = new JBLabel("<html><h2>" + text + "</h2></html>");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        return label;
    }

    private JPanel createFeatureList() {
        String[] features = {
            "🎯 JavaBean 方法生成<br>&nbsp;&nbsp;&nbsp;&nbsp;getter/setter/toString/equals/hashCode",
            "🔧 代码模板生成器<br>&nbsp;&nbsp;&nbsp;&nbsp;15种设计模式和架构模板",
            "📦 批量生成功能<br>&nbsp;&nbsp;&nbsp;&nbsp;支持选中包或多个文件进行批量处理",
            "🔄 代码重构助手<br>&nbsp;&nbsp;&nbsp;&nbsp;10种重构操作",
            "💬 智能注释生成<br>&nbsp;&nbsp;&nbsp;&nbsp;根据代码上下文自动生成注释",
            "🧹 代码清理助手<br>&nbsp;&nbsp;&nbsp;&nbsp;移除未使用导入、空行、调试代码等",
            "📊 代码分析工具<br>&nbsp;&nbsp;&nbsp;&nbsp;详细统计分析和质量检测",
            "📝 快速文档生成<br>&nbsp;&nbsp;&nbsp;&nbsp;自动生成标准JavaDoc文档",
            "⚙️ 高度可定制<br>&nbsp;&nbsp;&nbsp;&nbsp;快捷键自定义、多语言支持、灵活配置"
        };

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (String feature : features) {
            JBLabel label = new JBLabel("<html><div style='width: 400px;'>• " + feature + "</div></html>");
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
            {"功能", "快捷键", "说明"},
            {"智能一键生成", osModifier + "+Alt+G", "智能生成适合的方法"},
            {"批量生成", osModifier + "+Alt+B", "批量处理多个文件"},
            {"代码模板", osModifier + "+Alt+T", "15种设计模式模板"},
            {"重构助手", osModifier + "+Alt+R", "10种重构操作"},
            {"智能注释", osModifier + "+Alt+C", "自动生成注释"},
            {"代码清理", osModifier + "+Alt+L", "清理冗余代码"},
            {"代码分析", osModifier + "+Alt+A", "统计分析代码"},
            {"快速文档", osModifier + "+Alt+D", "生成JavaDoc"},
            {"折叠方法", osModifier + "+Alt+F", "折叠JavaBean方法"}
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
            "1. 右键菜单 - 在Java类中右键选择 \"JavaBean Tools\"",
            "2. 项目视图 - 选中包或文件右键选择 \"Batch Generate\"",
            "3. 设置面板 - File → Settings → Tools → OneClick",
            "4. 快捷键 - 使用上述快捷键快速访问功能"
        };

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        for (String instruction : instructions) {
            JBLabel label = new JBLabel("<html>" + instruction + "</html>");
            label.setBorder(JBUI.Borders.empty(2, 10));
            panel.add(label);
        }

        return panel;
    }

    private JPanel createTemplateList() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String[] categories = {
            "<b>设计模式:</b> Singleton, Builder, Factory, Observer, Strategy",
            "<b>架构层:</b> REST Controller, Service Layer, Repository Layer, Exception Handler",
            "<b>工具类:</b> Validation Utils, Date Utils, String Utils, File Utils, JSON Utils, Test Class"
        };

        for (String category : categories) {
            JBLabel label = new JBLabel("<html>• " + category + "</html>");
            label.setBorder(JBUI.Borders.empty(2, 10));
            panel.add(label);
        }

        return panel;
    }
}
