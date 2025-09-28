package com.glowxq.plugs.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

/**
 * 测试Action，用于验证插件是否正常工作
 */
public class TestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Messages.showInfoMessage("插件正常工作！", "测试");
    }

    @Override
    public void update(AnActionEvent e) {
        // 始终启用，用于测试
        e.getPresentation().setEnabled(true);
        e.getPresentation().setVisible(true);
    }
}
