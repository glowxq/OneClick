package com.glowxq.plugs.settings;

import com.glowxq.plugs.utils.I18nUtils;
import com.intellij.openapi.ui.Messages;
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
        
        JButton resetButton = new JButton(I18nUtils.message("settings.keymap.reset"));
        resetButton.addActionListener(e -> resetToDefaults());
        
        JButton validateButton = new JButton(I18nUtils.message("settings.keymap.validate"));
        validateButton.addActionListener(e -> validateShortcuts());
        
        JButton helpButton = new JButton(I18nUtils.message("settings.keymap.help"));
        helpButton.addActionListener(e -> showHelp());
        
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
        String osInfo = I18nUtils.message("settings.keymap.current.os", osName);
        
        if (osName.equals("macOS")) {
            osInfo += "\n" + I18nUtils.message("settings.keymap.mac.note");
        } else {
            osInfo += "\n" + I18nUtils.message("settings.keymap.windows.note");
        }
        
        osInfoLabel.setText("<html>" + osInfo.replace("\n", "<br>") + "</html>");
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
}
