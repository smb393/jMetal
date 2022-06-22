package org.uma.jmetal.auto.parameter.catalogue;

import java.util.List;
import org.uma.jmetal.auto.parameter.CategoricalParameter;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.operator.mutation.impl.LinkedPolynomialMutation;
import org.uma.jmetal.operator.mutation.impl.NonUniformMutation;
import org.uma.jmetal.operator.mutation.impl.PolynomialMutation;
import org.uma.jmetal.operator.mutation.impl.UniformMutation;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;

public class MutationParameter extends CategoricalParameter {
  public MutationParameter(String[] args, List<String> mutationOperators) {
    super("mutation", args, mutationOperators);
  }

  public MutationOperator<DoubleSolution> getParameter() {
    MutationOperator<DoubleSolution> result;
    int numberOfProblemVariables = (int) getNonConfigurableParameter("numberOfProblemVariables") ;
    Double mutationProbability = (Double) findGlobalParameter("mutationProbabilityFactor").getValue() * 1.0/numberOfProblemVariables;
    RepairDoubleSolutionStrategyParameter repairDoubleSolution =
            (RepairDoubleSolutionStrategyParameter) findGlobalParameter("mutationRepairStrategy");

    switch (getValue()) {
      case "polynomial":
        Double distributionIndex =
                (Double) findSpecificParameter("polynomialMutationDistributionIndex").getValue();
        result =
                new PolynomialMutation(
                        mutationProbability, distributionIndex, repairDoubleSolution.getParameter());
        break;
      case "linkedPolynomial":
        distributionIndex =
                (Double) findSpecificParameter("linkedPolynomialMutationDistributionIndex").getValue();
        result =
                new LinkedPolynomialMutation(
                        mutationProbability, distributionIndex, repairDoubleSolution.getParameter());
        break;
      case "uniform":
        Double perturbation = (Double) findSpecificParameter("uniformMutationPerturbation").getValue();
        result =
                new UniformMutation(mutationProbability, perturbation, repairDoubleSolution.getParameter());
        break;
      case "nonUniform":
        perturbation = (Double) findSpecificParameter("nonUniformMutationPerturbation").getValue();
        int maxIterations = (Integer) getNonConfigurableParameter("maxIterations") ;
        result =
                new NonUniformMutation(mutationProbability, perturbation, maxIterations, repairDoubleSolution.getParameter());
        break;
      default:
        throw new RuntimeException("Mutation operator does not exist: " + getName());
    }
    return result;
  }

  @Override
  public String getName() {
    return "mutation";
  }
}
