package com.glowxq.plugs.actions;

import com.glowxq.plugs.settings.OneClickSettings;
import com.glowxq.plugs.utils.I18nUtils;
import com.glowxq.plugs.utils.JavaBeanUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.ide.highlighter.JavaFileType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 批量生成JavaBean方法Action
 * 支持选中包或多个文件进行批量生成
 * 
 * @author glowxq
 */
public class BatchGenerateAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile[] selectedFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);

        // 只有在项目视图中选中文件或目录时才显示
        boolean visible = project != null && selectedFiles != null && selectedFiles.length > 0;
        e.getPresentation().setVisible(visible);
        e.getPresentation().setEnabled(visible);

        // 动态更新Action文本
        if (visible) {
            int fileCount = 0;
            int dirCount = 0;
            for (VirtualFile file : selectedFiles) {
                if (file.isDirectory()) {
                    dirCount++;
                } else {
                    fileCount++;
                }
            }

            String text;
            if (dirCount > 0 && fileCount > 0) {
                text = I18nUtils.message("action.batch.mixed", fileCount + dirCount);
            } else if (dirCount > 0) {
                text = I18nUtils.message("action.batch.packages", dirCount);
            } else {
                text = I18nUtils.message("action.batch.files", fileCount);
            }
            e.getPresentation().setText(text);
        }
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile[] selectedFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);

        if (project == null) {
            Messages.showErrorDialog("无法获取项目信息，请确保在项目中执行此操作", "错误");
            return;
        }

        if (selectedFiles == null || selectedFiles.length == 0) {
            Messages.showErrorDialog("请先选择要处理的文件或包", "错误");
            return;
        }

        // 收集所有需要处理的Java文件
        List<VirtualFile> javaFiles = collectJavaFiles(project, selectedFiles);

        if (javaFiles.isEmpty()) {
            Messages.showInfoMessage(project,
                I18nUtils.message("message.batch.no.java.files"),
                I18nUtils.message("action.batch.title"));
            return;
        }

        // 显示确认对话框
        int result = Messages.showYesNoDialog(project,
            I18nUtils.message("message.batch.confirm", javaFiles.size()),
            I18nUtils.message("action.batch.title"),
            Messages.getQuestionIcon());

        if (result != Messages.YES) {
            return;
        }

        // 使用进度条执行批量生成
        ProgressManager.getInstance().run(new Task.Backgroundable(project, 
            I18nUtils.message("action.batch.progress"), true) {
            
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                processBatchGeneration(project, javaFiles, indicator);
            }
        });
    }

    private List<VirtualFile> collectJavaFiles(Project project, VirtualFile[] selectedFiles) {
        List<VirtualFile> javaFiles = new ArrayList<>();
        
        for (VirtualFile file : selectedFiles) {
            if (file.isDirectory()) {
                // 如果是目录，递归收集所有Java文件
                collectJavaFilesFromDirectory(project, file, javaFiles);
            } else if (isJavaFile(file)) {
                // 如果是Java文件，直接添加
                javaFiles.add(file);
            }
        }
        
        return javaFiles;
    }

    private void collectJavaFilesFromDirectory(Project project, VirtualFile directory, List<VirtualFile> javaFiles) {
        // 递归遍历目录查找Java文件
        collectJavaFilesRecursively(directory, javaFiles);
    }

    private void collectJavaFilesRecursively(VirtualFile directory, List<VirtualFile> javaFiles) {
        if (directory == null || !directory.isDirectory()) {
            return;
        }

        VirtualFile[] children = directory.getChildren();
        if (children == null) {
            return;
        }

        for (VirtualFile child : children) {
            if (child.isDirectory()) {
                // 递归处理子目录
                collectJavaFilesRecursively(child, javaFiles);
            } else if (isJavaFile(child)) {
                // 添加Java文件
                javaFiles.add(child);
            }
        }
    }



    private boolean isJavaFile(VirtualFile file) {
        return file.getFileType() instanceof JavaFileType && file.getName().endsWith(".java");
    }

    private void processBatchGeneration(Project project, List<VirtualFile> javaFiles, ProgressIndicator indicator) {
        int totalFiles = javaFiles.size();
        int processedFiles = 0;
        int successCount = 0;
        int errorCount = 0;
        List<String> errorMessages = new ArrayList<>();

        PsiManager psiManager = PsiManager.getInstance(project);
        OneClickSettings settings = OneClickSettings.getInstance();

        for (VirtualFile file : javaFiles) {
            if (indicator.isCanceled()) {
                break;
            }

            indicator.setText(I18nUtils.message("message.batch.processing", file.getName()));
            indicator.setFraction((double) processedFiles / totalFiles);

            try {
                PsiFile psiFile = psiManager.findFile(file);
                if (psiFile instanceof PsiJavaFile) {
                    PsiJavaFile javaFile = (PsiJavaFile) psiFile;
                    
                    WriteCommandAction.runWriteCommandAction(project, () -> {
                        try {
                            processJavaFile(javaFile, settings);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    
                    successCount++;
                }
            } catch (Exception ex) {
                errorCount++;
                errorMessages.add(file.getName() + ": " + ex.getMessage());
            }

            processedFiles++;
        }

        // 显示结果
        showBatchResult(project, totalFiles, successCount, errorCount, errorMessages);
    }

    private void processJavaFile(PsiJavaFile javaFile, OneClickSettings settings) throws Exception {
        PsiClass[] classes = javaFile.getClasses();

        for (PsiClass psiClass : classes) {
            if (psiClass != null) {
                // 处理主类 - 使用现有的GenerateJavaBeanMethodsAction的逻辑
                generateJavaBeanMethodsForClass(psiClass, settings);

                // 如果设置中启用了内部类处理，则处理内部类
                if (settings.isProcessInnerClasses()) {
                    JavaBeanUtils.processInnerClasses(psiClass, settings);
                }
            }
        }
    }

    /**
     * 为单个类生成代码（使用智能一键生成逻辑）
     */
    private void generateJavaBeanMethodsForClass(PsiClass psiClass, OneClickSettings settings) throws Exception {
        // 使用智能一键生成的逻辑 - 调用GenerateJavaBeanMethodsAction的核心方法
        GenerateJavaBeanMethodsAction action = new GenerateJavaBeanMethodsAction();
        // 直接调用智能生成逻辑
        action.performSmartGeneration(psiClass.getProject(), psiClass);
    }

    private void showBatchResult(Project project, int totalFiles, int successCount, 
                                int errorCount, List<String> errorMessages) {
        StringBuilder message = new StringBuilder();
        message.append(I18nUtils.message("message.batch.result", totalFiles, successCount, errorCount));
        
        if (!errorMessages.isEmpty() && errorMessages.size() <= 5) {
            message.append("\n\n").append(I18nUtils.message("message.batch.errors")).append(":\n");
            for (String error : errorMessages) {
                message.append("• ").append(error).append("\n");
            }
        } else if (errorMessages.size() > 5) {
            message.append("\n\n").append(I18nUtils.message("message.batch.errors.count", errorMessages.size()));
        }

        if (errorCount > 0) {
            Messages.showWarningDialog(project, message.toString(), 
                I18nUtils.message("action.batch.title"));
        } else {
            Messages.showInfoMessage(project, message.toString(), 
                I18nUtils.message("action.batch.title"));
        }
    }
}
