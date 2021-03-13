package hu.bme.mit.yakindu.analysis.workhere;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;
import org.yakindu.sct.model.sgraph.Statechart;


import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		List<VariableDefinition> variables = new ArrayList<VariableDefinition>();
		List<EventDefinition> events = new ArrayList<EventDefinition>();
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof VariableDefinition) {
				variables.add((VariableDefinition) content);
			}
			else if(content instanceof EventDefinition) {
				events.add((EventDefinition) content);
			}
		}
		
		
		printImports();
		printStart();
		printSwitch(events);
		printPrint(variables);
		printGetConsole();
		
		System.out.println("}");
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
	
	public static void printPrint(List<VariableDefinition> variables) {
		System.out.println("\tpublic static void print(IExampleStatemachine s) {");
		for (VariableDefinition var : variables) {
			String name = var.getName();
			name= name.substring(0, 1).toUpperCase() + name.substring(1);
			System.out.println("\t\tSystem.out.println("+name+"= " + "s.getSCInterface().get"+name+"());");
			}
		System.out.println("\t}");
	}

	public static void printSwitch(List<EventDefinition> events) {
		System.out.println("\t\twhile (true){\n" + 
		"\t\t\tString cmd= getConsole();\n" + 
		"\t\t\tswitch (cmd){");
		for (EventDefinition var : events) {
			String name = var.getName();
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			System.out.println("\t\t\tcase \""+var.getName()+"\":\n\t\t\t\ts.getSCInterface().raise"+name+"();\n\t\t\tbreak;");
			}
		System.out.println("\t\t\tcase \"exit\":\n\t\t\t\tSystem.exit(0);\n\t\t\tbreak;");
		System.out.println("\t\t\t}\n\t\t\tprint(s);\n\t\t}\n\t}");
	}
	
	public static void printGetConsole() {
		System.out.println("\tpublic static String getConsole() throws IOException {\r\n" + 
				"\t\tchar c='\\n';\n" + 
				"\t\tString s=\"\";\n" + 
				"\t\twhile (true){\n" + 
				"\t\t\tc = (char)System.in.read();\n" + 
				"\t\t\tif ( c=='\\n') {break ;}\n" + 
				"\t\t\telse {s+=c;}\n" + 
				"\t\t}\n" + 
				"\t\treturn s;\n" + 
				"\t}");
	}
	public static void printStart() {
		System.out.println("public class RunStatechart {\r\n" + 
				"	\r\n" + 
				"	public static void main(String[] args) throws IOException {\r\n" + 
				"		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"		s.setTimer(new TimerService());\r\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"		\r\n" + 
				"		s.init();\r\n" + 
				"		s.enter();\r\n" + 
				"		s.runCycle();\n");
	}
	public static void printImports() {
		System.out.println("package hu.bme.mit.yakindu.analysis.workhere;\r\n" + 
				"\r\n" + 
				"import java.io.IOException;\r\n" + 
				"\r\n" + 
				"import hu.bme.mit.yakindu.analysis.RuntimeService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.TimerService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;");
	}
}
