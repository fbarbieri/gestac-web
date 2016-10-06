package ort.proyecto.gestac.core.agents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;

public class IssueManagementAgent extends GestacAgent {
	
	private static final long serialVersionUID = 1L;
	
	private Logger logger = LoggerFactory.getLogger(IssueManagementAgent.class);
	private Logger agentsLogger = LoggerFactory.getLogger("agents-activity");

	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new QueryBehaviour());
	}
	
	class QueryBehaviour extends CyclicBehaviour {
		@Override
		public void action() {
			ACLMessage message = receive();
			if (message!=null) {
				try {
					agentsLogger.debug(this.myAgent.getName() + ", message recieved: " + message.getContent() + ", conversationId:" + message.getConversationId());
					String[] parameters = message.getContent().split("&");
					String operation = parameters[0];
					switch (operation){
					case "getIssuesForSourceWithoutKnowledge":
						ACLMessage issuesForSourceMessage = createMessage("IssueDBAgent");
						issuesForSourceMessage.setContent(DBAgentOperations.GET_ISSUES_FOR_SOURCE_WITHOUT_KNOWLEDGE+
								"&"+parameters[1]+"&"+parameters[2]);
						send(issuesForSourceMessage);
						agentsLogger.debug(this.myAgent.getName() + ", message to IssueDBAgent: " + issuesForSourceMessage.getContent() + ", conversationId:" + issuesForSourceMessage.getConversationId());
						ACLMessage fromDB = blockingReceive(MessageTemplate.MatchConversationId(issuesForSourceMessage.getConversationId()));
						agentsLogger.debug(this.myAgent.getName() + ", reply from IssueDBAgent: " + fromDB.getContent() + ", conversationId:" + fromDB.getConversationId());
						sendReply(fromDB.getContent(), message);
						break;
					case "getIssuesForSourceWithKnowledgeByOtherSource":
						ACLMessage issuesForSourceWithKnowledgeMessage = createMessage("IssueDBAgent");
						issuesForSourceWithKnowledgeMessage.setContent(DBAgentOperations.GET_ISSUES_WITH_KNOWLEDGE_BY_OTHER_SOURCE+
								"&"+parameters[1]+"&"+parameters[2]);
						send(issuesForSourceWithKnowledgeMessage);
						agentsLogger.debug(this.myAgent.getName() + ", message to IssueDBAgent: " + issuesForSourceWithKnowledgeMessage.getContent() + ", conversationId:" + issuesForSourceWithKnowledgeMessage.getConversationId());
						ACLMessage issuesForSourceWithKnowledgeReply = blockingReceive(MessageTemplate.MatchConversationId(
								issuesForSourceWithKnowledgeMessage.getConversationId()));
						agentsLogger.debug(this.myAgent.getName() + ", reply from IssueDBAgent: " + issuesForSourceWithKnowledgeReply.getContent() + ", conversationId:" + issuesForSourceWithKnowledgeReply.getConversationId());
						sendReply(issuesForSourceWithKnowledgeReply.getContent(), message);
						break;
					case "getIssuesWithKnowledgeBySource":
						ACLMessage issuesWithKnowledgeByTheSource = createMessage("IssueDBAgent");
						issuesWithKnowledgeByTheSource.setContent(DBAgentOperations.GET_ISSUES_WITH_KNOWLEDGE_BY_SOURCE+
								"&"+parameters[1]+"&"+parameters[2]);
						send(issuesWithKnowledgeByTheSource);
						agentsLogger.debug(this.myAgent.getName() + ", message to IssueDBAgent: " + issuesWithKnowledgeByTheSource.getContent() + ", conversationId:" + issuesWithKnowledgeByTheSource.getConversationId());
						ACLMessage issuesWithKnowledgeByTheSourceReply = blockingReceive(MessageTemplate.MatchConversationId(
								issuesWithKnowledgeByTheSource.getConversationId()));
						agentsLogger.debug(this.myAgent.getName() + ", reply from IssueDBAgent: " + issuesWithKnowledgeByTheSourceReply.getContent() + ", conversationId:" + issuesWithKnowledgeByTheSourceReply.getConversationId());
						sendReply(issuesWithKnowledgeByTheSourceReply.getContent(), message);
						break;
					}
				} catch (Exception e) {
					String operation = message.getContent()!=null?message.getContent():"";
					logger.error("Error for opertaion " + operation, e);
					sendEmptyReply(message);
				}
			} else {
				agentsLogger.debug(this.myAgent.getName() + ", called block(), waiting for messages");
				block();
			}
		}
	}
}
