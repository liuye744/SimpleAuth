package com.codingcube.simpleauth.properties;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.expression.*;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.List;

public class EvaluationEnvironmentContext implements EvaluationContext {

    final private Environment environment;
    final private EvaluationContext evaluationContext = new StandardEvaluationContext();


    public EvaluationEnvironmentContext(Environment environment) {
        this.environment = environment;
    }

    @Override
    public TypedValue getRootObject() {
        return evaluationContext.getRootObject();
    }

    @Override
    public List<PropertyAccessor> getPropertyAccessors() {
        return evaluationContext.getPropertyAccessors();
    }

    @Override
    public List<ConstructorResolver> getConstructorResolvers() {
        return evaluationContext.getConstructorResolvers();
    }

    @Override
    public List<MethodResolver> getMethodResolvers() {
        return evaluationContext.getMethodResolvers();
    }

    @Override
    public BeanResolver getBeanResolver() {
        return evaluationContext.getBeanResolver();
    }

    @Override
    public TypeLocator getTypeLocator() {
        return evaluationContext.getTypeLocator();
    }

    @Override
    public TypeConverter getTypeConverter() {
        return evaluationContext.getTypeConverter();
    }

    @Override
    public TypeComparator getTypeComparator() {
        return evaluationContext.getTypeComparator();
    }

    @Override
    public OperatorOverloader getOperatorOverloader() {
        return evaluationContext.getOperatorOverloader();
    }

    @Override
    public void setVariable(String s, Object o) {
        evaluationContext.setVariable(s, o);
    }

    @Override
    public Object lookupVariable(String s) {
        final Object o = evaluationContext.lookupVariable(s);
        if (o != null){
            return o;
        }
        return environment.getProperty(s);
    }
}
