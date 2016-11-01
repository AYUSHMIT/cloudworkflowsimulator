package cws.core.algorithms;

import java.util.List;

import cws.core.cloudsim.CloudSimWrapper;
import cws.core.core.VMType;
import cws.core.dag.DAG;
import cws.core.engine.Environment;
import cws.core.provisioner.SimpleUtilizationBasedProvisioner;
import cws.core.scheduler.EnsembleDynamicScheduler;

public class DPDS extends DynamicAlgorithm {
    public DPDS(double budget, double deadline, List<DAG> dags, double maxScaling,
                AlgorithmStatistics ensembleStatistics, Environment environment, CloudSimWrapper cloudsim, VMType representativeVmType) {
        super(budget, deadline, dags, new EnsembleDynamicScheduler(cloudsim, environment, representativeVmType),
                new SimpleUtilizationBasedProvisioner(maxScaling, cloudsim, representativeVmType), ensembleStatistics, environment, cloudsim, representativeVmType);
    }
}
