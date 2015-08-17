package edu.ncsu.csc.ase.dristi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.ncsu.csc.ase.dristi.datastructure.ITuple;
import edu.ncsu.csc.ase.dristi.shallowparser.ParserFactory;
import edu.ncsu.csc.ase.dristi.util.TupleUtil;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;

/**
 * Class for invoking DRISTI Framework
 * @author rahul_pandita
 *
 */
public class TextAnalysisEngine {
	
	/**
	 * Creates the Tuple Representation of the Stanford-typed-Dependencies
	 * @param dependencies - Stanford-typed-Dependencies of a Sentence
	 * @return Tuple representation of {@code dependencies} or {@Code null} if none could be formed
	 */
	public static ITuple parse(SemanticGraph dependencies) {
		if (dependencies== null)
				return null;
		Set<IndexedWord> visited = new HashSet<IndexedWord>();
		if (dependencies.vertexSet().size() != 0) {
			if (dependencies.getRoots().size() > 0)
			{	
				List<IndexedWord> orderedList = dependencies.vertexListSorted();
				Set<IndexedWord> visitedSet = new HashSet<>();
				ITuple tuple = ParserFactory.getInstance().getParser("root").parse(dependencies.getFirstRoot(), dependencies,visited);
				TupleUtil.reorder(tuple, orderedList, visitedSet);
				return tuple;
			}
		}
		return null;
	}
}
