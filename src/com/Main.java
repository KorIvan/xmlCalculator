package com;


import com.task.model.Calculation;
import com.task.model.SimpleCalculator;
import com.task.model.XmlMarshaller;

public class Main {

	public static void main(String[] args) {
		if (args.length<2){
			System.err.println("Fill arguments: input.xml output.xml");
			return;
		}
		XmlMarshaller<SimpleCalculator> xm= new XmlMarshaller<SimpleCalculator>("com.task.model");
		Calculation calc=new Calculation(xm.unmarshall(args[0]));
		calc.calculate();
		xm.marshal(calc.getToSerialize(), args[1]);
	}

}
