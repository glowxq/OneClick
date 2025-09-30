package com.glowxq.plugs.settings;

import com.glowxq.plugs.utils.I18nUtils;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * 快捷键设置UI组件
 * 
 * @author glowxq
 */
public class KeymapSettingsComponent {

    private final JPanel myMainPanel;
    
    // 设置选项
    private final JBCheckBox enableShortcuts = new JBCheckBox();
    private final JBCheckBox showShortcutHints = new JBCheckBox();
    private final JBCheckBox detectConflicts = new JBCheckBox();
    
    // 快捷键输入框
    private final Map<String, JBTextField> shortcutFields = new HashMap<>();
    
    // 操作系统信息
    private final JBLabel osInfoLabel = new JBLabel();
    
    public KeymapSettingsComponent() {
        initializeTexts();
        initializeShortcutFields();
        myMainPanel = createMainPanel();
        setupEventListeners();
        updateOSInfo();
        addSmartShortcutDescription();
    }

    private void initializeTexts() {
        enableShortcuts.setText(I18nUtils.message("settings.keymap.enable"));
        showShortcutHints.setText(I18nUtils.message("settings.keymap.show.hints"));
        detectConflicts.setText(I18nUtils.message("settings.keymap.detect.conflicts"));
    }

    private void initializeShortcutFields() {
        shortcutFields.put("generateJavaBean", new JBTextField(20));
        shortcutFields.put("foldJavaBean", new JBTextField(20));
        shortcutFields.put("generateTemplate", new JBTextField(20));
        shortcutFields.put("refactorAssistant", new JBTextField(20));
        shortcutFields.put("smartComment", new JBTextField(20));
        shortcutFields.put("batchGenerate", new JBTextField(20));
        shortcutFields.put("codeCleanup", new JBTextField(20));
        shortcutFields.put("codeAnalysis", new JBTextField(20));
        shortcutFields.put("quickDoc", new JBTextField(20));
        shortcutFields.put("quickFix", new JBTextField(20));
    }

    private JPanel createMainPanel() {
        FormBuilder builder = FormBuilder.createFormBuilder();
        
        // 操作系统信息
        builder.addComponent(createOSInfoPanel());
        builder.addVerticalGap(10);
        
        // 基本设置
        builder.addComponent(createBasicSettingsPanel());
        builder.addVerticalGap(10);
        
        // 快捷键设置
        builder.addComponent(createShortcutPanel());
        builder.addVerticalGap(10);
        
        // 操作按钮
        builder.addComponent(createButtonPanel());
        
        builder.addComponentFillVertically(new JPanel(), 0);
        
        return builder.getPanel();
    }

    private JPanel createOSInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(JBUI.Borders.compound(
                JBUI.Borders.customLine(Color.GRAY, 1, 0, 0, 0),
                JBUI.Borders.empty(10)
        ));
        
