package com.glowxq.plugs.actions;

import com.glowxq.plugs.utils.I18nUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 代码统计分析器
 * 提供详细的代码统计和分析功能
 * 
 * @author glowxq
 */
public class CodeAnalysisAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (project == null || editor == null || psiFile == null) {
            return;
        }

        // 执行代码分析
        CodeAnalysisResult result = analyzeCode(psiFile);
        
        // 显示分析结果
        showAnalysisResult(project, result);
    }

    private CodeAnalysisResult analyzeCode(PsiFile psiFile) {
        CodeAnalysisResult result = new CodeAnalysisResult();
        
        if (psiFile instanceof PsiJavaFile) {
            analyzeJavaFile((PsiJavaFile) psiFile, result);
        } else {
            analyzeGenericFile(psiFile, result);
        }
        
        return result;
    }

    private void analyzeJavaFile(PsiJavaFile javaFile, CodeAnalysisResult result) {
        // 基本统计
        result.fileName = javaFile.getName();
        result.fileType = "Java";
        result.totalLines = countLines(javaFile.getText());
        result.codeLines = countCodeLines(javaFile);
        result.commentLines = countCommentLines(javaFile);
        result.blankLines = result.totalLines - result.codeLines - result.commentLines;

        // Java特定统计
        Collection<PsiClass> classes = PsiTreeUtil.findChildrenOfType(javaFile, PsiClass.class);
        result.classCount = classes.size();
        
        Collection<PsiMethod> methods = PsiTreeUtil.findChildrenOfType(javaFile, PsiMethod.class);
        result.methodCount = methods.size();
        
        Collection<PsiField> fields = PsiTreeUtil.findChildrenOfType(javaFile, PsiField.class);
        result.fieldCount = fields.size();
        
        Collection<PsiLocalVariable> variables = PsiTreeUtil.findChildrenOfType(javaFile, PsiLocalVariable.class);
        result.variableCount = variables.size();

        // 导入统计
        PsiImportList importList = javaFile.getImportList();
        if (importList != null) {
            result.importCount = importList.getImportStatements().length;
        }

        // 复杂度分析
        result.cyclomaticComplexity = calculateCyclomaticComplexity(javaFile);
        
        // 方法长度分析
        result.methodLengthStats = analyzeMethodLengths(methods);
        
        // 类大小分析
        result.classSizeStats = analyzeClassSizes(classes);
        
        // 代码质量指标
        result.qualityMetrics = analyzeCodeQuality(javaFile);
    }

    private void analyzeGenericFile(PsiFile psiFile, CodeAnalysisResult result) {
        result.fileName = psiFile.getName();
        result.fileType = psiFile.getFileType().getName();
        result.totalLines = countLines(psiFile.getText());
        result.codeLines = result.totalLines; // 简化处理
        result.commentLines = 0;
        result.blankLines = 0;
    }

    private int countLines(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        return text.split("\n").length;
    }

    private int countCodeLines(PsiJavaFile javaFile) {
        // 简化的代码行统计
        String text = javaFile.getText();
        String[] lines = text.split("\n");
        int codeLines = 0;
        
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("//") && !trimmed.startsWith("/*") && !trimmed.equals("*/")) {
                codeLines++;
            }
        }
        
        return codeLines;
    }

    private int countCommentLines(PsiJavaFile javaFile) {
        Collection<PsiComment> comments = PsiTreeUtil.findChildrenOfType(javaFile, PsiComment.class);
        int commentLines = 0;
        
        for (PsiComment comment : comments) {
            commentLines += countLines(comment.getText());
        }
        
        return commentLines;
    }

    private int calculateCyclomaticComplexity(PsiJavaFile javaFile) {
        int complexity = 1; // 基础复杂度
        
        // 统计决策点
        Collection<PsiIfStatement> ifStatements = PsiTreeUtil.findChildrenOfType(javaFile, PsiIfStatement.class);
        complexity += ifStatements.size();
        
        Collection<PsiWhileStatement> whileStatements = PsiTreeUtil.findChildrenOfType(javaFile, PsiWhileStatement.class);
        complexity += whileStatements.size();
        
        Collection<PsiForStatement> forStatements = PsiTreeUtil.findChildrenOfType(javaFile, PsiForStatement.class);
        complexity += forStatements.size();
        
        Collection<PsiSwitchStatement> switchStatements = PsiTreeUtil.findChildrenOfType(javaFile, PsiSwitchStatement.class);
        for (PsiSwitchStatement switchStmt : switchStatements) {
            PsiCodeBlock body = switchStmt.getBody();
            if (body != null) {
                Collection<PsiSwitchLabelStatement> cases = PsiTreeUtil.findChildrenOfType(body, PsiSwitchLabelStatement.class);
                complexity += cases.size();
            }
        }
        
        return complexity;
    }

    private MethodLengthStats analyzeMethodLengths(Collection<PsiMethod> methods) {
        List<Integer> lengths = methods.stream()
            .map(this::getMethodLength)
            .collect(Collectors.toList());
        
        if (lengths.isEmpty()) {
            return new MethodLengthStats(0, 0, 0, 0);
        }
        
        int min = Collections.min(lengths);
        int max = Collections.max(lengths);
        double avg = lengths.stream().mapToInt(Integer::intValue).average().orElse(0);
        int total = lengths.size();
        
        return new MethodLengthStats(min, max, avg, total);
    }

    private ClassSizeStats analyzeClassSizes(Collection<PsiClass> classes) {
        List<Integer> sizes = classes.stream()
            .map(this::getClassSize)
            .collect(Collectors.toList());
        
        if (sizes.isEmpty()) {
            return new ClassSizeStats(0, 0, 0, 0);
        }
        
        int min = Collections.min(sizes);
        int max = Collections.max(sizes);
        double avg = sizes.stream().mapToInt(Integer::intValue).average().orElse(0);
        int total = sizes.size();
        
        return new ClassSizeStats(min, max, avg, total);
    }

    private QualityMetrics analyzeCodeQuality(PsiJavaFile javaFile) {
        QualityMetrics metrics = new QualityMetrics();
        
        // 统计长方法（超过20行）
        Collection<PsiMethod> methods = PsiTreeUtil.findChildrenOfType(javaFile, PsiMethod.class);
        metrics.longMethods = (int) methods.stream()
            .mapToInt(this::getMethodLength)
            .filter(length -> length > 20)
            .count();
        
        // 统计大类（超过200行）
        Collection<PsiClass> classes = PsiTreeUtil.findChildrenOfType(javaFile, PsiClass.class);
        metrics.largeClasses = (int) classes.stream()
            .mapToInt(this::getClassSize)
            .filter(size -> size > 200)
            .count();
        
        // 统计TODO注释
        Collection<PsiComment> comments = PsiTreeUtil.findChildrenOfType(javaFile, PsiComment.class);
        metrics.todoComments = (int) comments.stream()
            .filter(comment -> comment.getText().toLowerCase().contains("todo"))
            .count();
        
        // 统计空的catch块
        Collection<PsiCatchSection> catchSections = PsiTreeUtil.findChildrenOfType(javaFile, PsiCatchSection.class);
        metrics.emptyCatchBlocks = (int) catchSections.stream()
            .filter(this::isEmptyCatchBlock)
            .count();
        
        return metrics;
    }

    private int getMethodLength(PsiMethod method) {
        PsiCodeBlock body = method.getBody();
        if (body == null) {
            return 0;
        }
        return countLines(body.getText());
    }

    private int getClassSize(PsiClass psiClass) {
        return countLines(psiClass.getText());
    }

    private boolean isEmptyCatchBlock(PsiCatchSection catchSection) {
        PsiCodeBlock catchBody = catchSection.getCatchBlock();
        if (catchBody == null) {
            return true;
        }
        
        PsiStatement[] statements = catchBody.getStatements();
        return statements.length == 0;
    }

    private void showAnalysisResult(Project project, CodeAnalysisResult result) {
        StringBuilder message = new StringBuilder();
        message.append("📊 代码分析报告\n");
        message.append("═══════════════════════════════════\n\n");
        
        // 基本信息
        message.append("📁 文件信息:\n");
        message.append("  • 文件名: ").append(result.fileName).append("\n");
        message.append("  • 文件类型: ").append(result.fileType).append("\n\n");
        
        // 行数统计
        message.append("📏 行数统计:\n");
        message.append("  • 总行数: ").append(result.totalLines).append("\n");
        message.append("  • 代码行数: ").append(result.codeLines).append("\n");
        message.append("  • 注释行数: ").append(result.commentLines).append("\n");
        message.append("  • 空白行数: ").append(result.blankLines).append("\n\n");
        
        if ("Java".equals(result.fileType)) {
            // Java特定统计
            message.append("☕ Java元素统计:\n");
            message.append("  • 类数量: ").append(result.classCount).append("\n");
            message.append("  • 方法数量: ").append(result.methodCount).append("\n");
            message.append("  • 字段数量: ").append(result.fieldCount).append("\n");
            message.append("  • 变量数量: ").append(result.variableCount).append("\n");
            message.append("  • 导入数量: ").append(result.importCount).append("\n\n");
            
            // 复杂度分析
            message.append("🔍 复杂度分析:\n");
            message.append("  • 圈复杂度: ").append(result.cyclomaticComplexity).append("\n\n");
            
            // 方法长度统计
            if (result.methodLengthStats.total > 0) {
                message.append("📐 方法长度统计:\n");
                message.append("  • 最短方法: ").append(result.methodLengthStats.min).append(" 行\n");
                message.append("  • 最长方法: ").append(result.methodLengthStats.max).append(" 行\n");
                message.append("  • 平均长度: ").append(String.format("%.1f", result.methodLengthStats.avg)).append(" 行\n\n");
            }
            
            // 类大小统计
            if (result.classSizeStats.total > 0) {
                message.append("📦 类大小统计:\n");
                message.append("  • 最小类: ").append(result.classSizeStats.min).append(" 行\n");
                message.append("  • 最大类: ").append(result.classSizeStats.max).append(" 行\n");
                message.append("  • 平均大小: ").append(String.format("%.1f", result.classSizeStats.avg)).append(" 行\n\n");
            }
            
            // 代码质量指标
            message.append("⚠️ 代码质量指标:\n");
            message.append("  • 长方法(>20行): ").append(result.qualityMetrics.longMethods).append("\n");
            message.append("  • 大类(>200行): ").append(result.qualityMetrics.largeClasses).append("\n");
            message.append("  • TODO注释: ").append(result.qualityMetrics.todoComments).append("\n");
            message.append("  • 空catch块: ").append(result.qualityMetrics.emptyCatchBlocks).append("\n");
        }
        
        Messages.showInfoMessage(project, message.toString(), "代码分析报告");
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        
        boolean enabled = project != null && editor != null && psiFile != null;
        e.getPresentation().setEnabledAndVisible(enabled);
    }

    // 数据类
    private static class CodeAnalysisResult {
        String fileName;
        String fileType;
        int totalLines;
        int codeLines;
        int commentLines;
        int blankLines;
        int classCount;
        int methodCount;
        int fieldCount;
        int variableCount;
        int importCount;
        int cyclomaticComplexity;
        MethodLengthStats methodLengthStats;
        ClassSizeStats classSizeStats;
        QualityMetrics qualityMetrics;
    }

    private static class MethodLengthStats {
        final int min, max, total;
        final double avg;
        
        MethodLengthStats(int min, int max, double avg, int total) {
            this.min = min;
            this.max = max;
            this.avg = avg;
            this.total = total;
        }
    }

    private static class ClassSizeStats {
        final int min, max, total;
        final double avg;
        
        ClassSizeStats(int min, int max, double avg, int total) {
            this.min = min;
            this.max = max;
            this.avg = avg;
            this.total = total;
        }
    }

    private static class QualityMetrics {
        int longMethods;
        int largeClasses;
        int todoComments;
        int emptyCatchBlocks;
    }
}
