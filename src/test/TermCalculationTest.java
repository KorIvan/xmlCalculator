package test;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

import org.junit.Test;

import com.task.model.Calculation;
import com.task.model.Operation;
import com.task.model.SimpleCalculator;
import com.task.model.Term;

public class TermCalculationTest {

	public Term arrangeTermWithArguments(double arg1, Operation type, double arg2) {
		Term term = new Term();
		term.setOperationType(type.toString());
		term.getArg().add(new BigInteger("" + (int) arg1));
		term.getArg().add(new BigInteger("" + (int) arg2));
		return term;
	}
	public Term arrangeTermWithOperations(Term arg1, Operation type, Term arg2) {
		Term term = new Term();
		term.setOperationType(type.toString());
		term.getOperation().add(arg1);
		term.getOperation().add(arg2);
		return term;
	}
	
	@Test
	public void testNestedTerms(){
		double arg1 = 10;
		double arg2 = 20;
		Term termInner1 = arrangeTermWithArguments(arg1, Operation.SUM, arg2);
		double arg3 = 15;
		double arg4 = 5;
		Term termInner2 = arrangeTermWithArguments(arg3, Operation.SUB, arg4);
		Term termOuter = arrangeTermWithOperations(termInner1, Operation.DIV, termInner2);
		Calculation calc = new Calculation(new SimpleCalculator());
		double expected=(arg1+arg2)/(arg3-arg4);
		double result = calc.getResult(termOuter);
		assertEquals(String.format("(%1$.3f + %2$.3f)/(%3$.3f-%4$.3f)= %5$.3f", arg1, arg2,arg3,arg4,arg1 + arg2), expected, result, 0.001);

		
	}
	@Test
	public void testSum() throws NoSuchFieldException, IllegalAccessException {
		double arg1 = 10;
		double arg2 = 20;
		Term term = arrangeTermWithArguments(arg1, Operation.SUM, arg2);
		Calculation calc = new Calculation(new SimpleCalculator());

		double result = calc.getResult(term);
		assertEquals(String.format("%1$.3f + %2$.3f= %3$.3f", arg1, arg2, arg1 + arg2), arg1 + arg2, result, 0.001);
	}

	@Test
	public void testSub() throws NoSuchFieldException, IllegalAccessException {
		double arg1 = 10;
		double arg2 = 20;
		Term term = arrangeTermWithArguments(arg1, Operation.SUB, arg2);
		Calculation calc = new Calculation(new SimpleCalculator());
		double result = calc.getResult(term);
		assertEquals(String.format("%1$.3f - %2$.3f= %3$.3f", arg1, arg2, arg1 - arg2), arg1 - arg2, result, 0.001);
	}

	@Test
	public void testDiv() throws NoSuchFieldException, IllegalAccessException {
		double arg1 = 10;
		double arg2 = 20;
		Term term = arrangeTermWithArguments(arg1, Operation.DIV, arg2);
		Calculation calc = new Calculation(new SimpleCalculator());

		double result = calc.getResult(term);

		assertEquals(String.format("%1$.3f / %2$.3f= %3$.3f", arg1, arg2, arg1 / arg2), arg1 / arg2, result, 0.001);
	}

	@Test
	public void testMul() throws NoSuchFieldException, IllegalAccessException {
		double arg1 = 10;
		double arg2 = 20;
		Term term = arrangeTermWithArguments(arg1, Operation.MUL, arg2);
		Calculation calc = new Calculation(new SimpleCalculator());

		double result = calc.getResult(term);

		assertEquals(String.format("%1$.3f * %2$.3f= %3$.3f", arg1, arg2, arg1 * arg2), arg1 * arg2, result, 0.001);
	}
	/**
	 * 	 Unsupported operationby calculator at getResult
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUnsupportedOperation(){
		double arg1 = 10;
		double arg2 = 20;
		Term term = arrangeTermWithArguments(arg1, Operation.MUL, arg2);
		term.setOperationType("POW");
		Calculation calc = new Calculation(new SimpleCalculator());

		double result = calc.getResult(term);
		assertEquals(String.format("%1$.3f ^ %2$.3f= %3$.3f", arg1, arg2, Math.pow(arg1, arg2)), Math.pow(arg1, arg2),
				result, 0.001);
	}
	/**
	 * Unsupported operation by calculator at doOperation()
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUnsupportedOperationDoOperation()
			throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		double arg1 = 10;
		double arg2 = 20;
		Term term = arrangeTermWithArguments(arg1, Operation.MUL, arg2);
		term.setOperationType("POW");

		Calculation calc = new Calculation(new SimpleCalculator());
		Method doOperation = calc.getClass().getDeclaredMethod("doOperation", Number.class, Operation.class,
				Number.class);
		doOperation.setAccessible(true);
		double result = Double.parseDouble(doOperation
				.invoke(calc, term.getArg1(), Operation.valueOf(term.getOperationType()), term.getArg2()).toString());

		assertEquals(String.format("%1$.3f ^ %2$.3f= %3$.3f", arg1, arg2, Math.pow(arg1, arg2)), Math.pow(arg1, arg2),
				result, 0.001);
	}

}
