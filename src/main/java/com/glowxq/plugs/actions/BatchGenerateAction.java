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
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile[] selectedFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);

        if (project == null || selectedFiles == null || selectedFiles.length == 0) {
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
        // 使用FileBasedIndex来高效查找Java文件
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        Collection<VirtualFile> files = FileBasedIndex.getInstance()
            .getContainingFiles(FileTypeIndex.NAME, JavaFileType.INSTANCE, scope);
        
        for (VirtualFile file : files) {
            if (isUnderDirectory(file, directory)) {
                javaFiles.add(file);
            }
        }
    }

    private boolean isUnderDirectory(VirtualFile file, VirtualFile directory) {
        VirtualFile parent = file.getParent();
        while (parent != null) {
            if (parent.equals(directory)) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
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
     * 为单个类生成JavaBean方法（简化版本）
     */
    private void generateJavaBeanMethodsForClass(PsiClass psiClass, OneClickSettings settings) throws Exception {
        Project project = psiClass.getProject();
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
        List<PsiField> fields = JavaBeanUtils.getInstanceFields(psiClass);

        if (fields.isEmpty()) {
            return;
        }

        // 移除现有的JavaBean方法
        List<PsiMethod> existingMethods = JavaBeanUtils.getAllJavaBeanMethods(psiClass, fields);
        for (PsiMethod method : existingMethods) {
            method.delete();
        }

        // 移除现有的分割注释
        if (settings.isGenerateSeparatorComment()) {
            JavaBeanUtils.removeSeparatorComment(psiClass);
        }

        // 找到插入点
        PsiElement insertionPoint = JavaBeanUtils.findInsertionPoint(psiClass, fields);
        PsiElement lastInserted = insertionPoint;

        // 添加分割注释
        if (settings.isGenerateSeparatorComment()) {
            List<PsiMethod> businessMethods = JavaBeanUtils.getBusinessMethods(psiClass, fields);
            if (!businessMethods.isEmpty()) {
                PsiElement commentElement = JavaBeanUtils.createFormattedSeparatorComment(factory, psiClass);
                lastInserted = JavaBeanUtils.insertCommentAfter(psiClass, commentElement, lastInserted);
            }
        }

        // 生成getter和setter方法
        if (settings.isGenerateGetterSetter()) {
            for (PsiField field : fields) {
                // 生成getter
                String getterCode = JavaBeanUtils.generateGetterCode(field);
                PsiMethod getterMethod = factory.createMethodFromText(getterCode, psiClass);
                lastInserted = JavaBeanUtils.insertAfter(psiClass, getterMethod, lastInserted);

                // 生成setter
                String setterCode = settings.isGenerateFluentSetters()
                    ? JavaBeanUtils.generateFluentSetterCode(field, psiClass)
                    : JavaBeanUtils.generateSetterCode(field);
                PsiMethod setterMethod = factory.createMethodFromText(setterCode, psiClass);
                lastInserted = JavaBeanUtils.insertAfter(psiClass, setterMethod, lastInserted);
            }
        }

        // 生成toString方法
        if (settings.isGenerateToString()) {
            String toStringCode;
            switch (settings.getToStringStyle()) {
                case "simple":
                    toStringCode = JavaBeanUtils.generateSimpleToStringCode(psiClass);
                    break;
                case "apache":
                    toStringCode = JavaBeanUtils.generateApacheToStringCode(psiClass);
                    break;
                default:
                    toStringCode = JavaBeanUtils.generateToStringCode(psiClass);
                    break;
            }
            PsiMethod toStringMethod = factory.createMethodFromText(toStringCode, psiClass);
            JavaBeanUtils.insertAfter(psiClass, toStringMethod, lastInserted);
        }
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

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile[] selectedFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        
        boolean enabled = project != null && selectedFiles != null && selectedFiles.length > 0;
        
        // 检查是否至少有一个Java文件或包含Java文件的目录
        if (enabled) {
            enabled = hasJavaFilesOrDirectories(selectedFiles);
        }
        
        e.getPresentation().setEnabledAndVisible(enabled);
        
        // 动态更新Action文本
        if (enabled && selectedFiles != null) {
            if (selectedFiles.length == 1) {
                if (selectedFiles[0].isDirectory()) {
                    e.getPresentation().setText(I18nUtils.message("action.batch.package.text"));
                } else {
                    e.getPresentation().setText(I18nUtils.message("action.batch.file.text"));
                }
            } else {
                e.getPresentation().setText(I18nUtils.message("action.batch.files.text", selectedFiles.length));
            }
        }
    }

    private boolean hasJavaFilesOrDirectories(VirtualFile[] files) {
        for (VirtualFile file : files) {
            if (file.isDirectory() || isJavaFile(file)) {
                return true;
            }
        }
        return false;
    }
}
