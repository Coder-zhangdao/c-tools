package com.bixuebihui.tablegen.generator;

import com.bixuebihui.generated.tablegen.pojo.T_metatable;
import com.bixuebihui.tablegen.TableUtils;
import com.bixuebihui.tablegen.entry.ColumnData;
import com.bixuebihui.tablegen.entry.TableSetInfo;
import com.github.jknack.handlebars.Handlebars;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;

import static com.bixuebihui.tablegen.NameUtils.firstUp;

/**
 * @author xwx
 */
public class PojoGenerator extends BaseGenerator{
    private static final Log LOG = LogFactory.getLog(PojoGenerator.class);

    public static String getExtendsClasses(TableSetInfo setInfo, String tableName) {
        if (setInfo.getTableDataExt() != null) {
            T_metatable tab = setInfo.getTableDataExt().get(tableName);
            if (tab != null) {
                StringBuilder sb = new StringBuilder();
                if (StringUtils.isNotBlank(tab.getExtrasuperclasses())) {
                    sb.append(", extends ").append(tab.getExtrasuperclasses());
                }
                return sb.toString();
            }
        }
        return "";
    }

    public String getAnnotationForClass(){
        /*
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户数据传输对象")
public class OverseaUserDto {
    @ApiModelProperty(value = "用户id")
    private int userId;
    @ApiModelProperty(value = "用户昵称")
    private String userNickname;
    @ApiModelProperty(value = "用户默认昵称")


   https://blog.csdn.net/sdyy321/article/details/40298081

    import com.fasterxml.jackson.annotation.JsonProperty;

    @JsonAlias can be used!
    @JsonAlias
这个注解是在JackSon 2.9版本中才有的。作用是在反序列化的时候可以让Bean的属性接收多个json字段的名称。可以加在字段上或者getter和setter方法上。


@JsonProperty("template_code")
@JsonIgnoreProperties
         */
        return "io.swagger.annotations.ApiModel;";
    }


    @Override
    public String getTargetFileName(String tableName) {

        return  config.getBaseSrcDir()+File.separator + "pojo" + File.separator + getClassName(tableName)
                + ".java";
    }

    @Override
    String getTemplateFileName() {
        return "pojo.java";
    }

    @Override
    public String getClassName(String tableName) {
      return getPojoClassName(tableName);
    }

    @Override
    protected void additionalSetting(Handlebars handlebars) {
        super.additionalSetting(handlebars);
        // usage: {{columnAnnotation aColumnData tableName}}
        handlebars.registerHelper("columnAnnotation", (col, options) -> TableUtils.getColumnAnnotation(config, setInfo, options.param(0),(ColumnData) col));

        // usage: {{columnDescription aColumnData tableName}}
        handlebars.registerHelper("columnDescription", (col, options) -> TableUtils.getColumnDescription(config, setInfo.getColumnsExtInfo(options.param(0)), options.param(0), (ColumnData)col));
    }

    @Override
    protected String getExtendsClasses(String tableName) {
      return getExtendsClasses(setInfo, tableName);
    }

}
