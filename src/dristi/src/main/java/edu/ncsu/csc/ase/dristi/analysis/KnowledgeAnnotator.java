package edu.ncsu.csc.ase.dristi.analysis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.ase.dristi.NLPParser;
import edu.ncsu.csc.ase.dristi.TextAnalysisEngine;
import edu.ncsu.csc.ase.dristi.datastructure.ITuple;
import edu.ncsu.csc.ase.dristi.datastructure.Tuple;
import edu.ncsu.csc.ase.dristi.heuristics.semantic.WordNetSimilarity;
import edu.ncsu.csc.ase.dristi.knowledge.Knowledge;
import edu.ncsu.csc.ase.dristi.knowledge.KnowledgeAtom;
import edu.stanford.nlp.semgraph.SemanticGraph;

public class KnowledgeAnnotator 
{

	public boolean isFunctionalityText(String text) {
		NLPParser parser = NLPParser.getInstance();
		List<SemanticGraph> dependencyTreeList = parser
				.getStanfordDependencies(text);
		for (SemanticGraph dependencyTree : dependencyTreeList) {
			Tuple tuple = (Tuple)TextAnalysisEngine.parse(dependencyTree);
			if (isDescribingFunctnality(tuple))
				return true;
		}
		return false;
	}

	public static void main(String[] args) {
		KnowledgeAnnotator k = new KnowledgeAnnotator();
		try {
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			System.out.println("Enter The Sentnce =>");
			String text = br.readLine();
			while (text.length() > 0) {
				if (k.isFunctionalityText(text)) {
					System.err.println("Describes Permission!");
				} else {
					System.err.println("Does Not Describe Permission!");
				}
				System.out.println("Enter The Sentnce =>");
				text = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean isDescribingFunctnality(Tuple t) {
		if (t == null)
			return false;
		Knowledge k = Knowledge.getInstance();
		KnowledgeAtom a = k.fetchAtom("calendar");
		ArrayList<ITuple> tList = searchAtom(t, a.getSynonyms(), new ArrayList<ITuple>());
		for (ITuple t1 : tList) {
			if ((t1 != null)
					&& (isUserOwned(t, t1) || isAction(a.getActions(), t, t1))
					|| isAugumentedAction(a.getActions(), t, t1)) {
				return true;
			}
		}
		a = k.fetchAtom("event");
		tList = searchAtom(t, a.getSynonyms(), new ArrayList<ITuple>());
		for (ITuple t1 : tList) {
			if ((t1 != null)
					&& (isUserOwned(t, t1) || isAction(a.getActions(), t, t1))
					|| isAction(a.getActions(), t, t1))
				return true;
		}
		return false;
	}

	private static boolean isAugumentedAction(List<String> actions,
			Tuple t, ITuple t1) {
		String s = "";
		if (t1.isTerminal())
			s = t1.getEntity().getName();
		else
			s = t1.getRelation().getName();
		for (String action : actions) {
			if (s.contains(action)) {
				System.err
						.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				return true;
			}
		}
		return false;
	}

	private static boolean isUserOwned(Tuple t1, ITuple t2) {
		ITuple t_parent = t1.findParent(t2);
		ITuple t_Sibling = t1.findSibling(t2);
		String comp_Value = "";
		if ((t_parent != null) && !t_parent.same(t2)) {
			comp_Value = t_parent.getRelation().getName();
			try {
				if (WordNetSimilarity.getInstance()
						.isSimilar(comp_Value, "and")
						|| WordNetSimilarity.getInstance().isSimilar(
								comp_Value, "or")
						|| WordNetSimilarity.getInstance().isSimilar(
								comp_Value, "in")) {
					return isUserOwned(t1, t_parent);
				} else if (WordNetSimilarity.getInstance().isSimilar(
						comp_Value, "owned")
						|| WordNetSimilarity.getInstance().isSimilar(
								comp_Value, "is")) {
					if (t_Sibling.isTerminal()) {
						if (WordNetSimilarity.getInstance().isSimilar(
								t_Sibling.getEntity().getName(), "user"))
							return true;
						else if (WordNetSimilarity.getInstance().isSimilar(
								t_Sibling.getEntity().getName(), "you"))
							return true;
						else if (WordNetSimilarity.getInstance().isSimilar(
								t_Sibling.getEntity().getName(), "their"))
							return true;
						else if (WordNetSimilarity.getInstance().isSimilar(
								t_Sibling.getEntity().getName(), "her"))
							return true;
						else if (WordNetSimilarity.getInstance().isSimilar(
								t_Sibling.getEntity().getName(), "his"))
							return true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private static boolean isAction(List<String> actions, ITuple t1,
			ITuple t2) {
		ITuple t_parent = t1.findParent(t2);
		ITuple t_Sibling = t1.findSibling(t2);
		String comp_Value = "";
		if ((t_parent != null) && !t_parent.same(t2)) {
			comp_Value = t_parent.getRelation().getName();
			for (String action : actions) {
				try {
					if (WordNetSimilarity.getInstance().isSimilar(comp_Value,
							action)) {
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (t_Sibling != null) {
				if (t_Sibling.isTerminal())
					comp_Value = t_Sibling.toString();
				else
					comp_Value = t_Sibling.getRelation().getName();
				for (String action : actions) {
					try {
						if (WordNetSimilarity.getInstance().isSimilar(
								comp_Value, action)) {
							return true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return isAction(actions, t1, t_parent);
		} else if (t_parent.same(t2)) {
			if (t_parent.isTerminal())
				comp_Value = t_parent.toString();
			else
				comp_Value = t_parent.getRelation().getName();
			for (String action : actions) {
				try {
					if (WordNetSimilarity.getInstance().isSimilar(comp_Value,
							action)) {
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static ArrayList<ITuple> searchAtom(ITuple t,
			ArrayList<String> atom, ArrayList<ITuple> tupleList) {
		if (t == null)
			return tupleList;
		if (t.isTerminal()) {
			for (String res : atom) {
				if (t.getEntity().getName().toString().toLowerCase()
						.contains(res)) {
					if (!tupleList.contains(t))
						tupleList.add(t);
				}
			}
		} else {
			for (String res : atom) {
				if (t.getRelation().getName().toLowerCase().contains(res)) {
					if (!tupleList.contains(t))
						tupleList.add(t);
				}
			}
			tupleList = searchAtom(t.getLeft(), atom, tupleList);
			tupleList = searchAtom(t.getRight(), atom, tupleList);
		}
		return tupleList;
	}
}