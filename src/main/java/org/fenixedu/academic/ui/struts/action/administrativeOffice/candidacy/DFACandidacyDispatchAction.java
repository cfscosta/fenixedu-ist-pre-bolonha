/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.administrativeOffice.candidacy;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacy.CandidacyDocument;
import org.fenixedu.academic.domain.candidacy.CandidacySituationType;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.dto.administrativeOffice.candidacy.DFACandidacyBean;
import org.fenixedu.academic.dto.administrativeOffice.candidacy.RegisterCandidacyBean;
import org.fenixedu.academic.dto.candidacy.CandidacyDocumentUploadBean;
import org.fenixedu.academic.dto.candidacy.PrecedentDegreeInformationBean;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.service.services.administrativeOffice.candidacy.EditPrecedentDegreeInformation;
import org.fenixedu.academic.service.services.commons.StateMachineRunner;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.masterDegree.administrativeOffice.candidate.RegisterCandidate;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.masterDegree.administrativeOffice.MasterDegreeOfficeApplication.MasterDegreeDfaApp;
import org.fenixedu.bennu.portal.servlet.PortalLayoutInjector;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = MasterDegreeDfaApp.class, path = "view-candidacy",
        titleKey = "link.masterDegree.administrativeOffice.dfaCandidacy.viewCandidacy")
@Mapping(path = "/dfaCandidacy", module = "masterDegreeAdministrativeOffice",
        input = "/candidacy/chooseDFACandidacyExecutionDegree.jsp", formBean = "dfaCandidacyForm")
@Forwards({
        @Forward(name = "fillCandidateData", path = "/masterDegreeAdministrativeOffice/candidacy/fillCandidateData.jsp"),
        @Forward(name = "prepareValidateCandidacyData",
                path = "/masterDegreeAdministrativeOffice/candidacy/prepareValidateCandidacyData.jsp"),
        @Forward(name = "showCandidacyValidateData",
                path = "/masterDegreeAdministrativeOffice/candidacy/showCandidacyValidateData.jsp"),
        @Forward(name = "showCandidacyAlterData", path = "/masterDegreeAdministrativeOffice/candidacy/showCandidacyAlterData.jsp"),
        @Forward(name = "viewCandidacyDetails", path = "/masterDegreeAdministrativeOffice/candidacy/viewCandidacyDetails.jsp"),
        @Forward(name = "chooseCandidacy", path = "/masterDegreeAdministrativeOffice/candidacy/chooseCandidacy.jsp"),
        @Forward(name = "alterSuccess", path = "/masterDegreeAdministrativeOffice/candidacy/alterSuccessPersonalData.jsp"),
        @Forward(name = "candidacyRegistration", path = "/masterDegreeAdministrativeOffice/candidacy/candidacyRegistration.jsp"),
        @Forward(name = "candidacyRegistrationSuccess",
                path = "/masterDegreeAdministrativeOffice/candidacy/candidacyRegistrationSuccess.jsp"),
        @Forward(name = "printRegistrationInformation",
                path = "/masterDegreeAdministrativeOffice/candidacy/candidacyRegistrationTemplate.jsp") })
public class DFACandidacyDispatchAction extends FenixDispatchAction {

    public ActionForward chooseDegreeTypePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        DFACandidacyBean candidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();
        candidacyBean.setDegree(null);
        candidacyBean.setDegreeCurricularPlan(null);
        candidacyBean.setExecutionYear(null);
        RenderUtils.invalidateViewState();
        request.setAttribute("candidacyBean", candidacyBean);
        

