package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		
		s.init();
		s.enter();
		s.runCycle();
		
		while (true){
			String cmd= getConsole();
			switch (cmd){
			case "start" :
				s.getSCInterface().raiseStart();
			 break;
			case "black" :
				s.getSCInterface().raiseBlack();
			 break;
			case "white" :
				s.getSCInterface().raiseWhite();
			 break;
			case "exit" :
				System.exit(0);
			 break;
			}
			print(s);
		}
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
	public static String getConsole() throws IOException {
		char c='\n';
		String s="";
		while (true){
			c = (char)System.in.read();
			if ( c=='\n') {
				break ;
			}
			else {
				s+=c;
			}
		}
		return s;
	}
}
