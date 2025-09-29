package com.glowxq.plugs.utils;

import com.glowxq.plugs.settings.KeymapSettings;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.keymap.ex.KeymapManagerEx;
import com.intellij.openapi.util.SystemInfo;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * 快捷键应用器
 * 负责将设置中的快捷键应用到实际的Actions上
 * 
 * @author glowxq
 */
public class KeymapApplier {

    private static final Map<String, String> ACTION_IDS = new HashMap<>();
    
    static {
        ACTION_IDS.put("generateJavaBean", "com.glowxq.plugs.GenerateJavaBeanMethodsAction");
        ACTION_IDS.put("foldJavaBean", "com.glowxq.plugs.FoldJavaBeanMethodsAction");
        ACTION_IDS.put("generateTemplate", "com.glowxq.plugs.GenerateCodeTemplateAction");
        ACTION_IDS.put("refactorAssistant", "com.glowxq.plugs.RefactorAssistantAction");
        ACTION_IDS.put("smartComment", "com.glowxq.plugs.SmartCommentAction");
        ACTION_IDS.put("batchGenerate", "com.glowxq.plugs.BatchGenerateAction");
        ACTION_IDS.put("codeCleanup", "com.glowxq.plugs.CodeCleanupAction");
        ACTION_IDS.put("codeAnalysis", "com.glowxq.plugs.CodeAnalysisAction");
        ACTION_IDS.put("quickDoc", "com.glowxq.plugs.QuickDocAction");
        ACTION_IDS.put("quickFix", "OneClick.QuickFix");
    }

    /**
     * 应用快捷键设置到当前Keymap
     */
    public static void applyKeymapSettings() {
        KeymapSettings settings = KeymapSettings.getInstance();
        applyKeymapSettings(settings);
    }

