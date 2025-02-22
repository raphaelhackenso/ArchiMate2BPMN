package at.campus02;

import java.util.List;
import org.javatuples.Pair;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputOutput;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaInputParameter;


/**
 * Use given Cockpit365 templates
 * 
 *
 */

public class ImplementedTemplates {

    // Helper functions ----------------------------
    private static String findProperty(List<Pair<String, String>> properties, String key, String defaultValue) {
        return properties.stream()
                .filter(pair -> key.equalsIgnoreCase(pair.getValue0()))
                .map(Pair::getValue1)
                .findFirst()
                .orElse(defaultValue);
    }


    private static void addInputParameter(BpmnModelInstance bpmnmodel, CamundaInputOutput inputOutput, String name, String value) {
        CamundaInputParameter inputParameter = bpmnmodel.newInstance(CamundaInputParameter.class);
            inputParameter.setAttributeValue("name", name, false);
            inputParameter.setTextContent(value);
            inputOutput.addChildElement(inputParameter);
    }


    private static void addRequiredInputParameter(BpmnModelInstance bpmnmodel, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties, String propertyName) {
        String value = findProperty(allProperties, propertyName, null);
        if (value == null || value.trim().isEmpty()) {
            Main.abort("Syntax Error! The required property '" + propertyName + "' is missing or empty!");
        }
        addInputParameter(bpmnmodel, inputOutput, propertyName, value);
    }
    // Helper functions ----------------------------


    // Analytics: Create / Update tracking entity
    public static void c365_appanalytics_updatetrackingentity(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_appanalytics::module_1_1-reports-reports_service::c365_appanalytics_updatetrackingentity");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_APPANALYTICS_UPDATETRACKINGENTITY_VISITORID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_APPANALYTICS_UPDATETRACKINGENTITY_PROCESSKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_APPANALYTICS_UPDATETRACKINGENTITY_MESSAGENAME");
        addInputParameter(bpmnmodel, inputOutput, "C365_APPANALYTICS_UPDATETRACKINGENTITY_FIRSTNAME", findProperty(allProperties, "C365_APPANALYTICS_UPDATETRACKINGENTITY_FIRSTNAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_APPANALYTICS_UPDATETRACKINGENTITY_LASTNAME", findProperty(allProperties, "C365_APPANALYTICS_UPDATETRACKINGENTITY_LASTNAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_APPANALYTICS_UPDATETRACKINGENTITY_EMAIL", findProperty(allProperties, "C365_APPANALYTICS_UPDATETRACKINGENTITY_EMAIL", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_APPANALYTICS_UPDATETRACKINGENTITY_PHONE", findProperty(allProperties, "C365_APPANALYTICS_UPDATETRACKINGENTITY_PHONE", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_APPANALYTICS_UPDATETRACKINGENTITY_USERAGENT", findProperty(allProperties, "C365_APPANALYTICS_UPDATETRACKINGENTITY_USERAGENT", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_APPANALYTICS_UPDATETRACKINGENTITY_USERAGENTFINGERPRINT", findProperty(allProperties, "C365_APPANALYTICS_UPDATETRACKINGENTITY_USERAGENTFINGERPRINT", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_APPANALYTICS_UPDATETRACKINGENTITY_OBJDATAMAP", findProperty(allProperties, "C365_APPANALYTICS_UPDATETRACKINGENTITY_OBJDATAMAP", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_APPANALYTICS_UPDATETRACKINGENTITY_PROCESSVARS", findProperty(allProperties, "C365_APPANALYTICS_UPDATETRACKINGENTITY_PROCESSVARS", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_APPANALYTICS_UPDATETRACKINGENTITY");
    }

    // Analytics: Link or create person object from analytics entity
    public static void c365_appanalytics_linkpersontoanalyticsentity(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_appanalytics::module_1_1-reports-reports_service::c365_appanalytics_linkpersontoanalyticsentity");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_APPANALYTICS_LINKPERSONTOANALYTICSENTITY_ANALYTICSENTITYID");
        addInputParameter(bpmnmodel, inputOutput, "C365_APPANALYTICS_LINKPERSONTOANALYTICSENTITY_OBJDATAMAP", findProperty(allProperties, "C365_APPANALYTICS_LINKPERSONTOANALYTICSENTITY_OBJDATAMAP", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_APPANALYTICS_LINKPERSONTOANALYTICSENTITY");
    }

    // Assign self-service process role to identity / person
    public static void c365_snippetorganizationdefault_addselfserviceprocessrole(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetorganizationdefault::module_1_1-reports-reports_service::c365_snippetorganizationdefault_addselfserviceprocessrole");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_SNIPPETORGANIZATIONDEFAULT_ASSIGNMENTREF");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_SNIPPETORGANIZATIONDEFAULT_PROCESSROLEREF");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_SNIPPETORGANIZATIONDEFAULT_ADDSELFSERVICEPROCESSROLE");
    }

    // Aufgabe
    public static void aufgabe(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "local::module_1_1-process-process_artefact::aufgabe");
    }

    // Aufgabe mit Details
    public static void aufgabemitdetails(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "local::module_1_1-process-process_artefact::aufgabemitdetails");
    }

    // Aufgabe mit Status
    public static void aufgabemitstatus(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "local::module_1_1-process-process_artefact::aufgabemitstatus");
    }

