package com.glowxq.plugs.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * 数据库工具集合Action
 * 提供数据库相关的代码生成功能
 */
public class DatabaseToolsAction extends AnAction {

    private static final List<DbTool> DB_TOOLS = Arrays.asList(
        new DbTool("🗃️ 生成Entity注解", "为实体类添加JPA注解", DatabaseToolsAction::generateEntityAnnotations),
        new DbTool("📝 生成SQL建表语句", "根据实体类生成CREATE TABLE语句", DatabaseToolsAction::generateCreateTableSql),
        new DbTool("🔍 生成Repository接口", "生成Spring Data JPA Repository", DatabaseToolsAction::generateRepository),
        new DbTool("📊 生成MyBatis Mapper", "生成MyBatis Mapper接口和XML", DatabaseToolsAction::generateMyBatisMapper),
        new DbTool("🔄 生成数据转换器", "生成Entity与DTO转换器", DatabaseToolsAction::generateConverter),
        new DbTool("📋 生成CRUD Service", "生成完整的CRUD Service类", DatabaseToolsAction::generateCrudService),
        new DbTool("🌐 生成REST Controller", "生成RESTful API控制器", DatabaseToolsAction::generateRestController),
        new DbTool("🔧 生成数据库配置", "生成数据源配置类", DatabaseToolsAction::generateDatabaseConfig)
    );

