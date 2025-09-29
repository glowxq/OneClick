package com.glowxq.plugs.actions;

import com.glowxq.plugs.utils.I18nUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * 代码模板生成Action
 * 提供常用的代码模板快速生成
 * 
 * @author glowxq
 */
public class GenerateCodeTemplateAction extends AnAction {

    private static final List<String> TEMPLATES = Arrays.asList(
        "Singleton Pattern",
        "Builder Pattern", 
        "Factory Pattern",
        "Observer Pattern",
        "Strategy Pattern",
        "REST Controller",
        "Service Layer",
        "Repository Layer",
        "Exception Handler",
        "Validation Utils",
        "Date Utils",
        "String Utils",
        "File Utils",
        "JSON Utils",
        "Test Class"
    );

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

        if (project == null || editor == null || psiFile == null) {
            return;
        }

        // 显示模板选择弹窗
        showTemplateSelectionPopup(project, editor, psiFile);
    }

    private void showTemplateSelectionPopup(Project project, Editor editor, PsiFile psiFile) {
        BaseListPopupStep<String> step = new BaseListPopupStep<String>(
                I18nUtils.message("action.template.title"), TEMPLATES) {
            @Override
            public PopupStep onChosen(String selectedValue, boolean finalChoice) {
                if (finalChoice) {
                    generateTemplate(project, editor, psiFile, selectedValue);
                }
                return FINAL_CHOICE;
            }
        };

        JBPopupFactory.getInstance()
                .createListPopup(step)
                .showInBestPositionFor(editor);
    }

    private void generateTemplate(Project project, Editor editor, PsiFile psiFile, String templateName) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                PsiClass psiClass = findTargetClass(psiFile, editor);
                if (psiClass == null) {
                    Messages.showErrorDialog(project, 
                        I18nUtils.message("message.no.class"), 
                        I18nUtils.message("action.template.title"));
                    return;
                }

                String generatedCode = generateCodeByTemplate(templateName, psiClass);
                if (generatedCode != null) {
                    insertCodeAtCursor(editor, generatedCode);
                    Messages.showInfoMessage(project,
                        I18nUtils.message("message.template.generated", templateName),
                        I18nUtils.message("action.template.title"));
                }
            } catch (Exception ex) {
                Messages.showErrorDialog(project, 
                    I18nUtils.message("message.generation.failed") + ": " + ex.getMessage(),
                    I18nUtils.message("action.template.title"));
            }
        });
    }

    private PsiClass findTargetClass(PsiFile psiFile, Editor editor) {
        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        return PsiTreeUtil.getParentOfType(element, PsiClass.class);
    }

    private String generateCodeByTemplate(String templateName, PsiClass psiClass) {
        String className = psiClass.getName();
        if (className == null) return null;

        switch (templateName) {
            case "Singleton Pattern":
                return generateSingletonPattern(className);
            case "Builder Pattern":
                return generateBuilderPattern(className, psiClass);
            case "REST Controller":
                return generateRestController(className);
            case "Service Layer":
                return generateServiceLayer(className);
            case "Repository Layer":
                return generateRepositoryLayer(className);
            case "Exception Handler":
                return generateExceptionHandler();
            case "Validation Utils":
                return generateValidationUtils();
            case "Date Utils":
                return generateDateUtils();
            case "String Utils":
                return generateStringUtils();
            case "File Utils":
                return generateFileUtils();
            case "JSON Utils":
                return generateJsonUtils();
            case "Test Class":
                return generateTestClass(className);
            default:
                return null;
        }
    }

    private String generateSingletonPattern(String className) {
        return String.format(
            "    // Singleton Pattern\n" +
            "    private static volatile %s instance;\n" +
            "    private static final Object lock = new Object();\n\n" +
            "    private %s() {\n" +
            "        // Private constructor\n" +
            "    }\n\n" +
            "    public static %s getInstance() {\n" +
            "        if (instance == null) {\n" +
            "            synchronized (lock) {\n" +
            "                if (instance == null) {\n" +
            "                    instance = new %s();\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        return instance;\n" +
            "    }\n",
            className, className, className, className
        );
    }

    private String generateBuilderPattern(String className, PsiClass psiClass) {
        StringBuilder builder = new StringBuilder();
        builder.append("    // Builder Pattern\n");
        builder.append("    public static class Builder {\n");
        
        // 添加字段
        PsiField[] fields = psiClass.getFields();
        for (PsiField field : fields) {
            if (!field.hasModifierProperty(PsiModifier.STATIC)) {
                builder.append("        private ").append(field.getType().getPresentableText())
                       .append(" ").append(field.getName()).append(";\n");
            }
        }
        
        builder.append("\n");
        
        // 添加setter方法
        for (PsiField field : fields) {
            if (!field.hasModifierProperty(PsiModifier.STATIC)) {
                String fieldName = field.getName();
                String fieldType = field.getType().getPresentableText();
                builder.append("        public Builder ").append(fieldName).append("(")
                       .append(fieldType).append(" ").append(fieldName).append(") {\n")
                       .append("            this.").append(fieldName).append(" = ").append(fieldName).append(";\n")
                       .append("            return this;\n")
                       .append("        }\n\n");
            }
        }
        
        builder.append("        public ").append(className).append(" build() {\n")
               .append("            return new ").append(className).append("(this);\n")
               .append("        }\n")
               .append("    }\n\n")
               .append("    private ").append(className).append("(Builder builder) {\n");
        
        // 添加构造函数赋值
        for (PsiField field : fields) {
            if (!field.hasModifierProperty(PsiModifier.STATIC)) {
                builder.append("        this.").append(field.getName())
                       .append(" = builder.").append(field.getName()).append(";\n");
            }
        }
        
        builder.append("    }\n\n")
               .append("    public static Builder builder() {\n")
               .append("        return new Builder();\n")
               .append("    }\n");
        
        return builder.toString();
    }

    private String generateRestController(String className) {
        String entityName = className.replace("Controller", "");
        return String.format(
            "    @Autowired\n" +
            "    private %sService %sService;\n\n" +
            "    @GetMapping\n" +
            "    public ResponseEntity<List<%s>> getAll() {\n" +
            "        List<%s> list = %sService.findAll();\n" +
            "        return ResponseEntity.ok(list);\n" +
            "    }\n\n" +
            "    @GetMapping(\"/{id}\")\n" +
            "    public ResponseEntity<%s> getById(@PathVariable Long id) {\n" +
            "        %s entity = %sService.findById(id);\n" +
            "        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();\n" +
            "    }\n\n" +
            "    @PostMapping\n" +
            "    public ResponseEntity<%s> create(@RequestBody %s entity) {\n" +
            "        %s saved = %sService.save(entity);\n" +
            "        return ResponseEntity.status(HttpStatus.CREATED).body(saved);\n" +
            "    }\n\n" +
            "    @PutMapping(\"/{id}\")\n" +
            "    public ResponseEntity<%s> update(@PathVariable Long id, @RequestBody %s entity) {\n" +
            "        entity.setId(id);\n" +
            "        %s updated = %sService.save(entity);\n" +
            "        return ResponseEntity.ok(updated);\n" +
            "    }\n\n" +
            "    @DeleteMapping(\"/{id}\")\n" +
            "    public ResponseEntity<Void> delete(@PathVariable Long id) {\n" +
            "        %sService.deleteById(id);\n" +
            "        return ResponseEntity.noContent().build();\n" +
            "    }\n",
            entityName, entityName.toLowerCase(), entityName, entityName, entityName.toLowerCase(),
            entityName, entityName, entityName.toLowerCase(),
            entityName, entityName, entityName, entityName.toLowerCase(),
            entityName, entityName, entityName, entityName.toLowerCase(),
            entityName.toLowerCase()
        );
    }

    private String generateServiceLayer(String className) {
        String entityName = className.replace("Service", "");
        return String.format(
            "    @Autowired\n" +
            "    private %sRepository %sRepository;\n\n" +
            "    public List<%s> findAll() {\n" +
            "        return %sRepository.findAll();\n" +
            "    }\n\n" +
            "    public %s findById(Long id) {\n" +
            "        return %sRepository.findById(id).orElse(null);\n" +
            "    }\n\n" +
            "    public %s save(%s entity) {\n" +
            "        return %sRepository.save(entity);\n" +
            "    }\n\n" +
            "    public void deleteById(Long id) {\n" +
            "        %sRepository.deleteById(id);\n" +
            "    }\n\n" +
            "    public boolean existsById(Long id) {\n" +
            "        return %sRepository.existsById(id);\n" +
            "    }\n",
            entityName, entityName.toLowerCase(), entityName, entityName.toLowerCase(),
            entityName, entityName.toLowerCase(),
            entityName, entityName, entityName.toLowerCase(),
            entityName.toLowerCase(),
            entityName.toLowerCase()
        );
    }

    private String generateRepositoryLayer(String className) {
        String entityName = className.replace("Repository", "");
        return String.format(
            "    // Custom query methods\n" +
            "    List<%s> findByNameContaining(String name);\n\n" +
            "    List<%s> findByActiveTrue();\n\n" +
            "    @Query(\"SELECT e FROM %s e WHERE e.createTime >= :startDate\")\n" +
            "    List<%s> findByCreateTimeAfter(@Param(\"startDate\") Date startDate);\n\n" +
            "    @Modifying\n" +
            "    @Query(\"UPDATE %s e SET e.active = :active WHERE e.id = :id\")\n" +
            "    int updateActiveStatus(@Param(\"id\") Long id, @Param(\"active\") boolean active);\n",
            entityName, entityName, entityName, entityName, entityName
        );
    }

    private String generateExceptionHandler() {
        return "    @ExceptionHandler(Exception.class)\n" +
               "    public ResponseEntity<ErrorResponse> handleException(Exception e) {\n" +
               "        ErrorResponse error = new ErrorResponse(\n" +
               "            HttpStatus.INTERNAL_SERVER_ERROR.value(),\n" +
               "            e.getMessage(),\n" +
               "            System.currentTimeMillis()\n" +
               "        );\n" +
               "        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);\n" +
               "    }\n\n" +
               "    @ExceptionHandler(IllegalArgumentException.class)\n" +
               "    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {\n" +
               "        ErrorResponse error = new ErrorResponse(\n" +
               "            HttpStatus.BAD_REQUEST.value(),\n" +
               "            e.getMessage(),\n" +
               "            System.currentTimeMillis()\n" +
               "        );\n" +
               "        return ResponseEntity.badRequest().body(error);\n" +
               "    }\n";
    }

    private String generateValidationUtils() {
        return "    public static boolean isNotEmpty(String str) {\n" +
               "        return str != null && !str.trim().isEmpty();\n" +
               "    }\n\n" +
               "    public static boolean isValidEmail(String email) {\n" +
               "        return isNotEmpty(email) && email.matches(\"^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}$\");\n" +
               "    }\n\n" +
               "    public static boolean isValidPhone(String phone) {\n" +
               "        return isNotEmpty(phone) && phone.matches(\"^1[3-9]\\\\d{9}$\");\n" +
               "    }\n\n" +
               "    public static boolean isInRange(int value, int min, int max) {\n" +
               "        return value >= min && value <= max;\n" +
               "    }\n";
    }

    private String generateDateUtils() {
        return "    private static final String DEFAULT_PATTERN = \"yyyy-MM-dd HH:mm:ss\";\n" +
               "    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);\n\n" +
               "    public static String formatNow() {\n" +
               "        return LocalDateTime.now().format(DEFAULT_FORMATTER);\n" +
               "    }\n\n" +
               "    public static String format(LocalDateTime dateTime) {\n" +
               "        return dateTime != null ? dateTime.format(DEFAULT_FORMATTER) : null;\n" +
               "    }\n\n" +
               "    public static LocalDateTime parse(String dateStr) {\n" +
               "        return isNotEmpty(dateStr) ? LocalDateTime.parse(dateStr, DEFAULT_FORMATTER) : null;\n" +
               "    }\n\n" +
               "    public static boolean isToday(LocalDateTime dateTime) {\n" +
               "        return dateTime != null && dateTime.toLocalDate().equals(LocalDate.now());\n" +
               "    }\n";
    }

    private String generateStringUtils() {
        return "    public static boolean isEmpty(String str) {\n" +
               "        return str == null || str.isEmpty();\n" +
               "    }\n\n" +
               "    public static boolean isNotEmpty(String str) {\n" +
               "        return !isEmpty(str);\n" +
               "    }\n\n" +
               "    public static String capitalize(String str) {\n" +
               "        if (isEmpty(str)) return str;\n" +
               "        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();\n" +
               "    }\n\n" +
               "    public static String toCamelCase(String str) {\n" +
               "        if (isEmpty(str)) return str;\n" +
               "        return Arrays.stream(str.split(\"[_-]\"))\n" +
               "                .map(String::toLowerCase)\n" +
               "                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))\n" +
               "                .collect(Collectors.joining());\n" +
               "    }\n";
    }

    private String generateFileUtils() {
        return "    public static String readFile(String filePath) throws IOException {\n" +
               "        return Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);\n" +
               "    }\n\n" +
               "    public static void writeFile(String filePath, String content) throws IOException {\n" +
               "        Files.writeString(Paths.get(filePath), content, StandardCharsets.UTF_8);\n" +
               "    }\n\n" +
               "    public static boolean exists(String filePath) {\n" +
               "        return Files.exists(Paths.get(filePath));\n" +
               "    }\n\n" +
               "    public static String getFileExtension(String fileName) {\n" +
               "        if (fileName == null || !fileName.contains(\".\")) return \"\";\n" +
               "        return fileName.substring(fileName.lastIndexOf(\".\") + 1);\n" +
               "    }\n";
    }

    private String generateJsonUtils() {
        return "    private static final ObjectMapper objectMapper = new ObjectMapper();\n\n" +
               "    public static String toJson(Object obj) {\n" +
               "        try {\n" +
               "            return objectMapper.writeValueAsString(obj);\n" +
               "        } catch (JsonProcessingException e) {\n" +
               "            throw new RuntimeException(\"Failed to convert to JSON\", e);\n" +
               "        }\n" +
               "    }\n\n" +
               "    public static <T> T fromJson(String json, Class<T> clazz) {\n" +
               "        try {\n" +
               "            return objectMapper.readValue(json, clazz);\n" +
               "        } catch (JsonProcessingException e) {\n" +
               "            throw new RuntimeException(\"Failed to parse JSON\", e);\n" +
               "        }\n" +
               "    }\n";
    }

    private String generateTestClass(String className) {
        return String.format(
            "    @Mock\n" +
            "    private %sRepository %sRepository;\n\n" +
            "    @InjectMocks\n" +
            "    private %sService %sService;\n\n" +
            "    @Test\n" +
            "    void testFindById() {\n" +
            "        // Given\n" +
            "        Long id = 1L;\n" +
            "        %s expected = new %s();\n" +
            "        when(%sRepository.findById(id)).thenReturn(Optional.of(expected));\n\n" +
            "        // When\n" +
            "        %s actual = %sService.findById(id);\n\n" +
            "        // Then\n" +
            "        assertThat(actual).isEqualTo(expected);\n" +
            "        verify(%sRepository).findById(id);\n" +
            "    }\n\n" +
            "    @Test\n" +
            "    void testSave() {\n" +
            "        // Given\n" +
            "        %s entity = new %s();\n" +
            "        when(%sRepository.save(entity)).thenReturn(entity);\n\n" +
            "        // When\n" +
            "        %s saved = %sService.save(entity);\n\n" +
            "        // Then\n" +
            "        assertThat(saved).isEqualTo(entity);\n" +
            "        verify(%sRepository).save(entity);\n" +
            "    }\n",
            className, className.toLowerCase(),
            className, className.toLowerCase(),
            className, className, className.toLowerCase(),
            className, className.toLowerCase(),
            className.toLowerCase(),
            className, className, className.toLowerCase(),
            className, className.toLowerCase(),
            className.toLowerCase()
        );
    }

    private void insertCodeAtCursor(Editor editor, String code) {
        int offset = editor.getCaretModel().getOffset();
        editor.getDocument().insertString(offset, code);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        boolean isJavaFile = psiFile instanceof PsiJavaFile;
        e.getPresentation().setEnabledAndVisible(isJavaFile);
    }
}
