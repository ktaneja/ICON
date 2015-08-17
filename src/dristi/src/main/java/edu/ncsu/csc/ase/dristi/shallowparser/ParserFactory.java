package edu.ncsu.csc.ase.dristi.shallowparser;

import java.util.HashMap;

public class ParserFactory 
{
	private HashMap<String, AbstractParser> parserMap = new HashMap<String, AbstractParser>();
	
	private static ParserFactory instance;
	
	
	public synchronized static ParserFactory getInstance() {
		if(instance == null)
		{
			instance = new ParserFactory();
			instance.parserMap.put("abbrev", AbbrevParser.getInstance());
			instance.parserMap.put("acomp", AcompParser.getInstance());
			instance.parserMap.put("advcl", AdvclParser.getInstance());
			instance.parserMap.put("advmod", AdvmodParser.getInstance());
			instance.parserMap.put("agent", AgentParser.getInstance());
			instance.parserMap.put("amod", AmodParser.getInstance());
			instance.parserMap.put("appos", ApposParser.getInstance());
			instance.parserMap.put("aux", AuxParser.getInstance());
			instance.parserMap.put("auxpass", AuxPassParser.getInstance());
			
			instance.parserMap.put("ccomp", CcompParser.getInstance());
			instance.parserMap.put("cc", CcParser.getInstance());
			instance.parserMap.put("complm", ComplmParser.getInstance());
			instance.parserMap.put("conj", ConjParser.getInstance());
			instance.parserMap.put("cop", CopParser.getInstance());
			instance.parserMap.put("csubj", CsubjParser.getInstance());
			instance.parserMap.put("csubjpass", CsubjPassParser.getInstance());
			
			instance.parserMap.put("dep", DepParser.getInstance());
			instance.parserMap.put("det", DetParser.getInstance());
			instance.parserMap.put("dobj", DobjParser.getInstance());
			
			instance.parserMap.put("expl", ExplParser.getInstance());
			
			instance.parserMap.put("infmod", InfmodParser.getInstance());
			instance.parserMap.put("iobj", IobjParser.getInstance());
			
			instance.parserMap.put("mark", MarkParser.getInstance());
			
			instance.parserMap.put("neg", NegParser.getInstance());
			instance.parserMap.put("nn", NnParser.getInstance());
			instance.parserMap.put("npadvmod", NpadvmodParser.getInstance());
			instance.parserMap.put("nsubj", NSubjParser.getInstance());
			instance.parserMap.put("nsubjpass", NSubjPassParser.getInstance());
			instance.parserMap.put("num", NumParser.getInstance());
			instance.parserMap.put("number", NumberParser.getInstance());
			
			instance.parserMap.put("partmod", PartmodParser.getInstance());
			instance.parserMap.put("pobj", PobjParser.getInstance());
			instance.parserMap.put("possessive", PossessiveParser.getInstance());
			instance.parserMap.put("poss", PossParser.getInstance());
			instance.parserMap.put("preconj", PreconjParser.getInstance());
			instance.parserMap.put("predet", PredetParser.getInstance());
			instance.parserMap.put("prepc", PrepcParser.getInstance());
			instance.parserMap.put("prep", PrepParser.getInstance());
			instance.parserMap.put("prt", PrtParser.getInstance());
			instance.parserMap.put("purpcl", PurpclParser.getInstance());
			
			instance.parserMap.put("quantmod", QuantmodParser.getInstance());
			
			instance.parserMap.put("rel", RelParser.getInstance());
			instance.parserMap.put("rcmod", RcmodParser.getInstance());
			instance.parserMap.put("root", RootParser.getInstance());
			
			instance.parserMap.put("tmod", TmodParser.getInstance());
			instance.parserMap.put("vmod", VmodParser.getInstance());
			instance.parserMap.put("xcomp", XcompParser.getInstance());
			
			
		}
		return instance;
	}
	
	public AbstractParser getParser(String dep)
	{
		return parserMap.get(dep);
	}
	
}