    public DatabaseToolsAction() {
        super("Database Tools", "Database related code generation utilities", null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        ListPopup popup = JBPopupFactory.getInstance().createListPopup(
            new BaseListPopupStep<DbTool>("选择数据库工具", DB_TOOLS) {
                @Override
                public PopupStep onChosen(DbTool selectedValue, boolean finalChoice) {
                    selectedValue.action.execute(e);
                    return FINAL_CHOICE;
                }

                @NotNull
                @Override
                public String getTextFor(DbTool value) {
                    return value.name + " - " + value.description;
                }
            }
        );
        popup.showInBestPositionFor(e.getDataContext());
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

    // 生成Entity注解
    private static void generateEntityAnnotations(AnActionEvent e) {
        PsiClass psiClass = getCurrentClass(e);
        if (psiClass == null) return;

        String tableName = Messages.showInputDialog(
            "请输入表名:",
            "生成Entity注解",
            Messages.getQuestionIcon(),
            camelToSnake(psiClass.getName()),
            null
        );

        if (tableName != null && !tableName.trim().isEmpty()) {
            String annotations = String.format(
                "@Entity\n" +
                "@Table(name = \"%s\")\n" +
                "@Data\n" +
                "@NoArgsConstructor\n" +
                "@AllArgsConstructor\n",
                tableName.trim()
            );
            insertTextAtClassTop(e, annotations);
            Messages.showInfoMessage("Entity注解已生成", "生成完成");
        }
    }

    // 生成SQL建表语句（符合阿里巴巴规范）
    private static void generateCreateTableSql(AnActionEvent e) {
        PsiClass psiClass = getCurrentClass(e);
        if (psiClass == null) return;

        Project project = e.getProject();
        if (project == null) return;

        StringBuilder sql = new StringBuilder();
        String tableName = camelToSnake(psiClass.getName());

        // 阿里巴巴规范：表名使用小写，单词间用下划线分隔
        sql.append("-- ").append(psiClass.getName()).append("表\n");
        sql.append("-- 符合阿里巴巴Java开发手册数据库规范\n");
        sql.append("CREATE TABLE `").append(tableName).append("` (\n");

        // 阿里巴巴规范：主键字段名为id，类型为BIGINT UNSIGNED
        sql.append("    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',\n");

        // 处理类字段
        for (PsiField field : psiClass.getFields()) {
            if (!field.hasModifierProperty(PsiModifier.STATIC) &&
                !field.hasModifierProperty(PsiModifier.FINAL) &&
                !"id".equals(field.getName())) {  // 跳过id字段，避免重复
                String fieldName = camelToSnake(field.getName());
                String fieldType = mapJavaTypeToSql(field.getType().getPresentableText());

                // 阿里巴巴规范：字段名使用反引号包裹
                sql.append("    `").append(fieldName).append("` ").append(fieldType);

                // 阿里巴巴规范：字符串类型字段必须指定是否允许为NULL
                if (fieldType.startsWith("VARCHAR") || fieldType.startsWith("TEXT")) {
                    sql.append(" DEFAULT NULL");
                }

                sql.append(" COMMENT '").append(field.getName()).append("',\n");
            }
        }

        // 阿里巴巴规范：添加通用字段（创建时间、更新时间、逻辑删除标记）
        sql.append("    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n");
        sql.append("    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n");
        sql.append("    `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0-未删除,1-已删除)',\n");

        // 阿里巴巴规范：主键定义
        sql.append("    PRIMARY KEY (`id`),\n");

        // 阿里巴巴规范：添加逻辑删除字段的索引
        sql.append("    KEY `idx_is_deleted` (`is_deleted`)\n");

        // 阿里巴巴规范：引擎使用InnoDB，字符集使用utf8mb4
        sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='")
           .append(psiClass.getName()).append("表';");

        // 在新的编辑器标签页中打开SQL
        openInNewEditor(project, sql.toString(), tableName + "_create.sql");
        Messages.showInfoMessage("SQL建表语句已在新标签页中打开", "生成完成");
    }

    // 生成Repository接口
    private static void generateRepository(AnActionEvent e) {
        PsiClass psiClass = getCurrentClass(e);
        if (psiClass == null) return;

        String className = psiClass.getName();
        String repositoryCode = String.format(
            "@Repository\n" +
            "public interface %sRepository extends JpaRepository<%s, Long> {\n" +
            "\n" +
            "    /**\n" +
            "     * 根据名称查找\n" +
            "     */\n" +
            "    Optional<%s> findByName(String name);\n" +
            "\n" +
            "    /**\n" +
            "     * 根据状态查找\n" +
            "     */\n" +
            "    List<%s> findByStatus(String status);\n" +
            "\n" +
            "    /**\n" +
            "     * 分页查询\n" +
            "     */\n" +
            "    @Query(\"SELECT e FROM %s e WHERE e.name LIKE %%:keyword%%\")\n" +
            "    Page<%s> findByKeyword(@Param(\"keyword\") String keyword, Pageable pageable);\n" +
            "}",
            className, className, className, className, className, className
        );
        
        insertTextAtCursor(e, repositoryCode);
        Messages.showInfoMessage("Repository接口已生成", "生成完成");
    }

    // 生成MyBatis Mapper
    private static void generateMyBatisMapper(AnActionEvent e) {
        PsiClass psiClass = getCurrentClass(e);
        if (psiClass == null) return;

        String className = psiClass.getName();
        String mapperCode = String.format(
            "@Mapper\n" +
            "public interface %sMapper {\n" +
            "\n" +
            "    /**\n" +
            "     * 插入记录\n" +
            "     */\n" +
            "    int insert(%s entity);\n" +
            "\n" +
            "    /**\n" +
            "     * 根据ID删除\n" +
            "     */\n" +
            "    int deleteById(Long id);\n" +
            "\n" +
            "    /**\n" +
            "     * 更新记录\n" +
            "     */\n" +
            "    int updateById(%s entity);\n" +
            "\n" +
            "    /**\n" +
            "     * 根据ID查询\n" +
            "     */\n" +
            "    %s selectById(Long id);\n" +
            "\n" +
            "    /**\n" +
            "     * 查询列表\n" +
            "     */\n" +
            "    List<%s> selectList(%s query);\n" +
            "}",
            className, className, className, className, className, className
        );
        
        insertTextAtCursor(e, mapperCode);
        Messages.showInfoMessage("MyBatis Mapper已生成", "生成完成");
    }

    // 生成数据转换器
    private static void generateConverter(AnActionEvent e) {
        PsiClass psiClass = getCurrentClass(e);
        if (psiClass == null) return;

        String className = psiClass.getName();
        String converterCode = String.format(
            "@Component\n" +
            "public class %sConverter {\n" +
            "\n" +
            "    /**\n" +
            "     * Entity转DTO\n" +
            "     */\n" +
            "    public %sDTO toDTO(%s entity) {\n" +
            "        if (entity == null) {\n" +
            "            return null;\n" +
            "        }\n" +
            "        return %sDTO.builder()\n" +
            "            // TODO: 添加字段映射\n" +
            "            .build();\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * DTO转Entity\n" +
            "     */\n" +
            "    public %s toEntity(%sDTO dto) {\n" +
            "        if (dto == null) {\n" +
            "            return null;\n" +
            "        }\n" +
            "        return %s.builder()\n" +
            "            // TODO: 添加字段映射\n" +
            "            .build();\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * Entity列表转DTO列表\n" +
            "     */\n" +
            "    public List<%sDTO> toDTOList(List<%s> entities) {\n" +
            "        return entities.stream()\n" +
            "            .map(this::toDTO)\n" +
            "            .collect(Collectors.toList());\n" +
            "    }\n" +
            "}",
            className, className, className, className, className, className, className, className, className
        );
        
        insertTextAtCursor(e, converterCode);
        Messages.showInfoMessage("数据转换器已生成", "生成完成");
    }

    // 生成CRUD Service
    private static void generateCrudService(AnActionEvent e) {
        PsiClass psiClass = getCurrentClass(e);
        if (psiClass == null) return;

        String className = psiClass.getName();
        String serviceCode = String.format(
            "@Service\n" +
            "@Transactional\n" +
            "@Slf4j\n" +
            "public class %sService {\n" +
            "\n" +
            "    @Autowired\n" +
            "    private %sRepository repository;\n" +
            "\n" +
            "    /**\n" +
            "     * 创建\n" +
            "     */\n" +
            "    public %s create(%s entity) {\n" +
            "        log.info(\"创建%s: {}\", entity);\n" +
            "        return repository.save(entity);\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 根据ID删除\n" +
            "     */\n" +
            "    public void deleteById(Long id) {\n" +
            "        log.info(\"删除%s: {}\", id);\n" +
            "        repository.deleteById(id);\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 更新\n" +
            "     */\n" +
            "    public %s update(%s entity) {\n" +
            "        log.info(\"更新%s: {}\", entity);\n" +
            "        return repository.save(entity);\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 根据ID查询\n" +
            "     */\n" +
            "    @Transactional(readOnly = true)\n" +
            "    public %s findById(Long id) {\n" +
            "        return repository.findById(id)\n" +
            "            .orElseThrow(() -> new EntityNotFoundException(\"%s not found: \" + id));\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 分页查询\n" +
            "     */\n" +
            "    @Transactional(readOnly = true)\n" +
            "    public Page<%s> findAll(Pageable pageable) {\n" +
            "        return repository.findAll(pageable);\n" +
            "    }\n" +
            "}",
            className, className, className, className, className, className, className, className, className, className, className, className
        );
        
        insertTextAtCursor(e, serviceCode);
        Messages.showInfoMessage("CRUD Service已生成", "生成完成");
    }

    // 生成REST Controller
    private static void generateRestController(AnActionEvent e) {
        PsiClass psiClass = getCurrentClass(e);
        if (psiClass == null) return;

        String className = psiClass.getName();
        String path = "/" + camelToSnake(className).replace("_", "-");
        
        String controllerCode = String.format(
            "@RestController\n" +
            "@RequestMapping(\"%s\")\n" +
            "@Slf4j\n" +
            "@Validated\n" +
            "public class %sController {\n" +
            "\n" +
            "    @Autowired\n" +
            "    private %sService service;\n" +
            "\n" +
            "    /**\n" +
            "     * 创建\n" +
            "     */\n" +
            "    @PostMapping\n" +
            "    public ResponseEntity<%s> create(@Valid @RequestBody %s entity) {\n" +
            "        %s created = service.create(entity);\n" +
            "        return ResponseEntity.ok(created);\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 删除\n" +
            "     */\n" +
            "    @DeleteMapping(\"/{id}\")\n" +
            "    public ResponseEntity<Void> delete(@PathVariable Long id) {\n" +
            "        service.deleteById(id);\n" +
            "        return ResponseEntity.ok().build();\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 更新\n" +
            "     */\n" +
            "    @PutMapping(\"/{id}\")\n" +
            "    public ResponseEntity<%s> update(@PathVariable Long id, @Valid @RequestBody %s entity) {\n" +
            "        entity.setId(id);\n" +
            "        %s updated = service.update(entity);\n" +
            "        return ResponseEntity.ok(updated);\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 查询详情\n" +
            "     */\n" +
            "    @GetMapping(\"/{id}\")\n" +
            "    public ResponseEntity<%s> findById(@PathVariable Long id) {\n" +
            "        %s entity = service.findById(id);\n" +
            "        return ResponseEntity.ok(entity);\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 分页查询\n" +
            "     */\n" +
            "    @GetMapping\n" +
            "    public ResponseEntity<Page<%s>> findAll(\n" +
            "            @RequestParam(defaultValue = \"0\") int page,\n" +
            "            @RequestParam(defaultValue = \"10\") int size) {\n" +
            "        Pageable pageable = PageRequest.of(page, size);\n" +
            "        Page<%s> result = service.findAll(pageable);\n" +
            "        return ResponseEntity.ok(result);\n" +
            "    }\n" +
            "}",
            path, className, className, className, className, className, className, className, className, className, className, className, className
        );
        
        insertTextAtCursor(e, controllerCode);
        Messages.showInfoMessage("REST Controller已生成", "生成完成");
    }

    // 生成数据库配置
    private static void generateDatabaseConfig(AnActionEvent e) {
        String configCode = 
            "@Configuration\n" +
            "@EnableJpaRepositories(basePackages = \"com.example.repository\")\n" +
            "@EnableTransactionManagement\n" +
            "public class DatabaseConfig {\n" +
            "\n" +
            "    @Bean\n" +
            "    @Primary\n" +
            "    @ConfigurationProperties(\"spring.datasource\")\n" +
            "    public DataSource dataSource() {\n" +
            "        return DataSourceBuilder.create().build();\n" +
            "    }\n" +
            "\n" +
            "    @Bean\n" +
            "    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {\n" +
            "        return new JpaTransactionManager(entityManagerFactory);\n" +
            "    }\n" +
            "\n" +
            "    @Bean\n" +
            "    public JpaVendorAdapter jpaVendorAdapter() {\n" +
            "        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();\n" +
            "        adapter.setShowSql(true);\n" +
            "        adapter.setGenerateDdl(false);\n" +
            "        adapter.setDatabasePlatform(\"org.hibernate.dialect.MySQL8Dialect\");\n" +
            "        return adapter;\n" +
            "    }\n" +
            "}";
        
        insertTextAtCursor(e, configCode);
        Messages.showInfoMessage("数据库配置已生成", "生成完成");
    }

    // 辅助方法
    private static PsiClass getCurrentClass(AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (!(psiFile instanceof PsiJavaFile)) {
            Messages.showWarningDialog("请在Java文件中使用此功能", "数据库工具");
            return null;
        }

        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) return null;

        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        
        if (psiClass == null) {
            Messages.showWarningDialog("请将光标放在类中", "数据库工具");
            return null;
        }
        
        return psiClass;
    }

    private static void insertTextAtCursor(AnActionEvent e, String text) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Project project = e.getProject();
        if (editor == null || project == null) return;

        WriteCommandAction.runWriteCommandAction(project, () -> {
            int offset = editor.getCaretModel().getOffset();
            editor.getDocument().insertString(offset, text);
        });
    }