        JBLabel titleLabel = new JBLabel(I18nUtils.message("settings.keymap.os.info"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        panel.add(osInfoLabel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createBasicSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(JBUI.Borders.compound(
                JBUI.Borders.customLine(Color.GRAY, 1, 0, 0, 0),
                JBUI.Borders.empty(10)
        ));
        
        JBLabel titleLabel = new JBLabel(I18nUtils.message("settings.keymap.basic"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(enableShortcuts);
        contentPanel.add(showShortcutHints);
        contentPanel.add(detectConflicts);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createShortcutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(JBUI.Borders.compound(
                JBUI.Borders.customLine(Color.GRAY, 1, 0, 0, 0),
                JBUI.Borders.empty(10)
        ));
        
        JBLabel titleLabel = new JBLabel(I18nUtils.message("settings.keymap.shortcuts"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = JBUI.insets(5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // 生成JavaBean方法
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(new JBLabel(I18nUtils.message("settings.keymap.generate.javabean")), gbc);
        gbc.gridx = 1;
        contentPanel.add(shortcutFields.get("generateJavaBean"), gbc);
        row++;
        
        // 折叠JavaBean方法
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(new JBLabel(I18nUtils.message("settings.keymap.fold.javabean")), gbc);
        gbc.gridx = 1;
        contentPanel.add(shortcutFields.get("foldJavaBean"), gbc);
        row++;
        
        // 生成代码模板
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(new JBLabel(I18nUtils.message("settings.keymap.generate.template")), gbc);
        gbc.gridx = 1;
        contentPanel.add(shortcutFields.get("generateTemplate"), gbc);
        row++;
        
        // 重构助手
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(new JBLabel(I18nUtils.message("settings.keymap.refactor.assistant")), gbc);
        gbc.gridx = 1;
        contentPanel.add(shortcutFields.get("refactorAssistant"), gbc);
        row++;
        
        // 智能注释
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(new JBLabel(I18nUtils.message("settings.keymap.smart.comment")), gbc);
        gbc.gridx = 1;
        contentPanel.add(shortcutFields.get("smartComment"), gbc);
        row++;

        // 批量生成
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(new JBLabel(I18nUtils.message("settings.keymap.batch.generate")), gbc);
        gbc.gridx = 1;
        contentPanel.add(shortcutFields.get("batchGenerate"), gbc);
        row++;

        // 代码清理
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(new JBLabel(I18nUtils.message("settings.keymap.code.cleanup")), gbc);
        gbc.gridx = 1;
        contentPanel.add(shortcutFields.get("codeCleanup"), gbc);
        row++;

        // 代码分析
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(new JBLabel(I18nUtils.message("settings.keymap.code.analysis")), gbc);
        gbc.gridx = 1;
        contentPanel.add(shortcutFields.get("codeAnalysis"), gbc);
        row++;

        // 快速文档
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(new JBLabel(I18nUtils.message("settings.keymap.quick.doc")), gbc);
        gbc.gridx = 1;
        contentPanel.add(shortcutFields.get("quickDoc"), gbc);
        row++;

        // 快速修复
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(new JBLabel(I18nUtils.message("settings.keymap.quick.fix")), gbc);
        gbc.gridx = 1;
        contentPanel.add(shortcutFields.get("quickFix"), gbc);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton presetButton = new JButton("快捷键预设");
        presetButton.addActionListener(e -> showPresetDialog());

        JButton resetButton = new JButton(I18nUtils.message("settings.keymap.reset"));
        resetButton.addActionListener(e -> resetToDefaults());

        JButton validateButton = new JButton(I18nUtils.message("settings.keymap.validate"));
        validateButton.addActionListener(e -> validateShortcuts());

        JButton helpButton = new JButton(I18nUtils.message("settings.keymap.help"));
        helpButton.addActionListener(e -> showHelp());

        panel.add(presetButton);
        panel.add(resetButton);
        panel.add(validateButton);
        panel.add(helpButton);

        return panel;
    }

    private void setupEventListeners() {
        // 启用/禁用快捷键时更新UI状态
        enableShortcuts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean enabled = enableShortcuts.isSelected();
                shortcutFields.values().forEach(field -> field.setEnabled(enabled));
            }
        });
    }

    private void updateOSInfo() {
        String osName = KeymapSettings.getOSName();
        String modifier = SystemInfo.isMac ? "Cmd" : "Ctrl";
        String altKey = SystemInfo.isMac ? "Option" : "Alt";

        String osInfo = "<b>当前操作系统</b>: " + osName + "<br>";
        osInfo += "<b>修饰键</b>: " + modifier + " (主要修饰键)<br>";
        osInfo += "<b>Alt键</b>: " + altKey + " (辅助修饰键)<br><br>";

        if (osName.equals("macOS")) {
            osInfo += "<i>💡 macOS提示: Cmd相当于Windows的Ctrl，Option相当于Windows的Alt</i>";
        } else {
            osInfo += "<i>💡 Windows/Linux提示: 使用Ctrl作为主修饰键，Alt作为辅助修饰键</i>";
        }

        osInfoLabel.setText("<html><div style='width: 400px;'>" + osInfo + "</div></html>");
    }

    private void resetToDefaults() {
        int result = Messages.showYesNoDialog(
            I18nUtils.message("settings.keymap.reset.confirm"),
            I18nUtils.message("settings.keymap.reset"),
            Messages.getQuestionIcon()
        );
        
        if (result == Messages.YES) {
            KeymapSettings settings = KeymapSettings.getInstance();
            settings.resetToDefaults();
            loadSettings(settings);
            
            Messages.showInfoMessage(
                I18nUtils.message("settings.keymap.reset.success"),
                I18nUtils.message("settings.keymap.reset")
            );
        }
    }

    private void validateShortcuts() {
        StringBuilder errors = new StringBuilder();
        
        for (Map.Entry<String, JBTextField> entry : shortcutFields.entrySet()) {
            String shortcut = entry.getValue().getText().trim();
            if (!shortcut.isEmpty() && !KeymapSettings.isValidShortcut(shortcut)) {
                String actionName = I18nUtils.message("settings.keymap." + entry.getKey().toLowerCase());
                errors.append(I18nUtils.message("settings.keymap.invalid.shortcut", actionName, shortcut)).append("\n");
            }
        }
        
        if (errors.length() > 0) {
            Messages.showErrorDialog(
                errors.toString(),
                I18nUtils.message("settings.keymap.validation.failed")
            );
        } else {
            Messages.showInfoMessage(
                I18nUtils.message("settings.keymap.validation.success"),
                I18nUtils.message("settings.keymap.validate")
            );
        }
    }

    private void showHelp() {
        String helpText = I18nUtils.message("settings.keymap.help.text");
        Messages.showInfoMessage(
            helpText,
            I18nUtils.message("settings.keymap.help")
        );
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public void loadSettings(@NotNull KeymapSettings settings) {
        enableShortcuts.setSelected(settings.isEnableShortcuts());
        showShortcutHints.setSelected(settings.isShowShortcutHints());
        detectConflicts.setSelected(settings.isDetectConflicts());
        
        // 加载快捷键
        for (Map.Entry<String, JBTextField> entry : shortcutFields.entrySet()) {
            String shortcut = settings.getShortcutForCurrentOS(entry.getKey());
            entry.getValue().setText(shortcut != null ? shortcut : "");
        }
        
        // 更新UI状态
        boolean enabled = enableShortcuts.isSelected();
        shortcutFields.values().forEach(field -> field.setEnabled(enabled));
    }

    public void saveSettings(@NotNull KeymapSettings settings) {
        settings.setEnableShortcuts(enableShortcuts.isSelected());
        settings.setShowShortcutHints(showShortcutHints.isSelected());
        settings.setDetectConflicts(detectConflicts.isSelected());
        
        // 保存快捷键
        for (Map.Entry<String, JBTextField> entry : shortcutFields.entrySet()) {
            String shortcut = entry.getValue().getText().trim();
            if (!shortcut.isEmpty()) {
                settings.setShortcutForCurrentOS(entry.getKey(), shortcut);
            }
        }
    }

    public boolean isModified(@NotNull KeymapSettings settings) {
        if (enableShortcuts.isSelected() != settings.isEnableShortcuts()) return true;
        if (showShortcutHints.isSelected() != settings.isShowShortcutHints()) return true;
        if (detectConflicts.isSelected() != settings.isDetectConflicts()) return true;
        
        // 检查快捷键是否修改
        for (Map.Entry<String, JBTextField> entry : shortcutFields.entrySet()) {
            String currentShortcut = entry.getValue().getText().trim();
            String savedShortcut = settings.getShortcutForCurrentOS(entry.getKey());
            if (!currentShortcut.equals(savedShortcut != null ? savedShortcut : "")) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 添加智能快捷键说明
     */
    private void addSmartShortcutDescription() {
        // 创建说明面板
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
        descPanel.setBorder(JBUI.Borders.compound(
                JBUI.Borders.customLine(new Color(200, 200, 200), 1),
                JBUI.Borders.empty(10)
        ));

        String osModifier = SystemInfo.isMac ? "Cmd" : "Ctrl";

        JBLabel titleLabel = new JBLabel("<html><h3>🚀 " + I18nUtils.message("settings.keymap.smart.title") + "</h3></html>");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String descText = I18nUtils.message("settings.keymap.smart.description", osModifier, osModifier, osModifier, osModifier, osModifier);
        JBLabel descLabel = new JBLabel("<html><div style='width: 450px;'>" + descText + "</div></html>");
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        descPanel.add(titleLabel);
        descPanel.add(Box.createVerticalStrut(5));
        descPanel.add(descLabel);

        // 将说明面板插入到主面板的顶部
        myMainPanel.add(descPanel, 0);
    }

    /**
     * 显示快捷键预设对话框
     */
    private void showPresetDialog() {
        String[] presets = {
            I18nUtils.message("settings.keymap.preset.default"),
            I18nUtils.message("settings.keymap.preset.vscode"),
            I18nUtils.message("settings.keymap.preset.eclipse"),
            I18nUtils.message("settings.keymap.preset.custom")
        };

        int selectedIndex = Messages.showChooseDialog(
            I18nUtils.message("settings.keymap.preset.choose"),
            I18nUtils.message("settings.keymap.preset.title"),
            presets,
            presets[0],
            Messages.getQuestionIcon()
        );

        String selected = selectedIndex >= 0 ? presets[selectedIndex] : null;

        if (selected != null) {
            applyPreset(selected);
        }
    }

    /**
     * 应用快捷键预设
     */
    private void applyPreset(String presetName) {
        Map<String, String> shortcuts = new HashMap<>();

        String defaultPreset = I18nUtils.message("settings.keymap.preset.default");
        String vscodePreset = I18nUtils.message("settings.keymap.preset.vscode");
        String eclipsePreset = I18nUtils.message("settings.keymap.preset.eclipse");

        if (presetName.equals(defaultPreset)) {
                shortcuts.put("generateJavaBean", SystemInfo.isMac ? "meta shift D" : "ctrl shift D");
                shortcuts.put("foldJavaBean", SystemInfo.isMac ? "meta shift F" : "ctrl shift F");
                shortcuts.put("batchGenerate", SystemInfo.isMac ? "meta shift B" : "ctrl shift B");
                shortcuts.put("generateTemplate", SystemInfo.isMac ? "meta shift T" : "ctrl shift T");
                shortcuts.put("refactorAssistant", SystemInfo.isMac ? "meta shift R" : "ctrl shift R");
                shortcuts.put("smartComment", SystemInfo.isMac ? "meta shift C" : "ctrl shift C");
                shortcuts.put("codeCleanup", SystemInfo.isMac ? "meta shift L" : "ctrl shift L");
                shortcuts.put("codeAnalysis", SystemInfo.isMac ? "meta shift A" : "ctrl shift A");
                shortcuts.put("quickDoc", SystemInfo.isMac ? "meta shift Q" : "ctrl shift Q");
        } else if (presetName.equals(vscodePreset)) {
                shortcuts.put("generateJavaBean", SystemInfo.isMac ? "meta shift D" : "ctrl shift D");
                shortcuts.put("foldJavaBean", SystemInfo.isMac ? "meta K meta 0" : "ctrl K ctrl 0");
                shortcuts.put("batchGenerate", SystemInfo.isMac ? "meta shift B" : "ctrl shift B");
                shortcuts.put("generateTemplate", SystemInfo.isMac ? "meta shift T" : "ctrl shift T");
                shortcuts.put("refactorAssistant", SystemInfo.isMac ? "meta shift R" : "ctrl shift R");
                shortcuts.put("smartComment", SystemInfo.isMac ? "meta SLASH" : "ctrl SLASH");
                shortcuts.put("codeCleanup", SystemInfo.isMac ? "meta shift L" : "ctrl shift L");
                shortcuts.put("codeAnalysis", SystemInfo.isMac ? "meta shift A" : "ctrl shift A");
                shortcuts.put("quickDoc", SystemInfo.isMac ? "meta shift Q" : "ctrl shift Q");
        } else if (presetName.equals(eclipsePreset)) {
                shortcuts.put("generateJavaBean", SystemInfo.isMac ? "meta shift D" : "ctrl shift D");
                shortcuts.put("foldJavaBean", SystemInfo.isMac ? "meta MINUS" : "ctrl MINUS");
                shortcuts.put("batchGenerate", SystemInfo.isMac ? "meta shift B" : "ctrl shift B");
                shortcuts.put("generateTemplate", SystemInfo.isMac ? "meta shift T" : "ctrl shift T");
                shortcuts.put("refactorAssistant", SystemInfo.isMac ? "meta shift R" : "ctrl shift R");
                shortcuts.put("smartComment", SystemInfo.isMac ? "meta shift C" : "ctrl shift C");
                shortcuts.put("codeCleanup", SystemInfo.isMac ? "meta shift L" : "ctrl shift L");
                shortcuts.put("codeAnalysis", SystemInfo.isMac ? "meta shift A" : "ctrl shift A");
                shortcuts.put("quickDoc", SystemInfo.isMac ? "meta shift Q" : "ctrl shift Q");
        } else {
            return; // 自定义预设不做任何操作
        }

        // 应用快捷键设置
        for (Map.Entry<String, String> entry : shortcuts.entrySet()) {
            JBTextField field = shortcutFields.get(entry.getKey());
            if (field != null) {
                field.setText(entry.getValue());
            }
        }

        Messages.showInfoMessage(
            I18nUtils.message("settings.keymap.preset.applied", presetName),
            I18nUtils.message("settings.keymap.preset.success")
        );
    }
}
