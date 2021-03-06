package g419.crete.core.resolver.factory;

import g419.crete.core.classifier.AbstractCreteClassifier;
import g419.crete.core.classifier.factory.ClassifierFactory;
import g419.crete.core.classifier.serialization.Serializer;
import g419.crete.core.instance.AbstractCreteInstance;
import g419.crete.core.instance.converter.AbstractCreteInstanceConverter;
import g419.crete.core.instance.converter.factory.CreteInstanceConverterFactory;
import g419.crete.core.instance.generator.AbstractCreteInstanceGenerator;
import g419.crete.core.instance.generator.CreteInstanceGeneratorFactory;
import g419.crete.core.resolver.AbstractCreteResolver;

import java.util.HashMap;
import java.util.List;

public class CreteResolverFactory {
	
	private HashMap<String, CreteResolverFactoryItem<?>> instances;
	
	private static class FactoryHolder {
        private static final CreteResolverFactory FACTORY = new CreteResolverFactory();
    }
	
	private CreteResolverFactory(){
		instances = new HashMap<String, CreteResolverFactoryItem<?>>();
	}
	
	public static CreteResolverFactory getFactory(){return FactoryHolder.FACTORY;}
	
	public void register(String name, CreteResolverFactoryItem<?> resolver){
		instances.put(name, resolver);
	}

	public <M extends Object, L extends Object> AbstractCreteResolver<M, ? extends AbstractCreteInstance<L>, ?, L> getResolver(String name, String classifierName, String generatorName, String converterName, List<String> features, Serializer<M> model){
		return getInstance(name, classifierName, generatorName, converterName, features, model);
	}
	
	
	@SuppressWarnings("unchecked")
	private <M extends Object, T extends AbstractCreteInstance<L>,I extends Object, L extends Object> AbstractCreteResolver<M, T, I, L> getInstance(String name, String classifierName, String generatorName, String converterName, List<String> featureNames, Serializer<M> model){
		AbstractCreteResolver<M, T, I, L> resolver = (AbstractCreteResolver<M, T, I, L>) ((CreteResolverFactoryItem<L>)instances.get(name)).getResolver();
//		AbstractCreteResolver<M, T, I, L> resolver = (AbstractCreteResolver<M, T, I, L>) instances.get(name);
		if(resolver == null) return null;
		
		AbstractCreteClassifier<M, I, L> clas =  ClassifierFactory.getFactory().getClassifier(resolver.getModelClass(), resolver.getClassifierInstanceClass(), resolver.getLabelClass(), classifierName, featureNames);
		clas.setModel(model);
		AbstractCreteInstanceGenerator<T, L> gen = CreteInstanceGeneratorFactory.getFactory().getInstance(resolver.getAbstractInstanceClass(), resolver.getLabelClass(), generatorName, featureNames);
		AbstractCreteInstanceConverter<T, I> conv = CreteInstanceConverterFactory.getFactory().getInstance(resolver.getAbstractInstanceClass(), resolver.getClassifierInstanceClass(), converterName, clas);
		
		resolver.setUp(clas, gen, conv);
		
		return resolver;
	}

}
