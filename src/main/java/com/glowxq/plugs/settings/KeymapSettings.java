package com.glowxq.plugs.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 快捷键设置
 * 支持不同操作系统的快捷键配置
 * 
 * @author glowxq
 */
@State(
    name = "OneClickKeymapSettings",
    storages = @Storage("oneclick-keymap.xml")
)
public class KeymapSettings implements PersistentStateComponent<KeymapSettings.State> {

    private State myState = new State();

    public static KeymapSettings getInstance() {
        return ApplicationManager.getApplication().getService(KeymapSettings.class);
    }

    @Override
    public @Nullable State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        XmlSerializerUtil.copyBean(state, myState);
    }

    public static class State {
        // Windows/Linux 快捷键
        public String generateJavaBeanWindows = "ctrl shift D";
        public String foldJavaBeanWindows = "ctrl shift F";
        public String generateTemplateWindows = "ctrl shift T";
        public String refactorAssistantWindows = "ctrl shift R";
        public String smartCommentWindows = "ctrl shift C";
        public String batchGenerateWindows = "ctrl shift B";
        public String codeCleanupWindows = "ctrl shift L";
        public String codeAnalysisWindows = "ctrl shift A";
        public String quickDocWindows = "ctrl shift Q";
        public String quickFixWindows = "ctrl shift X";

        // macOS 快捷键
        public String generateJavaBeanMac = "meta shift D";
        public String foldJavaBeanMac = "meta shift F";
        public String generateTemplateMac = "meta shift T";
        public String refactorAssistantMac = "meta shift R";
        public String smartCommentMac = "meta shift C";
        public String batchGenerateMac = "meta shift B";
        public String codeCleanupMac = "meta shift L";
        public String codeAnalysisMac = "meta shift A";
        public String quickDocMac = "meta shift Q";
        public String quickFixMac = "meta shift X";
        
        // 自定义快捷键映射
        public Map<String, String> customKeymaps = new HashMap<>();
        
        // 是否启用快捷键
        public boolean enableShortcuts = true;
        
        // 是否显示快捷键提示
        public boolean showShortcutHints = true;
        
        // 快捷键冲突检测
        public boolean detectConflicts = true;
    }

    // Getter methods for Windows/Linux
    public String getGenerateJavaBeanWindows() {
        return myState.generateJavaBeanWindows;
    }

    public String getFoldJavaBeanWindows() {
        return myState.foldJavaBeanWindows;
    }

    public String getGenerateTemplateWindows() {
        return myState.generateTemplateWindows;
    }

    public String getRefactorAssistantWindows() {
        return myState.refactorAssistantWindows;
    }

    public String getSmartCommentWindows() {
        return myState.smartCommentWindows;
    }

    public String getBatchGenerateWindows() {
        return myState.batchGenerateWindows;
    }

    public String getCodeCleanupWindows() {
        return myState.codeCleanupWindows;
    }

    public String getCodeAnalysisWindows() {
        return myState.codeAnalysisWindows;
    }

    public String getQuickDocWindows() {
        return myState.quickDocWindows;
    }

    public String getQuickFixWindows() {
        return myState.quickFixWindows;
    }

    // Getter methods for macOS
    public String getGenerateJavaBeanMac() {
        return myState.generateJavaBeanMac;
    }

    public String getFoldJavaBeanMac() {
        return myState.foldJavaBeanMac;
    }

    public String getGenerateTemplateMac() {
        return myState.generateTemplateMac;
    }

    public String getRefactorAssistantMac() {
        return myState.refactorAssistantMac;
    }

    public String getSmartCommentMac() {
        return myState.smartCommentMac;
    }

    public String getBatchGenerateMac() {
        return myState.batchGenerateMac;
    }

    public String getCodeCleanupMac() {
        return myState.codeCleanupMac;
    }

    public String getCodeAnalysisMac() {
        return myState.codeAnalysisMac;
    }

    public String getQuickDocMac() {
        return myState.quickDocMac;
    }

    public String getQuickFixMac() {
        return myState.quickFixMac;
    }

    // Setter methods for Windows/Linux
    public void setGenerateJavaBeanWindows(String shortcut) {
        myState.generateJavaBeanWindows = shortcut;
    }

    public void setFoldJavaBeanWindows(String shortcut) {
        myState.foldJavaBeanWindows = shortcut;
    }

    public void setGenerateTemplateWindows(String shortcut) {
        myState.generateTemplateWindows = shortcut;
    }

    public void setRefactorAssistantWindows(String shortcut) {
        myState.refactorAssistantWindows = shortcut;
    }

    public void setSmartCommentWindows(String shortcut) {
        myState.smartCommentWindows = shortcut;
    }

    public void setBatchGenerateWindows(String shortcut) {
        myState.batchGenerateWindows = shortcut;
    }

    public void setCodeCleanupWindows(String shortcut) {
        myState.codeCleanupWindows = shortcut;
    }

    public void setCodeAnalysisWindows(String shortcut) {
        myState.codeAnalysisWindows = shortcut;
    }

    public void setQuickDocWindows(String shortcut) {
        myState.quickDocWindows = shortcut;
    }

    public void setQuickFixWindows(String shortcut) {
        myState.quickFixWindows = shortcut;
    }

    // Setter methods for macOS
    public void setGenerateJavaBeanMac(String shortcut) {
        myState.generateJavaBeanMac = shortcut;
    }

    public void setFoldJavaBeanMac(String shortcut) {
        myState.foldJavaBeanMac = shortcut;
    }

    public void setGenerateTemplateMac(String shortcut) {
        myState.generateTemplateMac = shortcut;
    }

    public void setRefactorAssistantMac(String shortcut) {
        myState.refactorAssistantMac = shortcut;
    }

    public void setSmartCommentMac(String shortcut) {
        myState.smartCommentMac = shortcut;
    }

    public void setBatchGenerateMac(String shortcut) {
        myState.batchGenerateMac = shortcut;
    }

    public void setCodeCleanupMac(String shortcut) {
        myState.codeCleanupMac = shortcut;
    }

    public void setCodeAnalysisMac(String shortcut) {
        myState.codeAnalysisMac = shortcut;
    }

    public void setQuickDocMac(String shortcut) {
        myState.quickDocMac = shortcut;
    }

    public void setQuickFixMac(String shortcut) {
        myState.quickFixMac = shortcut;
    }

    // Custom keymap methods
    public Map<String, String> getCustomKeymaps() {
        return myState.customKeymaps;
    }

    public void setCustomKeymap(String actionId, String shortcut) {
        myState.customKeymaps.put(actionId, shortcut);
    }

    public String getCustomKeymap(String actionId) {
        return myState.customKeymaps.get(actionId);
    }

    // Settings methods
    public boolean isEnableShortcuts() {
        return myState.enableShortcuts;
    }

    public void setEnableShortcuts(boolean enable) {
        myState.enableShortcuts = enable;
    }

    public boolean isShowShortcutHints() {
        return myState.showShortcutHints;
    }

    public void setShowShortcutHints(boolean show) {
        myState.showShortcutHints = show;
    }

    public boolean isDetectConflicts() {
        return myState.detectConflicts;
    }

    public void setDetectConflicts(boolean detect) {
        myState.detectConflicts = detect;
    }

    /**
     * 根据当前操作系统获取快捷键
     */
    public String getShortcutForCurrentOS(String actionId) {
        boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
        
        switch (actionId) {
            case "generateJavaBean":
                return isMac ? getGenerateJavaBeanMac() : getGenerateJavaBeanWindows();
            case "foldJavaBean":
                return isMac ? getFoldJavaBeanMac() : getFoldJavaBeanWindows();
            case "generateTemplate":
                return isMac ? getGenerateTemplateMac() : getGenerateTemplateWindows();
            case "refactorAssistant":
                return isMac ? getRefactorAssistantMac() : getRefactorAssistantWindows();
            case "smartComment":
                return isMac ? getSmartCommentMac() : getSmartCommentWindows();
            case "batchGenerate":
                return isMac ? getBatchGenerateMac() : getBatchGenerateWindows();
            case "codeCleanup":
                return isMac ? getCodeCleanupMac() : getCodeCleanupWindows();
            case "codeAnalysis":
                return isMac ? getCodeAnalysisMac() : getCodeAnalysisWindows();
            case "quickDoc":
                return isMac ? getQuickDocMac() : getQuickDocWindows();
            case "quickFix":
                return isMac ? getQuickFixMac() : getQuickFixWindows();
            default:
                return getCustomKeymap(actionId);
        }
    }

    /**
     * 设置当前操作系统的快捷键
     */
    public void setShortcutForCurrentOS(String actionId, String shortcut) {
        boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
        
        switch (actionId) {
            case "generateJavaBean":
                if (isMac) setGenerateJavaBeanMac(shortcut);
                else setGenerateJavaBeanWindows(shortcut);
                break;
            case "foldJavaBean":
                if (isMac) setFoldJavaBeanMac(shortcut);
                else setFoldJavaBeanWindows(shortcut);
                break;
            case "generateTemplate":
                if (isMac) setGenerateTemplateMac(shortcut);
                else setGenerateTemplateWindows(shortcut);
                break;
            case "refactorAssistant":
                if (isMac) setRefactorAssistantMac(shortcut);
                else setRefactorAssistantWindows(shortcut);
                break;
            case "smartComment":
                if (isMac) setSmartCommentMac(shortcut);
                else setSmartCommentWindows(shortcut);
                break;
            case "batchGenerate":
                if (isMac) setBatchGenerateMac(shortcut);
                else setBatchGenerateWindows(shortcut);
                break;
            case "codeCleanup":
                if (isMac) setCodeCleanupMac(shortcut);
                else setCodeCleanupWindows(shortcut);
                break;
            case "codeAnalysis":
                if (isMac) setCodeAnalysisMac(shortcut);
                else setCodeAnalysisWindows(shortcut);
                break;
            case "quickDoc":
                if (isMac) setQuickDocMac(shortcut);
                else setQuickDocWindows(shortcut);
                break;
            case "quickFix":
                if (isMac) setQuickFixMac(shortcut);
                else setQuickFixWindows(shortcut);
                break;
            default:
                setCustomKeymap(actionId, shortcut);
                break;
        }
    }

    /**
     * 重置为默认快捷键
     */
    public void resetToDefaults() {
        myState.generateJavaBeanWindows = "ctrl shift D";
        myState.foldJavaBeanWindows = "ctrl shift F";
        myState.generateTemplateWindows = "ctrl shift T";
        myState.refactorAssistantWindows = "ctrl shift R";
        myState.smartCommentWindows = "ctrl shift C";
        myState.batchGenerateWindows = "ctrl shift B";
        myState.codeCleanupWindows = "ctrl shift L";
        myState.codeAnalysisWindows = "ctrl shift A";
        myState.quickDocWindows = "ctrl shift Q";
        myState.quickFixWindows = "ctrl shift X";
        
        myState.generateJavaBeanMac = "meta shift D";
        myState.foldJavaBeanMac = "meta shift F";
        myState.generateTemplateMac = "meta shift T";
        myState.refactorAssistantMac = "meta shift R";
        myState.smartCommentMac = "meta shift C";
        myState.batchGenerateMac = "meta shift B";
        myState.codeCleanupMac = "meta shift L";
        myState.codeAnalysisMac = "meta shift A";
        myState.quickDocMac = "meta shift Q";
        myState.quickFixMac = "meta shift X";
        
        myState.customKeymaps.clear();
        myState.enableShortcuts = true;
        myState.showShortcutHints = true;
        myState.detectConflicts = true;
    }

    /**
     * 获取操作系统名称
     */
    public static String getOSName() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac")) {
            return "macOS";
        } else if (osName.contains("win")) {
            return "Windows";
        } else if (osName.contains("nix") || osName.contains("nux")) {
            return "Linux";
        } else {
            return "Unknown";
        }
    }

    /**
     * 检查快捷键格式是否有效
     */
    public static boolean isValidShortcut(String shortcut) {
        if (shortcut == null || shortcut.trim().isEmpty()) {
            return false;
        }
        
        // 基本格式检查
        String[] parts = shortcut.toLowerCase().split("\\s+");
        for (String part : parts) {
            if (!isValidKeyPart(part)) {
                return false;
            }
        }
        
        return true;
    }

    private static boolean isValidKeyPart(String part) {
        return part.matches("(ctrl|alt|shift|cmd|meta|[a-z0-9]|f[1-9]|f1[0-2])");
    }
}