        return mapping.getInputForward();
    }

    public ActionForward chooseDegreePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        DFACandidacyBean candidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();
        candidacyBean.setDegreeCurricularPlan(null);
        candidacyBean.setExecutionYear(null);
        RenderUtils.invalidateViewState();
        request.setAttribute("candidacyBean", candidacyBean);

        return mapping.getInputForward();
    }

    public ActionForward chooseDegreeCurricularPlanPostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        DFACandidacyBean candidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("candidacyBean", candidacyBean);

        return mapping.getInputForward();
    }

    public ActionForward chooseExecutionDegreePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        DFACandidacyBean candidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();

        RenderUtils.invalidateViewState();
        request.setAttribute("candidacyBean", candidacyBean);

        return mapping.getInputForward();
    }

    public ActionForward chooseExecutionDegreeInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("candidacyBean", RenderUtils.getViewState().getMetaObject().getObject());

        return mapping.getInputForward();
    }

    public ActionForward fillCandidateData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Object object = RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("createCandidacyBean", object);

        return mapping.findForward("fillCandidateData");

    }

    @EntryPoint
    public ActionForward prepareChooseCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        return mapping.findForward("chooseCandidacy");

    }

    public ActionForward chooseCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            addActionMessage(request, "error.no.candidacy", candidacyNumber.toString());
            return mapping.findForward("chooseCandidacy");
        }

        storeCandidacyDataInRequest(request, candidacy);
        return mapping.findForward("viewCandidacyDetails");

    }

    protected void storeCandidacyDataInRequest(HttpServletRequest request, Candidacy candidacy) {
        List<CandidacyDocumentUploadBean> candidacyDocuments = new ArrayList<CandidacyDocumentUploadBean>();
        for (CandidacyDocument candidacyDocument : candidacy.getCandidacyDocumentsSet()) {
            candidacyDocuments.add(new CandidacyDocumentUploadBean(candidacyDocument));
        }
        request.setAttribute("candidacyDocuments", candidacyDocuments);
        request.setAttribute("candidacy", candidacy);

    }

    public ActionForward prepareValidateCandidacyData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("prepareValidateCandidacyData");

    }

    public ActionForward showCandidacyValidateData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            addActionMessage(request, "error.no.candidacy", candidacyNumber.toString());
            return prepareValidateCandidacyData(mapping, actionForm, request, response);
        }

        request.setAttribute("candidacy", candidacy);
        return mapping.findForward("showCandidacyValidateData");
    }

    public ActionForward prepareAlterCandidacyData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            addActionMessage(request, "error.no.candidacy", candidacyNumber.toString());
            return prepareValidateCandidacyData(mapping, actionForm, request, response);
        }

        request.setAttribute("personBean", new PersonBean(candidacy.getPerson()));

        PrecedentDegreeInformation precedentDegreeInformation = ((StudentCandidacy) candidacy).getPrecedentDegreeInformation();
        request.setAttribute("precedentDegreeInformation", new PrecedentDegreeInformationBean(precedentDegreeInformation));

        return mapping.findForward("showCandidacyAlterData");
    }

    public ActionForward schoolLevelPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        PrecedentDegreeInformationBean precedentDegreeInformationBean = getRenderedObject("precedentDegreeInformation");
        request.setAttribute("personBean", getRenderedObject("personBean"));
        RenderUtils.invalidateViewState();
        request.setAttribute("precedentDegreeInformation", precedentDegreeInformationBean);

        return mapping.findForward("showCandidacyAlterData");
    }

    public ActionForward alterCandidacyData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        PersonBean personBean = (PersonBean) getRenderedObject("personBean");
        try {
            personBean.save();            
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return mapping.findForward("showCandidacyAlterData");
        }
        
        PrecedentDegreeInformationBean precedentDegreeInformation =
                (PrecedentDegreeInformationBean) RenderUtils.getViewState("precedentDegreeInformation").getMetaObject()
                        .getObject();

        EditPrecedentDegreeInformation.run(precedentDegreeInformation);

        request.setAttribute("candidacyID", precedentDegreeInformation.getPrecedentDegreeInformation().getStudentCandidacy()
                .getExternalId());

        return mapping.findForward("alterSuccess");
    }

    public ActionForward validateData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        return goToNextState(mapping, actionForm, request, response, CandidacySituationType.STAND_BY_CONFIRMED_DATA.toString());
    }

    public ActionForward invalidateData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        return goToNextState(mapping, actionForm, request, response, CandidacySituationType.STAND_BY.toString());
    }

    private ActionForward goToNextState(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, String nextState) throws FenixActionException, FenixServiceException {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            throw new FenixActionException("invalid cadidacy number");
        }

        try {
            StateMachineRunner.run(new StateMachineRunner.RunnerArgs(candidacy.getActiveCandidacySituation(), nextState));
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("candidacy", candidacy);
            // return mapping.findForward("showCandidacyValidateData");
        }
        request.setAttribute("candidacyID", candidacy.getExternalId().toString());
        return viewCandidacy(mapping, actionForm, request, response);
    }

    public ActionForward viewCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String candidacyID = (String) getFromRequest(request, "candidacyID");

        Candidacy candidacy = FenixFramework.getDomainObject(candidacyID);
        if (candidacy == null) {
            throw new FenixActionException("error.invalid.candidacy.number");
        }

        storeCandidacyDataInRequest(request, candidacy);
        return mapping.findForward("viewCandidacyDetails");
    }

    public ActionForward prepareRegisterCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer candidacyNumber = Integer.valueOf((String) getFromRequest(request, "candidacyNumber"));

        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if (candidacy == null) {
            throw new FenixActionException("error.invalid.candidacy.number");
        }

        request.setAttribute("candidacy", candidacy);
        request.setAttribute("registerCandidacyBean", new RegisterCandidacyBean((StudentCandidacy) candidacy));
        ((DynaActionForm) actionForm).set("candidacyNumber", candidacyNumber);

        return mapping.findForward("candidacyRegistration");
    }

    public ActionForward registerCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        RegisterCandidacyBean registerCandidacyBean =
                (RegisterCandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();
        Candidacy candidacy = registerCandidacyBean.getCandidacy();

        try {
            RegisterCandidate.run(registerCandidacyBean);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("candidacy", candidacy);
            return mapping.findForward("candidacyRegistration");
        }

        request.setAttribute("candidacy", candidacy);
        return mapping.findForward("candidacyRegistrationSuccess");
    }

    public ActionForward cancelRegisterCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm candidacyForm = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) candidacyForm.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);

        storeCandidacyDataInRequest(request, candidacy);
        return mapping.findForward("viewCandidacyDetails");
    }

    public ActionForward printRegistrationInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm candidacyForm = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) candidacyForm.get("candidacyNumber");

        request.setAttribute("candidacy", Candidacy.readByCandidacyNumber(candidacyNumber));

        PortalLayoutInjector.skipLayoutOn(request);
        return mapping.findForward("printRegistrationInformation");
    }

    public ActionForward cancelCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        return goToNextState(mapping, actionForm, request, response, CandidacySituationType.CANCELLED.toString());
    }

}