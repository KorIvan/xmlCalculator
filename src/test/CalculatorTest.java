package test;

import org.junit.Test;

import com.task.model.Calculation;
import com.task.model.SimpleCalculator;
import com.task.model.XmlMarshaller;

public class CalculatorTest {

	private  static XmlMarshaller<SimpleCalculator> marsh = new XmlMarshaller<>("com.task.model");
	@Test
	public void calculate() {
		Calculation calc = new Calculation(marsh.unmarshall("task/sampleTest.xml"));
		calc.calculate();
		marsh.marshal(calc.getToSerialize(), "task/result.xml");
	}

	/**
	 * Inappropriate amount of Operations in Term
	 */
	@Test(expected = RuntimeException.class)
	public void calculateInvalidInputOperations() {
		Calculation calc = new Calculation(marsh.unmarshall("task/sampleTest3.xml"));
		calc.calculate();
		marsh.marshal(calc.getToSerialize(), "task/result2.xml");
	}

	/**
	 * Inappropriate amount of Arguments in Term
	 */
	@Test(expected = RuntimeException.class)
	public void calculateInvalidInputArguments() {
		Calculation calc = new Calculation(marsh.unmarshall("task/sampleTest4.xml"));
		calc.calculate();
		marsh.marshal(calc.getToSerialize(), "task/result2.xml");
	}

	/**
	 * Xml-file has no Expressions
	 */
	@Test(expected = RuntimeException.class)
	public void calculateInvalidInputNoExpressions() {
		Calculation calc = new Calculation(marsh.unmarshall("task/sampleTest5.xml"));
		calc.calculate();
		marsh.marshal(calc.getToSerialize(), "task/result2.xml");
	}

	/**
	 * Arguments have inappropriate type. Must be integer.
	 */
	@Test(expected = RuntimeException.class)
	public void calculateInvalidInputIllegalArguments() {
		Calculation calc = new Calculation(marsh.unmarshall("task/sampleTest6.xml"));
		calc.calculate();
		marsh.marshal(calc.getToSerialize(), "task/result2.xml");
	}

}
