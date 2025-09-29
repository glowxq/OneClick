package com.glowxq.plugs.actions;

import com.glowxq.plugs.utils.I18nUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * 显示OneClick插件概览页面的Action
 */
public class ShowOverviewAction extends AnAction {

    public ShowOverviewAction() {
        super(I18nUtils.message("action.show.overview.text"),
              I18nUtils.message("action.show.overview.description"),
              null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            ShowSettingsUtil.getInstance().showSettingsDialog(
                project, 
                "OneClick Overview"
            );
        }
    }
}
