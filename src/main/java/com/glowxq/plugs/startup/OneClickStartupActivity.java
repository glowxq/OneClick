package com.glowxq.plugs.startup;

import com.glowxq.plugs.utils.I18nUtils;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;

/**
 * OneClick插件启动活动
 * 在项目启动时显示插件功能介绍
 */
public class OneClickStartupActivity implements StartupActivity {

    private static final String NOTIFICATION_GROUP_ID = "OneClick Plugin";

    private static final String SHOWN_KEY = "oneclick.welcome.shown";

    @Override
    public void runActivity(@NotNull Project project) {
        // 检查是否已经显示过欢迎通知
        String shownVersion = System.getProperty(SHOWN_KEY);
        String currentVersion = "1.0.0";
        
        if (!currentVersion.equals(shownVersion)) {
            ApplicationManager.getApplication().invokeLater(() -> {
                showWelcomeNotification(project);
                System.setProperty(SHOWN_KEY, currentVersion);
            });
        }
    }

    private void showWelcomeNotification(Project project) {
        String osModifier = SystemInfo.isMac ? "Cmd" : "Ctrl";
        
        String content = String.format(
            "<html>" +
            "<h3>🎉 OneClick 插件已安装成功！</h3>" +
            "<p><b>快速开始：</b></p>" +
            "<ul>" +
            "<li>🎯 智能一键生成：<code>%s+Alt+G</code></li>" +
            "<li>📦 批量生成：<code>%s+Alt+B</code></li>" +
            "<li>🔧 代码模板：<code>%s+Alt+T</code></li>" +
            "<li>⚙️ 更多功能请查看设置面板</li>" +
            "</ul>" +
            "<p><i>点击下方按钮查看完整功能介绍</i></p>" +
            "</html>",
            osModifier, osModifier, osModifier
        );

        Notification notification = NotificationGroupManager.getInstance()
            .getNotificationGroup(NOTIFICATION_GROUP_ID)
            .createNotification(
                "OneClick - 智能代码生成器",
                content,
                NotificationType.INFORMATION
            );

        // 添加查看设置的动作
        notification.addAction(new AnAction("查看功能介绍") {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                ShowSettingsUtil.getInstance().showSettingsDialog(
                    project, 
                    "OneClick Overview"
                );
                notification.expire();
            }
        });

        // 添加快速设置的动作
        notification.addAction(new AnAction("打开设置") {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                ShowSettingsUtil.getInstance().showSettingsDialog(
                    project, 
                    "OneClick Settings"
                );
                notification.expire();
            }
        });

        // 添加关闭动作
        notification.addAction(new AnAction("知道了") {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                notification.expire();
            }
        });

        Notifications.Bus.notify(notification, project);
    }
}