    private static void insertTextAtClassTop(AnActionEvent e, String text) {
        PsiClass psiClass = getCurrentClass(e);
        if (psiClass == null) return;

        Project project = e.getProject();
        if (project == null) return;

        WriteCommandAction.runWriteCommandAction(project, () -> {
            int offset = psiClass.getTextRange().getStartOffset();
            Editor editor = e.getData(CommonDataKeys.EDITOR);
            if (editor != null) {
                editor.getDocument().insertString(offset, text);
            }
        });
    }

    private static String camelToSnake(String camelCase) {
        if (camelCase == null) return "";
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    private static String mapJavaTypeToSql(String javaType) {
        // 符合阿里巴巴规范的SQL类型映射
        switch (javaType) {
            case "String": return "VARCHAR(255)";
            case "Integer": case "int": return "INT";
            case "Long": case "long": return "BIGINT";
            case "Double": case "double": return "DOUBLE";
            case "Float": case "float": return "FLOAT";
            case "Boolean": case "boolean": return "TINYINT UNSIGNED";  // 阿里巴巴规范：使用UNSIGNED
            case "Date": case "LocalDateTime": return "DATETIME";
            case "LocalDate": return "DATE";
            case "LocalTime": return "TIME";
            case "BigDecimal": return "DECIMAL(19,4)";  // 阿里巴巴规范：金额类型使用DECIMAL(19,4)
            default: return "VARCHAR(255)";
        }
    }

    /**
     * 在新的编辑器标签页中打开内容
     */
    private static void openInNewEditor(Project project, String content, String fileName) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                // 创建一个轻量级虚拟文件
                LightVirtualFile virtualFile = new LightVirtualFile(fileName, content);

                // 在新的编辑器标签页中打开
                FileEditorManager.getInstance(project).openFile(virtualFile, true);
            } catch (Exception ex) {
                Messages.showErrorDialog(project, "打开编辑器失败: " + ex.getMessage(), "错误");
            }
        });
    }

    // 内部类定义数据库工具
    private static class DbTool {
        final String name;
        final String description;
        final DbToolAction action;

        DbTool(String name, String description, DbToolAction action) {
            this.name = name;
            this.description = description;
            this.action = action;
        }
    }

    @FunctionalInterface
    private interface DbToolAction {
        void execute(AnActionEvent e);
    }
}