    // Basic relatinoship
    public static void ccmsetup_basicrelationship01(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetdtxsetup::module_1_1-reports-reports_service::ccmsetup_basicrelationship01");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_APPKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_TRANSFERPROFILEKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_SOURCECONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_SOURCEITEMTYPENAME");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_SOURCEITEMTYPEID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_TARGETCONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_TEMPLATECLASSS");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_TEMPLATEITEMTYPE");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CCMSETUP_BASICRELATIONSHIP01");
    }

    // Calendar: Create new schedule entry
    public static void c365_snippetschedulebase_createevent(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetschedulebase::module_1_1-reports-reports_service::c365_snippetschedulebase_createevent");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUMMARY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_SNIPPETSCHEDULEBASE_CREATEEVENT_EVENTRANGEFROM");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_SNIPPETSCHEDULEBASE_CREATEEVENT_EVENTRANGETO");
        addInputParameter(bpmnmodel, inputOutput, "C365_SNIPPETSCHEDULEBASE_CREATEEVENT_EVENTTYPEKEY", findProperty(allProperties, "C365_SNIPPETSCHEDULEBASE_CREATEEVENT_EVENTTYPEKEY", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_SNIPPETSCHEDULEBASE_CREATEEVENT");
    }

    // Capture comment / note
    public static void comm_createmessageforobject(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetcommmsgbase::module_1_1-reports-reports_service::comm_createmessageforobject");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUBJECT");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "DESCRIPTION");
        addInputParameter(bpmnmodel, inputOutput, "ENTITY", findProperty(allProperties, "ENTITY", ""));
        addInputParameter(bpmnmodel, inputOutput, "COCKPITID", findProperty(allProperties, "COCKPITID", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "COMM_CREATEMESSAGEFOROBJECT");
    }

    // Chat: Create chat channel
    public static void c365_snippetchatbase_createchatchannel(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetchatbase::module_1_1-reports-reports_service::c365_snippetchatbase_createchatchannel");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CHATCHANNELTYPE");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUMMARY");
        addInputParameter(bpmnmodel, inputOutput, "CHANNELPERMROLES", findProperty(allProperties, "CHANNELPERMROLES", ""));
        addInputParameter(bpmnmodel, inputOutput, "CHANNELPERM", findProperty(allProperties, "CHANNELPERM", ""));
        addInputParameter(bpmnmodel, inputOutput, "CHANNELPERMCONTR", findProperty(allProperties, "CHANNELPERMCONTR", ""));
        addInputParameter(bpmnmodel, inputOutput, "CHATCHANNELFOROBJECTINSTANCE", findProperty(allProperties, "CHATCHANNELFOROBJECTINSTANCE", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_SNIPPETCHATBASE_CREATECHATCHANNEL");
    }

    // Chat: Post message to channel
    public static void c365_snippetchatbase_postmsgtochannel(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetchatbase::module_1_1-reports-reports_service::c365_snippetchatbase_postmsgtochannel");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CHATMESSAGEFROMROLEID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CHATMESSAGETITLE");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CHATMESSAGE");
        addInputParameter(bpmnmodel, inputOutput, "CHATMESSAGEFROMCONTRID", findProperty(allProperties, "CHATMESSAGEFROMCONTRID", ""));
        addInputParameter(bpmnmodel, inputOutput, "CHATMESSAGETOROLEID", findProperty(allProperties, "CHATMESSAGETOROLEID", ""));
        addInputParameter(bpmnmodel, inputOutput, "CHATCHANNEL", findProperty(allProperties, "CHATCHANNEL", ""));
        addInputParameter(bpmnmodel, inputOutput, "RELATEDOBJECTS", findProperty(allProperties, "RELATEDOBJECTS", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_SNIPPETCHATBASE_POSTMSGTOCHANNEL");
    }

    // Complete test step
    public static void testing_completeteststep(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetprocesstesting::module_1_1-reports-reports_service::testing_completeteststep");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "TESTRESULTSTEPID");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "TESTING_COMPLETETESTSTEP");
    }

    // Copy data (e.g. attachments) directly
    public static void ccm_copydatadirectly(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetdtxsetup::module_1_1-reports-reports_service::ccm_copydatadirectly");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ITEMIDENTIFIER");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "TARGETCONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "TARGETITEMIDENTIFIER");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "COPYDATADETAILS");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CCM_COPYDATADIRECTLY");
    }

    // Create a Ticket / Requirement
    public static void ccm_createticket(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetdtxprocbase::module_1_1-reports-reports_service::ccm_createticket");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ENTITY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ITEMTYPENAME");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUMMARY");
        addInputParameter(bpmnmodel, inputOutput, "CCM_CREATETICKET-title", findProperty(allProperties, "CCM_CREATETICKET-title", ""));
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CCM_CREATETICKET");
    }

    // Create an ITSM change
    public static void services_createchangeblock(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemanagement::module_1_1-reports-reports_service::services_createchangeblock");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SERVICES_CREATECHANGEBLOCK_CHANGETYPE");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUMMARY");
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_CREATECHANGEBLOCK_STARTDATEPLAN", findProperty(allProperties, "SERVICES_CREATECHANGEBLOCK_STARTDATEPLAN", ""));
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_CREATECHANGEBLOCK_STARTDATEIS", findProperty(allProperties, "SERVICES_CREATECHANGEBLOCK_STARTDATEIS", ""));
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_CREATECHANGEBLOCK_ENDDATEPLAN", findProperty(allProperties, "SERVICES_CREATECHANGEBLOCK_ENDDATEPLAN", ""));
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_CREATECHANGEBLOCK_ENDDATEIS", findProperty(allProperties, "SERVICES_CREATECHANGEBLOCK_ENDDATEIS", ""));
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_CREATECHANGEBLOCK_FROMFINDING", findProperty(allProperties, "SERVICES_CREATECHANGEBLOCK_FROMFINDING", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "SERVICES_CREATECHANGEBLOCK");
    }

    // Create an ITSM event
    public static void services_createeventblock(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemanagement::module_1_1-reports-reports_service::services_createeventblock");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SERVICES_CREATEEVENTBLOCK_EVENTTYPE");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUMMARY");
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_CREATEEVENTBLOCK_AFFECTEDASSET", findProperty(allProperties, "SERVICES_CREATEEVENTBLOCK_AFFECTEDASSET", ""));
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "SERVICES_CREATEEVENTBLOCK");
    }

    // Create an ITSM incident
    public static void services_createincidentblock(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemanagement::module_1_1-reports-reports_service::services_createincidentblock");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SERVICES_CREATEINCIDENTBLOCK_INCIDENTTYPE");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUMMARY");
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_CREATEINCIDENTBLOCK_STARTDATEPLAN", findProperty(allProperties, "SERVICES_CREATEINCIDENTBLOCK_STARTDATEPLAN", ""));
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_CREATEINCIDENTBLOCK_STARTDATEIS", findProperty(allProperties, "SERVICES_CREATEINCIDENTBLOCK_STARTDATEIS", ""));
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_CREATEINCIDENTBLOCK_ENDDATEPLAN", findProperty(allProperties, "SERVICES_CREATEINCIDENTBLOCK_ENDDATEPLAN", ""));
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_CREATEINCIDENTBLOCK_ENDDATEIS", findProperty(allProperties, "SERVICES_CREATEINCIDENTBLOCK_ENDDATEIS", ""));
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_CREATEINCIDENTBLOCK_OBJDATAMAP", findProperty(allProperties, "SERVICES_CREATEINCIDENTBLOCK_OBJDATAMAP", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "SERVICES_CREATEINCIDENTBLOCK");
    }

    // Create an ITSM service request
    public static void services_createservicerequestblock(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemanagement::module_1_1-reports-reports_service::services_createservicerequestblock");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SERVICES_CREATESERVICEREQUESTBLOCK_REQUESTTYPE");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SERVICES_CREATESERVICEREQUESTBLOCK_SEVERITY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUMMARY");
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "SERVICES_CREATESERVICEREQUESTBLOCK");
    }

    // Create asset handover
    public static void c365_appasset_newhandover(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_appasset::module_1_1-reports-reports_service::c365_appasset_newhandover");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_APPASSET_FORIDENTITY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_APPASSET_ASSET");
        addInputParameter(bpmnmodel, inputOutput, "C365_APPASSET_DESCRIPTION", findProperty(allProperties, "C365_APPASSET_DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_APPASSET_NEWHANDOVER");
    }

    // Create case with budget details
    public static void c365_budgeting_budget_assignmenttask(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_budgeting_budget::module_1_1-reports-reports_service::c365_budgeting_budget_assignmenttask");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUMMARY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "DESCRIPTION");
        addInputParameter(bpmnmodel, inputOutput, "ASSCONTEXTKEY", findProperty(allProperties, "ASSCONTEXTKEY", ""));
        addInputParameter(bpmnmodel, inputOutput, "ASSCONTEXTRECORDID", findProperty(allProperties, "ASSCONTEXTRECORDID", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_BUDGETING_BUDGET_ASSIGNMENTTASK_SUPPLIERNAME", findProperty(allProperties, "C365_BUDGETING_BUDGET_ASSIGNMENTTASK_SUPPLIERNAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_BUDGETING_BUDGET_ASSIGNMENTTASK_COSTUNITIDENTIFIER", findProperty(allProperties, "C365_BUDGETING_BUDGET_ASSIGNMENTTASK_COSTUNITIDENTIFIER", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_BUDGETING_BUDGET_ASSIGNMENTTASK_COSTUNITNAME", findProperty(allProperties, "C365_BUDGETING_BUDGET_ASSIGNMENTTASK_COSTUNITNAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_BUDGETING_BUDGET_ASSIGNMENTTASK_RECORDSPERYEAR", findProperty(allProperties, "C365_BUDGETING_BUDGET_ASSIGNMENTTASK_RECORDSPERYEAR", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_BUDGETING_BUDGET_ASSIGNMENTTASK_AMOUNTNET", findProperty(allProperties, "C365_BUDGETING_BUDGET_ASSIGNMENTTASK_AMOUNTNET", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_BUDGETING_BUDGET_ASSIGNMENTTASK_VATPERC", findProperty(allProperties, "C365_BUDGETING_BUDGET_ASSIGNMENTTASK_VATPERC", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_BUDGETING_BUDGET_ASSIGNMENTTASK_EXPENSETYPENAME", findProperty(allProperties, "C365_BUDGETING_BUDGET_ASSIGNMENTTASK_EXPENSETYPENAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_BUDGETING_BUDGET_ASSIGNMENTTASK");
    }

    // Create checklist entry for task
    public static void task_addentrytochecklist(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippettaskdefault::module_1_1-reports-reports_service::task_addentrytochecklist");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "taskContextIdentifier");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "checklistEntryContextIdentifier");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUMMARY");
        addInputParameter(bpmnmodel, inputOutput, "TASK_ADDENTRYTOCHECKLIST-title", findProperty(allProperties, "TASK_ADDENTRYTOCHECKLIST-title", ""));
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "STATEGROUP", findProperty(allProperties, "STATEGROUP", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "TASK_ADDENTRYTOCHECKLIST");
    }

    // Create Content
    public static void content_createcontent(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetcontentbase::module_1_1-reports-reports_service::content_createcontent");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CONTEXTKEY");
        addInputParameter(bpmnmodel, inputOutput, "SUMMARY", findProperty(allProperties, "SUMMARY", ""));
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "PARENT", findProperty(allProperties, "PARENT", ""));
        addInputParameter(bpmnmodel, inputOutput, "ITEMIDENTIFIER", findProperty(allProperties, "ITEMIDENTIFIER", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CONTENT_CREATECONTENT");
    }

    // Create controlling booking
    public static void fico_costentry(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetfico::module_1_1-reports-reports_service::fico_costentry");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUMMARY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "FICO_COSTENTRYCOSTVALUE");
        addInputParameter(bpmnmodel, inputOutput, "CONTEXTKEY", findProperty(allProperties, "CONTEXTKEY", ""));
        addInputParameter(bpmnmodel, inputOutput, "ITEMIDENTIFIER", findProperty(allProperties, "ITEMIDENTIFIER", ""));
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "FICO_COSTENTRYCASEID", findProperty(allProperties, "FICO_COSTENTRYCASEID", ""));
        addInputParameter(bpmnmodel, inputOutput, "FICO_COSTENTRYPARENT", findProperty(allProperties, "FICO_COSTENTRYPARENT", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "FICO_COSTENTRY");
    }

    // Create deputy
    public static void organization_deputyform(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetorganizationdefault::module_1_1-reports-reports_service::organization_deputyform");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "DEPUTYFORID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "DEPUTYID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "DEPUTYSTARTAT");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "DEPUTYENDAT");
        addInputParameter(bpmnmodel, inputOutput, "ORGANIZATION_DEPUTYFORM-title", findProperty(allProperties, "ORGANIZATION_DEPUTYFORM-title", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "ORGANIZATION_DEPUTYFORM");
    }

    // Create event
    public static void schedule_createevent(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetschedulebase::module_1_1-reports-reports_service::schedule_createevent");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "EVENT_EVENTTYPE");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "EVENT_TITLE");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "EVENT_EVENTFROM");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "EVENT_EVENTUNTIL");
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "ASSIGNEE_ASSIGNMENT", findProperty(allProperties, "ASSIGNEE_ASSIGNMENT", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "SCHEDULE_CREATEEVENT");
    }

    // Create Jira Ticket
    public static void testresult_create_ticket(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetprocesstesting::module_1_1-reports-reports_service::testresult_create_ticket");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "TESTRESULT_CREATE_TICKET");
    }

    // Create links between/inside objects
    public static void links_createlink(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_base_links::module_1_1-reports-reports_service::links_createlink");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "LINKS_OBJID1");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "LINKS_LINKTYPE");
        addInputParameter(bpmnmodel, inputOutput, "LINKS_CREATELINK-title", findProperty(allProperties, "LINKS_CREATELINK-title", ""));
        addInputParameter(bpmnmodel, inputOutput, "LINKS_OBJID2", findProperty(allProperties, "LINKS_OBJID2", ""));
        addInputParameter(bpmnmodel, inputOutput, "REFS_INOBJ1", findProperty(allProperties, "REFS_INOBJ1", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "LINKS_CREATELINK");
    }

    // Create or update an ITSM node
    public static void services_cuinfranode(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemanagement::module_1_1-reports-reports_service::services_cuinfranode");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "COCKPITID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SERVICES_CUINFRANODE_HOSTNAME");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SERVICES_CUINFRANODE_IPADDR");
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_CUINFRANODE_NODETYPEREF", findProperty(allProperties, "SERVICES_CUINFRANODE_NODETYPEREF", ""));
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_CUINFRANODE_PARENTNODEREF", findProperty(allProperties, "SERVICES_CUINFRANODE_PARENTNODEREF", ""));
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_CUINFRANODE_OBJDATAMAP", findProperty(allProperties, "SERVICES_CUINFRANODE_OBJDATAMAP", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "SERVICES_CUINFRANODE");
    }

    // Create or update audit for entry tag
    public static void c365_auditmanagement_auditpollfortag(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_auditmanagement::module_1_1-reports-reports_service::c365_auditmanagement_auditpollfortag");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_AUDITMANAGEMENT_AUDITPOLLFORTAG_FORAUDITENTRY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_AUDITMANAGEMENT_AUDITPOLLFORTAG_FORTAG");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_AUDITMANAGEMENT_AUDITPOLLFORTAG_FOROBJECTINSTANCE");
        addInputParameter(bpmnmodel, inputOutput, "SUMMARY", findProperty(allProperties, "SUMMARY", ""));
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_AUDITMANAGEMENT_AUDITPOLLFORTAG_OBJDATAMAP", findProperty(allProperties, "C365_AUDITMANAGEMENT_AUDITPOLLFORTAG_OBJDATAMAP", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_AUDITMANAGEMENT_AUDITPOLLFORTAG");
    }

    // Create or update exposure
    public static void c365_govhub_exposure(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_govhub::module_1_1-reports-reports_service::c365_govhub_exposure");
        addInputParameter(bpmnmodel, inputOutput, "COCKPITID", findProperty(allProperties, "COCKPITID", ""));
        addInputParameter(bpmnmodel, inputOutput, "SUMMARY", findProperty(allProperties, "SUMMARY", ""));
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_GOVHUB_EXPOSURE_OBJDATAMAP", findProperty(allProperties, "C365_GOVHUB_EXPOSURE_OBJDATAMAP", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_GOVHUB_EXPOSURE");
    }

    // Create or update finding
    public static void c365_govhub_finding(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_govhub::module_1_1-reports-reports_service::c365_govhub_finding");
        addInputParameter(bpmnmodel, inputOutput, "COCKPITID", findProperty(allProperties, "COCKPITID", ""));
        addInputParameter(bpmnmodel, inputOutput, "SUMMARY", findProperty(allProperties, "SUMMARY", ""));
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_GOVHUB_FINDING_OBJDATAMAP", findProperty(allProperties, "C365_GOVHUB_FINDING_OBJDATAMAP", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_GOVHUB_FINDING");
    }

    // Create Order Entry
    public static void case_orderentry(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetcrmbase::module_1_1-reports-reports_service::case_orderentry");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CASE_AMOUNT");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUMMARY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CASE_UNIQUEKEY");
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "CASE_PRODUCT", findProperty(allProperties, "CASE_PRODUCT", ""));
        addInputParameter(bpmnmodel, inputOutput, "CASE_UNITPRICENET", findProperty(allProperties, "CASE_UNITPRICENET", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CASE_ORDERENTRY");
    }

    // Create person with details
    public static void organization_assignmentservicetask(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetorganizationdefault::module_1_1-reports-reports_service::organization_assignmentservicetask");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "FIRSTNAME");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "LASTNAME");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "EMAIL");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SMSNUMBER");
        addInputParameter(bpmnmodel, inputOutput, "ASSCONTEXTKEY", findProperty(allProperties, "ASSCONTEXTKEY", ""));
        addInputParameter(bpmnmodel, inputOutput, "ASSCONTEXTRECORDID", findProperty(allProperties, "ASSCONTEXTRECORDID", ""));
        addInputParameter(bpmnmodel, inputOutput, "DAMIDENTITYTYPEIDENTIFIER", findProperty(allProperties, "DAMIDENTITYTYPEIDENTIFIER", ""));
        addInputParameter(bpmnmodel, inputOutput, "TITLEBEFORENAME", findProperty(allProperties, "TITLEBEFORENAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "TITLEAFTERNAME", findProperty(allProperties, "TITLEAFTERNAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "EMPLOYEEID", findProperty(allProperties, "EMPLOYEEID", ""));
        addInputParameter(bpmnmodel, inputOutput, "USERNAME", findProperty(allProperties, "USERNAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "PASSWORD", findProperty(allProperties, "PASSWORD", ""));
        addInputParameter(bpmnmodel, inputOutput, "ASSIGNMENTCOMPANYNAME", findProperty(allProperties, "ASSIGNMENTCOMPANYNAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "ASSIGNMENTBUNAME", findProperty(allProperties, "ASSIGNMENTBUNAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "ASSIGNMENTBU", findProperty(allProperties, "ASSIGNMENTBU", ""));
        addInputParameter(bpmnmodel, inputOutput, "ASSIGNMENTPARENTBUNAME", findProperty(allProperties, "ASSIGNMENTPARENTBUNAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "ASSIGNMENTPARENTBUID", findProperty(allProperties, "ASSIGNMENTPARENTBUID", ""));
        addInputParameter(bpmnmodel, inputOutput, "ASSIGNMENTCOSTUNITIDENTIFIER", findProperty(allProperties, "ASSIGNMENTCOSTUNITIDENTIFIER", ""));
        addInputParameter(bpmnmodel, inputOutput, "ASSIGNMENTCOSTUNITNAME", findProperty(allProperties, "ASSIGNMENTCOSTUNITNAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "ASSIGNMENTPOSITION", findProperty(allProperties, "ASSIGNMENTPOSITION", ""));
        addInputParameter(bpmnmodel, inputOutput, "ASSIGNMENTPOSITIONIDENTIFIER", findProperty(allProperties, "ASSIGNMENTPOSITIONIDENTIFIER", ""));
        addInputParameter(bpmnmodel, inputOutput, "ASSIGNMENTPOSITIONNAME", findProperty(allProperties, "ASSIGNMENTPOSITIONNAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "ASSIGNMENTPOSITIONSORTORDER", findProperty(allProperties, "ASSIGNMENTPOSITIONSORTORDER", ""));
        addInputParameter(bpmnmodel, inputOutput, "SENDWELCOME", findProperty(allProperties, "SENDWELCOME", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "ORGANIZATION_ASSIGNMENTSERVICETASK");
    }

    // Create User
    public static void ccm_newuser(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetdtxprocbase::module_1_1-reports-reports_service::ccm_newuser");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "userId");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "userFrstname");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "userLastname");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "userEMail");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "userPassword");
        addInputParameter(bpmnmodel, inputOutput, "CCM_NEWUSER-title", findProperty(allProperties, "CCM_NEWUSER-title", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CCM_NEWUSER");
    }

    // Create work item link
    public static void ccm_createlink(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetdtxsetup::module_1_1-reports-reports_service::ccm_createlink");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ITEMIDENTIFIER");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "LINKTYPE");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "TARGETCONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "TARGETLINK");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CCM_CREATELINK");
    }

    // CRM Manager
    public static void crmmanager(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetcrmbase::module_1_1-process-process_role::crmmanager");
    }

    // CRM: Order Fast Entry
    public static void case_orderfastentry(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetcrmbase::module_1_1-reports-reports_service::case_orderfastentry");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CASE_TITLE");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CASE_TYPE");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "PERSON_FIRSTNAME");
        addInputParameter(bpmnmodel, inputOutput, "PERSON_LASTNAME", findProperty(allProperties, "PERSON_LASTNAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "PERSON_EMAIL_P", findProperty(allProperties, "PERSON_EMAIL_P", ""));
        addInputParameter(bpmnmodel, inputOutput, "PERSON_EMAIL_B", findProperty(allProperties, "PERSON_EMAIL_B", ""));
        addInputParameter(bpmnmodel, inputOutput, "PERSON_MOBILE_P", findProperty(allProperties, "PERSON_MOBILE_P", ""));
        addInputParameter(bpmnmodel, inputOutput, "PERSON_MOBILE_B", findProperty(allProperties, "PERSON_MOBILE_B", ""));
        addInputParameter(bpmnmodel, inputOutput, "PERSON_PHONE_P", findProperty(allProperties, "PERSON_PHONE_P", ""));
        addInputParameter(bpmnmodel, inputOutput, "PERSON_PHONE_B", findProperty(allProperties, "PERSON_PHONE_B", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CASE_ORDERFASTENTRY");
    }

    // Dump process execution data to container
    public static void datacontainer_dumpprocex(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_base_datacontainer::module_1_1-reports-reports_service::datacontainer_dumpprocex");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "DATACONTAINER_EXECUTIONSTOREID");
        addInputParameter(bpmnmodel, inputOutput, "DATACONTAINER_DUMPPROCEX-title", findProperty(allProperties, "DATACONTAINER_DUMPPROCEX-title", ""));
        addInputParameter(bpmnmodel, inputOutput, "DATACONTAINER_SUMMARY_DE", findProperty(allProperties, "DATACONTAINER_SUMMARY_DE", ""));
        addInputParameter(bpmnmodel, inputOutput, "DATACONTAINER_SUMMARY_EN", findProperty(allProperties, "DATACONTAINER_SUMMARY_EN", ""));
        addInputParameter(bpmnmodel, inputOutput, "DATACONTAINER_FOROBJID", findProperty(allProperties, "DATACONTAINER_FOROBJID", ""));
        addInputParameter(bpmnmodel, inputOutput, "DATACONTAINER_DATASOURCEKEY", findProperty(allProperties, "DATACONTAINER_DATASOURCEKEY", ""));
        addInputParameter(bpmnmodel, inputOutput, "DATACONTAINER_DATASOURCEDETAIL", findProperty(allProperties, "DATACONTAINER_DATASOURCEDETAIL", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "DATACONTAINER_DUMPPROCEX");
    }

    // Employee onboarding
    public static void hr_onboarding(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_hr_processes::module_1_1-process-process_processdescription::hr_onboarding");
    }

    // Erforderliche Maßnahme
    public static void erforderlichemaßnahme(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "local::module_1_1-process-process_artefact::erforderlichemaßnahme");
    }

    // Event Management
    public static void itsm_event_management(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemgmt_processes::module_1_1-process-process_processdescription::itsm_event_management");
    }

    // Event Management ArchiMate
    public static void eventmgmt1(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "local::module_1_1-process-process_processdescription::eventmgmt1");
    }

    // Event Monitoring (ITSM)
    public static void itsm_event_monitoring(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemgmt_processes::module_1_1-process-process_processdescription::itsm_event_monitoring");
    }

    // Event occured (ITSM)
    public static void itsm_event_occurred(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemgmt_processes::module_1_1-process-process_processdescription::itsm_event_occurred");
    }

    // Execute test step
    public static void testing_executetestcase(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetprocesstesting::module_1_1-reports-reports_service::testing_executetestcase");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "TESTRUNID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "TESTPROCESSKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "TESTPROFILE");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "TESTSTEPVARIABLENAME");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "TESTING_EXECUTETESTCASE");
    }

    // Get object data
    public static void c365_processesappx_getobjectdata(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_processesappx::module_1_1-reports-reports_service::c365_processesappx_getobjectdata");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_PROCESSESAPPX_GETOBJECTDATA_KEYVALUEPAIRLIST");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_PROCESSESAPPX_GETOBJECTDATA");
    }

    // Get object details
    public static void c365_processesappx_getobjectdetails(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_processesappx::module_1_1-reports-reports_service::c365_processesappx_getobjectdetails");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CLAZZ");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "COCKPITID");
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_PROCESSESAPPX_GETOBJECTDETAILS");
    }

    // HR Manager
    public static void hrmanager(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippethrbase::module_1_1-process-process_role::hrmanager");
    }

    // HR Process Map
    public static void hr_map(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_hr_processes::module_1_1-process-process_processdescription::hr_map");
    }

    // IAM Administrator
    public static void iamadmin(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_appdam::module_1_1-process-process_role::iamadmin");
    }

    // IAM Manager
    public static void iammanager(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_appdam::module_1_1-process-process_role::iammanager");
    }

    // IAM: Create account
    public static void c365_appdam_newaccount(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_appdam::module_1_1-reports-reports_service::c365_appdam_newaccount");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_APPDAM_IDENTITYREF");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_APPDAM_TECHSERVICESERF");
        addInputParameter(bpmnmodel, inputOutput, "C365_APPDAM_OBJDATAMAP", findProperty(allProperties, "C365_APPDAM_OBJDATAMAP", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_APPDAM_NEWACCOUNT");
    }

    // Incident
    public static void incident(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "local::module_1_1-process-process_artefact::incident");
    }

    // Incident Management
    public static void inc01(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "local::module_1_1-process-process_processdescription::inc01");
    }

    // Incident Management (ITSM)
    public static void itsm_incident_management(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemgmt_processes::module_1_1-process-process_processdescription::itsm_incident_management");
    }

    // Incident Manager
    public static void incidentmanager(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemanagement::module_1_1-process-process_role::incidentmanager");
    }

    // Incident Registration (ITSM)
    public static void itsm_incident_registration(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemgmt_processes::module_1_1-process-process_processdescription::itsm_incident_registration");
    }

    // Incident Resolution First Level (ITSM)
    public static void itsm_incident(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemgmt_processes::module_1_1-process-process_processdescription::itsm_incident");
    }

    // Invoicing
    public static void invoicing(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetcrmbase::module_1_1-process-process_role::invoicing");
    }

    // IT Operations Manager
    public static void itoperationsmanager(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemanagement::module_1_1-process-process_role::itoperationsmanager");
    }

    // IT Operator
    public static void itoperator(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemanagement::module_1_1-process-process_role::itoperator");
    }

    // KI Manager
    public static void aimanager(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_appai::module_1_1-process-process_role::aimanager");
    }

    // Kontakt für Behörden
    public static void authoritycontact(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_govhub::module_1_1-process-process_role::authoritycontact");
    }

    // Leeres XML
    public static void prozess1(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "local::module_1_1-process-process_processdescription::prozess1");
    }

    // Log sensor value
    public static void auditlog_logsensorvalue(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_sensorsbase::module_1_1-reports-reports_service::auditlog_logsensorvalue");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SENSORKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SENSORVALUE");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "AUDITLOG_LOGSENSORVALUE");
    }

    // Magellan Compliance Simulation ausführen
    public static void magellan_simulation(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "magellan_processes::module_1_1-process-process_processdescription::magellan_simulation");
    }

    // Magellan Maturity Simulation ausführen
    public static void magellan_mat_simulation(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "magellan_processes::module_1_1-process-process_processdescription::magellan_mat_simulation");
    }

    // Magellan: Gap Import
    public static void magellan_processes_gapimport(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "magellan_processes::module_1_1-reports-reports_service::magellan_processes_gapimport");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "MAGELLAN_PROCESSES_JSONFILEREF");
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "MAGELLAN_PROCESSES_GAPIMPORT");
    }

    // Magellan: KPI Import
    public static void magellan_processes_kpiimport(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "magellan_processes::module_1_1-reports-reports_service::magellan_processes_kpiimport");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "MAGELLAN_PROCESSES_JSONFILEREF");
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "MAGELLAN_PROCESSES_KPIIMPORT");
    }

    // Mail: Send E-Mail Message (with params)
    public static void comm_sendemailwithparams(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetcommmsgbase::module_1_1-reports-reports_service::comm_sendemailwithparams");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SENTFROMNAME");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SENDTOEMAILADDR");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUBJECT");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "DESCRIPTION");
        addInputParameter(bpmnmodel, inputOutput, "REPLYTOADDR", findProperty(allProperties, "REPLYTOADDR", ""));
        addInputParameter(bpmnmodel, inputOutput, "REPLYTONAME", findProperty(allProperties, "REPLYTONAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "COMM_SENDEMAILWITHPARAMS");
    }

    // Mail: Send E-Mail Message with Attachment
    public static void comm_sendemail_with_attachment(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetcommmsgbase::module_1_1-reports-reports_service::comm_sendemail_with_attachment");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SENDTOEMAILADDR");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUBJECT");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "DESCRIPTION");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ATT_FILENAME");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ATT_CONTENT");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ATT_FORMAT");
        addInputParameter(bpmnmodel, inputOutput, "LINKS_OBJECTS", findProperty(allProperties, "LINKS_OBJECTS", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "COMM_SENDEMAIL_WITH_ATTACHMENT");
    }

    // Mail: Send Message to all members of a role
    public static void comm_sendmail_processrole(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetcommmsgbase::module_1_1-reports-reports_service::comm_sendmail_processrole");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "COMM_SENDMAIL_PROCESSROLE_ROLEKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUBJECT");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "DESCRIPTION");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "COMM_SENDMAIL_PROCESSROLE");
    }

    // Manage finding
    public static void govfinding(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_govhub_processes::module_1_1-process-process_processdescription::govfinding");
    }

    // Manage organization
    public static void manage_org(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetorganizationdefault::module_1_1-process-process_role::manage_org");
    }

    // MessageStart
    public static void message1(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "local::module_1_1-process-process_processdescription::message1");
    }

    // Migrate processes to latest version
    public static void process_migratetolatestintent(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_processesappx::module_1_1-reports-reports_service::process_migratetolatestintent");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "PROCESS_MIGRATETOLATESTINTENT_PROCESSID");
        addInputParameter(bpmnmodel, inputOutput, "processMigrationIntentHeader", findProperty(allProperties, "processMigrationIntentHeader", ""));
        addInputParameter(bpmnmodel, inputOutput, "PROCESS_MIGRATETOLATESTINTENT_ACTIVITYMAPPING", findProperty(allProperties, "PROCESS_MIGRATETOLATESTINTENT_ACTIVITYMAPPING", ""));
        addInputParameter(bpmnmodel, inputOutput, "PROCESS_MIGRATETOLATESTINTENT_PROCESSVARS", findProperty(allProperties, "PROCESS_MIGRATETOLATESTINTENT_PROCESSVARS", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "PROCESS_MIGRATETOLATESTINTENT");
    }

    // Mitarbeitende Rolle
    public static void employee(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippethrbase::module_1_1-process-process_role::employee");
    }

    // Mitarbeiter tritt ein
    public static void mitarbeitertrittein(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "local::module_1_1-process-process_artefact::mitarbeitertrittein");
    }

    // Prepare app
    public static void ccmsetup_prepareapp(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetdtxsetup::module_1_1-reports-reports_service::ccmsetup_prepareapp");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_APPKEY");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CCMSETUP_PREPAREAPP");
    }

    // Process cvss
    public static void ssm(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_govhub_processes::module_1_1-process-process_processdescription::ssm");
    }

    // Process event
    public static void vorfallabwickeln(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_govhub_processes::module_1_1-process-process_processdescription::vorfallabwickeln");
    }

    // Process managers
    public static void processmanager(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_procbase::module_1_1-process-process_role::processmanager");
    }

    // Process start (generic and unique instance)
    public static void c365_processesappx_processstartuniquegeneric(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_processesappx::module_1_1-reports-reports_service::c365_processesappx_processstartuniquegeneric");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_PROCESSESAPPX_PROCESSSTARTUNIQUEGENERIC_PROCESSKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_PROCESSESAPPX_PROCESSSTARTUNIQUEGENERIC_PROCESSBUSKEY");
        addInputParameter(bpmnmodel, inputOutput, "C365_PROCESSESAPPX_PROCESSSTARTUNIQUEGENERIC_PROCESSVARS", findProperty(allProperties, "C365_PROCESSESAPPX_PROCESSSTARTUNIQUEGENERIC_PROCESSVARS", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_PROCESSESAPPX_PROCESSSTARTUNIQUEGENERIC");
    }

    // Process start (unique)
    public static void c365_processesappx_processstartgeneric(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_processesappx::module_1_1-reports-reports_service::c365_processesappx_processstartgeneric");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_PROCESSESAPPX_PROCESSSTARTGENERIC_PROCESSKEY");
        addInputParameter(bpmnmodel, inputOutput, "C365_PROCESSESAPPX_PROCESSSTARTGENERIC_PROCESSVARS", findProperty(allProperties, "C365_PROCESSESAPPX_PROCESSSTARTGENERIC_PROCESSVARS", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_PROCESSESAPPX_PROCESSSTARTGENERIC");
    }

    // Process: Create OTA login token
    public static void process_issueotatokenforuser(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_processesappx::module_1_1-reports-reports_service::process_issueotatokenforuser");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "PROCESS_ISSUEOTATOKENFORUSER_FORUSERNAME");
        addInputParameter(bpmnmodel, inputOutput, "PROCESS_ISSUEOTATOKENFORUSER_VALIDINMINS", findProperty(allProperties, "PROCESS_ISSUEOTATOKENFORUSER_VALIDINMINS", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "PROCESS_ISSUEOTATOKENFORUSER");
    }

    // Process: Send Message Event
    public static void process_triggereventintent(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_processesappx::module_1_1-reports-reports_service::process_triggereventintent");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "PROCESS_TRIGGEREVENTINTENT_MESSAGENAME");
        addInputParameter(bpmnmodel, inputOutput, "PROCESS_TRIGGEREVENTINTENT_MESSAGEVARS", findProperty(allProperties, "PROCESS_TRIGGEREVENTINTENT_MESSAGEVARS", ""));
        addInputParameter(bpmnmodel, inputOutput, "PROCESS_TRIGGEREVENTINTENT_PROCEXID", findProperty(allProperties, "PROCESS_TRIGGEREVENTINTENT_PROCEXID", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "PROCESS_TRIGGEREVENTINTENT");
    }

    // Produktmanagement
    public static void productmanager(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetfacilitybase::module_1_1-process-process_role::productmanager");
    }

    // Projekt Portfolio Manager
    public static void portfoliomgr(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetprojectbase::module_1_1-process-process_role::portfoliomgr");
    }

    // Projektleiter
    public static void projectleader(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetprojectbase::module_1_1-process-process_role::projectleader");
    }

    // Public website
    public static void publicwebsite(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetcmsbase::module_1_1-process-process_role::publicwebsite");
    }

    // Query data from remote tool as array
    public static void ccm_pullonlyasarray(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetdtxprocbase::module_1_1-reports-reports_service::ccm_pullonlyasarray");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ITEMIDENTIFIER");
        addInputParameter(bpmnmodel, inputOutput, "CCM_PULLONLYASARRAY-title", findProperty(allProperties, "CCM_PULLONLYASARRAY-title", ""));
        addInputParameter(bpmnmodel, inputOutput, "CUSTOMEXPR", findProperty(allProperties, "CUSTOMEXPR", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CCM_PULLONLYASARRAY");
    }

    // Query identity data
    public static void c365_appdam_getentitydetails(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_appdam::module_1_1-reports-reports_service::c365_appdam_getentitydetails");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_APPDAM_IDENTITYREF");
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_APPDAM_GETENTITYDETAILS");
    }

    // R - Eco System Maturity
    public static void magellan_processes_rmat(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "magellan_processes::module_1_1-reports-reports_service::magellan_processes_rmat");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "MMAG_ECOSYSID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "MMAG_QUESTIONNAIREID");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "MAGELLAN_PROCESSES_RMAT");
    }

    // R - Maturity Master Data
    public static void magellan_processes_rmat_masterdata(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "magellan_processes::module_1_1-reports-reports_service::magellan_processes_rmat_masterdata");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "MMAG_ECOSYSID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "MMAG_QUESTIONNAIREID");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "MAGELLAN_PROCESSES_RMAT_MASTERDATA");
    }

    // Record call
    public static void comm_createcallback(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetcommmsgbase::module_1_1-reports-reports_service::comm_createcallback");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "COMM_CREATECALLBACK_NOTESUBJECT");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "COMM_CREATECALLBACK_NOTEDETAIL");
        addInputParameter(bpmnmodel, inputOutput, "createMessageExplain", findProperty(allProperties, "createMessageExplain", ""));
        addInputParameter(bpmnmodel, inputOutput, "COMM_CREATECALLBACK_BUSINESSCASE", findProperty(allProperties, "COMM_CREATECALLBACK_BUSINESSCASE", ""));
        addInputParameter(bpmnmodel, inputOutput, "COMM_CREATECALLBACK_CALLBACKBY", findProperty(allProperties, "COMM_CREATECALLBACK_CALLBACKBY", ""));
        addInputParameter(bpmnmodel, inputOutput, "COMM_CREATECALLBACK_CALLBACKDUE", findProperty(allProperties, "COMM_CREATECALLBACK_CALLBACKDUE", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "COMM_CREATECALLBACK");
    }

    // Refresh test result
    public static void testing_refreshtestresult(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetprocesstesting::module_1_1-reports-reports_service::testing_refreshtestresult");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "TESTRUNID");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "TESTING_REFRESHTESTRESULT");
    }

    // Request login for assignment
    public static void c365_snippetorganizationdefault_refuserlogin(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetorganizationdefault::module_1_1-reports-reports_service::c365_snippetorganizationdefault_refuserlogin");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_SNIPPETORGANIZATIONDEFAULT_ASSIGNMENTREF");
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_SNIPPETORGANIZATIONDEFAULT_REFUSERLOGIN");
    }

    // Request next number
    public static void numeerator_getnextnumber(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_base_numerator::module_1_1-reports-reports_service::numeerator_getnextnumber");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "NUMEERATOR_NAME");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "NUMEERATOR_IDENTIFIER");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "NUMEERATOR_SUBIDENTIFIER");
        addInputParameter(bpmnmodel, inputOutput, "NUMEERATOR_GETNEXTNUMBER-title", findProperty(allProperties, "NUMEERATOR_GETNEXTNUMBER-title", ""));
        addInputParameter(bpmnmodel, inputOutput, "NUMEERATOR_OUTPUTFORMAT", findProperty(allProperties, "NUMEERATOR_OUTPUTFORMAT", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "NUMEERATOR_GETNEXTNUMBER");
    }

    // Return approver
    public static void organization_getapprover(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetorganizationdefault::module_1_1-reports-reports_service::organization_getapprover");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ORGANIZATION_GETAPPROVER_FORASSIGNEE");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ORGANIZATION_GETAPPROVER_VARNAME");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "ORGANIZATION_GETAPPROVER");
    }

    // Return tasks of type
    public static void c365_processesappx_unresolvedtasks(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_processesappx::module_1_1-reports-reports_service::c365_processesappx_unresolvedtasks");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_PROCESSESAPPX_UNRESOLVEDTASKS_PROCEXID");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_PROCESSESAPPX_UNRESOLVEDTASKS");
    }

    // Risikomanager
    public static void riskmanager(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_govhub::module_1_1-process-process_role::riskmanager");
    }

    // Run job
    public static void ccm_runjob(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetdtxprocbase::module_1_1-reports-reports_service::ccm_runjob");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "JOBKEY");
        addInputParameter(bpmnmodel, inputOutput, "CCM_RUNJOB-title", findProperty(allProperties, "CCM_RUNJOB-title", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CCM_RUNJOB");
    }

    // Run query in external tool
    public static void ccm_runquery(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetdtxprocbase::module_1_1-reports-reports_service::ccm_runquery");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "QUERYKEY");
        addInputParameter(bpmnmodel, inputOutput, "CCM_RUNQUERY-title", findProperty(allProperties, "CCM_RUNQUERY-title", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CCM_RUNQUERY");
    }

    // Run query with result count
    public static void ccm_runquerycount(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetdtxprocbase::module_1_1-reports-reports_service::ccm_runquerycount");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCM_QUERY");
        addInputParameter(bpmnmodel, inputOutput, "CCM_RUNQUERYCOUNT-title", findProperty(allProperties, "CCM_RUNQUERYCOUNT-title", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CCM_RUNQUERYCOUNT");
    }

    // Run remote job
    public static void worker_runjob(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetremotejobs::module_1_1-reports-reports_service::worker_runjob");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "JOBKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "WORKERKEY");
        addInputParameter(bpmnmodel, inputOutput, "P1NAME", findProperty(allProperties, "P1NAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "P1VALUE", findProperty(allProperties, "P1VALUE", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "WORKER_RUNJOB");
    }

    // Run test
    public static void testing_starttestrun(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetprocesstesting::module_1_1-reports-reports_service::testing_starttestrun");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "TESTRUNNAME");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "TESTCASEID");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "TESTING_STARTTESTRUN");
    }

    // Search for keywords (case insensitive)
    public static void c365_processesappx_searchkeywords(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_processesappx::module_1_1-reports-reports_service::c365_processesappx_searchkeywords");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_PROCESSESAPPX_SEARCHKEYWORDS_TEXT");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "C365_PROCESSESAPPX_SEARCHKEYWORDS_KEYWORDS");
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_PROCESSESAPPX_SEARCHKEYWORDS");
    }

    // Send E-Mail Message
    public static void comm_sendemail(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetcommmsgbase::module_1_1-reports-reports_service::comm_sendemail");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SENDTOEMAILADDR");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUBJECT");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "DESCRIPTION");
        addInputParameter(bpmnmodel, inputOutput, "CONTEXTOBJID", findProperty(allProperties, "CONTEXTOBJID", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "COMM_SENDEMAIL");
    }

    // Send E-Mail message to assignments
    public static void organization_sendtoassignments(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetorganizationdefault::module_1_1-reports-reports_service::organization_sendtoassignments");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ASSIGNMENTS");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUBJECT");
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "ORGANIZATION_SENDTOASSIGNMENTS");
    }

    // Send Message
    public static void comm_sendmessage(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetmessagewithservice::module_1_1-reports-reports_service::comm_sendmessage");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SENDTOUSERID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "DESCRIPTION");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "COMM_SENDMESSAGE");
    }

    // Send Message to all members of a role (with attachment)
    public static void comm_sendmail_processrole_with_attachment(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetcommmsgbase::module_1_1-reports-reports_service::comm_sendmail_processrole_with_attachment");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "COMM_SENDMAIL_PROCESSROLE_WITH_ATTACHMENT_ROLEKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUBJECT");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "DESCRIPTION");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ATT_FILENAME");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ATT_CONTENT");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "COMM_SENDMAIL_PROCESSROLE_WITH_ATTACHMENT");
    }

    // Send Mobile Push Message
    public static void comm_sendpushnotification(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetcommmsgbase::module_1_1-reports-reports_service::comm_sendpushnotification");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "pushNotificationSubscriptionId");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "pushNotificationText");
        addInputParameter(bpmnmodel, inputOutput, "pushNotificationPayload", findProperty(allProperties, "pushNotificationPayload", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "COMM_SENDPUSHNOTIFICATION");
    }

    // Send SMS via Mailjet
    public static void c365_mailjetintegration_sendsms(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_mailjetintegration::module_1_1-reports-reports_service::c365_mailjetintegration_sendsms");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SMSAPITOKEN");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SMSTONUMBER");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "DESCRIPTION");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_MAILJETINTEGRATION_SENDSMS");
    }


    // Service Requests
    public static void c365_servicemanagement_reporting_requestsreport(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemanagement_reporting::module_1_1-reports-reports_service::c365_servicemanagement_reporting_requestsreport");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_SERVICEMANAGEMENT_REPORTING_REQUESTSREPORT");
    }

    // Start activity with FlowID
    public static void process_triggeractivitybyflowid(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_processesappx::module_1_1-reports-reports_service::process_triggeractivitybyflowid");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "PROCESS_TRIGGERACTIVITYBYFLOWID_PROCEXID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "PROCESS_TRIGGERACTIVITYBYFLOWID_FLOWID");
        addInputParameter(bpmnmodel, inputOutput, "processMigrationIntentHeader", findProperty(allProperties, "processMigrationIntentHeader", ""));
        addInputParameter(bpmnmodel, inputOutput, "PROCESS_TRIGGERACTIVITYBYFLOWID_PROCESSVARS", findProperty(allProperties, "PROCESS_TRIGGERACTIVITYBYFLOWID_PROCESSVARS", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "PROCESS_TRIGGERACTIVITYBYFLOWID");
    }

    // Start job (Service)
    public static void remote_requestjobexecutionservice(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetremotejobs::module_1_1-reports-reports_service::remote_requestjobexecutionservice");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "JOBKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "WORKER");
        addInputParameter(bpmnmodel, inputOutput, "JOBPARAMS", findProperty(allProperties, "JOBPARAMS", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "REMOTE_REQUESTJOBEXECUTIONSERVICE");
    }

    // System Administration (Root)
    public static void sysadmin_root(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_sysadmindefault::module_1_1-process-process_role::sysadmin_root");
    }


    // Tool query
    public static void ccmsetup_query01(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetdtxsetup::module_1_1-reports-reports_service::ccmsetup_query01");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_APPKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_QUERYNAME");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_SOURCECONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_QUERYKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_QUERYSTRING");
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CCMSETUP_QUERY01");
    }

    // Track log event
    public static void c365_processesappx_activitylog_trackevent(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_processesappx::module_1_1-reports-reports_service::c365_processesappx_activitylog_trackevent");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUMMARY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "COCKPITID");
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_PROCESSESAPPX_ACTIVITYLOG_TRACKEVENT_EVENTTYPEKEY", findProperty(allProperties, "C365_PROCESSESAPPX_ACTIVITYLOG_TRACKEVENT_EVENTTYPEKEY", ""));
        addInputParameter(bpmnmodel, inputOutput, "C365_PROCESSESAPPX_ACTIVITYLOG_TRACKEVENT_OBJDATAMAP", findProperty(allProperties, "C365_PROCESSESAPPX_ACTIVITYLOG_TRACKEVENT_OBJDATAMAP", ""));
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "C365_PROCESSESAPPX_ACTIVITYLOG_TRACKEVENT");
    }

    // Transfer profile entry
    public static void ccmsetup_transferprofileentry01(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetdtxsetup::module_1_1-reports-reports_service::ccmsetup_transferprofileentry01");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_APPKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_TRANSFERPROFILEKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_TRANSFERPROFILEID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_TRANSFERPROFILESORTORDER");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CCMSETUP_TRANSFERPROFILETRANSFERTYPE");
        addInputParameter(bpmnmodel, inputOutput, "CCMSETUP_TRANSFERPROFILEDEFAULTFIELDTOOL1", findProperty(allProperties, "CCMSETUP_TRANSFERPROFILEDEFAULTFIELDTOOL1", ""));
        addInputParameter(bpmnmodel, inputOutput, "CCMSETUP_TRANSFERPROFILEDEFAULTFIELDTOOL1CFNAME", findProperty(allProperties, "CCMSETUP_TRANSFERPROFILEDEFAULTFIELDTOOL1CFNAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "CCMSETUP_TRANSFERPROFILEDEFAULTFIELDTOOL2", findProperty(allProperties, "CCMSETUP_TRANSFERPROFILEDEFAULTFIELDTOOL2", ""));
        addInputParameter(bpmnmodel, inputOutput, "CCMSETUP_TRANSFERPROFILEDEFAULTFIELDTOOL2CFNAME", findProperty(allProperties, "CCMSETUP_TRANSFERPROFILEDEFAULTFIELDTOOL2CFNAME", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CCMSETUP_TRANSFERPROFILEENTRY01");
    }

    // Transition state
    public static void ccm_transitionstate(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetdtxprocbase::module_1_1-reports-reports_service::ccm_transitionstate");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ENTITY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ITEMIDENTIFIER");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "TARGETSTATE");
        addInputParameter(bpmnmodel, inputOutput, "CCM_TRANSITIONSTATE-title", findProperty(allProperties, "CCM_TRANSITIONSTATE-title", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CCM_TRANSITIONSTATE");
    }

    // Trigger audit
    public static void audittrigger(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_auditmanagement_processes::module_1_1-process-process_processdescription::audittrigger");
    }

    // Update an ITSM element
    public static void services_updateblock(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemanagement::module_1_1-reports-reports_service::services_updateblock");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "COCKPITID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ENTITY");
        addInputParameter(bpmnmodel, inputOutput, "VARPREFIX", findProperty(allProperties, "VARPREFIX", ""));
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_UPDATEBLOCK_OBJDATAMAP", findProperty(allProperties, "SERVICES_UPDATEBLOCK_OBJDATAMAP", ""));
        addInputParameter(bpmnmodel, inputOutput, "SERVICES_UPDATEBLOCK_RESULTMAP", findProperty(allProperties, "SERVICES_UPDATEBLOCK_RESULTMAP", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "SERVICES_UPDATEBLOCK");
    }

    // Update an ITSM event
    public static void services_updateeventblock(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_servicemanagement::module_1_1-reports-reports_service::services_updateeventblock");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SERVICES_UPDATEEVENTBLOCK_EVENTTYPE");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "COCKPITID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUMMARY");
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "STATEGROUP", findProperty(allProperties, "STATEGROUP", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "SERVICES_UPDATEEVENTBLOCK");
    }

    // Update data in remote tool
    public static void ccm_updateremotetooldata(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetdtxsetup::module_1_1-reports-reports_service::ccm_updateremotetooldata");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ENTITY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CONTEXTKEY");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "ITEMIDENTIFIER");
        addInputParameter(bpmnmodel, inputOutput, "CCM_UPDATEREMOTETOOLDATA-title", findProperty(allProperties, "CCM_UPDATEREMOTETOOLDATA-title", ""));
        addInputParameter(bpmnmodel, inputOutput, "CUSTOMEXPR", findProperty(allProperties, "CUSTOMEXPR", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "CCM_UPDATEREMOTETOOLDATA");
    }

    // Update status and basic data
    public static void status_updatestatuswithdatablock(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "c365_snippetstatusmgmt::module_1_1-reports-reports_service::status_updatestatuswithdatablock");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "CLAZZ");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "COCKPITID");
        addRequiredInputParameter(bpmnmodel, inputOutput, allProperties, "SUMMARY");
        addInputParameter(bpmnmodel, inputOutput, "DESCRIPTION", findProperty(allProperties, "DESCRIPTION", ""));
        addInputParameter(bpmnmodel, inputOutput, "STATEGROUP", findProperty(allProperties, "STATEGROUP", ""));
        addInputParameter(bpmnmodel, inputOutput, "STATE", findProperty(allProperties, "STATE", ""));
        addInputParameter(bpmnmodel, inputOutput, "STATUS_UPDATESTATUSWITHDATABLOCK_OBJDATAMAP", findProperty(allProperties, "STATUS_UPDATESTATUSWITHDATABLOCK_OBJDATAMAP", ""));
        addInputParameter(bpmnmodel, inputOutput, "serviceKey", "STATUS_UPDATESTATUSWITHDATABLOCK");
    }

    // Vorlage Change (modal)
    public static void vorlagechangemodal(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "local::module_1_1-process-process_artefact::vorlagechangemodal");
    }

    // Vorlage Gefährdung (modal)
    public static void vorlagegefährdungmodal(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "local::module_1_1-process-process_artefact::vorlagegefährdungmodal");
    }

    // Vorlage Incident Modal
    public static void vorlageincidentmodal(BpmnModelInstance bpmnmodel, ServiceTask tmpServiceTask, ExtensionElements extensionElements, CamundaInputOutput inputOutput, List<Pair<String, String>> allProperties) {
        tmpServiceTask.setAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "modelerTemplate", "local::module_1_1-process-process_artefact::vorlageincidentmodal");
    }


}