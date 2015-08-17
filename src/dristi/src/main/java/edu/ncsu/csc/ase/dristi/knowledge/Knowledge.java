package edu.ncsu.csc.ase.dristi.knowledge;

import java.util.HashMap;

public class Knowledge {
	
	private static HashMap<String, KnowledgeAtom> knowledgeMap;
	
	private static Knowledge instance;

	
	public static synchronized Knowledge getInstance() {
		if (instance == null) {
			instance = new Knowledge();
		}

		return instance;
	}

	private Knowledge() {
		knowledgeMap = new HashMap<String, KnowledgeAtom>();
		addDefaultAtoms();
	}
	
	public void addKnowledgeAtom(KnowledgeAtom atom)
	{
		knowledgeMap.put(atom.getName(), atom);
	}
	
	private void addDefaultAtoms() {
		KnowledgeAtom contactAtom = new KnowledgeAtom("contact");
		contactAtom.setActions("display", "view", "read", "access", "search",
				"load", "get", "share", "send", "set", "select", "update",
				"add", "delete");
		contactAtom.setProperties("number", "email", "location", "birthday",
				"anniversary", "widget");
		contactAtom.setSynonyms("address book", "adress book", "addressbook",
				"adressbook");

		KnowledgeAtom smsAtom = new KnowledgeAtom("sms");
		smsAtom.setActions("display", "view", "read", "access", "send",
				"receive", "share", "search");
		smsAtom.setProperties("number", "text", "content");
		smsAtom.setDependants(contactAtom);
		smsAtom.setSynonyms("message", "text message", "IM", "text");

		KnowledgeAtom emailAtom = new KnowledgeAtom("email");
		emailAtom.setActions("display", "view", "read", "access", "send",
				"receive", "share", "search");
		emailAtom.setProperties("number", "text", "content");
		emailAtom.setDependants(contactAtom);
		emailAtom.setSynonyms("mail");

		KnowledgeAtom micAtom = new KnowledgeAtom("mic");
		micAtom.setActions("record", "say", "talk");
		micAtom.setProperties("audio");
		micAtom.setSynonyms("microphone");

		KnowledgeAtom audioAtom = new KnowledgeAtom("audio");
		audioAtom.setActions("record", "start", "access", "resume", "start",
				"recognize", "cancel", "stop", "pause");
		audioAtom.setProperties("video");
		audioAtom.setSynonyms("voice", "sound", "recording", "speech", "noise");

		KnowledgeAtom calendarAtom = new KnowledgeAtom("calendar");
		calendarAtom.setActions("search", "get", "query", "update", "delete",
				"send", "view", "create", "schedule", "format", "access",
				"dismiss", "set", "save", "display", "sync");
		calendarAtom.setProperties("alert", "alarm", "timezone", "agenda",
				"event", "Attendee", "reminder");
		calendarAtom.setSynonyms("schedule");

		KnowledgeAtom eventAtom = new KnowledgeAtom("event");
		eventAtom.setActions("search", "get", "update", "delete", "send",
				"create", "schedule", "access", "dismiss", "set", "save",
				"view", "read", "display", "add", "repeat");
		eventAtom.setProperties("alert", "alarm", "timezone", "agenda",
				"Attendee", "reminder");
		eventAtom.setSynonyms("meeting", "task", "todo", "TO-DO", "agenda",
				"appointment", "reminder");

		KnowledgeAtom attendeeAtom = new KnowledgeAtom("Attendee");
		attendeeAtom.setActions("search", "get", "update", "delete", "send",
				"create", "schedule", "access", "dismiss", "set", "save",
				"view", "read", "display");
		attendeeAtom.setProperties("alert", "alarm", "timezone", "agenda",
				"Attendee", "reminder");
		attendeeAtom.setSynonyms("meeting", "task", "todo", "TO-DO", "agenda",
				"appointment");

		knowledgeMap.put("contact", contactAtom);
		knowledgeMap.put("sms", smsAtom);
		knowledgeMap.put("email", emailAtom);
		knowledgeMap.put("mic", micAtom);
		knowledgeMap.put("audio", audioAtom);
		knowledgeMap.put("calendar", calendarAtom);
		knowledgeMap.put("event", eventAtom);

	}

	public KnowledgeAtom fetchAtom(String atom) {
		return knowledgeMap.get(atom);
	}

}
