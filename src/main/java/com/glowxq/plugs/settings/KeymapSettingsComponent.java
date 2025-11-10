package com.glowxq.plugs.settings;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 快捷键设置UI组件
 * 
 * @author glowxq
 */
public class KeymapSettingsComponent {

    private final JPanel myMainPanel;
    
    // 快捷键输入框
    private final JBTextField shortcutField = new JBTextField(30);
    
    // 验证结果标签
    private final JBLabel validationLabel = new JBLabel();
    
    // 操作系统信息
    private final JBLabel osInfoLabel = new JBLabel();
    
    public KeymapSettingsComponent() {
        myMainPanel = createMainPanel();
        updateOSInfo();
        setupShortcutField();
        loadDefaultShortcut();
    }

    private JPanel createMainPanel() {
        FormBuilder builder = FormBuilder.createFormBuilder();
        
        // 快捷键设置
        builder.addComponent(createShortcutSettingsPanel());
        builder.addVerticalGap(10);
        
        // 操作系统信息
        builder.addComponent(createOSInfoPanel());
        
        builder.addComponentFillVertically(new JPanel(), 0);
        
        return builder.getPanel();
    }

    private JPanel createShortcutSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(JBUI.Borders.compound(
                JBUI.Borders.customLine(Color.GRAY, 1, 0, 0, 0),
                JBUI.Borders.empty(10)
        ));
        
        JBLabel titleLabel = new JBLabel("快捷键设置");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 13f));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(JBUI.Borders.empty(10, 0));
        
        // 说明文字
        String osModifier = SystemInfo.isMac ? "Cmd" : "Ctrl";
        String defaultShortcut = osModifier + "+Shift+D";
        JBLabel descLabel = new JBLabel("<html><div style='width: 500px; line-height: 1.6; margin-bottom: 10px;'>" +
            "自定义智能代码生成快捷键（默认：" + defaultShortcut + "）：<br><br>" +
            "• 支持格式：Ctrl+Shift+D 或 Cmd+Shift+D<br>" +
            "• 必须包含修饰键（Ctrl/Cmd/Alt/Shift）<br>" +
            "• 输入后按回车或失去焦点时自动验证</div></html>");
        contentPanel.add(descLabel, BorderLayout.NORTH);
        
        // 输入框和验证标签
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JBLabel("快捷键："), BorderLayout.WEST);
        inputPanel.add(shortcutField, BorderLayout.CENTER);
        inputPanel.add(validationLabel, BorderLayout.EAST);
        inputPanel.setBorder(JBUI.Borders.empty(5, 0));
        
        contentPanel.add(inputPanel, BorderLayout.CENTER);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupShortcutField() {
        // 添加焦点监听器，失去焦点时验证
        shortcutField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // 获得焦点时清空验证信息
                validationLabel.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                validateShortcut();
            }
        });
        
        // 添加键盘监听器，回车时验证
        shortcutField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    validateShortcut();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }
    
    public void loadDefaultShortcut() {
        KeymapSettings settings = KeymapSettings.getInstance();
        String shortcut = settings.getShortcutForCurrentOS("smartOneClick");
        if (shortcut == null || shortcut.isEmpty()) {
            String osModifier = SystemInfo.isMac ? "Cmd" : "Ctrl";
            shortcut = osModifier + "+Shift+D";
        }
        shortcutField.setText(shortcut);
    }
    
    private void validateShortcut() {
        String shortcut = shortcutField.getText().trim();
        
        if (shortcut.isEmpty()) {
            validationLabel.setText("<html><span style='color: red;'>❌ 快捷键不能为空</span></html>");
            return;
        }
        
        // 验证快捷键格式
        if (!isValidShortcutFormat(shortcut)) {
            validationLabel.setText("<html><span style='color: red;'>❌ 格式错误，示例：Ctrl+Shift+D</span></html>");
            return;
        }
        
        // 验证快捷键是否冲突（这里简化处理，实际应该检查IDEA的快捷键映射）
        validationLabel.setText("<html><span style='color: green;'>✅ 格式正确</span></html>");
    }
    
    private boolean isValidShortcutFormat(String shortcut) {
        // 基本格式验证：必须包含至少一个修饰键和一个主键
        // 支持的修饰键：Ctrl, Cmd, Alt, Shift
        // 主键：字母、数字、功能键等
        
        String upper = shortcut.toUpperCase();
        
        // 检查是否包含修饰键
        boolean hasModifier = upper.contains("CTRL") || upper.contains("CMD") || 
                             upper.contains("ALT") || upper.contains("SHIFT");
        
        if (!hasModifier) {
            return false;
        }
        
        // 检查是否包含主键（字母、数字或功能键）
        // 移除所有修饰键和+号，检查剩余部分
        String remaining = upper.replaceAll("CTRL|CMD|ALT|SHIFT|\\+|\\s", "");
        
        // 剩余部分应该是单个字符（字母、数字）或功能键名
        return !remaining.isEmpty() && remaining.length() <= 10; // 功能键名通常较短
    }

    private JPanel createOSInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(JBUI.Borders.compound(
                JBUI.Borders.customLine(Color.GRAY, 1, 0, 0, 0),
                JBUI.Borders.empty(10)
        ));
        
        JBLabel titleLabel = new JBLabel("操作系统信息");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        panel.add(osInfoLabel, BorderLayout.CENTER);
        
        return panel;
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

    public JPanel getPanel() {
        return myMainPanel;
    }
    
    public String getShortcut() {
        return shortcutField.getText().trim();
    }
    
    public void setShortcut(String shortcut) {
        shortcutField.setText(shortcut);
        validateShortcut();
    }
    
    public boolean isShortcutValid() {
        String shortcut = getShortcut();
        return !shortcut.isEmpty() && isValidShortcutFormat(shortcut);
    }
}
