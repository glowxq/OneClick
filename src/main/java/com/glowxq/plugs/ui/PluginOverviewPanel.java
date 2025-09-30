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

        // 智能快捷键说明
        builder.addComponent(createSectionLabel("智能快捷键说明"));
        builder.addComponent(createSmartShortcutDescription());
        builder.addVerticalGap(10);

        // 完整快捷键列表
        builder.addComponent(createSectionLabel("完整快捷键列表"));
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

    private JPanel createSmartShortcutDescription() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String osModifier = SystemInfo.isMac ? "Cmd" : "Ctrl";

        // 主要说明
        JBLabel mainDesc = new JBLabel("<html><div style='width: 500px;'><b>%s+Alt+G</b> 是智能一键快捷键。它会根据类的类型智能选择合适的生成操作：</div></html>".formatted(osModifier));
        mainDesc.setBorder(JBUI.Borders.empty(5, 10));
        panel.add(mainDesc);

        // JavaBean类说明
        JBLabel javaBeanDesc = new JBLabel("<html><div style='width: 500px;'>• <b>对于JavaBean类</b>：生成getter/setter/toString/equals/hashCode方法</div></html>".formatted());
        javaBeanDesc.setBorder(JBUI.Borders.empty(2, 20));
        panel.add(javaBeanDesc);

        // 业务类说明
        JBLabel businessDesc = new JBLabel("<html><div style='width: 500px;'>• <b>对于业务类</b>：生成Logger字段、serialVersionUID等</div></html>".formatted());
        businessDesc.setBorder(JBUI.Borders.empty(2, 20));
        panel.add(businessDesc);

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
