package org.uma.jmetal.example.operator;

import org.uma.jmetal.lab.visualization.plot.PlotFront;
import org.uma.jmetal.lab.visualization.plot.impl.PlotSmile;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.operator.mutation.impl.IntegerPolynomialMutation;
import org.uma.jmetal.problem.integerproblem.IntegerProblem;
import org.uma.jmetal.problem.singleobjective.NIntegerMin;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.bounds.Bounds;
import org.uma.jmetal.util.comparator.IntegerVariableComparator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * This class is intended to verify the working of the polynomial mutation operator for Integer
 * encoding.
 *
 * A figure depicting the values obtained when generating 100000 points, a granularity of 200, and a number
 * of different distribution index values (5, 10, 20) can be found here:
 * <a href="https://github.com/jMetal/jMetal/blob/master/figures/integerPolynomialMutation.png">
 * Polynomial mutation (Integer) </a>
 */
public class IntegerPolynomialMutationExample {
  /**
   * Program to generate data representing the distribution of points generated by a polynomial
   * mutation operator. The parameters to be introduced by the command line are:
   * - numberOfSolutions: number of solutions to generate
   * - granularity: number of subdivisions to be considered.
   * - distributionIndex: distribution index of the polynomial mutation operator
   * - outputFile: file containing the results
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) throws FileNotFoundException {
    int numberOfPoints ;
    int granularity ;
    double distributionIndex ;

    if (args.length !=3) {
      JMetalLogger.logger.info("Usage: numberOfSolutions granularity distributionIndex") ;
      JMetalLogger.logger.info("Using default parameters") ;

      numberOfPoints = 10000 ;
      granularity = 100 ;
      distributionIndex = 100.0 ;
    } else {
      numberOfPoints = Integer.parseInt(args[0]);
      granularity = Integer.parseInt(args[1]);
      distributionIndex = Double.parseDouble(args[2]);
    }

    IntegerProblem problem ;

    problem = new NIntegerMin(1, 10, -1000, 1000);
    MutationOperator<IntegerSolution> mutation = new IntegerPolynomialMutation(1.0, distributionIndex) ;

    IntegerSolution solution = problem.createSolution() ;
    solution.variables().set(0, 0);

    List<IntegerSolution> population = new ArrayList<>(numberOfPoints) ;
    for (int i = 0 ; i < numberOfPoints ; i++) {
      IntegerSolution newSolution = (IntegerSolution) solution.copy();
      mutation.execute(newSolution) ;
      population.add(newSolution) ;
    }

    population.sort(new IntegerVariableComparator());
    double[][] classifier = classify(population, problem, granularity);

    PlotFront plot = new PlotSmile(classifier) ;
    plot.plot();
  }

  private static double[][] classify(List<IntegerSolution> solutions, IntegerProblem problem, int granularity) {
    Bounds<Integer> bounds = problem.getBoundsForVariables().get(0);
    double grain = (bounds.getUpperBound() - bounds.getLowerBound()) / granularity ;
    double[][] classifier = new double[granularity][] ;
    for (int i = 0 ; i < granularity; i++) {
      classifier[i] = new double[2] ;
      classifier[i][0] = bounds.getLowerBound() + i * grain ;
      classifier[i][1] = 0 ;
    }

    for (IntegerSolution solution : solutions) {
      boolean found = false ;
      int index = 0 ;
      while (!found) {
        if (solution.variables().get(0) <= classifier[index][0]) {
          classifier[index][1] ++ ;
          found = true ;
        } else {
          if (index == (granularity - 1)) {
            classifier[index][1] ++ ;
            found = true ;
          } else {
            index++;
          }
        }
      }
    }

    return classifier ;
  }
}
