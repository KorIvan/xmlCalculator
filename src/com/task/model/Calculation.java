package com.task.model;

public final class Calculation {
	public Calculation(SimpleCalculator calculator) {
		this.calculator = calculator;
	}

	private SimpleCalculator calculator;

	/**
	 * Calculation
	 */
	public void calculate() {
		int exprCount= calculator.getExpressions().getExpression().size();

		if (isValid(calculator)) {
			for (int i = 0; i < exprCount; i++) {
				double expressionResult=getResult(calculator.getExpressions().getExpression().get(i).getOperation());
				if (calculator.getExpressionResults()==null)
					calculator.setExpressionResults(new SimpleCalculator.ExpressionResults());
				SimpleCalculator.ExpressionResults.ExpressionResult result = new SimpleCalculator.ExpressionResults.ExpressionResult();
				result.setResult(expressionResult);
				calculator.getExpressionResults().getExpressionResult().add(result);						
			}
		}
//		for (ExpressionResults.ExpressionResult d : calculator.expressionResults.expressionResult)
//			System.out.println(d.result);
	}

	/**
	 * A way to avoid marshalling Expressions field to output xml-file.
	 * 
	 * @return new SimpleCalculator, that have only field Results
	 */
	public SimpleCalculator getToSerialize() {
		SimpleCalculator toSerialize = new SimpleCalculator();
		toSerialize.setExpressionResults(this.calculator.getExpressionResults());
		return toSerialize;
	}

	/**
	 * Get expression result by visiting all Terms
	 * 
	 * @param t Term
	 * @return result of expression
	 */
	public double getResult(Term t) {
		if (t.getArg().size() == 0 && t.getOperation().size() != 0) {
			return doOperation(getResult(t.getOperation().get(0)), Operation.valueOf(t.getOperationType()),
					getResult(t.getOperation().get(1)));
		} else
			return doOperation(t.getArg().get(0), Operation.valueOf(t.getOperationType()), t.getArg().get(1));
	}
	/**
	 * Perform arithmetical operation on arguments
	 * 
	 * @param a1
	 *            argument 1
	 * @param operType
	 *            type fo arithmetical operation
	 * @param a2
	 *            argument 2
	 * @return
	 */
	public double doOperation(Number a1, Operation operType, Number a2) {
		switch (operType) {
		case SUM:
			return a1.doubleValue() + a2.doubleValue();
		case SUB:
			return a1.doubleValue() - a2.doubleValue();
		case DIV:
			return a1.doubleValue() / a2.doubleValue();
		case MUL:
			return a1.doubleValue() * a2.doubleValue();
		default:
			throw new IllegalArgumentException("Unsupported arithmetical operation.");
		}
	}

	private void visit(Term t) {
		if (t.getArg().size() == 0) {
			if (t.getOperation().size() != 2)
				throw new RuntimeException(
						"Xml-file has invalid format. Amount of Operations in Term must be equal 2.");
			else {
				visit(t.getOperation().get(0));
				visit(t.getOperation().get(1));
			}
		} else {
			if (t.getArg().size() != 2) {
				throw new RuntimeException("Xml-file has invalid format. Amount of Arguments in Term must be equal 2.");
			} else {
				if (t.getArg().get(0) == null || t.getArg().get(1) == null) {
					throw new IllegalArgumentException(
							"Xml-file has invalid format. Arguments must be instances of BigInteger. " + t.getArg());
				}
			}
		}
	}

	/**
	 * Validation of input xml-file.
	 * 
	 * @return true if valid
	 */
	private boolean isValid(SimpleCalculator calc) {
		int exprTotal = calc.getExpressions().getExpression().size();
		if (exprTotal == 0) {
			throw new RuntimeException("There are no expressions.");
		}
		for (int i = 0; i < exprTotal; i++) {
			this.visit(calc.getExpressions().getExpression().get(i).getOperation());
		}
		return true;
	}
}