    /**
     * 应用快捷键设置到当前Keymap
     */
    public static void applyKeymapSettings(KeymapSettings settings) {
        
        if (!settings.isEnableShortcuts()) {
            return;
        }

        try {
            KeymapManagerEx keymapManager = KeymapManagerEx.getInstanceEx();
            Keymap activeKeymap = keymapManager.getActiveKeymap();
            
            if (activeKeymap == null) {
                return;
            }

            // 获取当前操作系统的快捷键
            Map<String, String> shortcuts = getCurrentOSShortcuts(settings);
            
            // 应用快捷键
            for (Map.Entry<String, String> entry : shortcuts.entrySet()) {
                String actionKey = entry.getKey();
                String shortcutText = entry.getValue();
                String actionId = ACTION_IDS.get(actionKey);
                
                if (actionId != null && shortcutText != null && !shortcutText.trim().isEmpty()) {
                    applyShortcutToAction(activeKeymap, actionId, shortcutText);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error applying keymap settings: " + e.getMessage());
        }
    }

    /**
     * 获取当前操作系统的快捷键设置
     */
    private static Map<String, String> getCurrentOSShortcuts(KeymapSettings settings) {
        Map<String, String> shortcuts = new HashMap<>();
        
        if (SystemInfo.isMac) {
            shortcuts.put("generateJavaBean", settings.getGenerateJavaBeanMac());
            shortcuts.put("foldJavaBean", settings.getFoldJavaBeanMac());
            shortcuts.put("generateTemplate", settings.getGenerateTemplateMac());
            shortcuts.put("refactorAssistant", settings.getRefactorAssistantMac());
            shortcuts.put("smartComment", settings.getSmartCommentMac());
            shortcuts.put("batchGenerate", settings.getBatchGenerateMac());
        } else {
            shortcuts.put("generateJavaBean", settings.getGenerateJavaBeanWindows());
            shortcuts.put("foldJavaBean", settings.getFoldJavaBeanWindows());
            shortcuts.put("generateTemplate", settings.getGenerateTemplateWindows());
            shortcuts.put("refactorAssistant", settings.getRefactorAssistantWindows());
            shortcuts.put("smartComment", settings.getSmartCommentWindows());
            shortcuts.put("batchGenerate", settings.getBatchGenerateWindows());
        }
        
        return shortcuts;
    }

    /**
     * 将快捷键应用到指定Action
     */
    private static void applyShortcutToAction(Keymap keymap, String actionId, String shortcutText) {
        try {
            ActionManager actionManager = ActionManager.getInstance();
            AnAction action = actionManager.getAction(actionId);
            
            if (action == null) {
                return;
            }

            // 解析快捷键文本
            KeyStroke keyStroke = parseShortcut(shortcutText);
            if (keyStroke == null) {
                return;
            }

            // 移除现有的快捷键
            keymap.removeAllActionShortcuts(actionId);
            
            // 添加新的快捷键
            keymap.addShortcut(actionId, new com.intellij.openapi.actionSystem.KeyboardShortcut(keyStroke, null));
            
        } catch (Exception e) {
            System.err.println("Error applying shortcut '" + shortcutText + "' to action '" + actionId + "': " + e.getMessage());
        }
    }

    /**
     * 解析快捷键文本为KeyStroke
     */
    private static KeyStroke parseShortcut(String shortcutText) {
        try {
            if (shortcutText == null || shortcutText.trim().isEmpty()) {
                return null;
            }

            String[] parts = shortcutText.toLowerCase().trim().split("\\s+");
            if (parts.length == 0) {
                return null;
            }

            int modifiers = 0;
            String keyText = null;

            for (String part : parts) {
                switch (part) {
                    case "ctrl":
                        modifiers |= KeyEvent.CTRL_DOWN_MASK;
                        break;
                    case "alt":
                        modifiers |= KeyEvent.ALT_DOWN_MASK;
                        break;
                    case "shift":
                        modifiers |= KeyEvent.SHIFT_DOWN_MASK;
                        break;
                    case "cmd":
                    case "meta":
                        modifiers |= KeyEvent.META_DOWN_MASK;
                        break;
                    default:
                        keyText = part.toUpperCase();
                        break;
                }
            }

            if (keyText == null) {
                return null;
            }

            // 处理特殊键
            int keyCode = getKeyCode(keyText);
            if (keyCode == -1) {
                return null;
            }

            return KeyStroke.getKeyStroke(keyCode, modifiers);
            
        } catch (Exception e) {
            System.err.println("Error parsing shortcut: " + shortcutText + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取键码
     */
    private static int getKeyCode(String keyText) {
        try {
            // 处理字母键
            if (keyText.length() == 1 && Character.isLetter(keyText.charAt(0))) {
                return KeyEvent.getExtendedKeyCodeForChar(keyText.charAt(0));
            }
            
            // 处理数字键
            if (keyText.length() == 1 && Character.isDigit(keyText.charAt(0))) {
                return KeyEvent.getExtendedKeyCodeForChar(keyText.charAt(0));
            }
            
            // 处理功能键
            if (keyText.startsWith("F") && keyText.length() > 1) {
                try {
                    int fNum = Integer.parseInt(keyText.substring(1));
                    if (fNum >= 1 && fNum <= 24) {
                        return KeyEvent.VK_F1 + fNum - 1;
                    }
                } catch (NumberFormatException e) {
                    // 忽略
                }
            }
            
            // 处理其他特殊键
            switch (keyText) {
                case "SPACE": return KeyEvent.VK_SPACE;
                case "ENTER": return KeyEvent.VK_ENTER;
                case "TAB": return KeyEvent.VK_TAB;
                case "ESCAPE": return KeyEvent.VK_ESCAPE;
                case "BACKSPACE": return KeyEvent.VK_BACK_SPACE;
                case "DELETE": return KeyEvent.VK_DELETE;
                case "INSERT": return KeyEvent.VK_INSERT;
                case "HOME": return KeyEvent.VK_HOME;
                case "END": return KeyEvent.VK_END;
                case "PAGE_UP": return KeyEvent.VK_PAGE_UP;
                case "PAGE_DOWN": return KeyEvent.VK_PAGE_DOWN;
                case "UP": return KeyEvent.VK_UP;
                case "DOWN": return KeyEvent.VK_DOWN;
                case "LEFT": return KeyEvent.VK_LEFT;
                case "RIGHT": return KeyEvent.VK_RIGHT;
                default: return -1;
            }
            
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 重置快捷键为默认值
     */
    public static void resetToDefaults() {
        KeymapSettings settings = KeymapSettings.getInstance();
        settings.resetToDefaults();
        applyKeymapSettings();
    }
}
