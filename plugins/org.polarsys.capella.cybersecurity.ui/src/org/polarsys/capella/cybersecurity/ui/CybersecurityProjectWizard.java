/*******************************************************************************
 * Copyright (c) 2019 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *   
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.cybersecurity.ui;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.sirius.business.api.session.Session;
import org.polarsys.capella.core.platform.sirius.ui.project.NewProjectWizard;
import org.polarsys.capella.core.platform.sirius.ui.project.internal.WizardNewProjectCreationPage;
import org.polarsys.capella.core.platform.sirius.ui.project.operations.ProjectSessionCreationHelper;
import org.polarsys.capella.core.platform.sirius.ui.project.operations.SessionCreationHelper;
import org.polarsys.kitalpha.ad.services.manager.ViewpointActivationException;
import org.polarsys.kitalpha.ad.services.manager.ViewpointManager;
import org.polarsys.kitalpha.ad.viewpoint.predicate.exceptions.EvaluationException;

public class CybersecurityProjectWizard extends NewProjectWizard {

  @Override
  protected WizardNewProjectCreationPage createLocalProjectDescriptionPage() {
    return super.createLocalProjectDescriptionPage();
  }

  @Override
  protected SessionCreationHelper createSessionCreationHelper() {
    return new ProjectSessionCreationHelper(modelPage.isEpbsSelected(), modelPage.isOpaSelected(),
        getProjectApproach()) {

      @Override
      public Session createAirdSession(List<IFile> semanticFiles, URI airdResourceURI, IProgressMonitor monitor) {
        Session sess = super.createAirdSession(semanticFiles, airdResourceURI, monitor);
        try {
          ViewpointManager.getInstance(sess.getTransactionalEditingDomain().getResourceSet())
              .activate(CybersecurityUIActivator.VIEWPOINT_ID);
        } catch (ViewpointActivationException | EvaluationException e) {
          CybersecurityUIActivator.getDefault().getLog().log(new Status(IStatus.ERROR,
              CybersecurityUIActivator.getDefault().getBundle().getSymbolicName(), e.getMessage(), e));
        }
        return sess;
      }

    };
  }
}