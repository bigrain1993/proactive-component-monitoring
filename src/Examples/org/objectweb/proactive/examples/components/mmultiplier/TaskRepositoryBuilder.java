package org.objectweb.proactive.examples.components.mmultiplier;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ContentDescription;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.component.componentcontroller.remmos.Remmos;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.type.PAGCMInterfaceType;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;
import org.objectweb.proactive.core.node.Node;

public class TaskRepositoryBuilder {

	public static Component build(PAGCMTypeFactory patf, PAGenericFactory pagf, Node node, boolean managed) throws InstantiationException {
		List<PAGCMInterfaceType> array = new ArrayList<PAGCMInterfaceType>();
		InterfaceType itfType;
	
		itfType = patf.createGCMItfType(
				MMConstants.TASK_REPOSITORY_ITF,
				TaskRepository.class.getName(),
				PAGCMTypeFactory.SERVER,
				PAGCMTypeFactory.MANDATORY,
				PAGCMTypeFactory.SINGLETON_CARDINALITY);
		array.add((PAGCMInterfaceType) itfType); 
		
		PAGCMInterfaceType[] fItfType = array.toArray(new PAGCMInterfaceType[array.size()]);
		PAGCMInterfaceType[] nfItfType = new PAGCMInterfaceType[] {};
		if (managed) {
			nfItfType = Remmos.createMonitorableNFType(patf, fItfType, Constants.PRIMITIVE);
		}

		ComponentType compType = patf.createFcType(fItfType, nfItfType);
		ControllerDescription controllerDesc = new ControllerDescription(MMConstants.TASK_REPOSITORY_COMP, Constants.PRIMITIVE);
		ContentDescription contentDesc = new ContentDescription(TaskRepositoryImpl.class.getName());
	
		return pagf.newFcInstance(compType, controllerDesc, contentDesc, node);
	}

}
