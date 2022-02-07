package org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.localbestupdate.impl;

import org.uma.jmetal.experimental.componentbasedalgorithm.catalogue.pso.localbestupdate.LocalBestUpdate;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.comparator.DominanceComparator;

import java.util.List;

public class DefaultLocalBestUpdate implements LocalBestUpdate {
  private DominanceComparator<DoubleSolution> dominanceComparator ;

  /**
   * TODO: Terminar documentacion
   * Constructor
   * @param dominanceComparator
   */
  public DefaultLocalBestUpdate(DominanceComparator<DoubleSolution> dominanceComparator) {
    this.dominanceComparator = dominanceComparator ;
  }

  @Override
  public DoubleSolution[] update(List<DoubleSolution> swarm, DoubleSolution[] localBest) {
    for (int i = 0; i < swarm.size(); i++) {
      int result = dominanceComparator.compare(swarm.get(i), localBest[i]) ;
      if (result != 1) {
        localBest[i] = (DoubleSolution)swarm.get(i).copy() ;
      }
    }
    return localBest ;
  }

  public DominanceComparator<DoubleSolution> getDominanceComparator() {
    return dominanceComparator;
  }
}
