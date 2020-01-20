package io.tyloo.tcctransaction.interceptor;

import io.tyloo.api.*;
import io.tyloo.tcctransaction.common.MethodRole;
import io.tyloo.tcctransaction.support.FactoryBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/*
 * 注解方法上下文
 *
 * @Author:Zh1Cheung 945503088@qq.com
 * @Date: 15:33 2019/12/4
 *
 */
public class CompensableMethodContext {

    /**
     * 切入点
     */
    ProceedingJoinPoint pjp = null;

    /**
     * 注解方法
     */
    Method method = null;

    /**
     * 注解
     */
    Tyloo tyloo = null;

    /**
     * 传播级别
     */
    Propagation propagation = null;

    /**
     * 事务上下文
     */
    TylooContext tylooContext = null;

    public CompensableMethodContext(ProceedingJoinPoint pjp) {
        this.pjp = pjp;
        this.method = getCompensableMethod();
        this.tyloo = method.getAnnotation(Tyloo.class);
        this.propagation = tyloo.propagation();
        TylooContextLoader instance = (TylooContextLoader) FactoryBuilder.factoryOf(tyloo.transactionContextEditor()).getInstance();
        this.tylooContext = instance.get(pjp.getTarget(), method, pjp.getArgs());

    }

    public Tyloo getAnnotation() {
        return tyloo;
    }

    public Propagation getPropagation() {
        return propagation;
    }

    public TylooContext getTylooContext() {
        return tylooContext;
    }

    public Method getMethod() {
        return method;
    }

    /**
     * 获取唯一标识
     *
     * @return
     */
    public Object getUniqueIdentity() {
        Annotation[][] annotations = this.getMethod().getParameterAnnotations();

        for (int i = 0; i < annotations.length; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation.annotationType().equals(UniqueIdentity.class)) {

                    Object[] params = pjp.getArgs();
                    Object unqiueIdentity = params[i];

                    return unqiueIdentity;
                }
            }
        }

        return null;
    }

    /**
     * 获取注解方法
     *
     * @return
     */
    private Method getCompensableMethod() {
        Method method = ((MethodSignature) (pjp.getSignature())).getMethod();

        if (method.getAnnotation(Tyloo.class) == null) {
            try {
                method = pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                return null;
            }
        }
        return method;
    }

    /**
     * 通过该方法事务传播级别获取方法类型
     *
     * @param isTransactionActive
     * @return
     */
    public MethodRole getMethodRole(boolean isTransactionActive) {
        if ((propagation.equals(Propagation.REQUIRED) && !isTransactionActive && tylooContext == null) ||
                propagation.equals(Propagation.REQUIRES_NEW)) {
            return MethodRole.ROOT;
        } else if ((propagation.equals(Propagation.REQUIRED) || propagation.equals(Propagation.MANDATORY)) && !isTransactionActive && tylooContext != null) {
            return MethodRole.PROVIDER;
        } else {
            return MethodRole.NORMAL;
        }
    }

    public Object proceed() throws Throwable {
        return this.pjp.proceed();
    }
}