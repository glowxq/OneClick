package com.glowxq.plugs.settings;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

/**
 * 快捷键设置UI组件
 * 
 * @author glowxq
 */
public class KeymapSettingsComponent {

    private final JPanel myMainPanel;
    
    // 操作系统信息
    private final JBLabel osInfoLabel = new JBLabel();
    
    public KeymapSettingsComponent() {
        myMainPanel = createMainPanel();
        updateOSInfo();
    }

    private JPanel createMainPanel() {
        FormBuilder builder = FormBuilder.createFormBuilder();
        
        // 快捷键说明
        builder.addComponent(createShortcutDescription());
        builder.addVerticalGap(10);
        
        // 操作系统信息
        builder.addComponent(createOSInfoPanel());
        
        builder.addComponentFillVertically(new JPanel(), 0);
        
        return builder.getPanel();
    }

    private JPanel createShortcutDescription() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(JBUI.Borders.compound(
                JBUI.Borders.customLine(Color.GRAY, 1, 0, 0, 0),
                JBUI.Borders.empty(10)
        ));
        
        JBLabel titleLabel = new JBLabel("快捷键说明");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        String osModifier = SystemInfo.isMac ? "Cmd" : "Ctrl";
        String shortcut = osModifier + "+Shift+D";
        
        String description = "<html><div style='width: 500px; line-height: 1.6; padding: 10px;'>" +
            "<b style='font-size: 18px; color: #0066CC;'>" + shortcut + "</b><br><br>" +
            "这是插件的唯一快捷键，用于智能代码生成：<br><br>" +
            "• <b>JavaBean类</b>：生成 getter/setter/toString 方法<br>" +
            "• <b>枚举类</b>：生成 parse 方法<br><br>" +
            "在Java类或枚举类中按此快捷键即可自动生成相应代码。</div></html>";
        
        JBLabel descLabel = new JBLabel(description);
        panel.add(descLabel, BorderLayout.CENTER);
        
        return panel;
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
}
