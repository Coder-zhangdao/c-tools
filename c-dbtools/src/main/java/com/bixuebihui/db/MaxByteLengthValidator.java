package com.bixuebihui.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Size;
import java.io.UnsupportedEncodingException;


/**
 * see <a href="http://stackoverflow.com/questions/26337002/javax-validation-constraint-to-validate-a-string-length-in-bytes">数据库字符串长度校验</a>
 *    如果数据库用的是utf8mb4则长度会限制更短。
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class MaxByteLengthValidator implements ConstraintValidator<Size, String> {
	private static String UTF_8="UTF-8";
    private static final Log mLog = LogFactory.getLog(MaxByteLengthValidator.class);

    private int max;

    /**
     * <p>initialize.</p>
     *
     * @param constraintAnnotation a {@link javax.validation.constraints.Size} object.
     */
    @Override
    public void initialize(Size constraintAnnotation) {
        this.max = constraintAnnotation.max();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        try {
            return object == null || object.getBytes(UTF_8).length <= this.max;
        } catch (UnsupportedEncodingException e) {
            mLog.error(e);
        }
        return false;
    }
}
