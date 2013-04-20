package cws.core.provisioner;

import java.util.Iterator;
import java.util.Set;

import org.cloudbus.cloudsim.Log;

import cws.core.Provisioner;
import cws.core.VM;
import cws.core.WorkflowEngine;
import cws.core.WorkflowEvent;
import cws.core.cloudsim.CloudSimWrapper;
import cws.core.experiment.VMFactory;

public class SimpleQueueBasedProvisioner extends AbstractProvisioner implements Provisioner, WorkflowEvent {

    public SimpleQueueBasedProvisioner(CloudSimWrapper cloudsim) {
        super(cloudsim);
    }

    @Override
    public void provisionResources(WorkflowEngine engine) {

        // use the queued (released) jobs from the workflow engine
        int queueLength = engine.getQueueLength();

        Log.printLine(getCloudSim().clock() + " Provisioner: queue length: " + queueLength);

        // check the deadline and budget constraints
        double budget = engine.getBudget();
        double deadline = engine.getDeadline();
        double time = getCloudSim().clock();
        double cost = engine.getCost();

        // if we are close to the budget by one VM*hour
        if (budget <= cost || time > deadline) {
            return;
        }

        // add one VM if queue not empty
        if (queueLength > 0) {
            VM vm = VMFactory.createVM(1000, 1, 1.0, 1.0);
            Log.printLine(getCloudSim().clock() + " Starting VM: " + vm.getId());
            getCloudSim().send(engine.getId(), cloud.getId(), 0.0, VM_LAUNCH, vm);
        } else { // terminate free VMs
            Set<VM> freeVMs = engine.getFreeVMs();
            Iterator<VM> vmIt = freeVMs.iterator();
            while (vmIt.hasNext()) {
                VM vm = vmIt.next();
                vmIt.remove();
                Log.printLine(getCloudSim().clock() + " Terminating VM: " + vm.getId());
                getCloudSim().send(engine.getId(), cloud.getId(), 0.0, VM_TERMINATE, vm);
            }
        }
        getCloudSim().send(engine.getId(), engine.getId(), PROVISIONER_INTERVAL, PROVISIONING_REQUEST, null);
    }
}
