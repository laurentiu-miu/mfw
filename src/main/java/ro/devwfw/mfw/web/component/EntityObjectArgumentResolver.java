package ro.devwfw.mfw.web.component;

import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author LaurentiuM
 * @version createdOn: 1/5/16
 */
public class EntityObjectArgumentResolver extends RequestResponseBodyMethodProcessor {

    public EntityObjectArgumentResolver(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBodyEntityObject.class);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.hasParameterAnnotation(RequestBodyEntityObject.class);
    }

    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation annot : annotations) {
            if (annot.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = AnnotationUtils.getValue(annot);
                binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[]{hints});
            }
        }
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        RequestBodyEntityObject annotd = parameter.getParameterAnnotation(RequestBodyEntityObject.class);
        String attrName = (annotd != null) ? annotd.value() : null;
        String modelName = StringUtils.hasText(attrName) ? attrName : Conventions.getVariableNameForParameter(parameter);

        Object baseEntity = mavContainer.getModel().get(modelName);
        Object arg = readWithMessageConverters(webRequest, parameter, baseEntity.getClass());

        Annotation[] annotations = parameter.getParameterAnnotations();
        WebDataBinder binder = binderFactory.createBinder(webRequest, arg, modelName);
        for (Annotation annot : annotations) {
            if (annot.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = AnnotationUtils.getValue(annot);
                binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[]{hints});
                BindingResult bindingResult = binder.getBindingResult();
                if (bindingResult.hasErrors()) {
                    throw new MethodArgumentNotValidException(parameter, bindingResult);
                }
            }
        }
        mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + modelName, binder.getBindingResult());
        return arg;
    }

    @Override
    protected <T> Object readWithMessageConverters(NativeWebRequest webRequest,
                                                   MethodParameter methodParam, Type paramType) throws IOException, HttpMediaTypeNotSupportedException {

        final HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpInputMessage inputMessage = new ServletServerHttpRequest(servletRequest);

        RequestBodyEntityObject annot = methodParam.getParameterAnnotation(RequestBodyEntityObject.class);
        if (!annot.required()) {
            InputStream inputStream = inputMessage.getBody();
            if (inputStream == null) {
                return null;
            } else if (inputStream.markSupported()) {
                inputStream.mark(1);
                if (inputStream.read() == -1) {
                    return null;
                }
                inputStream.reset();
            } else {
                final PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
                int b = pushbackInputStream.read();
                if (b == -1) {
                    return null;
                } else {
                    pushbackInputStream.unread(b);
                }
                inputMessage = new ServletServerHttpRequest(servletRequest) {
                    @Override
                    public InputStream getBody() throws IOException {
                        // Form POST should not get here
                        return pushbackInputStream;
                    }
                };
            }
        }

        return super.readWithMessageConverters(inputMessage, methodParam, paramType);
    }

}
